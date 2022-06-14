package com.jiajia.framelibrary.skin;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.util.ArrayMap;

import com.jiajia.framelibrary.skin.callback.ISkinChangeListener;
import com.jiajia.framelibrary.skin.config.SkinConfig;
import com.jiajia.framelibrary.skin.config.SkinPrefUtils;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Numen_fan on 2022/4/24
 * Desc: 皮肤管理类
 */
public class SkinManager {

    @SuppressLint("StaticFieldLeak")
    private static final SkinManager instance;
    private Context mContext;
    private SkinResource mSkinResource;

    private final Map<ISkinChangeListener, List<SkinView>> mSkinViews = new ArrayMap<>();

    static {
        instance = new SkinManager();
    }

    private SkinManager() { }

    public static SkinManager getInstance() {
        return instance;
    }

    public void init(Context context) {
        this.mContext = context.getApplicationContext();

        // 每次打开应用都会走到这里，防止皮肤被删除，需要做一下校验
        String curSkinPath = SkinPrefUtils.getInstance(mContext).getSkinPath();
        File file = new File(curSkinPath);

        if (!file.exists()) {
            SkinPrefUtils.getInstance(mContext).clearSkinPath();
            return;
        }

        // 校验一下  能否获取到包名
        String packageName = context.getPackageManager().getPackageArchiveInfo(
                curSkinPath, PackageManager.GET_ACTIVITIES).applicationInfo.packageName;
        if (TextUtils.isEmpty(packageName)) {
            return;
        }

        // 校验以下签名 增量更新的时候再说


        // 做一些初始化的工作
        mSkinResource = new SkinResource(mContext, curSkinPath);

    }

    /**
     * 加载皮肤
     * @param skinPath
     */
    public int loadSkin(String skinPath) {

        File file = new File(skinPath);
        if (!file.exists()) {
            return  SkinConfig.SKIN_FILE_NO_EXIST;
        }

        // 校验一下  能否获取到包名
        String packageName = mContext.getPackageManager().getPackageArchiveInfo(
                skinPath, PackageManager.GET_ACTIVITIES).applicationInfo.packageName;
        if (TextUtils.isEmpty(packageName)) {
            return SkinConfig.SKIN_FILE_NO_ERROR;
        }

        // 1. 当前皮肤如果一样就不需要更换了
        String currentSkinPath = SkinPrefUtils.getInstance(mContext).getSkinPath();
        if (TextUtils.equals(currentSkinPath, skinPath)) {
            return SkinConfig.SKIN_CHANGE_NOTHING;
        }

        // 校验签名 增量更新再说

        // skinPath判断，并并且进行资源包的复制

        changeSkin(skinPath);

        // 保存当前皮肤路径
        SkinPrefUtils.getInstance(mContext).saveSkinPath(skinPath);

        return SkinConfig.SKIN_CHANGE_SUCCESS;

    }

    /**
     * 恢复默认
     */
    public int restoreDefault() {

        String curSkinPath = SkinPrefUtils.getInstance(mContext).getSkinPath();
        if (TextUtils.isEmpty(curSkinPath)) {
            return SkinConfig.SKIN_CHANGE_NOTHING; // 当前压根无换肤，没必要执行后续操作
        }

        String skinPath = mContext.getPackageResourcePath(); // 当前apk的路径

        changeSkin(skinPath);

        SkinPrefUtils.getInstance(mContext).clearSkinPath();

        return SkinConfig.SKIN_CHANGE_SUCCESS;

    }

    private void changeSkin(String skinPath) {
        // 初始化资源管理
        mSkinResource =  new SkinResource(mContext, skinPath);

        // 改变皮肤
        Set<ISkinChangeListener> keys =  mSkinViews.keySet();

        for (ISkinChangeListener key : keys) {
            List<SkinView> skinViews = mSkinViews.get(key);
            if (skinViews == null) {
                continue;
            }

            for (SkinView skinView : skinViews) {
                skinView.skin();
            }

            key.changeSkin(mSkinResource);
        }
    }

    /**
     * 拿到这个activity的所有SkinView
     */
    public List<SkinView> getSkinViews(ISkinChangeListener listener) {
        return  mSkinViews.get(listener);
    }

    /**
     * 注册
     */
    public void register(ISkinChangeListener listener, List<SkinView> skinViews) {
        mSkinViews.put(listener, skinViews);
    }

    public void unregister(ISkinChangeListener listener) {
        mSkinViews.remove(listener);
    }

    /**
     * 获取当前的皮肤资源
     */
    public SkinResource getSkinResource() {
        return mSkinResource;
    }

    /**
     * 检测要不要换肤
     * @param skinView
     */
    public void checkChangeSkin(SkinView skinView) {
        // 如果当前有皮肤，也就是保存的有皮肤路径，就换以下皮肤【此处频繁的读取sp，不太好】
        String currentSkinPath = SkinPrefUtils.getInstance(mContext).getSkinPath();

        if (TextUtils.isEmpty(currentSkinPath)) {
            return;
        }

        skinView.skin();


    }
}
