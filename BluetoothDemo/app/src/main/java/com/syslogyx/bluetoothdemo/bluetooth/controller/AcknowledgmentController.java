package com.syslogyx.bluetoothdemo.bluetooth.controller;

import android.os.CountDownTimer;
import android.util.Log;

import com.syslogyx.bluetoothdemo.bluetooth.constant.SignalConstants;
import com.syslogyx.bluetoothdemo.bluetooth.dos.AcknowledgmentDO;
import com.syslogyx.bluetoothdemo.bluetooth.dos.SignalPacketDo;
import com.syslogyx.bluetoothdemo.bluetooth.listeners.IPacketReceivedCallback;
import com.syslogyx.bluetoothdemo.bluetooth.parser.BluetoothPacketParser;

/**
 * Created by kaushik on 17-Feb-17.
 */

public class AcknowledgmentController {

    private static final String logCat = "Logg>>";

    private static AcknowledgmentController acknowledgmentController;
    private BluetoothPacketParser bluetoothPacketParser;
    private BluetoothMessageController bluetoothMessageController;
    /**
     * Keeps the object of the high priority data such as machine on/off command.
     */
    private HighPriorityData highPriorityData;

    /**
     * Timer class object to send ACK repeatedly
     */
    private CountDownTimer countDownTimer;


    private AcknowledgmentController() {
        initComponents();
    }

    /**
     * init all other components
     */
    private void initComponents() {
        bluetoothMessageController = BluetoothMessageController.getInstance();
        highPriorityData = new HighPriorityData();
        highPriorityData.setMarkedCritical(false);
    }

    /**
     * @return
     */
    public static AcknowledgmentController getInstance() {
        if (acknowledgmentController == null) {
            acknowledgmentController = new AcknowledgmentController();
        }
        return acknowledgmentController;
    }


    /**
     * * Method which identify the ack type and send ack respectively
     * *
     * * @param identifier
     * * @param data
     *
     * @param iPacketReceivedCallback
     */
    public void dispatchAcknowledgment(byte identifier, Object data, IPacketReceivedCallback iPacketReceivedCallback) {
        switch (identifier) {

            case SignalConstants.AckIdentifier.ID_NORMAL_MODE:
                Log.e(logCat, "ack -- normal mode");
                ackForPacketMode(data);
                break;

            case SignalConstants.AckIdentifier.ID_DCB_RESTART:
                Log.e(logCat, "ack -- 999");
                ackForDcbMode(iPacketReceivedCallback, data, SignalConstants.PacketIds.DCB_RESTART);
                break;

            case SignalConstants.AckIdentifier.ID_DCB_MODE:
                Log.e(logCat, "ack -- 998");
                ackForDcbMode(iPacketReceivedCallback, data, SignalConstants.PacketIds.DCB_MODE);
                break;

            case SignalConstants.AckIdentifier.ID_MACHINE_RELAY:
                Log.e(logCat, "ack -- 997");
                ackForDcbMode(iPacketReceivedCallback, data, SignalConstants.PacketIds.MACHINE_RELAY);
                break;

            case SignalConstants.AckIdentifier.ID_TOWER_LIGHT:
                Log.e(logCat, "ack -- 996");
                break;

            case SignalConstants.AckIdentifier.ID_COUNTER_UPDATE:
                Log.e(logCat, "ack -- 995");
                break;

            case SignalConstants.AckIdentifier.ID_TIME_UPDATE:
                Log.e(logCat, "ack -- 994");
                break;

            case SignalConstants.AckIdentifier.ID_MACHINE_STATUS:
                Log.e(logCat, "ack -- 993");
                break;

            case SignalConstants.AckIdentifier.ID_COUNTER_ENABLE:
                Log.e(logCat, "ack -- 992");
                break;

            case SignalConstants.AckIdentifier.ID_SENSOR_ENABLE:
                Log.e(logCat, "ack -- 991");
                break;

            case SignalConstants.AckIdentifier.ID_DELAY_TIMER_ENABLE:
                Log.e(logCat, "ack -- 990");
                break;

            default:
                throw new RuntimeException("Not a valid ack identifier");


        }
    }


    // TODO: 27/3/18 Need to update the code  this is temp code
    private void ackForPacketMode(Object data) {
        String action = SignalConstants.Actions.PACKET_MODE;
        if (data != null && data instanceof String) {
            action = (String) data;
        }
        String ackMessage = constructAckForPacketMode(action);
        sendAcknowledgment(ackMessage);
    }

    // TODO: 27/3/18 Need to update the code  this is temp code
    private void ackForDcbMode(IPacketReceivedCallback iPacketReceivedCallback, Object data, String packetId) {
        if (iPacketReceivedCallback != null) {
            bluetoothMessageController.setPacketReceivedCallback(iPacketReceivedCallback);
        }
        String action = SignalConstants.Actions.COMMAND_MODE;

        if (data != null && data instanceof String) {
            action = (String) data;
        }

        String ackMessage = constructAckPacketForCommandMode(packetId, action);
//        sendAcknowledgment(ackMessage);
        startTimer(ackMessage);
    }

    /**
     * Construct Acknowledgment in the format as specified for packet.
     *
     * @param acknowledgmentDO string format of the acknowledgement
     * @return
     */
    public String formatAck(AcknowledgmentDO acknowledgmentDO) {
        String acknowledgementMessage = null;
        bluetoothPacketParser = new BluetoothPacketParser();
        acknowledgementMessage = bluetoothPacketParser.constructAcknowledgementMessage(acknowledgmentDO);
        Log.d(logCat, "Ack--" + acknowledgementMessage);
        return acknowledgementMessage;
    }

