package com.luwei.checkhelperdemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.luwei.checkhelper.CheckHelper;
import com.luwei.checkhelper.MultiCheckHelper;
import com.luwei.checkhelper.SingleCheckHelper;
import com.luwei.recyclerview.adapter.extension.LwViewHolder;
import com.luwei.recyclerview.adapter.multitype.Items;
import com.luwei.recyclerview.adapter.multitype.LwAdapter;

import java.util.List;
import java.util.Locale;

/**
 * Created by Mr_Zeng
 *
 * @date 2019/1/5
 */
public class SingleCheckActivity extends AppCompatActivity {

    private RecyclerView mRvContent;
    private Items mItems;
    private LwAdapter mAdapter;
    protected SingleCheckHelper mCheckHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_check);
        mRvContent = findViewById(R.id.rv_content);
        initData();
        initView();
    }

    private void initView() {
        mAdapter = new LwAdapter(mItems);
        mAdapter.register(CheckBean.class, new NormalSingleBinder());
        mAdapter.register(String.class, new CheckBinder(createHelper()));
        mRvContent.setLayoutManager(new LinearLayoutManager(this));
        mRvContent.setAdapter(mAdapter);
    }

    private void initData() {
        mItems = new Items();
        for (int i = 0; i < 50; i++) {
            mItems.add(String.format(Locale.CHINA, "第%d个数据", i + 1));
        }
    }

    private CheckHelper createHelper() {
        mCheckHelper = new SingleCheckHelper();
        //注册选择器
        mCheckHelper.register(String.class, new CheckHelper.Checker<String, LwViewHolder>() {
            @Override
            public void check(String s, LwViewHolder holder) {
                holder.itemView.setBackgroundColor(0xFF73E0E4); //蓝色
                holder.setChecked(R.id.checkbox, true);
            }

            @Override
            public void unCheck(String s, LwViewHolder holder) {
                holder.itemView.setBackgroundColor(0xFFFFFFFF);  //白色
                holder.setChecked(R.id.checkbox, false);
            }
        });
        //选择监听
        mCheckHelper.addOnSelectListener(String.class, (s, viewHolder, isCheck) -> {
            if (isCheck) {
                String tips = "当前选择 " + mCheckHelper.getChecked(); //or + s
                Toast.makeText(viewHolder.itemView.getContext(), tips,
                        Toast.LENGTH_SHORT).show();
            }
        });
        //添加默认选中数据
//        mCheckHelper.add(mItems.get(0));
        //设置不能取消
//        mCheckHelper.setCanCancel(false);
        return mCheckHelper;
    }

}
