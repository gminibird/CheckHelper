package com.luwei.checkhelper;

import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Mr_Zeng
 *
 * @date 2019/1/3
 */
public abstract class CheckHelper {

    Map<Class, Checker> mCheckerMap;
    Map<Class, OnCheckListener> mCheckListenerMap;
    Map<Class, OnSelectListener> mSelectListenerMap;
    Map<Class, onBindListener> mOnBindListenerMap;

    public CheckHelper(){
        mCheckerMap = new HashMap<>(3);
        mCheckListenerMap = new HashMap<>(3);
        mSelectListenerMap = new HashMap<>(3);
        mOnBindListenerMap = new HashMap<>(3);
    }

    /**
     * 注册Check
     * @param clazz 实体类(数据)
     * @param checker 回调
     * @param <D> 类型
     */
    public <D> void register(Class<? extends D> clazz, Checker<D, ?> checker) {
        mCheckerMap.put(clazz, checker);
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
        if (clickedView == null) {
            throw new NullPointerException("ClickedView can not be null!");
        }
        bind(d, v, isChecked(d, v));
        clickedView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                select(d, v);
            }
        });
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
        OnCheckListener checkListener = mCheckListenerMap.get(d.getClass());
        if (checkListener != null) {
            checkListener.onCheck(d, v, toCheck);
        }
        onBindListener bindListener = mOnBindListenerMap.get(d.getClass());
        if (bindListener != null) {
            bindListener.onBind(d, v, toCheck);
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
        Checker checker = mCheckerMap.get(d.getClass());
        if (checker != null) {
            if (toCheck) {
                checker.check(d, v);
            } else {
                checker.unCheck(d, v);
            }
        }
        OnSelectListener selectListener = mSelectListenerMap.get(d.getClass());
        if (selectListener != null) {
            selectListener.onSelect(d, v, toCheck);
        }
        OnCheckListener checkListener = mCheckListenerMap.get(d.getClass());
        if (checkListener != null) {
            checkListener.onCheck(d, v, toCheck);
        }
    }


    public <D> void addOnCheckListener(Class<D> clazz, @NonNull OnCheckListener<D, ?> l) {
        mCheckListenerMap.put(clazz, l);
    }

    public <D> void addOnSelectListener(Class<D> clazz, @NonNull OnSelectListener<D, ?> l) {
        mSelectListenerMap.put(clazz, l);
    }

    public <D> void addOnBindListener(Class<D> clazz, @NonNull onBindListener<D, ?> l) {
        mOnBindListenerMap.put(clazz, l);
    }

    /**
     * 是否已选中
     */
    public abstract boolean isChecked(Object data, RecyclerView.ViewHolder view);

    public abstract boolean hasChecked();

    public abstract void add(Object d);

    public abstract void remove(Object d);

    /**
     * 选中状态和非选中状态的回调
     *
     * @param <V>
     * @param <D>
     */
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


    /**
     * 视图刷新就会调用，即{@link onBindListener}与{@link OnSelectListener}的交集
     *
     * @param <D> 一般可以理解为数据
     * @param <V> ViewHolder
     */
    public interface OnCheckListener<D, V extends RecyclerView.ViewHolder> {
        void onCheck(D d, V v, boolean isCheck);
    }

    /**
     * 视图绑定时调用
     * {@link android.support.v7.widget.RecyclerView.Adapter
     * #onBindViewHolder(RecyclerView.ViewHolder, int)}
     *
     * @param <D>
     * @param <V>
     */
    public interface onBindListener<D, V extends RecyclerView.ViewHolder> {
        void onBind(D d, V v, boolean isCheck);
    }

    /**
     * 选择(点击)时调用
     *
     * @param <D>
     * @param <V>
     */
    public interface OnSelectListener<D, V extends RecyclerView.ViewHolder> {
        void onSelect(D d, V v, boolean isCheck);
    }

}