package com.syslogyx.bluetoothdemo.bluetooth.controller;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.util.Log;


import com.syslogyx.bluetoothdemo.app.application.MyApplication;
import com.syslogyx.bluetoothdemo.app.constant.IConstants;
import com.syslogyx.bluetoothdemo.app.perference.Preferences;
import com.syslogyx.bluetoothdemo.bluetooth.connection.BluetoothSPP;
import com.syslogyx.bluetoothdemo.bluetooth.constant.BluetoothStateConstants;
import com.syslogyx.bluetoothdemo.bluetooth.constant.SignalConstants;
import com.syslogyx.bluetoothdemo.bluetooth.dos.SignalPacketDo;
import com.syslogyx.bluetoothdemo.bluetooth.listeners.IBluetoothCallback;
import com.syslogyx.bluetoothdemo.bluetooth.listeners.IPacketReceivedCallback;
import com.syslogyx.bluetoothdemo.bluetooth.model.BluetoothLocalModel;
import com.syslogyx.bluetoothdemo.bluetooth.parser.BluetoothPacketParser;

import java.util.ArrayList;
import java.util.Set;
import java.util.Timer;


/**
 * Created by kaushik on 02-Nov-16.
 */

public class BluetoothMessageController {
    private static final String logCat = "Logg>>";


    private Context context;
    private ArrayList<IBluetoothCallback> iBluetoothCallbacks;
    private BluetoothSPP bluetoothSPP;
    private BluetoothLocalModel bluetoothLocalModel;
    private BluetoothPacketParser bluetoothPacketParser;
    private int counter;

    //    private String relayStatusToSendInAck = SignalConstants.MACHINE_RELAY_STATUS_NORMAL;
    private static BluetoothMessageController bluetoothMessageController;
    private Preferences preferences;
    private SignalPacketDo signalpacketDo;
    private String lastSentAck;
    private IPacketReceivedCallback iPacketReceivedCallback;

    private int delay = 0;
    private int timeIntervalForRequest = 60000;
    private Timer timer;
//    public UptimeCounter uptimeCounter;


    private BluetoothMessageController() {
        this.context = MyApplication.getInstance().getApplicationContext();
        this.iBluetoothCallbacks = new ArrayList<>();
        initComponents();
    }

    /**
     * Initializes the setup here.
     */
    public void initialize() {
        initBlueToothComponent();
        checkForAutoConnect();
    }

    private void checkForAutoConnect() {
        if (!bluetoothMessageController.isBluetoothConnected()) {
            //as bluetooth is not connected hence notify service to start & other components to update there status.
            bluetoothConnectionListener.onDeviceDisconnected();
        } else {
            String device_address = preferences.getString(IConstants.PREFERENCES.KEY_PREF_BLUETOOTH_DEVICE_ADDRESS, null);
            String device_name = preferences.getString(IConstants.PREFERENCES.KEY_PREF_BLUETOOTH_DEVICE_NAME, null);
            bluetoothConnectionListener.onDeviceConnected(device_name, device_address);
        }
    }

    /**
     * Gets the single ton object.
     */
    public static BluetoothMessageController getInstance() {
        if (bluetoothMessageController == null) {
            bluetoothMessageController = new BluetoothMessageController();
        }
        return bluetoothMessageController;
    }


    /**
     * Registered the iBluetooth callback to have call back for bluetooth connections & disconnections.
     *
     * @param iBluetoothCallback
     */
    public void registerICallback(IBluetoothCallback iBluetoothCallback) {
        if (iBluetoothCallback != null && !iBluetoothCallbacks.contains(iBluetoothCallback)) {
            iBluetoothCallbacks.add(iBluetoothCallback);
        }
    }

    /**
     * Removes the particular callback.F
     *
     * @param iBluetoothCallback
     */
    public void removeICallback(IBluetoothCallback iBluetoothCallback) {
        if (iBluetoothCallback != null && iBluetoothCallbacks.contains(iBluetoothCallback)) {
            iBluetoothCallbacks.remove(iBluetoothCallback);
        }
    }

    /**
     * Removes the all callback.
     */
    public void removeAllICallback() {
        if (!iBluetoothCallbacks.isEmpty()) {
            iBluetoothCallbacks.clear();
        }
    }

    /**
     * Initialized all other Components
     */
    private void initComponents() {
        bluetoothLocalModel = BluetoothLocalModel.getInstance();
        bluetoothPacketParser = new BluetoothPacketParser();
        preferences = new Preferences(context);

    }

    /**
     * Initialized all bluetooth Components
     */
    private void initBlueToothComponent() {
        bluetoothSPP = BluetoothSPP.getInstance(context);
        bluetoothSPP.setBluetoothConnectionListener(bluetoothConnectionListener);
        bluetoothSPP.setOnDataReceivedListener(onDataReceivedListener);
    }


