package com.jiajia.framelibrary.skin.config;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Numen_fan on 2022/5/23
 * Desc:
 */
public class SkinPrefUtils {


    private static volatile SkinPrefUtils mInstance;

    private final Context mContext;

    private SkinPrefUtils(Context context) {
        this.mContext = context.getApplicationContext();

    }

    public static SkinPrefUtils getInstance(Context context) {
        if (mInstance == null) {
            synchronized (SkinPrefUtils.class) {
                if (mInstance == null) {
                    mInstance = new SkinPrefUtils(context);
                }
            }
        }
        return mInstance;
    }

    /**
     * 保存当前皮肤路径
     * @param skinPath
     */
    public void saveSkinPath(String skinPath) {

        SharedPreferences sp = mContext.getSharedPreferences(SkinConfig.SKIN_INFO_NAME, Context.MODE_PRIVATE);
        sp.edit().putString(SkinConfig.SKIN_PATH_NAME, skinPath).apply();
    }

    /**
     * 获取当前皮肤路径
     */
    public String getSkinPath() {
        SharedPreferences sp = mContext.getSharedPreferences(SkinConfig.SKIN_INFO_NAME, Context.MODE_PRIVATE);
        return sp.getString(SkinConfig.SKIN_PATH_NAME, "");
    }

    /**
     * 清空皮肤路径
     */
    public void clearSkinPath() {
        saveSkinPath("");
    }

}
