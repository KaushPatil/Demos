package com.syslogyx.bluetoothdemo.app.perference;

import android.content.Context;
import android.content.SharedPreferences;


/**
 * Created by kaushik on 22-Nov-16.
 * Generic class for performing any preferences related operations.
 */
public class Preferences {
    public static final byte VALUE_DEFAULT_DECIMAL = 0;
    private SharedPreferences preferences;
    private int PRIVATE_MODE = 0;
    public static final int KEY_NIGHT_MODE = 1;
    private Context context;
    private SharedPreferences.Editor edit;
    public static final String KEY_IS_DVICE_ADMINISTARTION_ACTIVE = "isDeviceAdministrationActive";

    public Preferences(Context context) {
        this.context = context;
        preferences = context.getSharedPreferences("", PRIVATE_MODE);
        edit = preferences.edit();
    }

    public void putString(String strKey, String strValue) {
        edit.putString(strKey, strValue);
        edit.commit();
    }

    public void putInt(String strKey, int value) {
        edit.putInt(strKey, value);
        edit.commit();
    }

    public void putBoolean(String strKey, boolean value) {
        edit.putBoolean(strKey, value);
        edit.commit();
    }

    public void putLong(String strKey, Long value) {
        edit.putLong(strKey, value);
        edit.commit();
    }

    public void putDouble(String strKey, String value) {
        edit.putString(strKey, value);
        edit.commit();
    }


    public String getString(String strKey, String defaultValue) {
        return preferences.getString(strKey, defaultValue);
    }

    public boolean getBoolean(String strKey, boolean defaultValue) {
        return preferences.getBoolean(strKey, defaultValue);
    }

    public int getInt(String strKey, int defaultValue) {
        return preferences.getInt(strKey, defaultValue);
    }

    public double getDouble(String strKey, double defaultValue) {
        return Double.parseDouble(preferences.getString(strKey, ""
                + defaultValue));
    }

