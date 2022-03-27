package com.jiajia.baselibrary.fixbug;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.nio.channels.FileChannel;
import java.util.ArrayList;

import dalvik.system.BaseDexClassLoader;

/**
 * Created by Numen_fan on 2022/3/26
 * Desc:
 */
public class FixDexManager {

    private static final String TAG = "FixDexManager";

    private Context mContext;
    private File mDexDir;

    public FixDexManager(Context context) {
        this.mContext = context;
        // 获取应用可以访问的dex目录
        this.mDexDir = context.getDir("odex", Context.MODE_PRIVATE);
    }

    /**
     * 修复dex包
     * @param fixDexPath
     */
    public void fixDex(String fixDexPath) throws Exception {

        // 1 先获取已运行的dexElement
        ClassLoader applicationClassLoader = mContext.getClassLoader();
        Object applicationDexElements = getDexElementByClassLoader(applicationClassLoader);

        // 2 获取下载好的补丁 dexElement
        // 2.1 移动到系统能够访问的dex目录下
        File srcFile = new File(fixDexPath);
        if (!srcFile.exists()) {
            throw new FileNotFoundException(fixDexPath);
        }

        File destFile = new File(mDexDir, srcFile.getName());
//        if (destFile.exists()) {
//            Log.d(TAG, "path [" + fixDexPath +"] has be load");
//            return;
//        }

        copyFile(srcFile, destFile.getAbsolutePath());

        // 2.2 ClassLoader读取fixDex路径, 为什么加入集合，一启动可能就要修复
        ArrayList<File> fixDexFiles = new ArrayList<>();
        fixDexFiles.add(destFile);

        File optimizeDirectory = new File(mDexDir, "odex");
        if (!optimizeDirectory.exists()) {
            optimizeDirectory.mkdirs();
        }
        // 修复
        for (File fixDexFile : fixDexFiles) {
            ClassLoader fixDexClassLoader = new BaseDexClassLoader(
                    fixDexFile.getAbsolutePath(), // dex路径
                    optimizeDirectory, // 解压路径
                    null, applicationClassLoader
                    );

            Object fixDexElements = getDexElementByClassLoader(fixDexClassLoader);

            // 3 将新的补丁插到已有dexElement前面，数组合并
            applicationDexElements = combineArray(fixDexElements, applicationDexElements);

            // 4 把合并的数组注入到原来的类中
            injectDexElements(applicationClassLoader, applicationDexElements);

        }



    }

    @SuppressLint("DiscouragedPrivateApi")
    private void injectDexElements(ClassLoader classLoader, Object dexElements) throws Exception{
        // 先获取pathList
        Field pathListField = BaseDexClassLoader.class.getDeclaredField("pathList");
        pathListField.setAccessible(true);
        Object pathList = pathListField.get(classLoader);

        // pathList里面的dexElements
        Field dexElementsField = pathList.getClass().getDeclaredField("dexElements");
        dexElementsField.setAccessible(true);

        dexElementsField.set(pathList, dexElements);

    }

    private void copyFile(File src, String destPath) throws IOException {
        FileChannel inChannel = null;
        FileChannel outChannel = null;

        File destFile = new File(destPath);
        try {

            if (!destFile.exists()) {
                destFile.createNewFile();
            }

            RandomAccessFile dest = new RandomAccessFile(destPath, "rw");
            inChannel = new FileInputStream(src).getChannel();
            outChannel = dest.getChannel();
            inChannel.transferTo(0, inChannel.size(), outChannel);
        } finally {
            if (inChannel != null) {
                inChannel.close();
            }
            if (outChannel != null) {
                outChannel.close();
            }
        }
    }

    /**
     * 通过反射获取DexElement
     */
    @SuppressLint("DiscouragedPrivateApi")
    private Object getDexElementByClassLoader(ClassLoader classLoader) throws Exception{
        // 1.先获取 pathList
        Field pathListField = BaseDexClassLoader.class.getDeclaredField("pathList");
        // IOC 熟悉反射
        pathListField.setAccessible(true);
        Object pathList = pathListField.get(classLoader);

        // 2. pathList里面的dexElements
        Field dexElementsField = pathList.getClass().getDeclaredField("dexElements");
        dexElementsField.setAccessible(true);

        return dexElementsField.get(pathList);
    }

    /**
     * 数组合并
     * @param arrayLhs 左边数组
     * @param arrayRhs 右边数组
     * @return 结果
     */
    private static Object combineArray(Object arrayLhs, Object arrayRhs) {
        Class<?> localClass = arrayLhs.getClass().getComponentType();
        int i = Array.getLength(arrayLhs);
        int j = i + Array.getLength(arrayRhs);

        Object result = Array.newInstance(localClass, j);

        for (int k = 0; k < j; k++) {
            if (k < i) {
                Array.set(result, k, Array.get(arrayLhs, k));
            } else {
                Array.set(result, k, Array.get(arrayRhs, k - i));
            }
        }
        return result;
    }

}
