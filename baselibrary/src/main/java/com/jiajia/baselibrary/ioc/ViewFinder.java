package com.jiajia.baselibrary.ioc;

import android.app.Activity;
import android.view.View;

/**
 * Created by Numen_fan on 2022/3/13
 * Desc: View的findViewById的辅助类
 */
public class ViewFinder {

    private Activity mActivity;
    private View mView;


    public ViewFinder(Activity activity) {
        this.mActivity = activity;

    }

    public ViewFinder(View view) {
        this.mView = view;
    }

    public View findViewById(int viewId) {
        return mActivity != null
                ? mActivity.findViewById(viewId) : mView.findViewById(viewId);
    }
}
