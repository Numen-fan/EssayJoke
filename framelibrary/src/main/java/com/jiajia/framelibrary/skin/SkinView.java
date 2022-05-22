package com.jiajia.framelibrary.skin;

import android.view.View;

import com.jiajia.framelibrary.skin.attr.SkinAttr;

import java.util.List;

/**
 * Created by Numen_fan on 2022/4/24
 * Desc:
 */
public class SkinView {

    private View mView;

    private List<SkinAttr> mAttrs;

    public SkinView(View view, List<SkinAttr> attrs) {
        this.mView = view;
        this.mAttrs = attrs;

    }

    public void skin() {
        for (SkinAttr attr : mAttrs) {
            attr.skin(mView);
        }
    }

}
