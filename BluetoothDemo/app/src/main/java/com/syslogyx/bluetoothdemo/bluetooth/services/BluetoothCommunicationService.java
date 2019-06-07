package com.syslogyx.bluetoothdemo.bluetooth.services;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.v7.app.NotificationCompat;
import android.util.Log;


import com.syslogyx.bluetoothdemo.ui.MainActivity;
import com.syslogyx.bluetoothdemo.R;
import com.syslogyx.bluetoothdemo.app.constant.IConstants;
import com.syslogyx.bluetoothdemo.app.perference.Preferences;
import com.syslogyx.bluetoothdemo.bluetooth.controller.BluetoothMessageController;
import com.syslogyx.bluetoothdemo.bluetooth.dos.SignalPacketDo;
import com.syslogyx.bluetoothdemo.bluetooth.listeners.IBluetoothCallback;

import java.util.Timer;
import java.util.TimerTask;


public class BluetoothCommunicationService extends Service implements IBluetoothCallback {

    private static final String logCat = "Logs BTService--";
    private static final int NOTIFICATION_ID = 1;

    private BluetoothMessageController bluetoothMessageController;
    private Timer timer;


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        Log.d(logCat, "BT Service is created");
        bluetoothMessageController = BluetoothMessageController.getInstance();
        bluetoothMessageController.registerICallback(this);
        bluetoothMessageController.initialize();

    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(logCat, "BT Service is start");

        runAsForeground();

        return START_STICKY;
    }


    private void runAsForeground() {
        Intent notificationIntent = new Intent(this, MainActivity.class);
//        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        //| PendingIntent.FLAG_ONE_SHOT
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(getResources().getString(R.string.app_name))
                .setContentText(getResources().getString(R.string.app_name))
                .setContentIntent(pendingIntent).build();

        notification.flags = (Notification.FLAG_SHOW_LIGHTS | Notification.FLAG_AUTO_CANCEL);
        startForeground(NOTIFICATION_ID, notification);
    }


    @Override
    public void onDestroy() {
        Log.d(logCat, "BT Service is stop");
        stopForeground(true);
    }

    @Override
    public void onDeviceConnected(String name, String address) {
        System.out.println("<<< onDeviceConnected");
        stopAutoConnection();
    }

    @Override
    public void onDeviceDisconnected() {
        System.out.println("<<< onDeviceDisconnected");
        stopAutoConnection();
        prepareForAutoConnection();
    }


    @Override
    public void onDeviceConnectionFailed() {

    }

    @Override
    public void onDataReceived(byte[] data, String message) {

    }

    @Override
    public void setSignalStatus(SignalPacketDo signalStatusDao) {

    }

    /**
     * Before start auto connection thread get address from preference and check then start the thread
     */
    private void prepareForAutoConnection() {

        //retrieving the device address from preference
        Preferences preferences = new Preferences(this);
        String device_address = preferences.getString(IConstants.PREFERENCES.KEY_PREF_BLUETOOTH_DEVICE_ADDRESS, null);

        //if device address in preference is null then no need to start the timer thread for auto connection
        if (device_address != null) {
            startAutoConnection();
        } else {
            Log.d(logCat, "no device found");
        }
    }

    /**
     * stop the auto connection
     */
    private void stopAutoConnection() {
        if (timer != null) {
            Log.d(logCat, "Auto connection is stop");
            timer.cancel();
            timer = null;
        }
    }


    /**
     * @param
     */
    private void startAutoConnection() {

        Log.d(logCat, "Auto connection is start");
        final long TIME_INTERVAL = 1000L;

        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {

                //get address of connected device
                String address = getDeviceAddressFromPref();
                // calling the method to show notification repeatedly
                if (address != null) {
                    requestAutoConnect(address);
                }
            }
        }, 0, TIME_INTERVAL);
    }

    /**
     * Method which return the address of connected device
     *
     * @return
     */
    private String getDeviceAddressFromPref() {
        //retrieving the device address from preference
        Preferences preferences = new Preferences(this);
        String device_address = preferences.getString(IConstants.PREFERENCES.KEY_PREF_BLUETOOTH_DEVICE_ADDRESS, null);
        return device_address;
    }


    /**
     * Requests to connect paired devices automatically.
     */
    private void requestAutoConnect(String device_address) {
        if (device_address != null) {
            Log.d(logCat, "Request to auto connect");
            if (!bluetoothMessageController.isBluetoothConnected()) {
                if (device_address != null) {
                    Log.d(logCat, "device - " + device_address);
                    bluetoothMessageController.autoConnect(device_address);
                }
            }
        }
    }


}