    /**
     * Update default value to signal packet DO
     *
     * @param message
     */
    public void updateSignalPacketDetails(String message) {
        //remove start and end characters
        message = message.substring(1, message.length() - 1);
        Log.d(logCat, "Received Msg Decrypt--" + message);

        signalpacketDo = bluetoothPacketParser.parseSignal(message);

        if (signalpacketDo != null) {
            //means data received correctly.
            bluetoothLocalModel.setMachineRelay(signalpacketDo.getMachineRelay());
            if (iBluetoothCallbacks != null && !iBluetoothCallbacks.isEmpty()) {
                for (IBluetoothCallback iBluetoothCallback : iBluetoothCallbacks) {
                    if (iBluetoothCallback != null) {
                        iBluetoothCallback.setSignalStatus(signalpacketDo);
                    }
                }
            }
        }
    }


    public void setPacketReceivedCallback(IPacketReceivedCallback iPacketReceivedCallback) {
        this.iPacketReceivedCallback = iPacketReceivedCallback;
    }

    BluetoothSPP.OnDataReceivedListener onDataReceivedListener = new BluetoothSPP.OnDataReceivedListener() {
        @Override
        public void onDataReceived(byte[] data, String message) {
            Log.d(logCat, "onDataReceived");
            Log.d(logCat, "Received Msg --" + message);
            handleReceivedData(data, message);
        }
    };

