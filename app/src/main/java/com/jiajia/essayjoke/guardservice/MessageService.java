package com.jiajia.essayjoke.guardservice;

import android.app.Notification;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.widget.Toast;

import com.jiajia.essayjoke.ProcessConnection;

public class MessageService extends Service {

    private static final String TAG = "MessageService";

    private int messsageId = 0;

    public MessageService() {

    }

    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startForeground(messsageId, new Notification());

        // 连接另一个service
        bindService(new Intent(this, GuardService.class), mServiceConnection, Context.BIND_IMPORTANT);

        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // 绑定
        return new ProcessConnection.Stub() {
            @Override
            public void method() throws RemoteException {

            }
        };
    }

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            // 连接上
            Toast.makeText(MessageService.this, "建立连接了", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            // 连接断开, 重新启动，重新绑定
            startService(new Intent(MessageService.this, GuardService.class));

            // 连接另一个service
            bindService(new Intent(MessageService.this, GuardService.class), mServiceConnection, Context.BIND_IMPORTANT);
        }
    };



}