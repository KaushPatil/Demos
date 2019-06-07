package com.syslogyx.bluetoothdemo.app.constant;


/**
 * Created by kaushik on 22-Nov-16.
 * Application level constants are defined here.
 */

public interface IConstants {

    int DEFAULT_VALUE = -1;

    String ACTION_SET_ADAPTER = "com.vyako.action.SET_ADAPTER";


    /**
     * Constant for User Type.
     */
    interface USER_TYPE {
        int NO_USER = 0;
        int ADMIN = 1;
        int PLANNING = 2;
        int PRODUCTION = 3;
        int OPERATOR = 4;
        int SUPERVISOR = 5;
    }

    /**
     * Constant for Login Status.
     */
    interface USER_LOGIN_STATUS {
        int LOGIN = 1;
        int LOGOUT = 0;
    }

    /**
     * Constants for Process Ids.
     */
    interface PROCESS_ID {
        int PRODUCTION = 1;
        int PLANNING = 2;
        int NPD = 3;
        int IT = 4;
        int STAMPING = 5;
        int HSP = 14;
        int STORE = 8;

    }

    /**
     * Constants for Roles.
     */
    interface ROLE {

        int HSP_OPERATOR = 68;
        int HSP_SUPERVISOR = 67;
        int HSP_GM = 0;
        int HSP_MANAGER = 0;
        int HSP_HEAD = 0;

        int STORE_OPERATOR = 0;
        int STORE_SUPERVISOR = 33;
        int STORE_GM = 0;
        int STORE_MANAGER = 0;
        int STORE_HEAD = 0;


        int STAMPING_OPERATOR = 0;
        int STAMPING_SUPERVISOR = 0;
        int STAMPING_GM = 0;
        int STAMPING_MANAGER = 0;
        int STAMPING_HEAD = 0;
        int STAMPING_SETTER = 66;

//        int PRODUCTION_OPERATOR = 1;
//        int PRODUCTION_SUPERVISOR = 2;
//        int PRODUCTION_GM = 3;
//        int PRODUCTION_MANAGER = 4;
//        int PRODUCTION_HEAD = 5;
//
//        int PLANNING_OPERATOR = 6;
//        int PLANNING_SUPERVISOR = 7;
//        int PLANNING_GM = 8;
//        int PLANNING_MANAGER = 9;
//        int PLANNING_HEAD = 10;
//
//        int NPD_OPERATOR = 11;
//        int NPD_SUPERVISOR = 12;
//        int NPD_GM = 13;
//        int NPD_MANAGER = 14;
//        int NPD_HEAD = 15;
//
//        int IT_OPERATOR = 16;
//        int IT_SUPERVISOR = 17;
//        int IT_GM = 18;
//        int IT_MANAGER = 19;
//        int IT_HEAD = 20;

    }

    /**
     * Constants for Machine User Process Id.
     */
    interface MACHINE_USER_PROCESS_ID {
        int HIGH_SPEED_ZONE = 14;
    }

    interface PREFERENCES {
        String KEY_PREF_PASSWORD = "password";
        String KEY_PREF_USERNAME = "username";
        String KEY_PREF_USER_CODE = "usercode";
        String KEY_PREF_USER_NAME = "name";
        String KEY_PREF_USER_EMAIL = "email";
        String KEY_PREF_USER_STATUS = "status";
        String KEY_PREF_LOGIN_STATUS = "login_status";
        String KEY_PREF_USER_TYPE = "user_type";
        String KEY_PREF_USER_ID = "user_id";
        String KEY_PREF_X_AUTH_TOKEN = "X-AUTH-TOKEN";

        String KEY_PREF_ROLE_ID = "role_id";
        String KEY_PREF_ROLE_ALIAS = "role_alias";
        String KEY_PREF_ROLE_NAME = "role_name";

        String KEY_PREF_PROCESS_ID = "process_id";
        String KEY_PREF_PROCESS_ALIAS = "process_alias";
        String KEY_PREF_PROCESS_NAME = "process_name";

        String KEY_PREF_BLUETOOTH_DEVICE_ADDRESS = "device_address";
        String KEY_PREF_BLUETOOTH_DEVICE_NAME = "device_name";

        String KEY_QUANTITY_REACHED = "quantity_reached";

