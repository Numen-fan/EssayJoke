package com.jiajia.framelibrary.skin.attr;

import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jiajia.framelibrary.skin.SkinManager;
import com.jiajia.framelibrary.skin.SkinResource;

/**
 * Created by Numen_fan on 2022/4/24
 * Desc:
 */
public enum SkinType {

    TEXT_COLOR("text_color") {
        @Override
        public void skin(View view, String resourceName) {
            SkinResource skinResource = getSkinResource();

            ColorStateList colors = skinResource.getColorByName(resourceName);
            if (colors == null) {
                return;
            }

            TextView textView = (TextView) view;

            textView.setTextColor(colors);
        }
    },

    BACKGROUND("background") {
        @Override
        public void skin(View view, String resourceName) {
            // 背景可能是图片，也可能是颜色
            SkinResource skinResource = getSkinResource();
            Drawable drawable = skinResource.getDrawableByName(resourceName);
            if (drawable != null) {
                ImageView imageView = (ImageView) view;
                imageView.setBackground(drawable);
                return;
            }

            // 可能是颜色
            ColorStateList colors = skinResource.getColorByName(resourceName);
            if (colors != null) {
                view.setBackgroundColor(colors.getDefaultColor());
            }
        }
    },

    SRC("src") {
        @Override
        public void skin(View view, String resourceName) {
            SkinResource skinResource = getSkinResource();
            Drawable drawable = skinResource.getDrawableByName(resourceName);
            if (drawable == null) {
                return;
            }
            ImageView imageView = (ImageView) view;
            imageView.setImageDrawable(drawable);
        }
    };

    private String mName;

    SkinType(String name) {
        mName = name;
    }

    public abstract void skin(View view, String resourceName);

    public String getResName() {
        return mName;
    }

    public SkinResource getSkinResource() {
        return SkinManager.getInstance().getSkinResource();
    }
}
