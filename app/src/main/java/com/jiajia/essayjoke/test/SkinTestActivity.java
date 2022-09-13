package com.jiajia.essayjoke.test;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.app.ActivityCompat;

import com.jiajia.essayjoke.R;
import com.jiajia.essayjoke.selectimage.ImageSelector;
import com.jiajia.framelibrary.BaseSkinActivity;
import com.jiajia.framelibrary.skin.SkinManager;

import java.io.File;
import java.util.ArrayList;

public class SkinTestActivity extends BaseSkinActivity {

    private static final String TAG = "SkinTestActivity";

    final String[] permissions = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
    };


    private ArrayList<String> mImageList;

    private ActivityResultLauncher<Intent> launcher;

    private ActivityResultLauncher<String[]> resultLauncher;

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

            // 图片选择器
//            selectImage();

            startActivity(TestPluglnActivity.class);


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
        // 6.0 请求权限，读写内存卡，拍照
        if (lackPermissions()) {
            resultLauncher.launch(permissions);
        } else {
            openSelectImagePage();
        }
    }

    private void openSelectImagePage() {
        ImageSelector.create().count(9).multi().origin(mImageList)
                .showCamera(true).start(this, launcher);
    }

    @Override
    protected void initData() {

        // 接收被选择的图片
        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                mImageList = result.getData().getStringArrayListExtra("EXTRA_RESULT");
                Log.e(TAG, mImageList.toString());
            }
        });

        // 请求权限
        resultLauncher = registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), result -> {
            for (Boolean granted : result.values()) {
                if (!granted) {
                    Toast.makeText(SkinTestActivity.this, "允许了才能使用", Toast.LENGTH_SHORT).show();
                }
            }
            openSelectImagePage();
        });
    }

    // 校验权限
    private boolean lackPermissions() {
        for (String permission : permissions) {
            if (ActivityCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                return true;
            }
        }
        return false;
    }
}