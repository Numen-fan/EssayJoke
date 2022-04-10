package com.jiajia.framelibrary.db;

import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.IOException;

/**
 * Created by Numen_fan on 2022/4/10
 * Desc:
 */
public class DaoSupportFactory {

    private static final String TAG = "DaoSupportFactory";

    private static volatile DaoSupportFactory mFactory;

    // 持有外部数据库的引用
    private SQLiteDatabase mSqliteDatabase;

    private DaoSupportFactory() {

        // 把数据库放到内存卡里面 判断是否有存储卡，6.0要动态申请权限
        File dbRoot = new File(Environment.getExternalStorageDirectory().getAbsoluteFile()
        + File.separator + "nhdz" + File.separator + "database");

        if (!dbRoot.exists()) {
            boolean mkdir = dbRoot.mkdirs();
            Log.w(TAG, "mkdir::" + mkdir);
        }

        File dbFile = new File(dbRoot, "nhdz.db"); // 这个db文件需要自己写吗？
        if (!dbFile.exists()) {
            try {
                boolean createDBFile = dbFile.createNewFile();
                Log.w(TAG, "createDBFile::" + createDBFile);
            } catch (IOException e) {
                Log.e(TAG, "创建数据库db文件出错");
                return;
            }
        }

        // 打开或者创建一个数据库
        mSqliteDatabase = SQLiteDatabase.openOrCreateDatabase(dbFile, null);
    }

    public static DaoSupportFactory getFactory() {
        if (mFactory == null) {
            synchronized (DaoSupportFactory.class) {
                if (mFactory == null) {
                    mFactory = new DaoSupportFactory();
                }
            }
        }
        return mFactory;
    }

    public <T> IDaoSupport<T> getDao(Class<T> clazz) {
        DaoSupport<T> daoSupport = new DaoSupport<>();
        daoSupport.init(mSqliteDatabase, clazz);
        return daoSupport;
    }
}
