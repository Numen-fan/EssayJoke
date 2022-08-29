package com.jiajia.essayjoke.selectimage;


import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.core.os.EnvironmentCompat;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jiajia.baselibrary.ioc.OnClick;
import com.jiajia.baselibrary.ioc.ViewById;
import com.jiajia.essayjoke.R;
import com.jiajia.framelibrary.BaseSkinActivity;
import com.jiajia.framelibrary.DefaultNavigationBar;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * 图片选择器
 */
public class SelectImageActivity extends BaseSkinActivity implements SelectImageListener {

    private static final String TAG = "SelectImageActivity";

    // 带过来的Key
    // 是否显示相机的EXTRA_KEY
    public static final String EXTRA_SHOW_CAMERA = "EXTRA_SHOW_CAMERA";
    // 总共可以选择多少张图片的EXTRA_KEY
    public static final String EXTRA_SELECT_COUNT = "EXTRA_SELECT_COUNT";
    // 原始的图片路径的EXTRA_KEY
    public static final String EXTRA_DEFAULT_SELECTED_LIST = "EXTRA_DEFAULT_SELECTED_LIST";
    // 选择模式的EXTRA_KEY
    public static final String EXTRA_SELECT_MODE = "EXTRA_SELECT_MODE";
    // 返回选择图片列表的EXTRA_KEY
    public static final String EXTRA_RESULT = "EXTRA_RESULT";

    private static final String pictureFileBasePath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator;

    // 加载所有的数据
    private static final int LOADER_TYPE = 0x0021;


    /*****************
     * 获取传递过来的参数
     *****************/
    // 选择图片的模式 - 多选
    public static final int MODE_MULTI = 0x0011;
    // 选择图片的模式 - 单选
    public static int MODE_SINGLE = 0x0012;
    // 单选或者多选，int类型的type
    private int mMode = MODE_MULTI;
    // int 类型的图片张数
    private int mMaxCount = 8;
    // boolean 类型的是否显示拍照按钮
    private boolean mShowCamera = true;
    // ArraryList<String> 已经选择好的图片
    private ArrayList<String> mResultList;

    @ViewById(R.id.image_list_rv)
    private RecyclerView mImageListRv;

    @ViewById(R.id.select_num)
    private TextView tvSelectNum;

    @ViewById(R.id.select_preview)
    private View previewBtn;

    @ViewById(R.id.select_finish)
    private View btnSelectFinish;

    private ActivityResultLauncher<Intent> takePhotoLauncher;

    private String picturePath = null;

    //用于保存拍照图片的uri,android 10以上
    private Uri mCameraUri;

    // 用于保存图片的文件路径，Android 10以下使用图片路径访问图片
    private String mCameraImagePath;


