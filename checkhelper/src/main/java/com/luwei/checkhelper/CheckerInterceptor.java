package com.luwei.checkhelper;

import android.util.Log;

/**
 * Created by Mr_Zeng
 *
 * @date 2019/2/18
 */
public class CheckerInterceptor implements Interceptor {

    private final CheckHelper.Checker mChecker;

    public CheckerInterceptor(CheckHelper.Checker checker) {
        mChecker = checker;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void intercept(Chain chain) {
        Stream stream = chain.stream();
        Log.e("=======CheckInt",stream.getD().toString());
        if (mChecker != null) {
            if (stream.isToCheck()) {
                mChecker.check(stream.getD(),stream.getV());
            }else {
                mChecker.unCheck(stream.getD(),stream.getV());
            }
        }
        chain.proceed(stream);
    }
}
