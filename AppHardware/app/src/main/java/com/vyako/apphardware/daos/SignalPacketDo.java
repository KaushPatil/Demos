package com.vyako.apphardware.daos;

import java.util.HashMap;

/**
 * Created by kaushik on 27-Oct-16.
 */

public class SignalPacketDo {

    private static final String logCat = "Logs SignalDao--";
    private int id;
    private String customRefId;
    private String dcd_uid;
    private String packet_id;
    private String timeStamp;
    private String counter;
    private String autoMode;
    private String manualMode;
    private String remainingSignals;
    private String towerStatus;
    private String machineRelay;
    private String mo_number;
    private int machine_id;
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

    public String getPacket_id() {
        return packet_id;
    }

    public void setPacket_id(String packet_id) {
        this.packet_id = packet_id;
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

    public String getMo_number() {
        return mo_number;
    }

    public void setMo_number(String mo_number) {
        this.mo_number = mo_number;
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

    public int getMachine_id() {
        return machine_id;
    }

    public void setMachine_id(int machine_id) {
        this.machine_id = machine_id;
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
            copySignalPacketDo.setPacket_id(currentSignalPacketDo.getPacket_id());
            copySignalPacketDo.setTimeStamp(currentSignalPacketDo.getTimeStamp());
            copySignalPacketDo.setCounter(currentSignalPacketDo.getCounter());
            copySignalPacketDo.setAutoMode(currentSignalPacketDo.getAutoMode());
            copySignalPacketDo.setManualMode(currentSignalPacketDo.getManualMode());
            copySignalPacketDo.setRemainingSignals(currentSignalPacketDo.getRemainingSignals());
            copySignalPacketDo.setTowerStatus(currentSignalPacketDo.getTowerStatus());
            copySignalPacketDo.setMachineRelay(currentSignalPacketDo.getMachineRelay());
            copySignalPacketDo.setMo_number(currentSignalPacketDo.getMo_number());
            copySignalPacketDo.setMachine_id(currentSignalPacketDo.getMachine_id());
            copySignalPacketDo.setStatus(currentSignalPacketDo.getStatus());
            copySignalPacketDo.setReceivedTimeStamp(currentSignalPacketDo.getReceivedTimeStamp());
            copySignalPacketDo.setSensorHashMap(currentSignalPacketDo.getSensorHashMap());

            return copySignalPacketDo;
        }
        return null;
    }

}
