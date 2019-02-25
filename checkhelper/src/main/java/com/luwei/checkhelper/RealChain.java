package com.luwei.checkhelper;

import android.support.annotation.NonNull;

import java.util.List;

/**
 * Created by Mr_Zeng
 *
 * @date 2019/2/18
 */
public class RealChain implements Interceptor.Chain {

    private int mIndex;
    private Stream mStream;
    List<Interceptor> mInterceptors;

    public RealChain(@NonNull List<Interceptor> interceptors, Stream stream, int index) {
        this.mStream = stream;
        this.mInterceptors = interceptors;
        this.mIndex = index;
    }

    @Override
    public void proceed(Stream stream) {
        if (mIndex >= mInterceptors.size()) {
            return;
        }
        RealChain next = new RealChain(mInterceptors, stream, mIndex + 1);
        Interceptor interceptor = mInterceptors.get(mIndex);
        interceptor.intercept(next);
    }

    @Override
    public Stream stream() {
        return mStream;
    }

}
