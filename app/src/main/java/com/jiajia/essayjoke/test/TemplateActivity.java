package com.jiajia.essayjoke.test;

import com.jiajia.baselibrary.http.HttpUtils;
import com.jiajia.framelibrary.BaseSkinActivity;
import com.jiajia.framelibrary.DefaultNavigationBar;
import com.jiajia.framelibrary.http.HttpCallBack;

/**
 * Created by Numen_fan on 2022/7/3
 * Desc:
 */
public class TemplateActivity  extends BaseSkinActivity {
    @Override
    protected int getLayoutId() {
        return 0;
    }

    @Override
    protected void initTitle() {

        DefaultNavigationBar navigationBar = new DefaultNavigationBar.Builder(this)
                .setTitle(" ")
                .build();

    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initData() {

//        HttpUtils.with(this).get().url("").execute(new HttpCallBack<T>() {
//            @Override
//            public void onSuccess(T result) {
//
//            }
//
//            @Override
//            public void onError(Exception e) {
//
//            }
//        });


    }
}
