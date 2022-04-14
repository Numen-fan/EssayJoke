package com.jiajia.framelibrary.db;

import android.database.sqlite.SQLiteDatabase;

import com.jiajia.framelibrary.db.curd.QuerySupport;

import java.util.List;

/**
 * Created by Numen_fan on 2022/4/10
 * Desc:
 */
public interface IDaoSupport<T> {

    void init(SQLiteDatabase database, Class<T> clazz);

    long insert(T object);

    /**
     * 批量插入
     */
    void insert(List<T> datas);

    /**
     * 获取专门查询的支持类
     */
    QuerySupport<T> querySupport();

    int delete(String whereCase, String[] whereArgs);

    int update(T obj, String whereCase, String[] whereArgs);


}
