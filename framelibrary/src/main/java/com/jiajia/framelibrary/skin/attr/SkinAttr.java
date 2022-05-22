package com.jiajia.framelibrary.skin.attr;

import android.view.View;

/**
 * Created by Numen_fan on 2022/4/24
 * Desc:
 */
public class SkinAttr {

    private String mResourceName;
    private SkinType mType;

    public SkinAttr(String resourceName, SkinType skinType) {
        mResourceName = resourceName;
        mType = skinType;
    }

    public void skin(View view) {
        mType.skin(view, mResourceName);
    }

}
