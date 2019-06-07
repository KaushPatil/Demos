package com.syslogyx.bluetoothdemo.bluetooth.dos;

import java.util.HashMap;

/**
 * Created by kaushik on 27-Oct-16.
 */

public class SignalPacketDo {

    private static final String logCat = "Logs SignalDao--";
    private int id;
    private String customRefId;
    private String dcd_uid;
    private String packetId;
    private String timeStamp;
    private String counter;
    private String autoMode;
    private String manualMode;
    private String remainingSignals;
    private String machineMode;
    private String machineRelay;
    private String towerStatus;
    private String packetAvailable;
    private String futureUse;
    private String moNumber;
    private int machineId;



    private int status;
    private String receivedTimeStamp;
    private String created;
    private String updated;
    private HashMap<Integer, String> sensorHashMap = new HashMap<>();


    public String getCustomRefId() {
        return customRefId;
    }

    public void setCustomRefId(String customRefId) {
        this.customRefId = customRefId;
    }

    public String getDcd_uid() {
        return dcd_uid;
    }

    public void setDcd_uid(String dcd_uid) {
        this.dcd_uid = dcd_uid;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getPacketId() {
        return packetId;
    }

    public void setPacketId(String packetId) {
        this.packetId = packetId;
    }

    public String getCounter() {
        return counter;
    }

    public void setCounter(String counter) {
        this.counter = counter;
    }

    public String getMachineRelay() {
        return machineRelay;
    }

    public void setMachineRelay(String machineRelay) {
        this.machineRelay = machineRelay;
    }

    public String getTowerStatus() {
        return towerStatus;
    }

    public void setTowerStatus(String towerStatus) {
        this.towerStatus = towerStatus;
    }

    public HashMap<Integer, String> getSensorHashMap() {
        return sensorHashMap;
    }

    public void setSensorHashMap(HashMap<Integer, String> sensorHashMap) {
        this.sensorHashMap = sensorHashMap;
    }

    public String getRemainingSignals() {
        return remainingSignals;
    }

    public void setRemainingSignals(String remainingSignals) {
        this.remainingSignals = remainingSignals;
    }

    public String getManualMode() {
        return manualMode;
    }

    public void setManualMode(String manualMode) {
        this.manualMode = manualMode;
    }

    public String getAutoMode() {
        return autoMode;
    }

    public void setAutoMode(String autoMode) {
        this.autoMode = autoMode;
    }



    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getUpdated() {
        return updated;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getReceivedTimeStamp() {
        return receivedTimeStamp;
    }

    public void setReceivedTimeStamp(String receivedTimeStamp) {
        this.receivedTimeStamp = receivedTimeStamp;
    }

    public String getMachineMode() {
        return machineMode;
    }

    public void setMachineMode(String machineMode) {
        this.machineMode = machineMode;
    }

    public String getPacketAvailable() {
        return packetAvailable;
    }

    public void setPacketAvailable(String packetAvailable) {
        this.packetAvailable = packetAvailable;
    }

    public String getFutureUse() {
        return futureUse;
    }

    public void setFutureUse(String futureUse) {
        this.futureUse = futureUse;
    }


    public String getMoNumber() {
        return moNumber;
    }

    public void setMoNumber(String moNumber) {
        this.moNumber = moNumber;
    }

    public int getMachineId() {
        return machineId;
    }

    public void setMachineId(int machineId) {
        this.machineId = machineId;
    }

    /**
     * Create the copy of current do
     *
     * @param currentSignalPacketDo
     * @return
     */
    public static SignalPacketDo copySignalDo(SignalPacketDo currentSignalPacketDo) {
        SignalPacketDo copySignalPacketDo = new SignalPacketDo();

        if (currentSignalPacketDo != null) {
            copySignalPacketDo.setId(currentSignalPacketDo.getId());
            copySignalPacketDo.setCustomRefId(currentSignalPacketDo.getCustomRefId());
            copySignalPacketDo.setDcd_uid(currentSignalPacketDo.getDcd_uid());
            copySignalPacketDo.setPacketId(currentSignalPacketDo.getPacketId());
            copySignalPacketDo.setTimeStamp(currentSignalPacketDo.getTimeStamp());
            copySignalPacketDo.setCounter(currentSignalPacketDo.getCounter());
            copySignalPacketDo.setAutoMode(currentSignalPacketDo.getAutoMode());
            copySignalPacketDo.setManualMode(currentSignalPacketDo.getManualMode());
            copySignalPacketDo.setRemainingSignals(currentSignalPacketDo.getRemainingSignals());
            copySignalPacketDo.setMachineMode(currentSignalPacketDo.getManualMode());
            copySignalPacketDo.setMachineRelay(currentSignalPacketDo.getMachineRelay());
            copySignalPacketDo.setTowerStatus(currentSignalPacketDo.getTowerStatus());
            copySignalPacketDo.setFutureUse(currentSignalPacketDo.getFutureUse());
            if (currentSignalPacketDo.getMoNumber() != null) {
                copySignalPacketDo.setMoNumber(currentSignalPacketDo.getMoNumber());
            } else {
                copySignalPacketDo.setMoNumber(null);
            }

            copySignalPacketDo.setMachineId(currentSignalPacketDo.getMachineId());
            copySignalPacketDo.setStatus(currentSignalPacketDo.getStatus());
            copySignalPacketDo.setReceivedTimeStamp(currentSignalPacketDo.getReceivedTimeStamp());
            copySignalPacketDo.setSensorHashMap(currentSignalPacketDo.getSensorHashMap());

            return copySignalPacketDo;
        }
        return null;
    }

}
