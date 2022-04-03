package com.jiajia.baselibrary.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Numen_fan on 2022/3/27
 * Desc:
 */

class AlertController {

    private final AlertDialog mDialog;
    private final Window mWindow;

    private DialogViewHelper mViewHelper;

    public AlertController(AlertDialog dialog, Window window) {
        this.mDialog = dialog;
        this.mWindow = window;
    }

    public void setDialogViewHelper(DialogViewHelper viewHelper) {
        this.mViewHelper = viewHelper;
    }

    public <T extends View> T getView(int viewId) {
        return mViewHelper.getView(viewId);
    }

    public void setText(int viewId, CharSequence charSequence) {
        mViewHelper.setText(viewId, charSequence);
    }



    public AlertDialog getDialog() {
        return mDialog;
    }

    /**
     * 获取Dialog的window
     */
    public Window getWindow() {
        return mWindow;
    }

    public void setOnClickListener(int viewId, View.OnClickListener listener) {
        mViewHelper.setOnclickListener(viewId, listener);
    }

    public static class AlertParams {

        public Context mContext;
        public int mThemeResId;

        // 点击空白是否能够取消
        public boolean mCancelable = true;

        // dialog Cancel监听
        public DialogInterface.OnCancelListener mOnCancelListener;

        // dialog dismiss监听
        public DialogInterface.OnDismissListener mOnDismissListener;

        // dialog Key监听
        public DialogInterface.OnKeyListener mOnKeyListener;

        // 布局
        public View mView;
        // 布局id
        public int mViewLayoutResId;


        // 存放一些字体的修改
        public SparseArray<CharSequence> mTextArray = new SparseArray<>();

        // 存放一些点击事件
        public SparseArray<View.OnClickListener> mClickArray = new SparseArray<>();

        // 宽高
        public int mWidth = ViewGroup.LayoutParams.WRAP_CONTENT;
        public int mHeight = ViewGroup.LayoutParams.WRAP_CONTENT;

        // 动画
        public int mAnimations = 0;

        // 位置
        public int mGravity = Gravity.CENTER;


        public AlertParams(Context context, int themeResId) {
            this.mContext = context;
            this.mThemeResId = themeResId;
        }

        /**
         * 绑定和设定参数
         */
        public void apply(AlertController mAlert) {

            // 1 设置Dialog布局 DialogViewHelper

            DialogViewHelper viewHelper = null;
            if (mViewLayoutResId != 0) {
                 viewHelper = new DialogViewHelper(mContext, mViewLayoutResId);
            }

            if (mView != null) {
                viewHelper = new DialogViewHelper();
                viewHelper.setContentView(mView);
            }
            if (viewHelper == null) {
                throw new IllegalArgumentException("请设置布局setContentView");
            }

            mAlert.setDialogViewHelper(viewHelper);

            // 给Dialog设置布局
            mAlert.getDialog().setContentView(viewHelper.getContentView());

            // 2 设置文本
            int textArraySize = mTextArray.size();
            for (int i = 0; i < textArraySize; i++) {
                viewHelper.setText(mTextArray.keyAt(i), mTextArray.valueAt(i));
            }

            // 3 设置点击事件
            int textClickSize = mClickArray.size();
            for (int i = 0; i < textClickSize; i++) {
                viewHelper.setOnclickListener(mClickArray.keyAt(i), mClickArray.valueAt(i));
            }

            // 4 配置自定义的效果 全屏 从底部弹出 默认动画等
            Window window = mAlert.getWindow();
            // 设置位置
            window.setGravity(mGravity);
            // 设置动画
            if (mAnimations != 0) {
                window.setWindowAnimations(mAnimations);
            }

            // 设置宽高
            WindowManager.LayoutParams params = window.getAttributes();
            params.width = mWidth;
            params.height = mHeight;
            window.setAttributes(params);

        }
    }
}
