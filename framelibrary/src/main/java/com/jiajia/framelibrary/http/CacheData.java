package com.jiajia.framelibrary.http;

/**
 * Created by Numen_fan on 2022/4/11
 * Desc: Http数据库缓存
 */
public class CacheData {

    private String mUrlKey;

    private String mResultJson;

    public CacheData() {

    }

    public CacheData(String urlkey, String resultJson) {
        this.mUrlKey = urlkey;
        this.mResultJson = resultJson;
    }

    public String getUrlKey() {
        return mUrlKey;
    }

    public void setUrlKey(String mUrlKey) {
        this.mUrlKey = mUrlKey;
    }

    public String getResultJson() {
        return mResultJson;
    }

    public void setResultJson(String mResultJson) {
        this.mResultJson = mResultJson;
    }


}
