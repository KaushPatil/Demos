package com.vyako.apphardware.parser;


import android.util.Log;


import com.vyako.apphardware.daos.AcknowledgmentDO;
import com.vyako.apphardware.daos.SignalPacketDo;

import java.util.HashMap;


/**
 * BluetoothSignalParser parse the machine string signal for {@link SignalPacketDo}
 * <p/>
 * Receiver end packet format "@STPL0254311476590790001007450111111111111G#" = (44-Bytes)
 * STPL0254311476590790001007450111111111111G = (42-Bytes)
 * </P>
 * "@" = Start Character (1-byte)
 * "STPL025431" = DCD UID (10-Bytes) (Numeric Only with Leading Zeros)
 * "1476590790" = TimeStamp (10-Byte)
 * "001" = Packet Identifier (3 byte)
 * "'007450"  =  Counter Value (6-Bytes )
 * "11111111111" = Sensors (11-Bytes)
 * "1" = Machine Relay Status (1-Byte)
 * "G" = Tower Lights Controlling Signal (1 Out of 5 Signals) (1- Byte)
 * "#" = End Character (1-Byte)
 * <p>
 * Machine Relay Status
 * (O-Machine OFF / C-MO Close & Machine OFF
 * H-MO Hold & Machine OFF
 * S-MO Start or Resume
 * R-Machine ON (Continue old Counting)
 * X-Ignore(Stay on old state))
 * <p>
 * Tower Lights Controlling Signal
 * (G - Green
 * B - Blue
 * Y - Yellow
 * O - Orange
 * R - Red)
 */

public class BluetoothSignalParser {
    //Start index of DCD_UID
    private final int DCD_UID_START_INDEX = 0;
    //End index of DCD_UID
    private final int DCD_UID_END_INDEX = 10;

    //Start index of Timestamp
    private final int TIME_STAMP_START_INDEX = 10;
    //End index of Timestamp
    private final int TIME_STAMP_END_INDEX = 20;

    //Start index of Packet Identifier
    private final int PACKET_ID_START_INDEX = 20;
    //Start index of Packet Identifier
    private final int PACKET_ID_END_INDEX = 23;

    //Start index of Counter
    private final int COUNTER_START_INDEX = 23;
    //End index of Counter
    private final int COUNTER_END_INDEX = 29;

    //Start index of Sensor Signal
    private final int SENSOR_SIGNAL_START_INDEX = 29;
    //End index of Sensor Signal
    private final int SENSOR_SIGNAL_END_INDEX = 40;

    //Start index of Machine Relay
    private final int MACHINE_RELAY_START_INDEX = 40;
    //End index of Machine Relay
    private final int MACHINE_RELAY_END_INDEX = 41;

    //Start index of Tower Status
    private final int TOWER_STATUS_START_INDEX = 41;
    //End index of Tower Status
    private final int TOWER_STATUS_END_INDEX = 42;

    //Total length of signal message
    private final int SIGNAL_MESSAGE_LENGTH = 42;


    // Sensor Signals (sensor signal is nothing but substring of packet we received)
    // Auto Mode from sensor signal 1st character
    private final int AUTO_MODE_START_INDEX = 0;
    private final int AUTO_MODE_END_INDEX = 1;

    // Manual Mode from sensor signal 2st character
    private final int MANUAL_MODE_START_INDEX = 1;
    private final int MANUAL_MODE_END_INDEX = 2;

    // Remaining sensor signal 1st character (excluding auto mode and manual mode i.e first 2 character's)
    private final int REMAINING_SIGNALS_START_INDEX = 2;


    public BluetoothSignalParser() {

    }

