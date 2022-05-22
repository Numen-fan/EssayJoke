package com.jiajia.framelibrary.skin;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.util.Log;

import androidx.core.content.res.ResourcesCompat;

import java.io.File;
import java.lang.reflect.Method;

/**
 * Created by Numen_fan on 2022/4/24
 * Desc: 皮肤资源类
 */
public class SkinResource {
    private static final String TAG = "SkinResource";

    // 资源通过这个对象获取
    private Resources mSkinResource;
    private String mPackageName;

    public SkinResource(Context context, String skinPath) {

        try {
            // 创建AssetManager, 并执行addAssetPath方法
            AssetManager manager = AssetManager.class.newInstance();
            Method method = AssetManager.class.getDeclaredMethod("addAssetPath", String.class);
            method.invoke(manager, skinPath);

            Resources superRes = context.getResources();
            mSkinResource= new Resources(manager, superRes.getDisplayMetrics(), superRes.getConfiguration());

            // 获取包名
            mPackageName = context.getPackageManager().getPackageArchiveInfo(
                    skinPath, PackageManager.GET_ACTIVITIES).applicationInfo.packageName;

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 通过名字获取Drawable
     * @param resName
     * @return
     */
    public Drawable getDrawableByName(String resName) {
        try {
            int resId = mSkinResource.getIdentifier(resName, "drawable", mPackageName);
            Drawable drawable = mSkinResource.getDrawable(resId);
            return drawable;
        } catch (Exception e) {
            Log.e(TAG, "getDrawableByName error" + e.getMessage());
            return null;
        }
    }


    public ColorStateList getColorByName(String resName) {
        try {
            int resId = mSkinResource.getIdentifier(resName, "color", mPackageName);
            ColorStateList colors = mSkinResource.getColorStateList(resId);
            return colors;
        } catch (Exception e) {
            Log.e(TAG, "getColorByName error" + e.getMessage());
            return null;
        }
    }
}
