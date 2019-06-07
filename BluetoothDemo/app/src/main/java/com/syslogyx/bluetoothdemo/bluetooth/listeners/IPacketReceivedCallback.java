package com.syslogyx.bluetoothdemo.bluetooth.listeners;


import com.syslogyx.bluetoothdemo.bluetooth.dos.SignalPacketDo;

/**
 * Created by Namrata on 22-Dec-17.
 */

public interface IPacketReceivedCallback {

    void onPacketReceived(boolean isPacketReceived, SignalPacketDo signalPacketDo);

}
