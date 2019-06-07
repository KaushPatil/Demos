package com.syslogyx.bluetoothdemo.bluetooth.parser;

import android.util.Log;

import com.syslogyx.bluetoothdemo.app.utils.Utils;
import com.syslogyx.bluetoothdemo.bluetooth.constant.SignalConstants;
import com.syslogyx.bluetoothdemo.bluetooth.dos.AcknowledgmentDO;
import com.syslogyx.bluetoothdemo.bluetooth.dos.SignalPacketDo;

import java.util.HashMap;

/**
 * Created by Kaushik on 3/4/18.
 * <p>
 * Parser the packet received String into DO (DCB - Android)
 * Parser the ACK DO into String (Android - DCB)
 */

public class BluetoothPacketParser {

    private static final String logCat = "Logg>>";

    /**
     * Factory Name, Factory Number and DCB ID
     * 10 digits (Bytes)
     */
    private final int DCD_UID_START_INDEX = 0;
    private final int DCD_UID_END_INDEX = 10;

    /**
     * Timestamp in Seconds
     * 10 digits (Bytes)
     */
    private final int TIME_STAMP_START_INDEX = 10;
    private final int TIME_STAMP_END_INDEX = 20;

    /**
     * Packet Id
     * 3 digits (Bytes)
     */
    private final int PACKET_ID_START_INDEX = 20;
    private final int PACKET_ID_END_INDEX = 23;

    /**
     * Counters
     * 6 digits (Bytes)
     */
    private final int COUNTER_START_INDEX = 23;
    private final int COUNTER_END_INDEX = 29;

    /**
     * Sensor Signal
     * 15 bytes
     */
    private final int SENSOR_SIGNAL_START_INDEX = 29;
    private final int SENSOR_SIGNAL_END_INDEX = 44;

    /**
     * Machine Mode status
     * 1 byte
     */
    private final int MACHINE_MODE_START_INDEX = 44;
    private final int MACHINE_MODE_END_INDEX = 45;

    /**
     * Machine Relay status
     * 1 byte
     */
    private final int MACHINE_RELAY_START_INDEX = 45;
    private final int MACHINE_RELAY_END_INDEX = 46;

    /**
     * Tower Light status
     * 1 byte
     */
    private final int TOWER_LIGHT_START_INDEX = 46;
    private final int TOWER_LIGHT_END_INDEX = 47;

    /**
     * Machine Status or Packet Available status
     * 1 byte
     */
    private final int MACHINE_STATUS_START_INDEX = 47;
    private final int MACHINE_STATUS_END_INDEX = 48;

    /**
     * Future use
     * 1 byte
     */
    private final int FUTURE_USE_START_INDEX = 48;
    private final int FUTURE_USE_END_INDEX = 49;

    //Total length of signal message
    private final int SIGNAL_MESSAGE_LENGTH = 49;


    // Sensor Signals (sensor signal is nothing but substring of packet we received)
    // Auto Mode from sensor signal 1st character
    private final int AUTO_MODE_START_INDEX = 0;
    private final int AUTO_MODE_END_INDEX = 1;

    // Manual Mode from sensor signal 2st character
    private final int MANUAL_MODE_START_INDEX = 1;
    private final int MANUAL_MODE_END_INDEX = 2;

    // Remaining sensor signal 1st character (excluding auto mode and manual mode i.e first 2 character's)
    private final int REMAINING_SIGNALS_START_INDEX = 2;


    public BluetoothPacketParser() {
    }

