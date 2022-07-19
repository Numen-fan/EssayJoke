package com.jiajia.framelibrary.http;

import android.annotation.SuppressLint;

/**
 * Created by fanjiajia02 on 2022/7/18
 * Desc: 一些Http相关的逻辑处理
 **/
public class HttpLogic {

    /**
     * 拼接首页文章拉取url
     * @param page 第几页
     * @return 完整url e.g. https://www.wanandroid.com/article/list/0/json
     */
    @SuppressLint("DefaultLocale")
    public static String getArticleUrl(int page) {
        if (page < 0) {
            page = 0;
        }
        return String.format(HttpConstant.articleUrl + "%d/json", page);
    }
}
