package com.jiajia.baselibrary.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.jiajia.baselibrary.R;

import java.lang.ref.WeakReference;

/**
 * Created by Numen_fan on 2022/3/27
 * Desc: 自定义的万能Dialog
 */
public class AlertDialog extends Dialog {

    private AlertController mAlert;

    public AlertDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);

        mAlert = new AlertController(this, getWindow());
    }

    /**
     * 设置文本
     */
    public void setText(int viewId, CharSequence charSequence) {
        mAlert.setText(viewId, charSequence);
    }

    public <T extends View> T getView(int viewId) {
        return mAlert.getView(viewId);
    }

    public void setOnclickListener(int viewId, View.OnClickListener onClickListener) {
        mAlert.setOnClickListener(viewId, onClickListener);
    }


    public static class Builder {

        private final  AlertController.AlertParams P;

        public Builder(Context context) {
            this(context, R.style.dialog);
        }

        public Builder(Context context, int themeResId) {
            P = new AlertController.AlertParams(context, themeResId);
        }

        public Builder setContentView(View view) {
            P.mView = view;
            P.mViewLayoutResId = 0;
            return this;
        }

        public Builder setContentView(int resId) {
            P.mView = null;
            P.mViewLayoutResId = resId;
            return this;
        }

        public Builder setText(int viewId, CharSequence text) {
            P.mTextArray.put(viewId, text);
            return this;
        }

        public Builder setOnClickListener(int view, View.OnClickListener listener) {
            P.mClickArray.put(view, listener);
            return this;
        }

        public Builder setCancelable(boolean cancelable) {
            P.mCancelable = cancelable;
            return this;
        }

        public Builder setOnCancelListener(OnCancelListener onCancelListener) {
            P.mOnCancelListener = onCancelListener;
            return this;
        }

        public Builder setOnDismissListener(OnDismissListener onDismissListener) {
            P.mOnDismissListener = onDismissListener;
            return this;
        }

        public Builder setOnKeyListener(OnKeyListener onKeyListener) {
            P.mOnKeyListener = onKeyListener;
            return this;
        }

        public Builder fullWidth() {
            P.mWidth = ViewGroup.LayoutParams.MATCH_PARENT;
            return this;
        }

        /**
         * 从底部弹出
         * @param isAnimation 是否有动画
         */
        public Builder fromBottom(boolean isAnimation) {
            if (isAnimation) {
                P.mAnimations = R.style.dialog_from_bottom_anim;
            }
            P.mGravity = Gravity.BOTTOM;
            return this;
        }

        /**
         * 设置宽高
         */
        public Builder setWidthAndHeight(int width, int height) {
            P.mWidth = width;
            P.mHeight = height;
            return this;
        }

        /**
         * 设置默认动画
         */
        public Builder addDefaultAnimation() {
            P.mAnimations = R.style.dialog_scale_anim;
            return this;
        }

        /**
         * 设置动画。
         */
        public Builder setAnimations(int styleAnimation) {
            P.mAnimations = styleAnimation;
            return this;
        }

        public AlertDialog create() {
            final AlertDialog dialog = new AlertDialog(P.mContext, P.mThemeResId);
            P.apply(dialog.mAlert);

            dialog.setCancelable(P.mCancelable);
            if (P.mCancelable) {
                dialog.setCanceledOnTouchOutside(true);
            }
            dialog.setOnCancelListener(P.mOnCancelListener);
            dialog.setOnDismissListener(P.mOnDismissListener);
            if (P.mOnKeyListener != null) {
                dialog.setOnKeyListener(P.mOnKeyListener);
            }
            return dialog;
        }

        /**
         * show dialog
         */
        public AlertDialog show() {
            final AlertDialog dialog = create();
            dialog.show();
            return dialog;
        }

    }
}
