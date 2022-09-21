package com.jiajia.essayjoke;

import android.app.Application;

import com.jiajia.baselibrary.ExceptionCrashHandler;
import com.jiajia.baselibrary.http.HttpUtils;
import com.jiajia.essayjoke.activity.ProxyActivity;
import com.jiajia.essayjoke.utils.HookStartActivityUtil;
import com.jiajia.framelibrary.http.OkHttpEngine;
import com.jiajia.framelibrary.skin.SkinManager;

/**
 * Created by Numen_fan on 2022/3/23
 * Desc:
 */
public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        HttpUtils.init(new OkHttpEngine());

        SkinManager.getInstance().init(this);

        ExceptionCrashHandler.getInstance().init(this);
        HookStartActivityUtil hookStartActivityUtil = new HookStartActivityUtil(this, ProxyActivity.class);
        try {
            hookStartActivityUtil.hookStartActivity();
            hookStartActivityUtil.hookLaunchActivity();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
