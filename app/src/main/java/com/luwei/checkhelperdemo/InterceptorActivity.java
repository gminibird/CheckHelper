package com.luwei.checkhelperdemo;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ProgressBar;

import com.luwei.checkhelper.Interceptor;
import com.luwei.checkhelper.Stream;

/**
 * Created by Mr_Zeng
 *
 * @date 2019/2/23
 */
public class InterceptorActivity extends SingleCheckActivity {

    private ProgressDialog mDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCheckHelper.addInterceptor(new Interceptor() {
            @Override
            public void intercept(Chain chain) {
                request(chain);
            }
        });
    }

    /**
     * 模仿网络请求
     */
    private void request(Interceptor.Chain chain) {
        if (mDialog == null) {
            mDialog = new ProgressDialog(InterceptorActivity.this);
        }
        mDialog.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                boolean result = true;
                //Stream里面可以拿到数据
                Stream stream = chain.stream();
                if (result) {
                    //调用该方法就会完成接下来的Check事件，不调用则拦截
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            chain.proceed(stream);
                        }
                    });
                }
                mDialog.dismiss();
            }
        }).start();
    }
}
