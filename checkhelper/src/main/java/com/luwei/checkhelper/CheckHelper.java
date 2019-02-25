package com.luwei.checkhelper;

import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Mr_Zeng
 *
 * @date 2019/1/3
 */
public abstract class CheckHelper {

    @Nullable
    Map<Class, Checker> mCheckerMap;
    @Nullable
    Map<Class, List<OnCheckListener>> mCheckListenerMap;
    @Nullable
    Map<Class, List<OnSelectListener>> mSelectListenerMap;
    @Nullable
    Map<Class, List<onBindListener>> mOnBindListenerMap;
    @Nullable
    private List<Interceptor> mInterceptors;
    @Nullable
    private List<Interceptor> mDownStreamInterceptors;
    private final int DEFAULT_MAP_SIZE = 4;

    public CheckHelper() {
    }

    /**
     * 注册Check
     *
     * @param clazz   实体类(数据)，每一个实体类型对应一个Checker
     * @param checker 回调，可以根据需求提供选中以及非选中的状态
     * @param <D>     类型
     */
    public <D> void register(Class<? extends D> clazz, Checker<D, ?> checker) {
        if (mCheckerMap == null) {
            mCheckerMap = new HashMap<>();
        }
        mCheckerMap.put(clazz, checker);
    }

    /**
     * 绑定(当前状态)
     */
    public final void bind(Object d, RecyclerView.ViewHolder v) {
        bind(d, v, null);
    }

    /**
     * 绑定(当前状态)
     *
     * @param clickedView 需要设置点击事件的view id
     */
    public final void bind(Object d, RecyclerView.ViewHolder v, @IdRes int clickedView) {
        bind(d, v, v.itemView.findViewById(clickedView));
    }


