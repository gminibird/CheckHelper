package com.luwei.checkhelperdemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btn_single_check).setOnClickListener(this);
        findViewById(R.id.btn_multi_check).setOnClickListener(this);
        findViewById(R.id.btn_interceptor).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_single_check:
                //单选
                startActivity(new Intent(this,SingleCheckActivity.class));
                break;
            case R.id.btn_multi_check:
                //多选
                startActivity(new Intent(this,MultiCheckActivity.class));
                break;
            case R.id.btn_interceptor:
                //拦截器
                startActivity(new Intent(this,InterceptorActivity.class));
        }
    }
}
