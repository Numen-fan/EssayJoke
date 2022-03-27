package com.jiajia.baselibrary;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

/**
 * Created by Numen_fan on 2022/3/23
 * Desc: 崩溃搜集，单例
 */
public class ExceptionCrashHandler implements Thread.UncaughtExceptionHandler {
    private static final String TAG = "ExceptionCrashHandler";

    private static ExceptionCrashHandler mInstance;

    private Context mContext;

    private Thread.UncaughtExceptionHandler mDefaultExceptionHandler;

    public static ExceptionCrashHandler getInstance() {
        if (mInstance == null) {
            synchronized (ExceptionCrashHandler.class) {
                if (mInstance == null) {
                    mInstance = new ExceptionCrashHandler();
                }
            }
        }
        return mInstance;
    }

    public void init(Context context) {
        this.mContext = context;
        // 设置全局异常捕捉类
        Thread.currentThread().setUncaughtExceptionHandler(this);

        Thread.currentThread();
        this.mDefaultExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
    }

    @Override
    public void uncaughtException(@NonNull Thread t, @NonNull Throwable e) {
        // 全局异常
        Log.e(TAG, "全局异常了");

        // 写文件，上报服务器等
        // 1 崩溃的详细信息 2 应用信息 报名，版本号  3 手机信息  4 上传问题，上传文件不在这里处理


        // 系统的默认处理还是要有的（崩溃闪退等）
        mDefaultExceptionHandler.uncaughtException(t, e);
    }
}
