package com.vyako.apphardware.daos;

/**
 * DO to holds the date of the acknowledgement packet.
 * While sends to the DCD  as a recieved acknowledgement as well as initiated packet.
 * Created by kaushik on 23-Feb-17.
 */

public class AcknowledgmentDO {

    private int id;
    private String dcd_uid;
    private String packet_id;
    private String startStrokesCount;
    private String updateIdentifier;
    private String endStrokesCount;
    private String machineRelay;
    private String towerStatus;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDcd_uid() {
        return dcd_uid;
    }

    public void setDcd_uid(String dcd_uid) {
        this.dcd_uid = dcd_uid;
    }

    public String getPacket_id() {
        return packet_id;
    }

    public void setPacket_id(String packet_id) {
        this.packet_id = packet_id;
    }

    public String getStartStrokesCount() {
        return startStrokesCount;
    }

    public void setStartStrokesCount(String startStrokesCount) {
        this.startStrokesCount = startStrokesCount;
    }

    public String getUpdateIdentifier() {
        return updateIdentifier;
    }

    public void setUpdateIdentifier(String updateIdentifier) {
        this.updateIdentifier = updateIdentifier;
    }

    public String getEndStrokesCount() {
        return endStrokesCount;
    }

    public void setEndStrokesCount(String endStrokesCount) {
        this.endStrokesCount = endStrokesCount;
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
}
