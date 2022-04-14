package com.jiajia.essayjoke;

import android.app.Application;

import com.jiajia.baselibrary.ExceptionCrashHandler;
import com.jiajia.baselibrary.http.HttpUtils;
import com.jiajia.framelibrary.http.OkHttpEngine;

/**
 * Created by Numen_fan on 2022/3/23
 * Desc:
 */
public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        HttpUtils.init(new OkHttpEngine());

        ExceptionCrashHandler.getInstance().init(this);

    }
}
