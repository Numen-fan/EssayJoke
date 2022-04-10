package com.jiajia.framelibrary.db;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.lang.reflect.Field;

/**
 * Created by Numen_fan on 2022/4/10
 * Desc:
 */
public class DaoSupport<T> implements IDaoSupport<T> {

    private static final String TAG = "DaoSupport";

    private SQLiteDatabase mSqliteDatabase;
    private Class<T> mClazz;

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

    }
}
