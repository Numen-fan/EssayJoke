package com.jiajia.essayjoke;

import android.annotation.SuppressLint;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Environment;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.jiajia.baselibrary.http.HttpUtils;
import com.jiajia.baselibrary.ioc.CheckNet;
import com.jiajia.baselibrary.ioc.OnClick;
import com.jiajia.baselibrary.ioc.ViewById;
import com.jiajia.essayjoke.mode.DiscoverListResult;
import com.jiajia.essayjoke.mode.HomeMode;
import com.jiajia.framelibrary.BaseSkinActivity;
import com.jiajia.framelibrary.DefaultNavigationBar;
import com.jiajia.baselibrary.http.BaseMode;
import com.jiajia.framelibrary.http.HttpCallBack;

import java.io.File;
import java.lang.reflect.Method;

public class MainActivity extends BaseSkinActivity {

    private static final String TAG = "MainActivity";

    @SuppressLint("NonConstantResourceId")
    @ViewById(R.id.test_tv)
    private TextView mTestTv;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initTitle() {
        DefaultNavigationBar navigationBar =
                new DefaultNavigationBar.Builder(this)
                        .setTitle("投稿")
                        .setRightText("发布")
                        .setRightClickListener(v -> Toast.makeText(MainActivity.this , "发布了", Toast.LENGTH_SHORT).show())
                        .builder();
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

    @OnClick(R.id.test_iv)
    @CheckNet   // 此方法需要check网络连接状态
    private void onClick(View view) {
//        AlertDialog dialog = new AlertDialog.Builder(this)
//                .setContentView(R.layout.detail_comment_dialog)
//                .setText(R.id.submit_btn, "发送")
//                .fullWidth()
//                .fromBottom(true)
//                .show();
//
//        // Dialog去操作点击事件
//        final EditText commitEt = dialog.getView(R.id.comment_editor);
//        dialog.setOnclickListener(R.id.submit_btn,
//                v -> Toast.makeText(MainActivity.this, commitEt.getText().toString().trim(), Toast.LENGTH_SHORT).show());

//        startActivity(TestActivity.class);

        startActivity(SkinTestActivity.class);

//        queryData();

    }

    @OnClick(R.id.test_tv)
    private void tvClick(View view) {
        startActivity(TestActivity.class);
    }

    private void queryData() {
        // 路径 和 参数都需要放大JNI中，因为反编译会有问题
        HttpUtils.with(this).url("https://www.wanandroid.com/article/list/0/json")
                .cache(true).execute(new HttpCallBack<HomeMode>() {
            @Override
            public void onSuccess(HomeMode mode) {

            }

            @Override
            public void onError(Exception e) {

            }
        });
    }
}