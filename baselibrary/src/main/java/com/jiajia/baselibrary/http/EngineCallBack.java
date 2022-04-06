package com.jiajia.baselibrary.http;

/**
 * Created by Numen_fan on 2022/4/5
 * Desc: http请求回调
 */
public interface EngineCallBack {

    // 错误
    void onError(Exception e);

    // 成功
    void onSuccess(String result);

    // 默认的
    EngineCallBack DEFAULT_CALLBACK = new EngineCallBack() {

        @Override
        public void onError(Exception e) {

        }

        @Override
        public void onSuccess(String result) {

        }
    };

}
