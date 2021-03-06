package com.jiajia.essayjoke.test;

import android.os.Environment;

import com.jiajia.essayjoke.R;
import com.jiajia.framelibrary.BaseSkinActivity;
import com.jiajia.framelibrary.skin.SkinManager;

import java.io.File;

public class SkinTestActivity extends BaseSkinActivity {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_skin_test;
    }

    @Override
    protected void initTitle() {

    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initListener() {
        // 换肤
        findViewById(R.id.btn_change_skin).setOnClickListener(v -> {
            String skinPath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "red.skin";
            SkinManager.getInstance().loadSkin(skinPath);

        });

        // 默认
        findViewById(R.id.btn_skin_default).setOnClickListener(v -> {
            SkinManager.getInstance().restoreDefault();
        });

        // 跳转
        findViewById(R.id.btn_skin_turn).setOnClickListener(v -> {
            startActivity(SkinTestActivity.class); // 跳转自己
        });
    }

    @Override
    protected void initData() {

    }
}