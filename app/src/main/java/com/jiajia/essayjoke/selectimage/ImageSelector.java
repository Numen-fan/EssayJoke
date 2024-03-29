package com.jiajia.essayjoke.selectimage;

import static android.app.Activity.RESULT_OK;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import androidx.activity.ComponentActivity;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

/**
 * Created by Darren on 2016/12/21.
 * Email: 240336124@qq.com
 * Description: 图片选择的链式调用  这个类是公开的  SelectImageActivity 不公开的
 */

public class ImageSelector {
    // 最多可以选择多少张图片 - 默认8张
    private int mMaxCount = 9;
    // 选择图片的模式 - 默认多选
    private int mMode = SelectImageActivity.MODE_MULTI;
    // 是否显示拍照的相机
    private boolean mShowCamera = true;
    // 原始的图片
    private ArrayList<String> mOriginData;

    // 启动SelectImageActivity
    private ActivityResultLauncher<Intent> launcher;


    private ImageSelector() {

    }

    public static ImageSelector create() {
        return new ImageSelector();
    }

    /**
     * 单选模式
     */
    public ImageSelector single() {
        mMode = SelectImageActivity.MODE_SINGLE;
        return this;
    }

    /**
     * 多选模式
     */
    public ImageSelector multi() {
        mMode = SelectImageActivity.MODE_MULTI;
        return this;
    }

    /**
     * 设置可以选多少张图片
     */
    public ImageSelector count(int count) {
        mMaxCount = count;
        return this;
    }

    /**
     * 是否显示相机
     */
    public ImageSelector showCamera(boolean showCamera) {
        mShowCamera = showCamera;
        return this;
    }

    /**
     * 原来选择好的图片
     */
    public ImageSelector origin(ArrayList<String> originList) {
        this.mOriginData = originList;
        return this;
    }

    /**
     * 启动执行 权限6.0自己需要去申请，也可以用我的权限申请框架
     */
    public void start(Activity activity, ActivityResultLauncher<Intent> launcher) {
        Intent intent = new Intent(activity, SelectImageActivity.class);
        addParamsByIntent(intent);
        launcher.launch(intent);
    }

    /**
     * 启动执行 权限6.0自己需要去申请，也可以用我的权限申请框架
     */
    public void start(Fragment fragment, ActivityResultLauncher<Intent> launcher) {
        Intent intent = new Intent(fragment.getContext(), SelectImageActivity.class);
        addParamsByIntent(intent);
        launcher.launch(intent);
    }

    /**
     * 给Intent添加参数
     *
     * @param intent
     */
    private void addParamsByIntent(Intent intent) {
        intent.putExtra(SelectImageActivity.EXTRA_SHOW_CAMERA, mShowCamera);
        intent.putExtra(SelectImageActivity.EXTRA_SELECT_COUNT, mMaxCount);
        if (mOriginData != null && mMode == SelectImageActivity.MODE_MULTI) {
            intent.putStringArrayListExtra(SelectImageActivity.EXTRA_DEFAULT_SELECTED_LIST, mOriginData);
        }
        intent.putExtra(SelectImageActivity.EXTRA_SELECT_MODE, mMode);
    }

}
