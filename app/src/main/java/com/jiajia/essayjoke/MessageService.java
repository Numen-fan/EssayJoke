package com.jiajia.essayjoke;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

public class MessageService extends Service {
    public MessageService() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        // 绑定
        return mBinder;
    }

    private final UserAidl.Stub mBinder = new UserAidl.Stub() {
        @Override
        public String getUserName() throws RemoteException {
            return "numen.fan@gmail.com";
        }

        @Override
        public String getUserPwd() throws RemoteException {
            return "123456";
        }
    };

}