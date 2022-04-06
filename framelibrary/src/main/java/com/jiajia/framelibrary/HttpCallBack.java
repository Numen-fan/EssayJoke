package com.jiajia.framelibrary;

import android.content.Context;

import com.google.gson.Gson;
import com.jiajia.baselibrary.http.EngineCallBack;
import com.jiajia.baselibrary.http.HttpUtils;

import java.util.Map;

/**
 * Created by Numen_fan on 2022/4/6
 * Desc:
 */
public abstract class HttpCallBack<T> implements EngineCallBack {

    @Override
    public void onPreExecute(Context context, Map<String, Object> params) {
        // 在这里添加请求参数，与具体的业务逻辑相关
        // 项目名称，需要用context去获取
        params.put("app_name", "joke_essay");
        params.put("version_name", "5.7.0");
        params.put("ac", "wifi");
        params.put("device_id", "30036118478");
        params.put("device_brand", "Xiaomi");
        params.put("update_version_code", "5701");
        params.put("manifest_version_code", "570");
        params.put("longitude", "113.000366");
        params.put("latitude", "28.171377");
        params.put("device_platform", "android");

        onPreExecute();

    }

    // 开始执行了
    public void onPreExecute() {

    }

    @Override
    public void onSuccess(String result) {
        Gson gson = new Gson();
        T data = (T) gson.fromJson(result, HttpUtils.analysisClazzInfo(this));
        onSuccess(data);
    }

    // 返回直接可以操作的对象
    public abstract void onSuccess(T result);
}