    private void handleReceivedData(byte[] data, String message) {

        try {

            if (message.startsWith(SignalConstants.Packet.PACKET_START_CHAR)
                    && message.endsWith(SignalConstants.Packet.PACKET_END_CHAR)
                    && message.length() == SignalConstants.Packet.MESSAGE_LENGTH_DCB) {

                message = message.substring(1, message.length() - 1);
                Log.d(logCat, "Received Msg -- valid packet - " + message);
                signalpacketDo = bluetoothPacketParser.parseSignal(message);

                if (signalpacketDo != null) {

                    bluetoothMessageController.proceedFinalMessage(message, signalpacketDo);

                    if (iBluetoothCallbacks != null && !iBluetoothCallbacks.isEmpty()) {
                        for (IBluetoothCallback iBluetoothCallback : iBluetoothCallbacks) {
                            if (iBluetoothCallback != null) {
                                iBluetoothCallback.onDataReceived(data, message);
                            }
                        }
                    }


                } else {
                    Log.d(logCat, "Received Do -- Not a valid signal DO.");
                }

            } else {
                Log.d(logCat, "Received Msg -- Not a valid packet. - " + message);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param message
     * @param signalpacketDo
     */
    private void proceedFinalMessage(String message, SignalPacketDo signalpacketDo) {
        if (signalpacketDo != null) {

            //means data received correctly.
            bluetoothLocalModel.setMachineRelay(signalpacketDo.getMachineRelay());
            if (iBluetoothCallbacks != null && !iBluetoothCallbacks.isEmpty()) {
                for (IBluetoothCallback iBluetoothCallback : iBluetoothCallbacks) {
                    if (iBluetoothCallback != null) {
                        iBluetoothCallback.setSignalStatus(signalpacketDo);
                    }
                }
            }

            if (isValidPacket(message, signalpacketDo)) {
                Log.d(logCat, "Received Msg -- packet Accepted. - " + message);

                String packetId = signalpacketDo.getPacketId();
                String machineMode = signalpacketDo.getMachineMode();

                Log.d(logCat, "Received Mode -- machineMode - " + machineMode);

                switch (machineMode) {
                    case SignalConstants.Actions.DCB_RESTART:
                        onDCBRest(signalpacketDo);
                        break;

                    case SignalConstants.Actions.COMMAND_MODE:
                        onCommandMode(signalpacketDo);
                        break;

                    case SignalConstants.Actions.PACKET_MODE:
                        onPacketMode(signalpacketDo);
                        break;

                    default:
                        Log.d(logCat, "Received Mode -- Not a valid mode - " + machineMode);
                        break;
//

                }

            } else {
                Log.d(logCat, "Received Msg -- packet Rejected. - " + message);

            }
        }
    }


    private boolean isValidPacket(String message, SignalPacketDo signalpacketDo) {
        return true;
    }

    private void onDCBRest(SignalPacketDo signalpacketDo) {
        AcknowledgmentController.getInstance().dispatchAcknowledgment(SignalConstants.AckIdentifier.ID_NORMAL_MODE, SignalConstants.Actions.COMMAND_MODE, null);
    }

    private void onPacketMode(SignalPacketDo signalpacketDo) {
        String packetId = signalpacketDo.getPacketId();
        int pactId = Integer.parseInt(packetId);
        if (pactId <= 256) {
            AcknowledgmentController.getInstance().dispatchAcknowledgment(SignalConstants.AckIdentifier.ID_NORMAL_MODE, null, null);
        } else {
            if (iPacketReceivedCallback != null) {
                iPacketReceivedCallback.onPacketReceived(true, signalpacketDo);
            }
        }
    }


    private void onCommandMode(SignalPacketDo signalpacketDo) {

        if (iPacketReceivedCallback != null) {
            iPacketReceivedCallback.onPacketReceived(true, signalpacketDo);
        }

    }


    BluetoothSPP.BluetoothConnectionListener bluetoothConnectionListener = new BluetoothSPP.BluetoothConnectionListener() {


        @Override
        public void onDeviceConnected(String name, String address) {
            Log.d(logCat, "onDeviceConnected");
//            if (LocalModel.getInstance().getUptimeCounter() != null) {
//                LocalModel.getInstance().getUptimeCounter().stopTimer();
//
//            }
            if (preferences != null) {
                saveDataToPreference(name, address);
            }

//            if (AutoBluetoothConnectService.isServiceRunning) {
//                //stops service here.
//                stopService();
//            }

            if (iBluetoothCallbacks != null && !iBluetoothCallbacks.isEmpty()) {
                for (IBluetoothCallback iBluetoothCallback : iBluetoothCallbacks) {
                    if (iBluetoothCallback != null) {
                        iBluetoothCallback.onDeviceConnected(name, address);
                    }
                }
            }
        }


        /**
         * Save connected device name and address to preferences.
         *
         * @param address
         */
        private void saveDataToPreference(String name, String address) {
            Log.d(logCat, " store data in preference");
            preferences.putString(IConstants.PREFERENCES.KEY_PREF_BLUETOOTH_DEVICE_ADDRESS, address);
            preferences.putString(IConstants.PREFERENCES.KEY_PREF_BLUETOOTH_DEVICE_NAME, name);
        }

        @Override
        public void onDeviceDisconnected() {
            Log.d(logCat, "onDeviceDisconnected");

//            int roleId = LocalModel.getInstance().getUserRoleId();
//            int processId = LocalModel.getInstance().getUserProcessId();


//            //Check for operator
//            if (processId == IConstants.PROCESS_ID.HSP && roleId == IConstants.ROLE.HSP_OPERATOR) {
//                new BluetoothDisconnectionDelayReason().startTimerWhenBluetoothDisconnected();
//
//            }

//            //checking the error is in progress
//            if (ErrorSignalManager.getInstance().isErrorInProgress()) {
//                Log.d(logCat, "ErrorProgress : Yes");
//                //if error is in progress the and device is disconnected then set true
//                setBluetoothDisconnectedInErrorFlow(true);
//                disableBluetoothInErrorFlow();
//            } else {
//                //making this false if currently not in the error flow.
//
//            }

            if (iBluetoothCallbacks != null && !iBluetoothCallbacks.isEmpty()) {
                for (IBluetoothCallback iBluetoothCallback : iBluetoothCallbacks) {
                    if (iBluetoothCallback != null) {
                        iBluetoothCallback.onDeviceDisconnected();
                    }
                }
            }
        }

        @Override
        public void onDeviceConnectionFailed() {
            Log.d(logCat, "onDeviceConnectionFailed");

            if (iBluetoothCallbacks != null && !iBluetoothCallbacks.isEmpty()) {
                for (IBluetoothCallback iBluetoothCallback : iBluetoothCallbacks) {
                    if (iBluetoothCallback != null) {
                        iBluetoothCallback.onDeviceConnectionFailed();
                    }
                }
            }
        }
    };


    /**
     * Return the {@link SignalPacketDo }
     * current SignalPacketDo
     *
     * @return
     */
    public SignalPacketDo getsignalpacketDo() {
        return signalpacketDo;
    }


    /**
     * Update old instance occupied by {@link BluetoothSPP}
     *
     * @param context
     */
    public void updateInstance(Context context) {
        this.context = context;
        initBlueToothComponent();

    }


    /**
     * Return true if bluetooth is connected else return false.
     *
     * @return
     */
    public boolean isBluetoothConnected() {
        if (bluetoothSPP != null) {
            return bluetoothSPP.isBluetoothConnected();
        }
        return false;
    }


    /**
     * Check for bluetooth is supported or not
     *
     * @return
     */
    public boolean isBluetoothSupported() {
        if (bluetoothSPP != null) {
            return bluetoothSPP.isBluetoothAvailable();
        }
        return false;
    }

    /**
     * check for bluetooth is enable or not
     *
     * @return
     */
    public boolean isBluetoothEnabled() {
        if (bluetoothSPP != null) {
            return bluetoothSPP.isBluetoothEnabled();
        }
        return false;
    }


    public void enableBluetooth() {
        if (bluetoothSPP != null) {
            if (!bluetoothSPP.isBluetoothEnabled()) {
                bluetoothSPP.enable();
                Log.d(logCat, "Bluetooth is Enabled ");
            } else {
                Log.d(logCat, "Bluetooth is Already Enabled ");

            }
        } else {
            Log.d(logCat, "Bluetooth Object is null");

        }
    }

    public void disableBluetooth() {
        if (bluetoothSPP != null) {
            if (bluetoothSPP.isBluetoothEnabled()) {
                bluetoothSPP.disable();
                Log.d(logCat, "Bluetooth Disabled ");
            }
        }
    }

    /**
     * Enable Bluetooth and and set up bluetooth connection is not connected
     */
    public void enableBluetoothAndSetUpConnection() {

        enableBluetooth();

        if (!isServiceAvailable()) {
            setupConnection();
        } else {
            Log.d(logCat, "ErrorProgress : Bluetooth connection is already Setup ");
        }
    }

    /**
     * Check the bluetooth service is available or not
     *
     * @return
     */
    public boolean isServiceAvailable() {
        if (bluetoothSPP != null) {
            return bluetoothSPP.isServiceAvailable();
        }
        return false;
    }


    /**
     * Return connected device name
     *
     * @return
     */
    public String getConnectedDeviceName() {
        if (bluetoothSPP != null) {
            return bluetoothSPP.getConnectedDeviceName() + " - " + bluetoothSPP.getConnectedDeviceAddress();
        }
        return null;
    }

    /**
     * Auto connect bluetooth
     *
     * @param address
     */
    public void autoConnect(String address) {
        if (bluetoothSPP != null) {
            bluetoothSPP.connect(address);
        }
    }

    /**
     * Here we can set and start the bluetooth services
     */
    public void setupConnection() {
        if (bluetoothSPP != null) {
            bluetoothSPP.setupService();
            bluetoothSPP.startService(BluetoothStateConstants.DEVICE_ANDROID);
            Log.d(logCat, "Bluetooth Connection is setup ");
        }
    }

    /**
     * Method which connect to bluetooth device
     *
     * @param address
     */
    public void connect(String address) {
        if (bluetoothSPP != null) {
            bluetoothSPP.connect(address);
        }
    }

    /**
     * Method which connect to bluetooth device
     *
     * @param data
     */
    public void connect(Intent data) {
        if (bluetoothSPP != null) {
            bluetoothSPP.connect(data);
        }
    }

    /**
     * Disconnect the bluetooth device
     */
    public void disconnect() {
        if (bluetoothSPP != null) {
            bluetoothSPP.disconnect();
        }
    }

    /**
     * return the bluetooth a
     *
     * @return
     */
    public BluetoothAdapter getBluetoothAdapter() {

        if (bluetoothSPP != null) {
            return bluetoothSPP.getBluetoothAdapter();
        }
        return null;
    }

    /**
     * Check the device is in discovering mode
     *
     * @return
     */
    public boolean isDiscovering() {
        if (bluetoothSPP != null) {
            BluetoothAdapter bluetoothAdapter = bluetoothSPP.getBluetoothAdapter();
            if (bluetoothAdapter != null) {
                return bluetoothAdapter.isDiscovering();
            }
        }
        return false;
    }

    /**
     * Cancel the discovering mode
     *
     * @return
     */
    public void cancelDiscovery() {
        if (bluetoothSPP != null) {
            BluetoothAdapter bluetoothAdapter = bluetoothSPP.getBluetoothAdapter();
            if (bluetoothAdapter != null) {
                bluetoothAdapter.cancelDiscovery();
            }
        }
    }

    /**
     * Start the discovering mode
     *
     * @return
     */
    public void startDiscovery() {
        if (bluetoothSPP != null) {
            BluetoothAdapter bluetoothAdapter = bluetoothSPP.getBluetoothAdapter();
            if (bluetoothAdapter != null) {
                bluetoothAdapter.startDiscovery();
            }
        }
    }


    /**
     * Method which return the array of paired devices
     *
     * @return
     */
    public Set<BluetoothDevice> getBondedDevices() {
        if (bluetoothSPP != null) {
            BluetoothAdapter bluetoothAdapter = bluetoothSPP.getBluetoothAdapter();
            if (bluetoothAdapter != null) {
                return bluetoothAdapter.getBondedDevices();
            }
        }
        return null;
    }


    /**
     * Sends acknowledgement in string form.
     */
    public void sendAck(String ackString, boolean maintainAsLastAck) {
        Log.d(logCat, "s--" + ackString);
        if (maintainAsLastAck) {
            this.lastSentAck = ackString;
        }
        if (ackString != null && bluetoothSPP != null) {
            if (bluetoothMessageController.isBluetoothConnected()) {
                bluetoothSPP.send(ackString, true);
            }
        }
    }
}

