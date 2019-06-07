package com.syslogyx.bluetoothdemo.bluetooth.constant;

/**
 * Created by kaushik on 24-Oct-16.
 */

public interface SignalConstants {

//
//    //Constant related to Tower light status
//    String TOWER_LIGHT_GREEN = "G";
//    String TOWER_LIGHT_BLUE = "B";
//    String TOWER_LIGHT_YELLOW = "Y";
//    String TOWER_LIGHT_ORANGE = "O";
//    String TOWER_LIGHT_RED = "D";
//
//    //Constant related to Tower light status
//    String TOWER_LIGHT_FOR_MAINTENANCE = TOWER_LIGHT_ORANGE;
//    String TOWER_LIGHT_FOR_TECHNICAL = TOWER_LIGHT_BLUE;
//    String TOWER_LIGHT_FOR_QUALITY = TOWER_LIGHT_YELLOW;
//    String TOWER_LIGHT_FOR_TOOLS = TOWER_LIGHT_BLUE;
//    String TOWER_LIGHT_FOR_MACHINE_ON = TOWER_LIGHT_GREEN;
//    String TOWER_LIGHT_FOR_MACHINE_OFF = TOWER_LIGHT_RED;
//
//
//    //Normal flow machine relay
//    String MACHINE_RELAY_STATUS_NORMAL = "X";
//    String MACHINE_RELAY_STATUS_MACHINE_ON = "Z";
//    String MACHINE_RELAY_STATUS_MACHINE_OFF = "O";
//    String MACHINE_RELAY_STATUS_CLOSE_MACHINE_OFF = "T";
//    String MACHINE_RELAY_STATUS_HOLD_MACHINE_OFF = "H";
//    String MACHINE_RELAY_STATUS_START_MACHINE_ON = "I";
//    String MACHINE_RELAY_STATUS_START_CHANGE_OVER = "A";
//    String MACHINE_RELAY_STATUS_FORWARD = "F";
//    String MACHINE_RELAY_STATUS_MAKE_IDEAL_STROKE = "L";
//    String MACHINE_RELAY_STATUS_ECS = "K";
//    String MACHINE_RELAY_STATUS_START_TEST = "W";
//    String MACHINE_RELAY_STATUS_STOP_TEST = "J";

    /**
     * Packet Default and Basic constants
     */
    interface Packet {
        /**
         * Packet Start character constant
         */
        String PACKET_START_CHAR = "@";

        /**
         * Packet End character constant
         */
        String PACKET_END_CHAR = "#";

        /**
         * Packet Received length
         * DCB - Android
         */
        int MESSAGE_LENGTH_DCB = 51;

        /**
         * Packet Received length
         * DCB - Android
         */
        int MESSAGE_LENGTH_ANDROID = 38;

        /**
         * Total no of Sensor
         */
        int TOTAL_SENSOR_COUNT = 16;

        /**
         * Default Packet 51 length
         */
        String DEFAULT_PACKET = "@000000000000000000000000000000000000000000000000G#";

        /**
         * Default packet id
         * Length 3 digits
         */
        String DEFAULT_PACKET_ID = "000";

        /**
         * Default Counter
         * Length 6 digits
         */
        String DEFAULT_COUNTERS = "000000";

        /**
         * Packet with any event status
         */
        int EVENT_FOUND_STATUS = 1;
        int EVENT_NORMAL_STATUS = 0;

        /**
         * Default
         * Factory Name Length 4 digits
         * Factory Number Length 2 digits
         */
        String FACTORY_NAME = "SCEL";
        String FACTORY_NO = "01";

        /**
         * Default DCB ID
         * Length 4 digits
         */
        String DCB_ID = "1234";

        /**
         * Factory Name + Factory No + DCB ID
         */
        String FACTORY_DCB = FACTORY_NAME + FACTORY_NO + DCB_ID;


        /**
         * Default Mo number
         */
        String DEFAULT_MO_NUMBER = null;

        /**
         * Default Machine id
         */
        String DEFAULT_MACHINE_ID = null;


        String DEFAULT_ALL_SENSOR = "XXXXXXXXXXXXXXXX";
    }

    /**
     * Packet Action or Command
     */
    interface Actions {

        /**
         * Normal default action
         */
        String DEFAULT_ACTION = "X";

