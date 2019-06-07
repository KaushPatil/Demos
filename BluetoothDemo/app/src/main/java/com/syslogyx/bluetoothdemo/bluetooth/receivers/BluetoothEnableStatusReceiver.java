package com.syslogyx.bluetoothdemo.bluetooth.receivers;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.syslogyx.bluetoothdemo.app.constant.IConstants;
import com.syslogyx.bluetoothdemo.bluetooth.controller.BluetoothMessageController;


public class BluetoothEnableStatusReceiver extends BroadcastReceiver {

    private static final String logCat = "Logs BESBroadcast--";
    private BluetoothMessageController bluetoothMessageController;

    public BluetoothEnableStatusReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent != null) {

            final String action = intent.getAction();

            if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {

                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);
                bluetoothMessageController = BluetoothMessageController.getInstance();

                switch (state) {

                    case BluetoothAdapter.STATE_OFF:
                        Log.d(logCat, "off");
                        enableBluetooth();
                        break;

                    case BluetoothAdapter.STATE_TURNING_OFF:
                        Log.d(logCat, "turning off");
                        break;

                    case BluetoothAdapter.STATE_ON:
                        Log.d(logCat, "on");
                        sendBroadcastToLoadAdapter(context);
                        break;

                    case BluetoothAdapter.STATE_TURNING_ON:
                        Log.d(logCat, "turning on");
                        break;
                }
            }
        }
    }

    /**
     * Enable the bluetooth if disabled
     */
    private void enableBluetooth() {
//        if (!bluetoothMessageController.isBluetoothEnabled()) {
//            bluetoothMessageController.enableBluetooth();
//        }
//
//        if (!bluetoothMessageController.isServiceAvailable()) {
//            bluetoothMessageController.setupConnection();
//        }

        // TODO: 16/3/18 need to uncomment
//        if (!ErrorSignalManager.getInstance().isErrorInProgress()) {
//            Log.d(logCat, "Error flow is in progress");
//            bluetoothMessageController.enableBluetoothAndSetUpConnection();
//        }
    }


    /**
     * Send the broadcast to the fragment that bluetooth is enabled
     *
     * @param context
     */
    private void sendBroadcastToLoadAdapter(Context context) {
        Intent intent = new Intent(IConstants.ACTION_SET_ADAPTER);
        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(context);
        localBroadcastManager.sendBroadcast(intent);
    }
}

