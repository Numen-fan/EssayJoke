package com.jiajia.essayjoke.test;

import android.content.Intent;
import android.os.Environment;
import android.util.Log;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import com.jiajia.essayjoke.R;
import com.jiajia.essayjoke.selectimage.SelectImageActivity;
import com.jiajia.framelibrary.BaseSkinActivity;
import com.jiajia.framelibrary.skin.SkinManager;

import java.io.File;
import java.util.ArrayList;

public class SkinTestActivity extends BaseSkinActivity {

    private static final String TAG = "SkinTestActivity";


    private ArrayList<String> mImageList;

    private ActivityResultLauncher<Intent> launcher;

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
//            SkinManager.getInstance().loadSkin(skinPath);

            selectImage();


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

    private void selectImage() {
        Intent intent = new Intent(this, SelectImageActivity.class);
        intent.putExtra(SelectImageActivity.EXTRA_SELECT_COUNT,9);
        intent.putExtra(SelectImageActivity.EXTRA_SELECT_MODE,SelectImageActivity.MODE_MULTI);
        intent.putStringArrayListExtra(SelectImageActivity.EXTRA_DEFAULT_SELECTED_LIST, mImageList);
        intent.putExtra(SelectImageActivity.EXTRA_SHOW_CAMERA, true);
        launcher.launch(intent);
    }

    @Override
    protected void initData() {
        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                mImageList = result.getData().getStringArrayListExtra("EXTRA_RESULT");
                Log.e(TAG, mImageList.toString());
            }
        });
    }
}