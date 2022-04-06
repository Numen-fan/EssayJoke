package com.jiajia.baselibrary.http;

import android.content.Context;
import android.util.ArrayMap;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;

/**
 * Created by Numen_fan on 2022/4/5
 * Desc: 自己网络请求工具
 */
public class HttpUtils{

    private String mUrl;
    private int mType = GET;

    private static final int POST = 0x0001;
    private static final int GET = 0x0010;

    private final Map<String, Object> mParams;

    // 上下文
    private final Context mContext;

    private HttpUtils(Context context) {
        this.mContext = context;
        mParams = new ArrayMap<>();
    }

    // 给一个默认的Engine
    private static IHttpEngine mHttpEngine = new OkHttpEngine();

    // 初始化Engine
    public static void init(IHttpEngine httpEngine) {
        mHttpEngine = httpEngine;
    }

    // 更换Engine
    public void exchangeEngine(IHttpEngine httpEngine) {
        mHttpEngine = httpEngine;
    }

    // ========== start of 链式调用 ============

    /**
     * 调用方式入口
     */
    public static HttpUtils with(Context context) {
        return new HttpUtils(context);
    }

    public HttpUtils url(String url) {
        this.mUrl = url;
        return this;
    }

    public HttpUtils post() {
        mType = POST;
        return this;
    }

    public HttpUtils get() {
        mType = GET;
        return this;
    }

    /**
     * 添加参数
     */
    public HttpUtils addParam(String key, Object value) {
        mParams.put(key, value);
        return this;
    }

    public HttpUtils addParam(Map<String, Object> params) {
        mParams.putAll(params);
        return this;
    }

    /**
     * 添加回调，执行
     */
    public void execute(EngineCallBack callBack) {
        if (callBack == null) {
            callBack = EngineCallBack.DEFAULT_CALLBACK;
        }
        // 判断执行方法
        if (mType == POST) {
            post(mUrl, mParams, callBack);
        } else if (mType == GET) {
            get(mUrl, mParams, callBack);
        }
    }

    public void execute() {
        execute(null);
    }

    // ========== end of 链式调用 ============

    private void get(String url, Map<String, Object> params, EngineCallBack callBack) {
        mHttpEngine.get(mContext,url, params, callBack);

    }

    private void post(String url, Map<String, Object> params, EngineCallBack callBack) {
        mHttpEngine.post(mContext, url, params, callBack);
    }

    /**
     * 拼接参数
     */
    public static String jointParams(String url, Map<String, Object> params) {
        if (params == null || params.size() <= 0) {
            return url;
        }

        StringBuilder stringBuffer = new StringBuilder(url);
        // 处理url的最后一个字符
        if (!url.contains("?")) {
            stringBuffer.append("?");
        } else if (!url.endsWith("?")) { // 那就说明url包括了?, 且没在最后一个字符
            stringBuffer.append("&");
        }

        for (Map.Entry<String, Object> entry : params.entrySet()) {
            stringBuffer.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
        }

        stringBuffer.deleteCharAt(stringBuffer.length() - 1);

        return stringBuffer.toString();
    }

    /**
     * 解析一个类上面的class信息
     */
    public static Class<?> analysisClazzInfo(Object object) {
        Type genType = object.getClass().getGenericSuperclass();
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        return (Class<?>) params[0];
    }

}
