package com.jiajia.essayjoke;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jiajia.baselibrary.dialog.AlertDialog;
import com.jiajia.baselibrary.http.EngineCallBack;
import com.jiajia.baselibrary.http.HttpUtils;
import com.jiajia.baselibrary.ioc.CheckNet;
import com.jiajia.baselibrary.ioc.OnClick;
import com.jiajia.baselibrary.ioc.ViewById;
import com.jiajia.essayjoke.mode.DiscoverListResult;
import com.jiajia.framelibrary.BaseSkinActivity;
import com.jiajia.framelibrary.DefaultNavigationBar;
import com.jiajia.framelibrary.HttpCallBack;

import java.util.Map;

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

        // 路径 和 参数都需要放大JNI中，因为反编译会有问题
//        HttpUtils.with(this).url("http://is.snssdk.com/2/essay/discovery/v3/")
//                .addParam("iid", "6152551759")
//                .addParam("aid", "7")
//                .execute(new HttpCallBack<DiscoverListResult>() {
//
//                    @Override
//                    public void onSuccess(DiscoverListResult result) {
//
//                    }
//
//                    @Override
//                    public void onError(Exception e) {
//
//                    }
//                });

    }

    @OnClick({R.id.test_iv, R.id.test_tv})
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

        startActivity(TestActivity.class);

    }
}