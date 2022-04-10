package com.jiajia.framelibrary.db;

/**
 * Created by Numen_fan on 2022/4/10
 * Desc:
 */
public class DaoUtil {

    public static String getColumnType(String type) {
        String value = null;
        if ("String".equals(type)) {
            value =  "text"; // 这里加上了空格
        } else if ("int".equals(type)) {
            value = "integer";
        } else if ("boolean".equals(type)) {
            value = "boolean";
        } else if ("float".equals(type)) {
            value = "float";
        } else if ("double".equals(type)) {
            value = "double";
        } else if ("char".equals(type)) {
            value = "varchar";
        } else if ("long".equals(type)) {
            value = "long";
        }

        return " " + value; // 添加一个空格
    }

    public static String getTableName(Class<?> clazz) {
        return clazz.getSimpleName();
    }
}
