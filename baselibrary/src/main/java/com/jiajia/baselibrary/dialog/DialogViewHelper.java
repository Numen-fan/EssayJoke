package com.jiajia.baselibrary.dialog;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import java.lang.ref.WeakReference;

/**
 * Created by Numen_fan on 2022/3/27
 * Desc: DialogView的辅助处理类
 */
class DialogViewHelper {

    private View mContentView = null;

    // 防止霸气侧漏
    private final SparseArray<WeakReference<View>> mViews = new SparseArray<>();

    public DialogViewHelper() {
        
    }

    public DialogViewHelper(Context context, int viewLayoutResId) {
        mContentView = LayoutInflater.from(context).inflate(viewLayoutResId, null);
    }

    public void setContentView(View view) {
        this.mContentView = view;
    }

    /**
     * 设置文本
     */
    public void setText(int viewId, CharSequence charSequence) {
        // 每次都findViewById比较麻烦
        TextView tv = getView(viewId);
        if (tv != null) {
            tv.setText(charSequence);
        }
    }

    public void setOnclickListener(int viewId, View.OnClickListener onClickListener) {
        View view = getView(viewId);
        if (view != null) {
            view.setOnClickListener(onClickListener);
        }
    }

    public  <T extends View> T getView(int viewId) {

        View view = null;
        if (mViews.get(viewId) != null) {
            view = mViews.get(viewId).get();
        }

        if (view == null) {
            view = mContentView.findViewById(viewId);
            if (view != null) {
                mViews.put(viewId, new WeakReference<>(view));
            }
        }
        return (T) view;
    }

    /**
     * 获取内容布局
     */
    public View getContentView() {
        return mContentView;
    }
}
