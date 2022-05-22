package com.jiajia.framelibrary.skin.support;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;

import com.jiajia.framelibrary.skin.attr.SkinAttr;
import com.jiajia.framelibrary.skin.attr.SkinType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Numen_fan on 2022/4/24
 * Desc: 皮肤属性解析的支持类
 */
public class SkinAttrSupport {

    private static final String TAG = "SkinAttrSupport";

    /**
     * 获取SkinAttr的属性
     * 将View的{@param attrs}属性中我们关注的属性转为 SkinAttr属性
     */
    public static List<SkinAttr> getSkinAttrs(Context context, AttributeSet attrs) {
        // background src textColor
        List<SkinAttr> skinAttrs = new ArrayList<>();

        int attrLength = attrs.getAttributeCount();

        for (int i = 0; i < attrLength; i++) {
            // 获取属性 名称 和 值
            String attrName = attrs.getAttributeName(i);
            String attrValue = attrs.getAttributeValue(i);

            // 只获取需要的属性
            SkinType skinType = getSkinType(attrName);

            if (skinType != null) {
                // 资源名称  目前只有attrValue 是一个@int类型的值
                String resName = getResName(context, attrValue);
                if (TextUtils.isEmpty(resName)) {
                    continue;
                }
                SkinAttr skinAttr = new SkinAttr(resName, skinType);

                skinAttrs.add(skinAttr);
            }
        }

        return skinAttrs;
    }

    /**
     * 得到属性所属的SkinType, 只考虑关注的部分
     */
    private static SkinType getSkinType(String attrName) {
        SkinType[] skinTypes = SkinType.values(); // 枚举的values
        for (SkinType skinType : skinTypes) {
            if (skinType.getResName().equals(attrName)) {
                return skinType;
            }
        }
        return null;
    }

    /**
     * 根据@int值得到资源文件的名称
     */
    private static String getResName(Context context, String attrValue) {

        if (attrValue == null) {
            return null;
        }

        if (attrValue.startsWith("@")) {
            attrValue = attrValue.substring(1);
            int resId = Integer.parseInt(attrValue);
            // 更具int id 找到资源名称
            return context.getResources().getResourceEntryName(resId);
        }
        return null;
    }
}
