package com.jiajia.baselibrary.ioc;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by Numen_fan on 2022/3/13
 * Desc:
 */
public class ViewUtils {

    public static void inject(Activity activity) {
        inject(new ViewFinder(activity), activity);
    }

    public static void inject(View view) {
        inject(new ViewFinder(view), view);
    }

    public static void inject(View view, Object object) {
        inject(new ViewFinder(view), object);
    }

    // 兼容上面三个方法， object ---> 反射需要执行的类
    private static void inject(ViewFinder finder, Object object) {
        // 1 注入属性
        injectField(finder, object);
        // 2 注入事件
        injectEvent(finder, object);
    }

    /**
     * 注入属性
     */
    private static void injectField(ViewFinder finder, Object object) {
        // 1 通过反射获取类里面的所有属性
        Class<?> clazz = object.getClass();
        Field[] fields = clazz.getDeclaredFields(); // 获取所有的属性，包括私有和公有的属性
        // 2 遍历所有的属性，并筛选出具有特定注解的属性
        for (Field field : fields) {
            ViewById viewById = field.getAnnotation(ViewById.class);
            if (viewById != null) {
                // 找到viewId，R.id.xxx
                int viewId = viewById.value();
                // 3 通过ViewFinder进行findViewById
                View view = finder.findViewById(viewId);

                if (view == null) {
                    continue;
                }
                // 注入所有的修饰符，包括private public
                field.setAccessible(true);

                // 4 动态的注入所有的修饰符
                try {
                    field.set(object, view); // 相当于为object的field属性赋值view
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 事件注入
     */
    private static void injectEvent(ViewFinder finder, Object object) {
        // 1 通过反射获取object中所有的方法属性
        Class<?> clazz = object.getClass();
        Method[] methods = clazz.getDeclaredMethods();
        // 2 遍历所有的方法属性，筛选具有注解的方法
        for (Method method : methods) {
            OnClick onClick = method.getAnnotation(OnClick.class);
            if (onClick != null) {
                // 是否需要checkNet
                boolean isCheckNet = method.getAnnotation(CheckNet.class) != null;
                // 3 通过注解，获取注解中的viewId
                int[] viewIds = onClick.value();
                // 4 通过viewId和finder执行findViewById,拿到View。
                for (int viewId : viewIds) {
                    View view = finder.findViewById(viewId);
                    if (view == null) {
                        continue;
                    }
                    // 5 对View设置点击事件
                    view.setOnClickListener(new DeclareOnClickListener(object, method, isCheckNet));
                }
            }
        }
    }

    /**
     * View的点击事件
     */
    private static class DeclareOnClickListener implements View.OnClickListener {

        private final Object mHandler; // mMethod的宿主
        private final Method mMethod; // 声明的事件
        private final boolean mIsCheckNet; // 是否需要check网络

        public DeclareOnClickListener(Object handler, Method method, boolean isCheckNet) {
            this.mHandler = handler;
            this.mMethod = method;
            this.mIsCheckNet = isCheckNet;
        }

        @Override
        public void onClick(View v) {

            // 校验网络（todo：如何将网络话术写得更加合理，需要考虑，当前library可能输出为jar包）
            if (mIsCheckNet && !networkAvailable(v.getContext())) {
                Toast.makeText(v.getContext(), "当前网络不可用", Toast.LENGTH_SHORT).show();
                return;
            }

            // 注入所有的修饰符，包括private和public
            mMethod.setAccessible(true);
            // 反射invoke执行
            try {
                mMethod.invoke(mHandler, v);
            } catch (Exception e) {
                try {
                    // 为了兼容不传view的参数，这里再次执行一下无参的method
                    mMethod.invoke(mHandler, null);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        }

        /**
         * 判断网络是否可用
         */
        private static boolean networkAvailable(Context context) {
            try {
                ConnectivityManager connectivityManager = (ConnectivityManager) context
                        .getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
                if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
                    return true;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return false;
        }
    }
}

