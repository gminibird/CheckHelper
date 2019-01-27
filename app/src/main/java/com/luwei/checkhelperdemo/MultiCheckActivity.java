package com.luwei.checkhelperdemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.luwei.checkhelper.CheckHelper;
import com.luwei.checkhelper.MultiCheckHelper;
import com.luwei.checkhelper.SingleCheckHelper;
import com.luwei.recyclerview.adapter.extension.LwViewHolder;
import com.luwei.recyclerview.adapter.multitype.Items;
import com.luwei.recyclerview.adapter.multitype.LwAdapter;

import java.util.Locale;

/**
 * Created by Mr_Zeng
 *
 * @date 2019/1/27
 */
public class MultiCheckActivity extends AppCompatActivity
        implements View.OnClickListener {

    private RecyclerView mRvContent;
    private Items mItems;
    private LwAdapter mAdapter;
    private MultiCheckHelper mCheckHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_check);
        mRvContent = findViewById(R.id.rv_content);
        initData();
        initView();
    }

    private void initView() {
        findViewById(R.id.btn_select_all).setOnClickListener(this);
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

    /**
     * @return
     */
    private CheckHelper createHelper() {
        mCheckHelper = new MultiCheckHelper();
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
        //选择监听器
        mCheckHelper.addOnSelectListener(String.class, (s, viewHolder, isCheck) -> {
            if (isCheck) {
                String tips = "选择" + s + ",一共已选" + mCheckHelper.getSelected().size() + "个";
                Toast.makeText(viewHolder.itemView.getContext(), tips, Toast.LENGTH_SHORT).show();
            }
        });
        //添加默认数据
//        mCheckHelper.setHasDefault(true);
//        mCheckHelper.add(mItems.get(0));
        return mCheckHelper;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_select_all:
                if (mCheckHelper.isAllChecked(mItems)) {
                    //取消全选
                    mCheckHelper.unCheckAll(mAdapter);
                } else {
                    //全选
                    mCheckHelper.checkAll(mItems, mAdapter);
                }
                break;
        }
    }
}
