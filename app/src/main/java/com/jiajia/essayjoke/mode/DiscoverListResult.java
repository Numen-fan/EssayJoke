package com.jiajia.essayjoke.mode;

import java.util.List;

/**
 * Created by Numen_fan on 2022/4/6
 * Desc: 首页
 */
public class DiscoverListResult {

    int curPage;

    int offset;

    boolean over;

    int pageCount;

    int size;

    int total;

    List<DataItem> datas;

    /**
     * datas中每一项
     */
    public static class DataItem {

        public String apkLink;
        public Integer audit;
        public String author;
        public Boolean canEdit;
        public Integer chapterId;
        public String chapterName;
        public Boolean collect;
        public Integer courseId;
        public String desc;
        public String descMd;
        public String envelopePic;
        public Boolean fresh;
        public String host;
        public Integer id;
        public String link;
        public String niceDate;
        public String niceShareDate;
        public String origin;
        public String prefix;
        public String projectLink;
        public Long publishTime;
        public Integer realSuperChapterId;
        public Integer selfVisible;
        public Long shareDate;
        public String shareUser;
        public Integer superChapterId;
        public String superChapterName;
        public List<Tags> tags;
        public String title;
        public Integer type;
        public Integer userId;
        public Integer visible;
        public Integer zan;

        public DataItem() {

        }

        /**
         * tags中每一项
         */
        public static class Tags {
            public String name;
            public String url;

            public Tags(){ }
        }
    }

}
