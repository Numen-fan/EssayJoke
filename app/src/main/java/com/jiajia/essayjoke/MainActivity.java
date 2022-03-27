package com.jiajia.essayjoke;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.jiajia.baselibrary.dialog.AlertDialog;
import com.jiajia.baselibrary.ioc.CheckNet;
import com.jiajia.baselibrary.ioc.OnClick;
import com.jiajia.baselibrary.ioc.ViewById;
import com.jiajia.framelibrary.BaseSkinActivity;

public class MainActivity extends BaseSkinActivity {

    private static final String TAG = "MainActivity";

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

    @OnClick({R.id.test_iv, R.id.test_tv})
    @CheckNet   // 此方法需要check网络连接状态
    private void onClick(View view) {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setContentView(R.layout.detail_comment_dialog)
                .setText(R.id.submit_btn, "发送")
                .fullWidth()
                .fromBottom(true)
                .show();

        // Dialog去操作点击事件
        final EditText commitEt = dialog.getView(R.id.comment_editor);
        dialog.setOnclickListener(R.id.submit_btn,
                v -> Toast.makeText(MainActivity.this, commitEt.getText().toString().trim(), Toast.LENGTH_SHORT).show());

    }
}