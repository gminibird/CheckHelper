package com.luwei.checkhelper;

import android.support.annotation.NonNull;
import android.util.Log;

import java.util.List;

/**
 * Created by Mr_Zeng
 *
 * @date 2019/2/18
 */
public class OnCheckLInterceptor implements Interceptor {

    private List<CheckHelper.OnCheckListener> mListeners;

    public OnCheckLInterceptor(@NonNull List<CheckHelper.OnCheckListener> listeners){
        mListeners = listeners;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void intercept(Chain chain) {
        RealChain realChain = (RealChain) chain;
        Stream stream = realChain.stream();
        for (CheckHelper.OnCheckListener mListener : mListeners) {
            if (mListener!=null){
                mListener.onCheck(stream.getD(),stream.getV(),stream.isToCheck());
            }
        }
        chain.proceed(stream);
    }
}
