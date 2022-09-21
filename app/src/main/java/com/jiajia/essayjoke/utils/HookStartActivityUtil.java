package com.jiajia.essayjoke.utils;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;

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

    public void hookLaunchActivity() throws Exception{
        // 3.4.1 获取ActivityThread实例
        Class<?> atClass = Class.forName("android.app.ActivityThread");
        Field scatField = atClass.getDeclaredField("sCurrentActivityThread");
        scatField.setAccessible(true);
        Object sCurrentActivityThread = scatField.get(null);
        // 3.4.2 获取ActivityThread中的mH
        Field mhField = atClass.getDeclaredField("mH");
        mhField.setAccessible(true);
        Object mHandler = mhField.get(sCurrentActivityThread);
        // 3.4.3 hook  handleLaunchActivity
        // 给Handler设置CallBack回掉,也只能通过发射
        Class<?> handlerClass = Class.forName("android.os.Handler");
        Field mCallbackField = handlerClass.getDeclaredField("mCallback");
        mCallbackField.setAccessible(true);
        mCallbackField.set(mHandler, new HandlerCallBack());
    }

    private final class HandlerCallBack implements Handler.Callback {

        @Override
        public boolean handleMessage(Message msg) {
            // 每发一个消息都会走一次这个CallBack发放

            // 根据版本，进行适配
            if (msg.what == 100) {
                replaceBackIntent(msg);
            } else if (msg.what == 159) {
                handleLaunchActivity(msg);
            }
            return false;
        }
    }

    /**
     * API 27及其以下的hook方法
     *
     * @param record 一个ActivityClientRecord对象
     */
    private void replaceBackIntent(Object record)  {
        try {
            // 1.从record 获取过安检的Intent
            Field intentField = record.getClass().getDeclaredField("intent");
            intentField.setAccessible(true);
            Intent safeIntent = (Intent) intentField.get(record);
            // 2.从safeIntent中获取原来的originIntent
            Intent originIntent = safeIntent.getParcelableExtra(EXTRA_ORIGIN_INTENT);
            // 3.重新设置回去
            if(originIntent != null){
                intentField.set(record,originIntent);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 开始启动创建Activity拦截
     * @param msg
     */
    private void handleLaunchActivity(Message msg)  {
        try {
            Object transaction = msg.obj;  // 是一个ClientTransaction
            Class<?> transClass = Class.forName("android.app.servertransaction.ClientTransaction");
            if (transaction.getClass() != transClass) {
                return;
            }

            Method getCallbacks = transClass.getDeclaredMethod("getCallbacks");
            getCallbacks.setAccessible(true);
            List<Object> callbacks = (List<Object>) getCallbacks.invoke(transaction); // 得到所有的callbacks

            if (callbacks == null || callbacks.size() == 0) {
                return;
            }

            Class<?> launchActivityItemClass = Class.forName("android.app.servertransaction.LaunchActivityItem");
            for (Object callback : callbacks) {
                if (callback.getClass() == launchActivityItemClass) { // 是LaunchActivityItem的一个实例了
                    Field intentFiled = launchActivityItemClass.getDeclaredField("mIntent"); // mIntent声明
                    intentFiled.setAccessible(true);
                    Intent safeIntent = (Intent) intentFiled.get(callback); // 得到代理的那个通过安检的Intent
                    Intent originIntent = safeIntent.getParcelableExtra(EXTRA_ORIGIN_INTENT);
                    if (originIntent != null) {
                        // 将LaunchActivityItem的mIntent属性重新赋值原本的Intent
                        intentFiled.set(callback, originIntent);

                        Method getActivityToken = transClass.getDeclaredMethod("getActivityToken");
                        getActivityToken.setAccessible(true);
                        Object binder = getActivityToken.invoke(transaction); // 拿到了IBinder对象

                        // 拿到ActivityThread实例
                        Class<?> activityThread = Class.forName("android.app.ActivityThread");
                        Field activityThreadFiled = activityThread.getDeclaredField("sCurrentActivityThread");
                        activityThreadFiled.setAccessible(true);
                        Object activityThreadObj = activityThreadFiled.get(null); // 静态的变量直接获取

                        // 拿到ActivityClientRecord
                        Method getLaunchingActivity = activityThread.getMethod("getLaunchingActivity", IBinder.class);
                        getLaunchingActivity.setAccessible(true);
                        Object record = getLaunchingActivity.invoke(activityThreadObj, binder);

                        // 从ActivityClientRecord实例中替换掉Intent
                        replaceBackIntent(record);

                        Log.i(TAG, "success replace back");
                    }
                }
            }

        }catch (Exception e){
           Log.e(TAG, e.getMessage());
        }
    }

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
            // 替换Intent,过AndroidManifest.xml检测
            if (method.getName().equals("startActivity")) {
                // 1 首先获取原来的intent
                Intent originIntent = (Intent) args[3];

                // 2 创建一个安全的Intent
                Intent safeIntent = new Intent(mContext, mProxyClass);
                args[3] = safeIntent;

                // 3 绑定原来的Intent
                safeIntent.putExtra(EXTRA_ORIGIN_INTENT , originIntent);

            }

            return method.invoke(mObject, args);
        }
    }
}
