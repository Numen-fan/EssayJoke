package com.jiajia.essayjoke;

import android.app.Application;

import com.jiajia.baselibrary.ExceptionCrashHandler;

/**
 * Created by Numen_fan on 2022/3/23
 * Desc:
 */
public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        ExceptionCrashHandler.getInstance().init(this);

    }
}
