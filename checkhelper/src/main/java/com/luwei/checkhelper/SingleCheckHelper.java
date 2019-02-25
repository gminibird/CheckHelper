package com.luwei.checkhelper;

import android.support.v7.widget.RecyclerView;
import android.util.Log;

/**
 * Created by Mr_Zeng
 *
 * @date 2019/1/3
 */
public class SingleCheckHelper extends CheckHelper {

    private final int TAG = R.id.check_tag;
    private final Object TAG_VALUE = new Object();
    //是否可以取消
    private boolean canCancel = true;

    //一般可以理解为Data（Bean）
    private Object d;
    private RecyclerView.ViewHolder v;

    public SingleCheckHelper() {
        addDownSteamInterceptor(new Interceptor() {
            @Override
            public void intercept(Chain chain) {
                synchronized (SingleCheckHelper.this){
                    Stream stream = chain.stream();
                    Object d = stream.getD();
                    RecyclerView.ViewHolder v = stream.getV();
                    stream.setChecked(!isChecked(d,v));
                    Log.e("=======single",d.toString());
                    if (stream.isToCheck()) {
                        unCheckPre(d);
                        setTag(v);
                        SingleCheckHelper.this.v = v;
                        SingleCheckHelper.this.d = d;
                    } else {
                        if (!canCancel) {
                            return;
                        }
                        clearTag(v);
                        SingleCheckHelper.this.d = null;
                        SingleCheckHelper.this.v = null;
                    }
                    chain.proceed(stream);
                }
            }
        });
    }

    @Override
    public void select(Object d, RecyclerView.ViewHolder v, boolean toCheck) {
        super.select(d, v, toCheck);

    }

    @Override
    public void bind(Object d, RecyclerView.ViewHolder v, boolean toCheck) {
        if (toCheck) {
            unCheckPre(d);
            setTag(v);
            this.v = v;
            this.d = d;
        } else {
            clearTag(v);
        }
        super.bind(d, v, toCheck);
    }

    private void setTag(RecyclerView.ViewHolder v) {
        if (v != null) {
            v.itemView.setTag(TAG, TAG_VALUE);
        }
    }

    private void clearTag(RecyclerView.ViewHolder v) {
        if (v != null && v.itemView.getTag(TAG) != null) {
            v.itemView.setTag(TAG, null);
        }
    }

    private void unCheckPre(Object d) {
        if (this.d == null || this.d.equals(d)) {
            return;
        }
        if (this.d != null && this.v != null && this.v.itemView.getTag(TAG) != null) {
            //当上一个选中存在并且可见时置为非选
            Checker checker = mCheckerMap.get(this.d.getClass());
            checker.unCheck(this.d,this.v);
        }
    }

    @Override
    public boolean isChecked(Object d, RecyclerView.ViewHolder v) {
        return this.d != null && this.d.equals(d);
    }

    @Override
    public boolean hasChecked() {
        return d != null;
    }

    @Override
    public void add(Object d) {
        this.d = d;
    }

    @Override
    public void remove(Object d) {
        if (this.d.equals(d)) {
            this.d = null;
        }
    }

    public boolean isCanCancel() {
        return canCancel;
    }

    public void setCanCancel(boolean canCancel) {
        this.canCancel = canCancel;
    }

    public <T> T getChecked() {
        return (T) d;
    }

}
