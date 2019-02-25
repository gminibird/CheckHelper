package com.luwei.checkhelper;

import android.util.Log;

import java.util.List;

/**
 * Created by Mr_Zeng
 *
 * @date 2019/2/18
 */
public class OnSelectLInterceptor implements Interceptor{

    private final List<CheckHelper.OnSelectListener> mListeners;

    public OnSelectLInterceptor(List<CheckHelper.OnSelectListener> listeners){
        mListeners = listeners;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void intercept(Chain chain) {
        Stream stream = chain.stream();
        Log.e("=======select",stream.getD().toString()+stream.isToCheck());
        for (CheckHelper.OnSelectListener listener : mListeners) {
            if (listener != null) {
                listener.onSelect(stream.getD(),stream.getV(),stream.isToCheck());
            }
        }
        chain.proceed(stream);
    }
}