    public long getLong(String strKey) {
        return preferences.getLong(strKey, VALUE_DEFAULT_DECIMAL);
    }

//    /**
//     * Save machine details.
//     *
//     * @param machineDo
//     */
//    public void saveMachineDetailsInPrefernces(MachineDo machineDo) {
//        putInt(IConstants.PREFERENCES.KEY_MACHINE_ID, machineDo.getId());
//        putString(IConstants.PREFERENCES.KEY_MACHINE_NAME, machineDo.getName());
//        putInt(IConstants.PREFERENCES.KEY_MACHINE_STATUS, machineDo.getStatus());
//        putString(KEY_MACHINE_CODE, machineDo.getCode());
//        sendBroadcastForMachineChanged();
//    }
//
//    /**
//     * Update machine status.
//     *
//     * @param status
//     */
//    public void updateMachineStatus(int status) {
//        putInt(IConstants.PREFERENCES.KEY_MACHINE_STATUS, status);
//    }
//
//    /**
//     * Send broadcast when selected machine saved in preferences.
//     */
//    private void sendBroadcastForMachineChanged() {
//        Intent smsStatusUpdateintent = new Intent();
//        smsStatusUpdateintent.setAction(BaseFragmentActivity.ACTION_MACHINE_CAHNGED);
//        MyApplication.getInstance().getApplicationContext().sendBroadcast(smsStatusUpdateintent);
//    }
//
//
//    /**
//     * Returns saved machine details.
//     *
//     * @return
//     */
//    public MachineDo getMachineDetailsFromPrefernces() {
//        MachineDo machineDo = new MachineDo();
//        machineDo.setId(getInt(IConstants.PREFERENCES.KEY_MACHINE_ID, -1));
//        machineDo.setName(getString(IConstants.PREFERENCES.KEY_MACHINE_NAME, ""));
//        machineDo.setStatus(getInt(IConstants.PREFERENCES.KEY_MACHINE_STATUS, 0));
//        machineDo.setCode(getString(KEY_MACHINE_CODE, ""));
//        return machineDo;
//    }
//
//
//    /**
//     * Save the user details.
//     *
//     * @param loginUserDo
//     */
//    public void saveUserDetailsInPreference(TokenDO loginUserDo) {
//        if (loginUserDo != null) {
//            putString(IConstants.PREFERENCES.KEY_PREF_X_AUTH_TOKEN, loginUserDo.getToken());
//            UserDO userDO = loginUserDo.getUser();
//            if (userDO != null) {
//                putInt(IConstants.PREFERENCES.KEY_PREF_USER_ID, userDO.getId());
//                putString(IConstants.PREFERENCES.KEY_PREF_USERNAME, userDO.getUsername());
//                putString(IConstants.PREFERENCES.KEY_PREF_USER_CODE, userDO.getEmployee_code());
//                putString(IConstants.PREFERENCES.KEY_PREF_USER_NAME, userDO.getName());
//                putString(IConstants.PREFERENCES.KEY_PREF_USER_EMAIL, userDO.getEmail());
//                putInt(IConstants.PREFERENCES.KEY_PREF_USER_TYPE, userDO.getAccountType());
//                putInt(IConstants.PREFERENCES.KEY_PREF_LOGIN_STATUS, userDO.getLoginStatus());
//                putInt(IConstants.PREFERENCES.KEY_PREF_USER_STATUS, userDO.getStatus());
//            }
//
//            RoleDO roleDO = loginUserDo.getRole();
//            if (roleDO != null) {
//                putInt(IConstants.PREFERENCES.KEY_PREF_ROLE_ID, roleDO.getId());
//                putString(IConstants.PREFERENCES.KEY_PREF_ROLE_ALIAS, roleDO.getAlias());
//                putString(IConstants.PREFERENCES.KEY_PREF_ROLE_NAME, roleDO.getName());
//
//                ProcessDo processDo = roleDO.getProcess();
//                if (processDo != null) {
//                    putInt(IConstants.PREFERENCES.KEY_PREF_PROCESS_ID, processDo.getId());
//                    putString(IConstants.PREFERENCES.KEY_PREF_PROCESS_ALIAS, processDo.getAlias());
//                    putString(IConstants.PREFERENCES.KEY_PREF_PROCESS_NAME, processDo.getName());
//                }
//            }
//
//        }
//    }
//
//    /**
//     * Returns user details.
//     *
//     * @return
//     */
//    public TokenDO getUserDetailsFromPreferences() {
//        TokenDO tokenDO = new TokenDO();
//        tokenDO.setToken(getString(IConstants.PREFERENCES.KEY_PREF_X_AUTH_TOKEN, null));
//        UserDO userDO = new UserDO();
//        userDO.setId(getInt(IConstants.PREFERENCES.KEY_PREF_USER_ID, 0));
//        userDO.setUsername(getString(IConstants.PREFERENCES.KEY_PREF_USERNAME, null));
//        userDO.setName(getString(IConstants.PREFERENCES.KEY_PREF_USER_NAME, null));
//        userDO.setEmail(getString(IConstants.PREFERENCES.KEY_PREF_USER_EMAIL, null));
//        userDO.setAccountType(getInt(IConstants.PREFERENCES.KEY_PREF_USER_TYPE, 0));
//        userDO.setLoginStatus(getInt(IConstants.PREFERENCES.KEY_PREF_LOGIN_STATUS, 0));
//        userDO.setStatus(getInt(IConstants.PREFERENCES.KEY_PREF_USER_STATUS, 0));
//
//        tokenDO.setUser(userDO);
//        return tokenDO;
//    }
//
//
//    /**
//     * Set PrevLoggedSignalStatusDO as json string.
//     *
//     * @param prevLoggedSignalStatusDO
//     */
//    public void setPrevLoggedSignalStatusDO(SignalStatusDao prevLoggedSignalStatusDO) {
//        //Convert the prevLoggedSignalStatusDO in json string and save in pref
//        Gson gson = new Gson();
//        String prevLoggedSignalStatusDOJsonString = gson.toJson(prevLoggedSignalStatusDO);
//        putString(IConstants.PREFERENCES.PREV_LOGGED_SIGNAL_PACKET_DO, prevLoggedSignalStatusDOJsonString);
//    }
//
//    /**
//     * Returns last packet id when mode is change to pref.
//     */
//    public SignalStatusDao getPrevLoggedSignalStatusDO() {
//        //Convert the json string into prevLoggedSignalStatusDO and return
//        String prevLoggedSignalStatusDOJsonString = getString(IConstants.PREFERENCES.PREV_LOGGED_SIGNAL_PACKET_DO, null);
//        Gson gson = new Gson();
//        SignalStatusDao signalStatusDao = gson.fromJson(prevLoggedSignalStatusDOJsonString, SignalStatusDao.class);
//        return signalStatusDao;
//    }
//
//    /**
//     * Set the SPM value.
//     *
//     * @param spm
//     */
//    public void setSpmInPreference(int spm) {
//        putInt(IConstants.PREFERENCES.KEY_SPM_COUNT, spm);
//    }
//
//    /**
//     * Returns stored SPM.
//     *
//     * @return
//     */
//    public int getSpmFromPreference() {
//        return getInt(IConstants.PREFERENCES.KEY_SPM_COUNT, VALUE_DEFAULT_DECIMAL);
//    }
//
//    /**
//     * Set end strokes count.
//     *
//     * @param endStrokesCount
//     */
//    public void setEndStrokesCount(int endStrokesCount) {
//        putInt(IConstants.PREFERENCES.KEY_END_STROKES_COUNT, endStrokesCount);
//    }
//
//    /**
//     * Returns end stroke counts.
//     *
//     * @return
//     */
//    public int getEndStrokesCount() {
//        return getInt(IConstants.PREFERENCES.KEY_END_STROKES_COUNT, VALUE_DEFAULT_DECIMAL);
//    }
//
//    /**
//     * Set total wastage.
//     */
//    public void setTotalWastage(int totalWastage) {
//        putInt(IConstants.PREFERENCES.KEY_TOTAL_WASTAGE, totalWastage);
//    }
//
//    /**
//     * Returns total wastage.
//     *
//     * @return
//     */
//    public int getTotalWastage() {
//        return getInt(IConstants.PREFERENCES.KEY_TOTAL_WASTAGE, 0);
//    }
}
