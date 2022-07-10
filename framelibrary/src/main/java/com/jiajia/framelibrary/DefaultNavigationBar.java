package com.jiajia.framelibrary;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.jiajia.baselibrary.navigationbar.AbsNavigationBar;

/**
 * Created by Numen_fan on 2022/4/3
 * Desc:
 */
public class DefaultNavigationBar extends AbsNavigationBar<DefaultNavigationBar.Builder.DefaultNavigationParams> {

    public DefaultNavigationBar(Builder.DefaultNavigationParams params) {
        super(params);
    }


    @Override
    public int bindLayoutId() {
        return R.layout.title_bar;
    }

    @Override
    public void applyView() {
        // 绑定效果
        setText(R.id.title, getParams().mTitle);
        setText(R.id.right_text, getParams().mRightTitle);
        setOnClickListener(R.id.right_text, getParams().mRightClickListener);

        // 设置左边icon的点击事件
        setOnClickListener(R.id.back, getParams().mLeftOnClickListener);

        setViewVisibility(R.id.back, getParams().leftIconVisible);

    }


    /**
     * builder
     */
    public static class Builder extends AbsNavigationBar.Builder {

        DefaultNavigationParams P;



        public Builder(Context context, ViewGroup parent) {
            super(context, parent);
            P = new DefaultNavigationParams(context, parent);
        }

        public Builder(Context context) {
            this(context, null);
        }


        public DefaultNavigationBar build() {
            DefaultNavigationBar navigationBar = new DefaultNavigationBar(P);
            return navigationBar;
        }

        // 1. 设置所有的效果

        /**
         * 设置title
         */
        public DefaultNavigationBar.Builder setTitle(String title) {
            P.mTitle = title;
            return this;
        }

        /**
         * 设置右边title
         */
        public DefaultNavigationBar.Builder setRightText(String rightTitle) {
            P.mRightTitle = rightTitle;
            return this;
        }

        /**
         * 设置右边的图片
         */
        public DefaultNavigationBar.Builder setRightIcon(int rightIconResId) {
            P.mRightIconResId = rightIconResId;
            return this;
        }

        public DefaultNavigationBar.Builder hideLeftIcon() {
            P.leftIconVisible = View.INVISIBLE;
            return this;
        }

        /**
         * 设置右侧view的点击事件
         */
        public DefaultNavigationBar.Builder setRightClickListener(View.OnClickListener listener) {
            P.mRightClickListener = listener;
            return this;
        }

        /**
         * 设置左侧view的点击事件
         */
        public DefaultNavigationBar.Builder setLeftClickListener(View.OnClickListener listener) {
            P.mLeftOnClickListener = listener;
            return this;
        }


        /**
         * params
         */
        public static class DefaultNavigationParams extends AbsNavigationBarParams {

            // 2. 放置所有的效果
            public String mTitle;
            public String mRightTitle;
            public int mRightIconResId;
            public int leftIconVisible = View.VISIBLE;
            public View.OnClickListener mRightClickListener;
            // 默认back为关闭此页面
            public View.OnClickListener mLeftOnClickListener = v -> ((Activity) mContext).finish();

            public DefaultNavigationParams(Context context, ViewGroup parent) {
                super(context, parent);
            }
        }
    }
}