    /**
     * Parse the signal string into SignalPacketDo
     * message = "SCEL0112341522739565999000100111111111111111C1GNX"
     * DCB ID - "SCEL011234" (10 bytes)
     * Time stamp - "1522739565" (10 bytes)
     * Packet Id - "999" (3 bytes)
     * Counters - "000100" (6 bytes)
     * Sensor - "111111111111111" (15 bytes)
     * Machine mode - "C" (1 byte)
     * Machine Relay - "1" (1 byte)
     * Tower Light - "G" (1 byte)
     * Machine status - "N" (1 byte)
     * Future use - "X" (1 byte)
     *
     * @param message signal string which need to parse
     * @return
     */
    public SignalPacketDo parseSignal(String message) {
        if (message != null && message.length() == SIGNAL_MESSAGE_LENGTH) {
            SignalPacketDo signalStatusDao = new SignalPacketDo();

            //Parsing DCD_UID
            String dcd_uid = message.substring(DCD_UID_START_INDEX, DCD_UID_END_INDEX);
            signalStatusDao.setDcd_uid(dcd_uid);

            //Parsing Timestamp
            String timeStamp = message.substring(TIME_STAMP_START_INDEX, TIME_STAMP_END_INDEX);
            signalStatusDao.setTimeStamp(timeStamp);

            //parsers Packet Id
            String packet_id = message.substring(PACKET_ID_START_INDEX, PACKET_ID_END_INDEX);
            signalStatusDao.setPacketId(packet_id);

            //Parsing Counter and checking the number format exception
            String counter = message.substring(COUNTER_START_INDEX, COUNTER_END_INDEX);
            signalStatusDao.setCounter(counter);

            //Parsing Sensor signal
            String sensorSignal = message.substring(SENSOR_SIGNAL_START_INDEX, SENSOR_SIGNAL_END_INDEX);
            HashMap<Integer, String> sensorHashMap = getSensorData(sensorSignal);
            signalStatusDao.setSensorHashMap(sensorHashMap);

            //Get the auto mode value from the sensor signals auto mode is 1st character of sensor signal
            String autoMode = sensorSignal.substring(AUTO_MODE_START_INDEX, AUTO_MODE_END_INDEX);
            signalStatusDao.setAutoMode(autoMode);

            //Get the manual mode value from the sensor signals manual mode is 2nd character of sensor signal
            String manualMode = sensorSignal.substring(MANUAL_MODE_START_INDEX, MANUAL_MODE_END_INDEX);
            signalStatusDao.setManualMode(manualMode);

            //add remaining signal from sensor signal
            String remainingSignals = sensorSignal.substring(REMAINING_SIGNALS_START_INDEX, sensorSignal.length());
            signalStatusDao.setRemainingSignals(remainingSignals);

            //Parsing Machine Mode
            String machineMode = message.substring(MACHINE_MODE_START_INDEX, MACHINE_MODE_END_INDEX);
            signalStatusDao.setMachineMode(machineMode);

            //Parsing MachineRelay
            String machineRelay = message.substring(MACHINE_RELAY_START_INDEX, MACHINE_RELAY_END_INDEX);
            signalStatusDao.setMachineRelay(machineRelay);

            //Parsing TowerStatus
            String towerStatus = message.substring(TOWER_LIGHT_START_INDEX, TOWER_LIGHT_END_INDEX);
            signalStatusDao.setTowerStatus(towerStatus);

            //Parsing Machine status Available packets
            String availablePacket = message.substring(MACHINE_STATUS_START_INDEX, MACHINE_STATUS_END_INDEX);
            signalStatusDao.setPacketAvailable(availablePacket);

            //Parsing Future use
            String futureUse = message.substring(FUTURE_USE_START_INDEX, FUTURE_USE_END_INDEX);
            signalStatusDao.setPacketAvailable(futureUse);

            //Received time stamp
            signalStatusDao.setReceivedTimeStamp(Utils.getCurrentTimeStampInSecondsInGMT());

            //status of packet to normal
            signalStatusDao.setStatus(SignalConstants.Packet.EVENT_NORMAL_STATUS);

            // TODO: 3/4/18 go to  generateSignalPacketUniqueId method and uncomment code
            //generate the unique packet id.(custom ref id)
            String signalPacketId = Utils.generateSignalPacketUniqueId(packet_id, timeStamp);
            signalStatusDao.setCustomRefId(signalPacketId);

            // TODO: 3/4/18 get mo number uncomment code
//            //set current mo number
//            if (MoManager.getInstance().getCurrentMoNumber() != null) {
//                signalStatusDao.setMoNumber(MoManager.getInstance().getCurrentMoNumber());
//            } else {
//                signalStatusDao.setMoNumber(null);
//            }
            signalStatusDao.setMoNumber(null);

            // TODO: 3/4/18 get machine id from local model uncomment code
//            signalStatusDao.setMachineId(LocalModel.getInstance().getCurrentMachineId());
            signalStatusDao.setMachineId(0);


            try {

            } catch (Exception e) {
                e.printStackTrace();
                Log.d(logCat, "parseSignal: Exception - " + e.getMessage());
                return null;
            }
            return signalStatusDao;
        }
        return null;
    }


    /**
     * Returns the HashMap of status signal
     *
     * @param sensorSignal
     * @return
     */
    private HashMap<Integer, String> getSensorData(String sensorSignal) {
        //signalKeyInc represents the label of sensor signal,As sensor signal starts with 2 i.e it is initialize with 2 and
        // using it as key of respective sensor signal


        int signalKeyInc = 2;
        HashMap<Integer, String> sensorHashMap = new HashMap<>();
        for (int index = 0; index < sensorSignal.length(); index++) {
            String signalValue = sensorSignal.charAt(index) + "";
            sensorHashMap.put(signalKeyInc, signalValue);
            signalKeyInc++;
        }
        return sensorHashMap;

    }

    /**
     * Returns the Acknowledgement string data
     * Acknowledgement format "@SCEL011234998000100P1111111111111111#" = (38 Bytes)
     * <p>
     * "@" = Start identifier  (1 Bytes)
     * "SCEL011234" = DCB ID (10 Bytes)
     * "998" = Packet Id (3 Bytes)
     * "000100" = Start Counter (6 Bytes)
     * "P" = Action command (1 Bytes)
     * "111111111111111" = sensor status / date time / end counter  (16 Bytes)
     * "#" = End identifier (1 Bytes)
     *
     * @param acknowledgmentDO
     * @return
     */
    public String constructAcknowledgementMessage(AcknowledgmentDO acknowledgmentDO) {
        if (acknowledgmentDO != null) {

            String dcd_uid = acknowledgmentDO.getDcd_uid();
            String packetId = acknowledgmentDO.getPacketId();
            String startCounter = acknowledgmentDO.getStartStrokesCount();
            String actionIdentifier = acknowledgmentDO.getActionIdentifier();
            String sensorStatus = acknowledgmentDO.getSensorStatus();

            String finalAck = SignalConstants.Packet.PACKET_START_CHAR +
                    dcd_uid + packetId + startCounter + actionIdentifier + sensorStatus +
                    SignalConstants.Packet.PACKET_END_CHAR;

            Log.d("Logs Ack--", "size = " + finalAck.length() + " Ack Packet = " + finalAck);
            return finalAck;
        }
        return null;
    }


}
