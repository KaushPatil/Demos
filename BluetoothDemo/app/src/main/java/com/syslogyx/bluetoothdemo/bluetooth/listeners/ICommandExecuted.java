package com.syslogyx.bluetoothdemo.bluetooth.listeners;

import com.syslogyx.bluetoothdemo.bluetooth.dos.SignalPacketDo;

/**
 * Created by root on 20/3/18.
 */

public interface ICommandExecuted {

    void onSuccess(boolean isAllCommandExecuted, SignalPacketDo signalPacketDo);
}
