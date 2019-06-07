package com.vyako.apphardware.daos;

/**
 * Created by kaushik on 27-Oct-16.
 */

public class SignalStatusDao {

    private static final String logCat = "Logs SignalDao--";

    private String SignalId;
    private String SignalValue;

    public SignalStatusDao() {

    }

    public String getSignalId() {
        return SignalId;
    }

    public void setSignalId(String signalId) {
        SignalId = signalId;
    }

    public String getSignalValue() {
        return SignalValue;
    }

    public void setSignalValue(String signalValue) {
        SignalValue = signalValue;
    }


}
