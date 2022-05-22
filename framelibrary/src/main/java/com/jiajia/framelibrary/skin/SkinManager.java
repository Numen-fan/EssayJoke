package com.jiajia.framelibrary.skin;

import android.app.Activity;
import android.content.Context;
import android.util.ArrayMap;

import com.jiajia.framelibrary.BaseSkinActivity;
import com.jiajia.framelibrary.skin.attr.SkinAttr;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Numen_fan on 2022/4/24
 * Desc: 皮肤管理类
 */
public class SkinManager {

    private static final SkinManager instance;
    private Context mContext;
    private SkinResource mSkinResource;

    private Map<Activity, List<SkinView>> mSkinViews = new ArrayMap<>();

    static {
        instance = new SkinManager();
    }

    private SkinManager() { }

    public static SkinManager getInstance() {
        return instance;
    }

    public void init(Context context) {
        this.mContext = context.getApplicationContext();
    }

    /**
     * 加载皮肤
     * @param skinPath
     */
    public void loadSkin(String skinPath) {
        // 校验签名 增量更新再说

        // skinPath判断，并并且进行资源包的复制

        // 初始化资源管理
        mSkinResource =  new SkinResource(mContext, skinPath);

        // 改变皮肤
        Set<Activity> keys =  mSkinViews.keySet();

        for (Activity key : keys) {
            List<SkinView> skinViews = mSkinViews.get(key);

            for (SkinView skinView : skinViews) {
                skinView.skin();
            }
        }
    }

    /**
     * 恢复默认
     */
    public void restoreDefault() {

    }

    /**
     * 拿到这个activity的所有SkinView
     */
    public List<SkinView> getSkinViews(Activity activity) {
        return  mSkinViews.get(activity);
    }

    /**
     * 注册
     */
    public void register(Activity activity, List<SkinView> skinViews) {
        mSkinViews.put(activity, skinViews);
    }

    /**
     * 获取当前的皮肤资源
     */
    public SkinResource getSkinResource() {
        return mSkinResource;
    }
}
