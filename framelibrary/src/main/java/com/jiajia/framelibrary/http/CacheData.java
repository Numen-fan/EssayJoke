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

    public String getmUrlKey() {
        return mUrlKey;
    }

    public void setmUrlKey(String mUrlKey) {
        this.mUrlKey = mUrlKey;
    }

    public String getmResultJson() {
        return mResultJson;
    }

    public void setmResultJson(String mResultJson) {
        this.mResultJson = mResultJson;
    }


}
