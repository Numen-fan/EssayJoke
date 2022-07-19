package com.jiajia.essayjoke.fragment;


import android.annotation.SuppressLint;
import android.view.View;

import com.jiajia.baselibrary.base.BaseFragment;
import com.jiajia.baselibrary.http.HttpUtils;
import com.jiajia.baselibrary.ioc.OnClick;
import com.jiajia.baselibrary.ioc.ViewById;
import com.jiajia.essayjoke.R;
import com.jiajia.essayjoke.entity.Article;
import com.jiajia.framelibrary.http.HttpCallBack;
import com.jiajia.framelibrary.http.HttpConstant;
import com.jiajia.framelibrary.http.HttpLogic;
import com.jiajia.framelibrary.http.OkHttpEngine;

/**
 * Created by Numen_fan on 2016/12/3.
 */
public class HomeFragment extends BaseFragment {

    @SuppressLint("NonConstantResourceId")
    @ViewById(R.id.test)
    View testView;

    @Override
    protected void initView() {
        testView = rootView.findViewById(R.id.test);


    }

    @Override
    protected void initData() {



    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home;
    }

    @OnClick(R.id.test)
    private void getArticle(View v) {
        HttpUtils.with(getContext()).url(HttpLogic.getArticleUrl(0)).cache(true).get().execute();
    }

}
