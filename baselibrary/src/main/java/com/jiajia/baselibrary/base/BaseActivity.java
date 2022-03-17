package com.jiajia.baselibrary.base;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.jiajia.baselibrary.ioc.ViewUtils;

/**
 * Created by Numen_fan on 2022/3/16
 * Desc:
 */
public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(getLayoutId());

        ViewUtils.inject(this);

        initTitle();

        initView();

        initListener();

        initData();

    }

    protected abstract @LayoutRes int getLayoutId();

    protected abstract void initTitle();

    protected abstract void initView();

    protected abstract void initListener();

    protected abstract void initData();

    /**
     * 启动Activity
     * 不具备通用型，比如Intent中需要有参数等
     */
    protected void startActivity(Class<?> clazz) {
        Intent intent = new Intent(this, clazz);
        startActivity(intent);

    }
}
