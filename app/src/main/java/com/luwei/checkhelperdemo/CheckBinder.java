package com.luwei.checkhelperdemo;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.luwei.checkhelper.CheckHelper;
import com.luwei.checkhelper.SingleCheckHelper;
import com.luwei.recyclerview.adapter.extension.LwItemBinder;
import com.luwei.recyclerview.adapter.extension.LwViewHolder;
import com.luwei.recyclerview.adapter.multitype.Items;

import java.util.List;

/**
 * Created by Mr_Zeng
 *
 * @date 2019/1/5
 */
public class CheckBinder extends LwItemBinder<String> {

    CheckHelper mHelper;

    public CheckBinder(@NonNull CheckHelper helper){
        mHelper = helper;
    }


    @Override
    protected View getView(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        return inflater.inflate(R.layout.item_check, parent, false);
    }

    @Override
    protected void onBind(@NonNull LwViewHolder holder, @NonNull String item) {
        holder.setText(R.id.tv_content, item);
        mHelper.bind(item, holder, holder.itemView);
    }


}
