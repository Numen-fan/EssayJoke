package com.jiajia.essayjoke.selectimage;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.jiajia.essayjoke.R;
import com.jiajia.framelibrary.recyclerview.adapter.CommonRecyclerAdapter;
import com.jiajia.framelibrary.recyclerview.adapter.OnItemClickListener;
import com.jiajia.framelibrary.recyclerview.adapter.ViewHolder;

import java.util.List;

/**
 * Email 240336124@qq.com
 * Created by Darren on 2017/4/9.
 * Version 1.0
 * Description:
 */
public class SelectImageListAdapter extends CommonRecyclerAdapter<String> {

    private final List<String> mResultList; // 已经选择的集合

    private SelectImageListener listener;

    public SelectImageListAdapter(Context context, List<String> data, @NonNull List<String> imageList) {
        super(context, data, R.layout.media_chooser_item);
        this.mResultList = imageList;
    }

    @Override
    public void convert(ViewHolder holder, String item) {
        if(TextUtils.isEmpty(item)){
            // 显示拍照
            holder.setViewVisibility(R.id.camera_ll, View.VISIBLE);
            holder.setViewVisibility(R.id.media_selected_indicator, View.INVISIBLE);
            holder.setViewVisibility(R.id.image, View.INVISIBLE);
        }else{
            // 显示图片
            holder.setViewVisibility(R.id.camera_ll, View.INVISIBLE);
            holder.setViewVisibility(R.id.media_selected_indicator,View.VISIBLE);
            holder.setViewVisibility(R.id.image, View.VISIBLE);

            // 显示图片利用Glide
            ImageView imageView = holder.getView(R.id.image);
            Glide.with(mContext).load(item).centerCrop().into(imageView);

            // 点亮选择的图片
            ImageView selectIndicator = holder.getView(R.id.media_selected_indicator);
            selectIndicator.setSelected(mResultList.contains(item));

            // 给条目增加点击事件
            holder.setOnItemClickListener(v -> {
                // 没有就加入集合，有就移除
                if (mResultList.contains(item)) {
                    mResultList.remove(item);
                } else {
                    if (mResultList.size() >= 9) {
                        Toast.makeText(holder.itemView.getContext(), "最多只能选择9张", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    mResultList.add(item);
                }
                notifyItemChanged(holder.getAdapterPosition());

                // 通知外面显示
                if (listener != null) {
                    listener.select();
                }
            });
        }
    }

    public void  setSelectListener(SelectImageListener listener) {
        this.listener = listener;
    }
}
