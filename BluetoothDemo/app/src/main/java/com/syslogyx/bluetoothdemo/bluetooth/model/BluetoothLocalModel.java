package com.syslogyx.bluetoothdemo.bluetooth.model;


import com.syslogyx.bluetoothdemo.app.application.MyApplication;
import com.syslogyx.bluetoothdemo.app.constant.IConstants;
import com.syslogyx.bluetoothdemo.app.perference.Preferences;

/**
 * Singleton class to maintain the local data
 */

public class BluetoothLocalModel {

    private static BluetoothLocalModel bluetoothLocalModel;
    //Maintain machine relay in BluetoothLocalModel
    private String machineRelay;

    private String connectedBluetoothName;
    private String connectedBluetoothAddress;

    private BluetoothLocalModel() {
    }

    /**
     * Returns the instance of {@link BluetoothLocalModel}
     *
     * @return {@link BluetoothLocalModel}
     */
    public static BluetoothLocalModel getInstance() {
        if (bluetoothLocalModel == null) {
            bluetoothLocalModel = new BluetoothLocalModel();
        }
        return bluetoothLocalModel;
    }

    public String getMachineRelay() {
        return machineRelay;
    }

    public void setMachineRelay(String machineRelay) {
        this.machineRelay = machineRelay;
    }

    public String getConnectedBluetoothName() {
        Preferences preferences = new Preferences(MyApplication.getInstance().getApplicationContext());
        String connectedBluetoothName = preferences.getString(IConstants.PREFERENCES.KEY_PREF_BLUETOOTH_DEVICE_NAME, null);
        return connectedBluetoothName;
    }


    public String getConnectedBluetoothAddress() {
        Preferences preferences = new Preferences(MyApplication.getInstance().getApplicationContext());
        String connectedBluetoothAddress = preferences.getString(IConstants.PREFERENCES.KEY_PREF_BLUETOOTH_DEVICE_ADDRESS, null);
        return connectedBluetoothAddress;
    }
}
