package com.jiajia.essayjoke;

import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import com.jiajia.baselibrary.ioc.CheckNet;
import com.jiajia.baselibrary.ioc.OnClick;
import com.jiajia.baselibrary.ioc.ViewById;
import com.jiajia.framelibrary.BaseSkinActivity;

public class MainActivity extends BaseSkinActivity {

    @ViewById(R.id.test_tv)
    private TextView mTestTv;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initTitle() {

    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initData() {

    }

    @OnClick({R.id.test_tv, R.id.test_iv})
    @CheckNet   // 此方法需要check网络连接状态
    private void onClick(View view) {
        Toast.makeText(this, "点击了我", Toast.LENGTH_SHORT).show();
    }
}