package com.jiajia.essayjoke.test;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.ActivityResultRegistry;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.jiajia.baselibrary.fixbug.FixDexManager;
import com.jiajia.baselibrary.ioc.ViewById;
import com.jiajia.essayjoke.R;
import com.jiajia.framelibrary.BaseSkinActivity;
import com.jiajia.framelibrary.db.DaoSupport;
import com.jiajia.framelibrary.db.DaoSupportFactory;
import com.jiajia.framelibrary.db.IDaoSupport;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class TestActivity extends BaseSkinActivity {

    private static final String TAG = "TestActivity";

    private ActivityResultLauncher<Intent> mRegister;

    @ViewById(R.id.image)
    private ImageView mImage;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_test;
    }

    @Override
    protected void initTitle() {

    }

    @Override
    protected void initView() {

    }

    public void testDataBase() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 10086);
        } else {
            // 热修复
//          fixDexBug();

            // 数据库读取
//          IDaoSupport<Person> dao = DaoSupportFactory.getFactory().getDao(Person.class);
//          List<Person> list = dao.querySupport().selection("age > ?").selectionArgs("25").query();
//          System.out.println(list);

            // 插件换肤

        }
    }


    @Override
    protected void initListener() {
        findViewById(R.id.btn_test).setOnClickListener( v -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && !Environment.isExternalStorageManager()) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                mRegister.launch(intent);
            } else {
//                testDataBase();
                changeSkin();
            }
        });
    }

    @Override
    protected void initData() {
        mRegister = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == PackageManager.PERMISSION_GRANTED) {
//                        testDataBase();
                        changeSkin();
                    }
                });
    }

    private void fixDexBug() {
        File fixFile = new File(Environment.getExternalStorageDirectory(), "fix.dex");

        if (fixFile.exists()) {
            FixDexManager fixDexManager = new FixDexManager(this);
            try {
                fixDexManager.fixDex(fixFile.getAbsolutePath());
                Toast.makeText(this, "修复成功", Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "修复失败", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void changeSkin() {
        try {
            // 创建AssetManager, 并执行addAssetPath方法
            AssetManager manager = AssetManager.class.newInstance();
            Method method = AssetManager.class.getDeclaredMethod("addAssetPath", String.class);
            method.invoke(manager, Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator
                    + "red.skin");

            Resources superRes = getResources();
            Resources resources = new Resources(manager, superRes.getDisplayMetrics(), superRes.getConfiguration());

            int drawableId = resources.getIdentifier("icon", "mipmap", "com.jiajia.mypractisedemos");
            mImage.setBackground(ResourcesCompat.getDrawable(resources, drawableId, null));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 10086 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "申请文件访问权限成功");
//            DaoSupportFactory.getFactory().getDao(Person.class);
            changeSkin();
        } else {
            Toast.makeText(this, "权限申请失败, 无法使用", Toast.LENGTH_LONG).show();
        }
    }
}