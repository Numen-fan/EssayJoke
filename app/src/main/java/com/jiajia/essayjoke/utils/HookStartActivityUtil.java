package com.jiajia.essayjoke.utils;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by Numen_fan on 2022/9/12
 * Desc:
 */
public class HookStartActivityUtil {

    private static final String TAG = "HookStartActivityUtil";

    private final Context mContext;

    private final Class<?> mProxyClass;

    public static final String EXTRA_ORIGIN_INTENT = "extra_origin_intent";

    public HookStartActivityUtil(Context context, Class<?> proxyClass) {
        this.mContext = context;
        this.mProxyClass = proxyClass;
    }

//    public void hookLaunchActivity() throws Exception{
//        // 3.4.1 获取ActivityThread实例
//        Class<?> atClass = Class.forName("android.app.ActivityThread");
//        Field scatField = atClass.getDeclaredField("sCurrentActivityThread");
//        scatField.setAccessible(true);
//        Object sCurrentActivityThread = scatField.get(null);
//        // 3.4.2 获取ActivityThread中的mH
//        Field mhField = atClass.getDeclaredField("mH");
//        mhField.setAccessible(true);
//        Object mHandler = mhField.get(sCurrentActivityThread);
//        // 3.4.3 hook  handleLaunchActivity
//        // 给Handler设置CallBack回掉,也只能通过发射
//        Class<?> handlerClass = Class.forName("android.os.Handler");
//        Field mCallbackField = handlerClass.getDeclaredField("mCallback");
//        mCallbackField.setAccessible(true);
//        mCallbackField.set(mHandler,new HandlerCallBack());
//    }

    public void hookStartActivity() throws Exception{
        // 3.1 获取ActivityManager里面的IActivityManagerSingleton
        Class<?> amnClass = Class.forName("android.app.ActivityTaskManager");

        // 获取属性
        Field gDefaultField = amnClass.getDeclaredField("IActivityTaskManagerSingleton");
        // 设置权限
        gDefaultField.setAccessible(true);
        Object gDefault = gDefaultField.get(null); // 因为IActivityTaskManagerSingleton是静态的，所以直接通过类信息获取，不需要通过类对象

        // 3.2 获取IActivityManagerSingleton中的mInstance属性值
        Class<?> singletonClass = Class.forName("android.util.Singleton");

        Method getMethod = singletonClass.getDeclaredMethod("get");
        getMethod.setAccessible(true);
        getMethod.invoke(gDefault);

        Field mInstanceField = singletonClass.getDeclaredField("mInstance");
        mInstanceField.setAccessible(true);
        Object iamInstance = mInstanceField.get(gDefault);

        Class<?> iamClass = Class.forName("android.app.IActivityTaskManager");
        iamInstance = Proxy.newProxyInstance(HookStartActivityUtil.class.getClassLoader(),
                new Class[]{iamClass},
                // InvocationHandler 必须执行者，谁去执行
                new StartActivityInvocationHandler(iamInstance));

        // 3.重新指定
        mInstanceField.set(gDefault, iamInstance);
    }

    private class StartActivityInvocationHandler implements InvocationHandler {
        // 方法执行者
        private final Object mObject;
        public StartActivityInvocationHandler(Object object){
            this.mObject = object;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            Log.e(TAG, "hook到的方法 method = " + method.getName());
            // 替换Intent,过AndroidManifest.xml检测
            if (method.getName().equals("startActivity")) {
                // 1 首先获取原来的intent
                Intent originIntent = (Intent) args[3];

                // 2 创建一个安全的Intent
                Intent safeIntent = new Intent(mContext, mProxyClass);
                args[3] = safeIntent;

                // 3 绑定原来的Intent
                safeIntent.putExtra(EXTRA_ORIGIN_INTENT ,originIntent);




            }

            return method.invoke(mObject, args);
        }
    }
}
