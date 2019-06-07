package com.syslogyx.bluetoothdemo.bluetooth.dos;

/**
 * DO to holds the date of the acknowledgement packet.
 * While sends to the DCD  as a recieved acknowledgement as well as initiated packet.
 * Created by kaushik on 23-Feb-17.
 */

public class AcknowledgmentDO {

    private int id;
    private String dcd_uid;
    private String packetId;
    private String startStrokesCount;
    private String actionIdentifier;
    /**
     * all 16 sensor enable disable status update
     * Date Time update
     * End counter update
     */
    private String sensorStatus;


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

    public String getPacketId() {
        return packetId;
    }

    public void setPacketId(String packetId) {
        this.packetId = packetId;
    }

    public String getStartStrokesCount() {
        return startStrokesCount;
    }

    public void setStartStrokesCount(String startStrokesCount) {
        this.startStrokesCount = startStrokesCount;
    }

    public String getActionIdentifier() {
        return actionIdentifier;
    }

    public void setActionIdentifier(String actionIdentifier) {
        this.actionIdentifier = actionIdentifier;
    }

    public String getSensorStatus() {
        return sensorStatus;
    }

    public void setSensorStatus(String sensorStatus) {
        this.sensorStatus = sensorStatus;
    }
}
