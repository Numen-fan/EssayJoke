package com.jiajia.framelibrary.http;

import android.text.TextUtils;

import com.jiajia.framelibrary.db.DaoSupportFactory;
import com.jiajia.framelibrary.db.IDaoSupport;

import java.util.List;

/**
 * Created by Numen_fan on 2022/4/14
 * Desc:
 */
public class CacheDataUtil {

    static final IDaoSupport<CacheData> daoSupport = DaoSupportFactory.getFactory().getDao(CacheData.class);

    public static String getCacheResultJson(String queryUrl) {

        List<CacheData> cacheDataList = daoSupport.querySupport().selection("mUrlKey=?").selectionArgs(queryUrl).query();
        if (cacheDataList.size() > 0) {
            CacheData cacheData = cacheDataList.get(0);
            String resultJson = cacheData.getResultJson();
            if (!TextUtils.isEmpty(resultJson)) {
                return resultJson;
            }
        }
        return "";
    }

    public static long updateCacheData(String url, String data) {
        // 缓存数据
        final IDaoSupport<CacheData> daoSupport = DaoSupportFactory.getFactory().getDao(CacheData.class);
        daoSupport.delete("mUrlKey=?", new String[]{url});
        return daoSupport.insert(new CacheData(url, data));
    }
}
