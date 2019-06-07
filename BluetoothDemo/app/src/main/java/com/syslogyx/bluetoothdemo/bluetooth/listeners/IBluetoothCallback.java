package com.syslogyx.bluetoothdemo.bluetooth.listeners;


import com.syslogyx.bluetoothdemo.bluetooth.dos.SignalPacketDo;

/**
 * Created by kaushik on 02-Nov-16.
 */

public interface IBluetoothCallback {

    public void onDeviceConnected(String name, String address);

    public void onDeviceDisconnected();

    public void onDeviceConnectionFailed();

    public void onDataReceived(byte[] data, String message);

    public void setSignalStatus(SignalPacketDo signalPacketDo);

}