        /**
         * 999 Reset DCB
         */
        String DCB_RESTART = "I";

        /**
         * 998 Packet Mode
         */
        String PACKET_MODE = "P";

        /**
         * 998 Command Mode
         */
        String COMMAND_MODE = "C";

        /**
         * 997 Machine relay status
         */
        String MACHINE_RELAY_ON = "1";
        String MACHINE_RELAY_OFF = "0";

        /**
         * 996 Tower light Color stats
         */
        String TOWER_LIGHT_RED = "R";
        String TOWER_LIGHT_BLUE = "B";
        String TOWER_LIGHT_GREEN = "G";
        String TOWER_LIGHT_ORANGE = "O";
        String TOWER_LIGHT_YELLOW = "Y";

        /**
         * ECS Department tower light color status
         */
        String TOWER_LIGHT_FOR_MAINTENANCE = TOWER_LIGHT_ORANGE;
        String TOWER_LIGHT_FOR_TECHNICAL = TOWER_LIGHT_BLUE;
        String TOWER_LIGHT_FOR_QUALITY = TOWER_LIGHT_YELLOW;
        String TOWER_LIGHT_FOR_TOOLS = TOWER_LIGHT_BLUE;
        String TOWER_LIGHT_FOR_MACHINE_ON = TOWER_LIGHT_GREEN;
        String TOWER_LIGHT_FOR_MACHINE_OFF = TOWER_LIGHT_RED;

        /**
         * Identifiers for
         * 995 = U = counter update
         * 994 = T = Time Update
         */
        String COUNTER_UPDATE_IDENTIFIER = "U";
        String TIME_UPDATE_IDENTIFIER = "T";

        /**
         * 993 Machine status
         */
        String MACHINE_STATUS = "M";
        String AVAILABLE = "Y";
        String NOT_AVAILABLE = "N";

        /**
         * Enable Disable Sensor
         */
        String ENABLE = "1";
        String DISABLE = "0";


    }

    /**
     * identifier for event when send ack to the DCD
     */
    interface AckIdentifier {

        byte ID_DCB_RESTART = 10;
        byte ID_DCB_MODE = 11;
        byte ID_MACHINE_RELAY = 12;
        byte ID_TOWER_LIGHT = 13;
        byte ID_COUNTER_UPDATE = 14;
        byte ID_TIME_UPDATE = 15;
        byte ID_MACHINE_STATUS = 16;
        byte ID_COUNTER_ENABLE = 17;
        byte ID_SENSOR_ENABLE = 18;
        byte ID_DELAY_TIMER_ENABLE = 19;
        byte ID_NORMAL_MODE = 20;

    }

    /**
     * Special Packet id
     */
    interface PacketIds {

        /**
         * When DCB Restart just ACk of 999 packet
         */
        String DCB_RESTART = "999";

        /**
         * Change DCB mode from packet mode to command mode or vice versa
         * <p>
         * P = Packet Mode
         * C = Actions Mode
         */
        String DCB_MODE = "998";

        /**
         * Machine Relay i.e Machine On / Off
         * 1 = Machine On
         * 0 = Machine Off
         */
        String MACHINE_RELAY = "997";

        /**
         * Set Tower Light of machine
         * <p>
         * R = Red
         * B = Blue
         * G = Green
         * O = Orange
         * Y = Yellow
         */
        String TOWER_LIGHT = "996";


        /**
         * Send the start counter to DCB
         * <p>
         * U = Update Counter Identifier
         */
        String COUNTER_UPDATE = "995";


        /**
         * Send the time to DCB
         * T = Update time Identifier
         * Format : hhmmssddmmyy
         */
        String TIME_UPDATE = "994";

        /**
         * Check the machine status
         * If DCB Already have packet then switch to packet mode and received hold packet
         */
        String MACHINE_STATUS = "993";


        /**
         * Set the DCB Counter Enable or Disable
         * E = Enable
         * D = Disable
         */
        String COUNTER_ENABLE = "992";

        /**
         * Set the DCB Sensor Enable or Disable
         * 1 = Enable
         * 0 = Disable
         */
        String SENSOR_ENABLE = "991";

        /**
         * Set the DCB 2 min delay Enable or Disable
         * 1 = Enable
         * 0 = Disable
         */
        String DELAY_TIMER_ENABLE = "990";

    }
}
