package com.jiajia.baselibrary.http;

import android.content.Context;

import java.util.Map;

/**
 * Created by Numen_fan on 2022/4/5
 * Desc: 自己的网络引擎规范
 */
public interface IHttpEngine {

    // get 请求

    void get(Context context, String url, Map<String, Object> params, EngineCallBack callBack);

    // post 请求
    void post(Context context, String url, Map<String, Object> params, EngineCallBack callBack);

    // download

    // upload

    // https 添加证书
}