    private  final ArrayList<String> images = new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_select_image;
    }

    @Override
    protected void initTitle() {
        DefaultNavigationBar navigationBar = new DefaultNavigationBar.Builder(this)
                .setTitle("所有图片")
                .build();
    }

    @Override
    protected void initView() {


    }

    @Override
    protected void initListener() {

        // 注册拍照launcher
        takePhotoLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result == null || result.getResultCode() != RESULT_OK) {
                return;
            }

            // 拍照成功
            int insertIndex = mShowCamera ? 1 : 0; // 在最前面
            String path = getUriPath();
            images.add(insertIndex, path);
            mResultList.add(path);
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        loadImages();
    }

    @Override
    protected void initData() {

        // 1.获取传递过来的参数
        Intent intent = getIntent();
        mMode = intent.getIntExtra(EXTRA_SELECT_MODE, mMode);
        mMaxCount = intent.getIntExtra(EXTRA_SELECT_COUNT, mMaxCount);
        mShowCamera = intent.getBooleanExtra(EXTRA_SHOW_CAMERA, mShowCamera);
        mResultList = intent.getStringArrayListExtra(EXTRA_DEFAULT_SELECTED_LIST);
        if (mResultList == null) {
            mResultList = new ArrayList<>();
        }

        if (mShowCamera) {
            images.add("");
        }
    }

    private void loadImages() {
        // 2 加载本地图片数据
        initImageList();

        // 3 改变显示
        exchangeViewShow();
    }

    /**
     * 改变预览， 每次选择都要刷新
     */
    @SuppressLint("SetTextI18n")
    private void exchangeViewShow() {

        if (mResultList.size() > 0) {
            previewBtn.setEnabled(true);
            previewBtn.setOnClickListener(v -> {});
        } else {
            previewBtn.setEnabled(false);
            previewBtn.setOnClickListener(null);
        }

        tvSelectNum.setText(mResultList.size() + "/" + mMaxCount);
    }

    @OnClick(R.id.select_finish)
    private void selectFinish(View view) {
        // 将选好的图片传到前一个页面
        Intent intent = new Intent();
        intent.putStringArrayListExtra(EXTRA_RESULT, mResultList);
        setResult(RESULT_OK, intent);
        finish();
    }

    /**
     * 2 ContentProvider获取内存卡中的所有图片
     */
    private void initImageList() {

        // 耗时操作，开线程、AsyncTask（都不建议）
        // 使用LoadManager
        LoaderManager.getInstance(this).initLoader(LOADER_TYPE, null, mLoaderCallback);
    }

    /**
     * 加载图片的CallBack
     */
    private final LoaderManager.LoaderCallbacks<Cursor> mLoaderCallback =
            new LoaderManager.LoaderCallbacks<Cursor>() {

                private final String[] IMAGE_PROJECTION = {
                        MediaStore.Images.Media.DATA,
                        MediaStore.Images.Media.DISPLAY_NAME,
                        MediaStore.Images.Media.DATE_ADDED,
                        MediaStore.Images.Media.MIME_TYPE,
                        MediaStore.Images.Media.SIZE,
                        MediaStore.Images.Media._ID};

                @NonNull
                @Override
                public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
                    // 查询数据库一样 语句
                    return new CursorLoader(SelectImageActivity.this,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_PROJECTION,
                            IMAGE_PROJECTION[4] + ">0 AND " + IMAGE_PROJECTION[3] + "=? OR "
                                    + IMAGE_PROJECTION[3] + "=?",
                            new String[]{"image/jpeg", "image/png"}, IMAGE_PROJECTION[2] + " DESC");
                }



                @Override
                public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
                    // 解析，封装到集合  只保存String路径
                    if (data != null && data.getCount() > 0) {

                        data.moveToFirst();

                        // 不断的遍历循环
                        while (data.moveToNext()) {
                            // 只保存路径
                            String path = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[0]));
                            images.add(path);
                        }

                        // 显示列表数据
                        showImageList(images);
                    }
                }

                @Override
                public void onLoaderReset(Loader<Cursor> loader) {

                }
            };

    /**
     * 3.展示获取到的图片显示到列表
     */
    private void showImageList(ArrayList<String> images) {
        SelectImageListAdapter listAdapter = new SelectImageListAdapter(this, images, mResultList);
        mImageListRv.setLayoutManager(new GridLayoutManager(this, 4));
        listAdapter.setSelectListener(this);
        mImageListRv.setAdapter(listAdapter);
    }


    @Override
    public void select() {
        exchangeViewShow();
    }

    @Override
    public void takePhoto() {
        // 调起相近进行拍照，此时已经活得了相机的权限
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        picturePath = pictureFileBasePath + "IMG_" + System.currentTimeMillis() + ".jpg";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // Android 10 适配
            mCameraUri = createImageUri();
        } else {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (photoFile != null) {
                mCameraImagePath = photoFile.getAbsolutePath();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    //适配Android 7.0文件权限，通过FileProvider创建一个content类型的Uri
                    mCameraUri = FileProvider.getUriForFile(this, getPackageName() + ".fileprovider", photoFile);
                } else {
                    mCameraUri = Uri.fromFile(photoFile);
                }
            }
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mCameraUri);
        takePhotoLauncher.launch(intent);
    }

    /**
     * 创建图片地址uri,用于保存拍照后的照片 Android 10以后使用这种方法
     */
    private Uri createImageUri() {
        String status = Environment.getExternalStorageState();
        // 判断是否有SD卡,优先使用SD卡存储,当没有SD卡时使用手机存储
        if (status.equals(Environment.MEDIA_MOUNTED)) {
            return getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new ContentValues());
        } else {
            return getContentResolver().insert(MediaStore.Images.Media.INTERNAL_CONTENT_URI, new ContentValues());
        }
    }

    /**
     * 创建保存图片的文件
     */
    private File createImageFile() throws IOException {
        String imageName = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if (!storageDir.exists()) {
            storageDir.mkdir();
        }
        File tempFile = new File(storageDir, imageName);
        if (!Environment.MEDIA_MOUNTED.equals(EnvironmentCompat.getStorageState(tempFile))) {
            return null;
        }
        return tempFile;
    }

    // 从uri中解析出path
    private String getUriPath() {
        String path = "";
        Cursor cursor = getContentResolver().query(mCameraUri, new String[]{MediaStore.Images.Media.DATA}, null, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                if (columnIndex > -1) {
                    path = cursor.getString(columnIndex);
                }
            }
            cursor.close();
        }
        return path;
    }

}