package com.jiajia.framelibrary.db;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.ArrayMap;
import android.util.Log;

import com.jiajia.framelibrary.db.curd.QuerySupport;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Numen_fan on 2022/4/10
 * Desc:
 */
public class DaoSupport<T> implements IDaoSupport<T> {

    private static final String TAG = "DaoSupport";

    private SQLiteDatabase mSqliteDatabase;
    private Class<T> mClazz;

    private static final Object[] mPutMethodArgs = new Object[2];
    private static final Map<String, Method> mPutMethods = new ArrayMap<>();

    private QuerySupport<T> mQuerySupport;

    /**
     * 初始化数据库表相关
     * @param database 数据库
     * @param clazz  一个Class，对应Table
     */
    @Override
    public void init(SQLiteDatabase database, Class<T> clazz) {
        mSqliteDatabase = database;
        mClazz = clazz;

        // 创建表
        StringBuilder sb = new StringBuilder();
        sb.append("create table if not exists ").append(DaoUtil.getTableName(clazz))
                .append(" (id integer primary key autoincrement, ");

        Field[] fields = mClazz.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true); // 权限
            String name = field.getName();
            String type = field.getType().getSimpleName(); // int String boolean
            // 需要将type进行数据库类型的转换
            // int -> integer String -> text
            sb.append(name).append(DaoUtil.getColumnType(type)).append(", ");
        }

        sb.replace(sb.length() - 2, sb.length(), ")");

        String createTableSql = sb.toString();

        Log.e(TAG, "创建表的语句->" + createTableSql);

        mSqliteDatabase.execSQL(createTableSql);
    }

    /**
     * 插入任意类型
     */
    @Override
    public void insert(T o) {
        mSqliteDatabase.insert(DaoUtil.getTableName(o.getClass()), null, contentValuesByObj(o));
    }

    /**
     * 批量插入
     */
    @Override
    public void insert(List<T> datas) {
        // 批量插入采用事物的方式
        mSqliteDatabase.beginTransaction();
        for (T data : datas) {
            insert(data);
        }
        mSqliteDatabase.setTransactionSuccessful();
        mSqliteDatabase.endTransaction();
    }

    @Override
    public QuerySupport<T> querySupport() {
        if (mQuerySupport == null) {
            mQuerySupport = new QuerySupport<>(mSqliteDatabase, mClazz);
        }
        return mQuerySupport;
    }

    @Override
    public int delete(String whereCase, String[] whereArgs) {
        return mSqliteDatabase.delete(DaoUtil.getTableName(mClazz), whereCase, whereArgs);
    }

    @Override
    public int update(T obj, String whereCase, String... whereArgs) {
        ContentValues values = contentValuesByObj(obj);
        return mSqliteDatabase.update(DaoUtil.getTableName(mClazz), values, whereCase, whereArgs);
    }

    /**
     * @param obj 转成ContentValues
     */
    private ContentValues contentValuesByObj(T obj) {
        ContentValues values = new ContentValues();

        Field[] fields = mClazz.getDeclaredFields(); // 可以先缓存起来
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                String key = field.getName();
                Object value = field.get(obj);
                if (value == null) {
                    continue;
                }
                mPutMethodArgs[0] = key;
                mPutMethodArgs[1] = value;

                String filedType = field.getType().getName();
                // 使用反射，获取具体的value类型值。但在一定程度上会影响性能, 做一个cache
                Method putMethod = mPutMethods.get(filedType);
                if (putMethod == null) {
                    putMethod = ContentValues.class.getDeclaredMethod("put", String.class, value.getClass());
                    mPutMethods.put(filedType, putMethod);
                }
                putMethod.invoke(values, mPutMethodArgs);
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            } finally {
                mPutMethodArgs[0] = null;
                mPutMethodArgs[1] = null;
            }
        }
        return values;
    }
}