    /**
     * 绑定(当前状态)
     *
     * @param v           ViewHolder
     * @param d           数据
     * @param clickedView 需要设置点击事件的view
     */
    public final void bind(final Object d, final RecyclerView.ViewHolder v, View clickedView) {
        if (clickedView != null) {
            clickedView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    select(d, v);
                }
            });
        }
        bind(d, v, isChecked(d, v));
    }

    /**
     * 绑定
     * {@link android.support.v7.widget.RecyclerView.Adapter
     * #onBindViewHolder(RecyclerView.ViewHolder, int)} 方法内触发
     *
     * @param toCheck 是否选中
     */
    @SuppressWarnings("unchecked")
    public void bind(Object d, RecyclerView.ViewHolder v, boolean toCheck) {
        Checker checker = mCheckerMap.get(d.getClass());
        if (checker != null) {
            if (toCheck) {
                checker.check(d, v);
            } else {
                checker.unCheck(d, v);
            }
        }
        if (mCheckListenerMap != null) {
            List<OnCheckListener> checkListeners = mCheckListenerMap.get(d.getClass());
            if (checkListeners != null) {
                for (OnCheckListener checkListener : checkListeners) {
                    if (checkListener != null) {
                        checkListener.onCheck(d, v, toCheck);
                    }
                }
            }
        }
        if (mOnBindListenerMap != null) {
            List<onBindListener> onBindListeners = mOnBindListenerMap.get(d.getClass());
            if (onBindListeners != null) {
                for (onBindListener onBindListener : onBindListeners) {
                    if (onBindListener != null) {
                        onBindListener.onBind(d, v, toCheck);
                    }
                }
            }
        }
    }

    /**
     * 选择(或者说点击)
     */
    public void select(Object data, RecyclerView.ViewHolder view) {
        select(data, view, !isChecked(data, view));
    }

    /**
     * 选择，点击时触发
     *
     * @param toCheck 是否选中
     */
    @SuppressWarnings("unchecked")
    public void select(Object d, RecyclerView.ViewHolder v, boolean toCheck) {
        List<Interceptor> interceptors = new ArrayList<>();
        if (mInterceptors != null) {
            interceptors.addAll(mInterceptors);
        }
        if (mDownStreamInterceptors != null) {
            interceptors.addAll(mDownStreamInterceptors);
        }
        if (mCheckerMap != null) {
            Checker checker = mCheckerMap.get(d.getClass());
            interceptors.add(new CheckerInterceptor(checker));
        }
        if (mSelectListenerMap != null) {
            List<OnSelectListener> selectListeners = mSelectListenerMap.get(d.getClass());
            if (selectListeners != null) {
                interceptors.add(new OnSelectLInterceptor(selectListeners));
            }
        }
        if (mCheckListenerMap != null) {
            List<OnCheckListener> checkListeners = mCheckListenerMap.get(d.getClass());
            if (checkListeners != null) {
                interceptors.add(new OnCheckLInterceptor(checkListeners));
            }
        }
        RealChain originalChain = new RealChain(interceptors, new Stream(d, v, toCheck), 0);
        originalChain.proceed(originalChain.stream());
    }


    /**
     * 每次{@link Adapter#onBindViewHolder(RecyclerView.ViewHolder, int)}
     * 方法调用以及item每次点击都会调用此方法
     */
    public <D> void addOnCheckListener(Class<D> clazz, @NonNull OnCheckListener<D, ?> l) {
        if (mCheckListenerMap == null) {
            mCheckListenerMap = new HashMap<>(DEFAULT_MAP_SIZE);
        }
        List<OnCheckListener> listeners = mCheckListenerMap.get(clazz);
        if (listeners == null) {
            listeners = new ArrayList<>();
            mCheckListenerMap.put(clazz, listeners);
        }
        listeners.add(l);
    }

    public <D> void addOnSelectListener(Class<D> clazz, @NonNull OnSelectListener<D, ?> l) {
        if (mSelectListenerMap == null) {
            mSelectListenerMap = new HashMap<>(DEFAULT_MAP_SIZE);
        }
        List<OnSelectListener> listeners = mSelectListenerMap.get(clazz);
        if (listeners == null) {
            listeners = new ArrayList<>();
            mSelectListenerMap.put(clazz, listeners);
        }
        listeners.add(l);
    }

    public <D> void addOnBindListener(Class<D> clazz, @NonNull onBindListener<D, ?> l) {
        if (mOnBindListenerMap == null) {
            mOnBindListenerMap = new HashMap<>(DEFAULT_MAP_SIZE);
        }
        List<onBindListener> listeners = mOnBindListenerMap.get(clazz);
        if (listeners == null) {
            listeners = new ArrayList<>();
            mOnBindListenerMap.put(clazz, listeners);
        }
        listeners.add(l);
    }

    public void addInterceptor(Interceptor interceptor) {
        if (mInterceptors == null) {
            mInterceptors = new ArrayList<>();
        }
        mInterceptors.add(0, interceptor);
    }

    protected void addDownSteamInterceptor(Interceptor interceptor) {
        if (mDownStreamInterceptors == null) {
            mDownStreamInterceptors = new ArrayList<>();
        }
        mDownStreamInterceptors.add(0, interceptor);
    }


    /**
     * 是否已选中
     */
    public abstract boolean isChecked(Object data, RecyclerView.ViewHolder view);

    public abstract boolean hasChecked();

    public abstract void add(Object d);

    public abstract void remove(Object d);

    public abstract <T> T getChecked();


    public interface Checker<D, V extends RecyclerView.ViewHolder> {

        /**
         * 切换到选中状态时会调用
         */
        void check(D d, V v);

        /**
         * 切换到非选中状态时会调用
         */
        void unCheck(D d, V v);
    }

    public interface OnCheckListener<D, V extends RecyclerView.ViewHolder> {

        /**
         * 视图刷新就会调用，即{@link onBindListener}与{@link OnSelectListener}的条件并集
         */
        void onCheck(D d, V v, boolean isCheck);
    }


    public interface onBindListener<D, V extends RecyclerView.ViewHolder> {

        /**
         * 每次{@link Adapter#onBindViewHolder(RecyclerView.ViewHolder, int)}方法调用
         * 或者
         * 主动调用{@link #bind(Object, ViewHolder)}及{@link #bind(Object, ViewHolder, boolean)}
         * 时调用
         */
        void onBind(D d, V v, boolean isCheck);
    }


    public interface OnSelectListener<D, V extends RecyclerView.ViewHolder> {

        /**
         * 只有点击时或者主动调用
         * {@link #select(Object, ViewHolder, boolean)}
         * or
         * {@link #select(Object, ViewHolder)}
         * 方法时调用
         */
        void onSelect(D d, V v, boolean isCheck);
    }
}