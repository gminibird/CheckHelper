package com.luwei.checkhelperdemo;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.luwei.recyclerview.adapter.extension.LwItemBinder;
import com.luwei.recyclerview.adapter.extension.LwViewHolder;

/**
 * Created by Mr_Zeng
 *
 * @date 2019/1/5
 */
public class NormalSingleBinder extends LwItemBinder<CheckBean> {

    private CheckBean mCheckedBean;
    int mPosition;
    private LwViewHolder mCheckedHolder;

    @Override
    protected View getView(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        return inflater.inflate(R.layout.item_check, parent, false);
    }

    @Override
    protected void onBind(@NonNull LwViewHolder holder, @NonNull CheckBean item) {
        holder.setText(R.id.tv_content, item.getContent());
        CheckBox checkBox = holder.getView(R.id.checkbox);
        checkBox.setChecked(item.isChecked());
        holder.getView().setOnClickListener(v -> {
            holder.setChecked(R.id.checkbox, !item.isChecked());
            item.setChecked(!item.isChecked());
            if (mCheckedHolder != null && !mCheckedHolder.equals(holder)) {
                mCheckedHolder.setChecked(R.id.checkbox, false);
                mCheckedBean.setChecked(false);
//                getAdapter().notifyItemChanged(mPosition);
            }
            mCheckedHolder = holder;
            mCheckedBean = item;
            mPosition = holder.getAdapterPosition();
        });
    }
}
