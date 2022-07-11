package com.jiajia.framelibrary.http;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;

import com.jiajia.baselibrary.http.EngineCallBack;
import com.jiajia.baselibrary.http.HttpUtils;
import com.jiajia.baselibrary.http.IHttpEngine;
import com.jiajia.framelibrary.db.DaoSupportFactory;
import com.jiajia.framelibrary.db.IDaoSupport;

import java.io.File;
import java.io.IOException;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Numen_fan on 2022/4/5
 * Desc: OkHttp引擎
 */
public class OkHttpEngine implements IHttpEngine {

    private static final String TAG = "OkHttpEngine";

    private static final OkHttpClient mOkHttpClient = new OkHttpClient();

    private static Handler mHandler = new Handler(Looper.getMainLooper());

    @Override
    public void get(Context context, String url, Map<String, Object> params, boolean cache, final EngineCallBack callBack) {

        final String queryUrl = HttpUtils.jointParams(url, params);

        Log.e("Get请求路径：", queryUrl);

        // 1. 缓存判断
        String cacheData = "";
        if (cache) {
            cacheData = CacheDataUtil.getCacheResultJson(queryUrl);
            if (!TextUtils.isEmpty(cacheData)) {
                Log.w(TAG, "有缓存数据");
                String finalCacheData = cacheData;
                mHandler.post(()->callBack.onSuccess(finalCacheData)); // 这个地方不能return，需要去查询最新的数据
            }
        }

        Request.Builder requestBuilder = new Request.Builder().url(queryUrl).tag(context);
        //可以省略，默认是GET请求
        Request request = requestBuilder.build();

        final String tempCacheData = cacheData;
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mHandler.post(()->callBack.onError(e));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.body() == null) {
                    callBack.onFail("response body is null");
                    return;
                }

                String resultJson = Objects.requireNonNull(response.body()).string();

                if (cache && !TextUtils.isEmpty(tempCacheData) && tempCacheData.equals(resultJson)) {
                    Log.w(TAG, "和缓存数据一致");
                    return;
                }

                // 执行成功方法
                mHandler.post(() -> callBack.onSuccess(resultJson));

                if (cache) {
                    CacheDataUtil.updateCacheData(url, resultJson);
                }
            }
        });
    }

    @Override
    public void post(Context context, String url, Map<String, Object> params, boolean cache, final EngineCallBack callBack) {
        final String jointUrl = HttpUtils.jointParams(url, params);  //打印
        Log.e("Post请求路径：", jointUrl);

        // 了解 OkHttp
        RequestBody requestBody = appendBody(params);
        Request request = new Request.Builder().url(url).tag(context)
                .post(requestBody)
                .build();

        mOkHttpClient.newCall(request).enqueue(
                new Callback() {
                    // 这个 两个回掉方法都不是在主线程中
                    @Override
                    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                        if (response.body() == null) {
                            callBack.onSuccess("response body is null");
                            return;
                        }
                        String result = Objects.requireNonNull(response.body()).string();
                        Log.e("Post返回结果：", result);
                        callBack.onSuccess(result);
                    }

                    @Override
                    public void onFailure(@NonNull Call call, @NonNull IOException e) {
                        callBack.onError(e);
                    }
                }
        );
    }

    /**
     * 组装post请求参数body
     */
    protected RequestBody appendBody(Map<String, Object> params) {
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);
        addParams(builder, params);
        return builder.build();
    }

    /**
     * 添加参数
     */
    private void addParams(MultipartBody.Builder builder, Map<String, Object> params) {
        if (params == null || params.isEmpty()) {
            return;
        }
        for (String key : params.keySet()) {
            builder.addFormDataPart(key, params.get(key) + "");
            Object value = params.get(key);
            if (value instanceof File) {
                // 处理文件 --> Object File
                File file = (File) value;
                builder.addFormDataPart(key, file.getName(), RequestBody
                        .create(MediaType.parse(guessMimeType(file
                                .getAbsolutePath())), file));
            } else if (value instanceof List) {
                // 代表提交的是 List集合
                try {
                    // 但集合的类型并不一定是File啊
                    List<File> listFiles = (List<File>) value;
                    for (int i = 0; i < listFiles.size(); i++) {
                        // 获取文件
                        File file = listFiles.get(i);
                        builder.addFormDataPart(key + i, file.getName(), RequestBody
                                .create(MediaType.parse(guessMimeType(file
                                        .getAbsolutePath())), file));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                builder.addFormDataPart(key, value + "");
            }
        }
    }

    /**
     * 猜测文件类型
     */
    private String guessMimeType(String path) {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String contentTypeFor = fileNameMap.getContentTypeFor(path);
        if (contentTypeFor == null) {
            contentTypeFor = "application/octet-stream";
        }
        return contentTypeFor;
    }
}