    /**
     * Parse the signal string into {@link SignalPacketDo}
     *
     * @param message signal string which need to parse
     * @return
     * @SCEL010001149037256000200051101111111111SG# "STPL0254311476590790001007450111111111111G"
     * Receiver end packet format "STPL0254311476590790001007450111111111111G"
     */
    public SignalPacketDo parseSignal(String message) {
        if (message != null && message.length() == SIGNAL_MESSAGE_LENGTH) {
            SignalPacketDo signalStatusDao = new SignalPacketDo();
            try {
                //Parsing DCD_UID
                String dcd_uid = message.substring(DCD_UID_START_INDEX, DCD_UID_END_INDEX);
                signalStatusDao.setDcd_uid(dcd_uid);

                //Parsing Timestamp
                String timeStamp = message.substring(TIME_STAMP_START_INDEX, TIME_STAMP_END_INDEX);
                signalStatusDao.setTimeStamp(timeStamp);

                //parsers Packet Identifier
                String packet_id = message.substring(PACKET_ID_START_INDEX, PACKET_ID_END_INDEX);
                signalStatusDao.setPacket_id(packet_id);

                //Parsing Counter and checking the number format exception
                String counter = message.substring(COUNTER_START_INDEX, COUNTER_END_INDEX);
                try {
                    int counts = Integer.parseInt(counter);
                    signalStatusDao.setCounter(counter);
                } catch (NumberFormatException e) {
                    System.out.println("Counters : " + counter);
                    e.printStackTrace();
//                    throw new NumberFormatException();
                    return null;
                }
//                signalStatusDao.setCounter(counter);

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

                //Parsing MachineRelay
                String machineRelay = message.substring(MACHINE_RELAY_START_INDEX, MACHINE_RELAY_END_INDEX);
                signalStatusDao.setMachineRelay(machineRelay);

                //Parsing TowerStatus
                String towerStatus = message.substring(TOWER_STATUS_START_INDEX, TOWER_STATUS_END_INDEX);
                signalStatusDao.setTowerStatus(towerStatus);

                //set current mo number
//                signalStatusDao.setMo_number(MoManager.getInstance().getCurrentMoNumber());
//                signalStatusDao.setMo_number("MO-001");

                //Received time stamp
                signalStatusDao.setReceivedTimeStamp(String.valueOf(System.currentTimeMillis()));

                //status of packet to normal
                signalStatusDao.setStatus(1);

//                signalStatusDao.setMachine_id(LocalModel.getInstance().getCurrentMachineId());
                signalStatusDao.setMachine_id(1001);

                //generate the unique packet id.(custom ref id)
//                String signalPacketId = Utils.generateSignalPacketUniqueId(packet_id, timeStamp);
                String signalPacketId = "ID-1001";
                signalStatusDao.setCustomRefId(signalPacketId);

            } catch (Exception e) {
                e.printStackTrace();
                System.out.println(e.getMessage());
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
     * Acknowledgement format "@STPL0254310011G#" = (17-Bytes)
     * "@" = Start Byte
     * "STPL025431" = DCD UID (10-Bytes) (Numeric Only with Leading Zeros)
     * "001" = packet identifier (3-Bytes)
     * "000000" = startStrokesCount
     * "000000" = endStrokesCount
     * <p>
     * "U/N/X" = getUpdateIdentifier (U - Update / N - No Update / X - Ignore)
     * <p>
     * "O/C/H/S/R/X" = Machine ON/OFF Command
     * (O-Machine OFF / C-MO Close & Machine OFF
     * H-MO Hold & Machine OFF
     * S-MO Start or Resume
     * R-Machine ON (Continue old Counting)
     * X-Ignore(Stay on old state))
     * <p>
     * "G/B/Y/O/R" = Tower Lights Controlling Signal (5 Signals)
     * (G - Green
     * B - Blue
     * Y - Yellow
     * O - Orange
     * R - Red)
     * "#" = End Character
     *
     * @param acknowledgmentDO
     * @return
     */

    public String constructAcknowledgementMessage(AcknowledgmentDO acknowledgmentDO) {

        if (acknowledgmentDO != null) {
            String dcd_uid = acknowledgmentDO.getDcd_uid();
            String packet_id = acknowledgmentDO.getPacket_id();
            String startStrokesCount = acknowledgmentDO.getStartStrokesCount();
            String updateIdentifier = acknowledgmentDO.getUpdateIdentifier();
            String endStrokesCount = acknowledgmentDO.getEndStrokesCount();
            String towerStatus = acknowledgmentDO.getTowerStatus();
            String machineRelay = acknowledgmentDO.getMachineRelay();

//            String machineRelay = BluetoothLocalModel.getInstance().getMachineRelay();
            String finalAck = "@" + dcd_uid + packet_id + startStrokesCount + updateIdentifier + endStrokesCount + machineRelay + towerStatus + "#";
            Log.d("Logs Ack--", "size = " + finalAck.length() + " Ack Packet = " + finalAck);
            return finalAck;
        }
        return null;
    }

}
