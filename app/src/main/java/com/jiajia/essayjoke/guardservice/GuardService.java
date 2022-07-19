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

import androidx.annotation.Nullable;

import com.jiajia.essayjoke.ProcessConnection;

/**
 * Created by Numen_fan on 2022/6/23
 * Desc: 守护进程
 */
public class GuardService extends Service {

    private final int GuardId = 1;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        startForeground(GuardId, new Notification());

        // 连接另一个service
        bindService(new Intent(this, MessageService.class), mServiceConnection, Context.BIND_IMPORTANT);

        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
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
            Toast.makeText(GuardService.this, "建立连接了", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            // 连接断开, 重新启动，重新绑定
            startService(new Intent(GuardService.this, MessageService.class));

            // 连接另一个service
            bindService(new Intent(GuardService.this, MessageService.class), mServiceConnection, Context.BIND_IMPORTANT);
        }
    };
}
