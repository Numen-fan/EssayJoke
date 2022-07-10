package com.jiajia.baselibrary.navigationbar;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Numen_fan on 2022/4/1
 * Desc:
 */
public abstract class AbsNavigationBar<P extends AbsNavigationBar.Builder.AbsNavigationBarParams> implements INavigationBar {

    private final P mParams;
    private View mNavigationView;

    public  AbsNavigationBar(P params) {
        this.mParams = params;
        createAndBindView();
    }

    public P getParams() {
        return mParams;
    }

    public void setText(int viewId, String text) {
        TextView tv = findViewById(viewId);
        if (tv != null && !TextUtils.isEmpty(text)) {
            tv.setVisibility(View.VISIBLE);
            tv.setText(text);
        }
    }

    public void setOnClickListener(int viewId, View.OnClickListener listener) {
        View v = findViewById(viewId);
        if (v != null && listener != null) {
            v.setOnClickListener(listener);
        }
    }

    public void setViewVisibility(int viewId, int visible) {
        findViewById(viewId).setVisibility(visible);
    }

    public <T extends View> T findViewById(int viewId) {
        return mNavigationView.findViewById(viewId);
    }

    /**
     * 创建和绑定view
     */
    private void createAndBindView() {
        // 1.创建View

        if (mParams.mParent == null) {
            ViewGroup activityRoot = (ViewGroup) ((Activity)(mParams.mContext)).getWindow().getDecorView();
            mParams.mParent = (ViewGroup) activityRoot.getChildAt(0);
        }

        if (mParams.mParent == null) {
            return;
        }

        mNavigationView = LayoutInflater.from(mParams.mContext).inflate(bindLayoutId(), mParams.mParent, false);

        // 2.添加
        mParams.mParent.addView(mNavigationView, 0);

        applyView();

    }


    @Override
    public int bindLayoutId() {
        return 0;
    }

    @Override
    public void applyView() {

    }

    public abstract static class Builder {

        public Builder(Context context, ViewGroup parent) {

        }


        public abstract AbsNavigationBar build();


        public static class AbsNavigationBarParams {

            public Context mContext;
            public ViewGroup mParent;

            public AbsNavigationBarParams(Context context, ViewGroup parent) {
                this.mContext = context;
                this.mParent = parent;
            }


        }

    }

}