        String KEY_MACHINE_ID = "machine_id";
        String KEY_MACHINE_NAME = "machine_name";
        String KEY_MACHINE_STATUS = "machine_status";
        String KEY_MACHINE_TYPE_ID = "machine_type_id";
        String KEY_MACHINE_CODE = "machine_code";
        String KEY_MACHINE_TYPE_NAME = "machine_type_name";
        String KEY_MACHINE_CATEGORY_ID = "machine_category_id";
        String KEY_MACHINE_CATEGORY_NAME = "machine_category_name";
        String KEY_MACHINE_USER_PROCESS_ID = "machine_user_process_id";
        String KEY_MACHINE_USER_PROCESS_NAME = "machine_user_process_name";
        String KEY_MACHINE_DCD_UID = "machine_dcd_uid";


        String KEY_CURRENT_MO_NUMBER = "currentMoNumber";
        String KEY_MO_START_TIME = "moStartTime";
        String KEY_LAST_MO_NUMBER = "lastInProgressMoNumber";

        String PREV_LOGGED_SIGNAL_PACKET_DO = "prevLoggedSignalPacketDO";

        String KEY_SPM_COUNT = "spm";
        String KEY_LAST_MODE_TIME_STAMP = "lastModeTimeStamp";
        String KEY_LAST_MODE_PACKET_ID = "lastModeSignalPacketId";
        String KEY_LAST_RECEIVED_MODE_TIME_STAMP = "lastReceivedModeTimeStamp";


        String KEY_START_DOWN_TIME = "start_time";
        String KEY_END_DOWN_TIME = "end_time";
        String KEY_END_STROKES_COUNT = "endStrokesCount";
        String KEY_TOTAL_WASTAGE = "totalWastage";

        String KEY_HOST_URL = "hostUrl";

        String KEY_BREAK_MINUTES = "breakMinutes";
        String KEY_BREAK_SECONDS = "breakSeconds";
        String KEY_BREAK_HOURS = "breakHours";
        String RUNNING_SHIFT = "runningShift";
        String ISSUE_TOOL = "issuetool";
        String RETURN_TOOL = "returntool";
        String REPAIR_BOOKING = "repairbooking";
    }

    /**
     * Constants to prepare req body for updating mo status.
     */
    interface REQ_CONSTANT_FOR_UPDATE_MO_STATUS {
        int CATEGORY = 1;
        int PROCESS_ID = 14;
        int OPERATION_TYPE_ID = 0;
        String TYPE_HSP = "PROCESS";
        String TYPE_MO = "MO";

        int PROCESS_MO = 11;
        int PROCESS_HSP = 12;


    }

    /**
     * Constants for Device Id.
     */
    interface DEVICE_IDS {
        int DEVICE_ID = -1;
        int DEVICE_ANDROID = 1;
        int DEVICE_WEB = 2;
    }

    /**
     * Constants for Shift timing
     */
    interface SHIFT_TIME {
        int SHIFT1_HOUR = 6;
        int SHIFT1_MINUTE = 0;

        int SHIFT2_HOUR = 14;
        int SHIFT2_MINUTE = 30;

//        int SHIFT3_HOUR = 23;
//        int SHIFT3_MINUTE = 0;

        int SHIFT3_HOUR = 15;
        int SHIFT3_MINUTE = 20;

    }

    /**
     * Constants for Shift Type.
     */
    interface SHIFT_TYPE {
        int SHIFT_1 = 1;
        int SHIFT_2 = 2;
        int SHIFT_3 = 3;
    }

    /**
     * Constants for NVA code.
     */
    interface NVA_CODE {
        int JOB_CHANGEOVER = 1;
        int COIL_SETUP = 2;
        int JOB_UNDER_INSPECTION = 3;
        int TOOL_UNDER_REPAIR = 4;
        int PRODUCTION_STOP = 5;
        int EQIPMENT_UNDER_BREAKSOWN = 6;
        int PLANNED_SCHEDULED_MAINTENANCE = 7;
        int TRAIL = 8;
        int NO_POWER = 9;
        int NO_OPERATOR = 10;
        int NO_RAW_MATERIAL = 11;
        int NO_SLITTED_RAW_MATERIAL = 12;
        int NO_CUT_STRIP_MATERIAL = 13;
        int NO_COMPONENT_TO_FEED = 14;
        int NO_TOOL = 15;
        int TRAINING = 16;
        int MACHINE_NPR = 17;
        int OTHER = 18;
    }
}