    /**
     * Prepares the ack do by setting the values passed in parameters.
     *
     * @return
     */
    private AcknowledgmentDO prepareAckDO(String dcdUUID, String packetId, String
            startStrokesCount, String actionIdentifier, String sensorData) {
        AcknowledgmentDO acknowledgmentDO = new AcknowledgmentDO();
        acknowledgmentDO.setDcd_uid(dcdUUID);
        acknowledgmentDO.setPacketId(packetId);
        acknowledgmentDO.setStartStrokesCount(startStrokesCount);
        acknowledgmentDO.setActionIdentifier(actionIdentifier);
        acknowledgmentDO.setSensorStatus(sensorData);
        return acknowledgmentDO;
    }

    /**
     * Send acknowledgment
     *
     * @param ackString
     */
    private void sendAcknowledgment(String ackString) {
        if (ackString != null) {
            Log.e(logCat, "SendPacket--" + ackString);
            bluetoothMessageController.sendAck(ackString, true);
        }
    }

    /**
     * Construct the acknowledgment packet format string to send to DCB as command ack.
     *
     * @param packetId
     * @param action
     * @return
     */
    private String constructAckPacketForCommandMode(String packetId, String action) {

        String dcdUUID = SignalConstants.Packet.FACTORY_DCB;
        String startCounter = SignalConstants.Packet.DEFAULT_COUNTERS;

        String sensors = SignalConstants.Packet.DEFAULT_ALL_SENSOR;

        if (highPriorityData != null && highPriorityData.isMarkedCritical()) {
            Log.d(logCat, "ACK SENT >> UPDATE CRITICAL DATA TO NORMAL");

            if (highPriorityData.getAction() != null) {
                action = highPriorityData.getAction();
            }

            if (highPriorityData.getStartCounter() != null) {
                startCounter = highPriorityData.getStartCounter();
            }

            highPriorityData.isDataUsed = true;
            //calls as data has been used, so give callback.
        }

        AcknowledgmentDO acknowledgmentDO = prepareAckDO(dcdUUID, packetId, startCounter, action, sensors);
        return formatAck(acknowledgmentDO);
    }


    /**
     * Construct the acknowledgment packet format string to send to DCB as normal ack.
     * Prepare ack is  based on received packet
     */
    private String constructAckForPacketMode(String actionVariable) {
        Log.d(logCat, "constructAckForPacketMode");
        SignalPacketDo signalPacketDo = bluetoothMessageController.getsignalpacketDo();

        if (signalPacketDo != null) {

            String dcdUUID = signalPacketDo.getDcd_uid();
            String action = signalPacketDo.getMachineMode();
            String packetId = signalPacketDo.getPacketId();
            String startCounter = signalPacketDo.getCounter();

            String sensors = SignalConstants.Packet.DEFAULT_ALL_SENSOR;


            if (highPriorityData != null && highPriorityData.isMarkedCritical()) {
                Log.d(logCat, "ACK SENT >> UPDATE CRITICAL DATA TO NORMAL");

                if (highPriorityData.getAction() != null) {
                    action = highPriorityData.getAction();
                }

                highPriorityData.isDataUsed = true;
                //calls as data has been used, so give callback.
            }

            //prepared the acknowledgement do to format details into the string.
            AcknowledgmentDO acknowledgmentDO = prepareAckDO(dcdUUID, packetId, startCounter, action, sensors);
//            Log.d(logCat, "StartCounter9- " + acknowledgmentDO.getStartStrokesCount() + "");
            //sends packet string formatted.
            return formatAck(acknowledgmentDO);
        }

        return null;
    }


    /**
     * Keeps the high priority data, if enabled them load data from this class.
     */
    private class HighPriorityData {
        /**
         * For setting the start counter as well if needed, intend to set start counter in the hold and close acknowledgement for now.
         */
        private String startCounter;
        private String action;
        private String sensor;
        private boolean isMarkedCritical;

        /**
         * Is data used.
         */
        private boolean isDataUsed;

        public String getStartCounter() {
            return startCounter;
        }

        public void setStartCounter(String startCounter) {
            this.startCounter = startCounter;
        }


        public boolean isMarkedCritical() {
            return isMarkedCritical;
        }

        public void setMarkedCritical(boolean markedCritical) {
            isMarkedCritical = markedCritical;
        }

        public String getAction() {
            return action;
        }

        public void setAction(String action) {
            this.action = action;
        }

        public String getSensor() {
            return sensor;
        }

        public void setSensor(String sensor) {
            this.sensor = sensor;
        }

        /**
         * Calls this method when data gets used & has to be disable.
         */
        public void onCriticalDataUsed() {
            this.isMarkedCritical = false;
        }
    }


    private void startTimer(final String ack) {

        countDownTimer = new CountDownTimer(3000, 500) {

            public void onTick(long millisUntilFinished) {
//                Log.d(logCat, "onTick: " + millisUntilFinished);
                Log.d(logCat, "onTick: event time " + millisUntilFinished / 500);

                sendAcknowledgment(ack);
            }

            public void onFinish() {
                Log.d(logCat, "onFinish: ");
            }
        };

        countDownTimer.start();

    }


    public void stopTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
        }
    }


}
