package com.jiajia.framelibrary;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.LayoutInflaterCompat;

import com.jiajia.baselibrary.base.BaseActivity;
import com.jiajia.framelibrary.skin.SkinManager;
import com.jiajia.framelibrary.skin.SkinResource;
import com.jiajia.framelibrary.skin.attr.SkinAttr;
import com.jiajia.framelibrary.skin.SkinView;
import com.jiajia.framelibrary.skin.callback.ISkinChangeListener;
import com.jiajia.framelibrary.skin.support.SkinAppCompatViewInflater;
import com.jiajia.framelibrary.skin.support.SkinAttrSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Numen_fan on 2022/3/17
 * Desc:
 */
public abstract class BaseSkinActivity extends BaseActivity implements LayoutInflater.Factory2, ISkinChangeListener {

    // 后面的插件换肤 预留的东西

    private SkinAppCompatViewInflater mSkinAppCompatViewInflater;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        LayoutInflater layoutInflater = LayoutInflater.from(this);
        LayoutInflaterCompat.setFactory2(layoutInflater, this);

        super.onCreate(savedInstanceState);

    }


    @Nullable
    @Override
    public View onCreateView(@Nullable View parent, @NonNull String name, @NonNull Context context, @NonNull AttributeSet attrs) {
        // 拦截View的创建，获取View之后要去解析
        // 1 创建view
        View view = createView(parent, name, context, attrs);

//        Log.e("TAG", view + "");

        // 2 解析属性 src textColor background 自定义的属性
        // 2.1 一个Activity的布局对应多个这样的SkinView
        if (view != null) {
            List<SkinAttr> skinAttrs = SkinAttrSupport.getSkinAttrs(context, attrs);
            SkinView skinView = new SkinView(view, skinAttrs);
            // 3 统一交给SkinManager管理
            manageSkinView(skinView);

            // 4 判断一下是否需要换肤
            SkinManager.getInstance().checkChangeSkin(skinView);

        }

        return view;
    }

    @Override
    public void changeSkin(SkinResource skinResource) {
        // 在=做一些第三方的改变，比如这个页面使用了第三方的view

    }

    /**
     * 统一管理SkinView
     */
    private void manageSkinView(SkinView skinView) {

        List<SkinView> skinViews = SkinManager.getInstance().getSkinViews(this);

        if (skinViews == null) {
            skinViews = new ArrayList<>();
            SkinManager.getInstance().register(this, skinViews);
        }

        // 新建的view添加到
        skinViews.add(skinView);
    }

    private View createView(View parent, String name, Context context, AttributeSet attrs) {
        if (mSkinAppCompatViewInflater == null) {
            mSkinAppCompatViewInflater = new SkinAppCompatViewInflater();
        }

        // inheritContext 始终为false, 因为最小为Android 21

        return mSkinAppCompatViewInflater.createView(parent, name, context, attrs);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        SkinManager.getInstance().unregister(this);
    }
}
