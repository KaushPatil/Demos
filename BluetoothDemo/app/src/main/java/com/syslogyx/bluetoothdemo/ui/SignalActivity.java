package com.syslogyx.bluetoothdemo.ui;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.syslogyx.bluetoothdemo.R;
import com.syslogyx.bluetoothdemo.bluetooth.constant.SignalConstants;
import com.syslogyx.bluetoothdemo.bluetooth.controller.AcknowledgmentController;
import com.syslogyx.bluetoothdemo.bluetooth.controller.BluetoothMessageController;
import com.syslogyx.bluetoothdemo.bluetooth.dos.SignalPacketDo;
import com.syslogyx.bluetoothdemo.bluetooth.listeners.IBluetoothCallback;
import com.syslogyx.bluetoothdemo.bluetooth.listeners.IPacketReceivedCallback;


public class SignalActivity extends AppCompatActivity implements View.OnClickListener, IBluetoothCallback {

    private static final String logCat = "Logg>>";
    private Button loginButton, logoutButton, towerLightButton;
    private Button moStartButton, moHoldButton, moCloseButton;
    private Button machineOnButton, machineOffButton, ecsButton;
    private Button jobChangeButton, caqButton, breakStartButton;
    private Button idleStartButton, idleEndButton, breakEndButton;
    private Button jhStartButton, jhEndButton;
    private TextView packetRecTextView;
    private CountDownTimer countDownTimer;
    private SignalPacketDo signalPacketDo;
    private BluetoothMessageController bluetoothMessageController;
    private boolean packetAvalaible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signal);

        initViews();

        registerViews();
        initComponents();
//        countDownTimer();


//        Log.d(logCat, "onCreate: time --" + Utils.getCurrentDataAndTime());
//
//        String[] dateTimeArray = Utils.getDateTimeArray();
//        Log.d(logCat, "onCreate: array --" + dateTimeArray.length);
//        Log.d(logCat, "onCreate: array 1 --" + dateTimeArray[0]);
//        Log.d(logCat, "onCreate: array 2 --" + dateTimeArray[1]);

    }


    /**
     * Initialized the other components here
     */
    private void initComponents() {
        bluetoothMessageController = BluetoothMessageController.getInstance();
        bluetoothMessageController.registerICallback(this);
    }


    private void registerViews() {
        loginButton.setOnClickListener(this);
        logoutButton.setOnClickListener(this);
        towerLightButton.setOnClickListener(this);
        moStartButton.setOnClickListener(this);
        moHoldButton.setOnClickListener(this);
        moCloseButton.setOnClickListener(this);
        machineOnButton.setOnClickListener(this);
        machineOffButton.setOnClickListener(this);
        ecsButton.setOnClickListener(this);
        jobChangeButton.setOnClickListener(this);
        caqButton.setOnClickListener(this);
        breakStartButton.setOnClickListener(this);
        breakEndButton.setOnClickListener(this);
        idleStartButton.setOnClickListener(this);
        idleEndButton.setOnClickListener(this);
        jhStartButton.setOnClickListener(this);
        jhEndButton.setOnClickListener(this);
    }

    private void initViews() {

        loginButton = findViewById(R.id.login_button);
        logoutButton = findViewById(R.id.logout_button);
        towerLightButton = findViewById(R.id.tower_button);
        moStartButton = findViewById(R.id.start_button);
        moHoldButton = findViewById(R.id.hold_button);
        moCloseButton = findViewById(R.id.close_button);
        machineOnButton = findViewById(R.id.machine_on_button);
        machineOffButton = findViewById(R.id.machine_off_button);
        ecsButton = findViewById(R.id.ecs_button);
        jobChangeButton = findViewById(R.id.job_change_button);
        caqButton = findViewById(R.id.caq_button);
        breakStartButton = findViewById(R.id.break_start_button);
        breakEndButton = findViewById(R.id.break_end_button);
        idleStartButton = findViewById(R.id.start_idle_button);
        idleEndButton = findViewById(R.id.end_idle_button);
        jhStartButton = findViewById(R.id.jh_start_button);
        jhEndButton = findViewById(R.id.jh_end_button);
        packetRecTextView = findViewById(R.id.packet_rec_tv);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.login_button:
                onLoginButtonClicked();
                break;

            case R.id.logout_button:
                onLogoutButtonClick();
                break;

            case R.id.tower_button:
                break;

            case R.id.start_button:
                break;

            case R.id.hold_button:
                break;

            case R.id.close_button:
                break;

            case R.id.machine_on_button:
                break;

            case R.id.machine_off_button:
                break;

            case R.id.ecs_button:
                break;

            case R.id.job_change_button:
                break;

            case R.id.caq_button:
                break;

            case R.id.break_start_button:
                break;

            case R.id.break_end_button:
                break;

            case R.id.start_idle_button:
                break;

            case R.id.end_idle_button:
                break;

            case R.id.jh_start_button:
                break;

            case R.id.jh_end_button:
                break;
        }
    }

    /**
     * On logout button click
     */
    private void onLogoutButtonClick() {
        Log.d(logCat, "onLogoutButtonClick: ");
        sendLogoutCommands(SignalConstants.AckIdentifier.ID_DCB_MODE, SignalConstants.Actions.COMMAND_MODE);
    }

    /**
     * Execute all command for logout
     * and wait to receive ack of send packets
     *
     * @param ackId
     * @param action
     */
    private void sendLogoutCommands(byte ackId, String action) {
        Log.d(logCat, "Ack--  sendLogoutCommands : AckId --" + ackId);
        AcknowledgmentController.getInstance().dispatchAcknowledgment(ackId, action, new IPacketReceivedCallback() {
            @Override
            public void onPacketReceived(boolean isPacketReceived, SignalPacketDo signalPacketDo) {
                if (isPacketReceived && signalPacketDo != null) {
                    Log.d(logCat, "Ack--  received packet Id : " + signalPacketDo.getPacket_id());

                    switch (signalPacketDo.getPacket_id()) {

                        case SignalConstants.PacketIds.DCB_MODE:
                            AcknowledgmentController.getInstance().stopTimer();
                            sendLogoutCommands(SignalConstants.AckIdentifier.ID_MACHINE_RELAY, SignalConstants.Actions.MACHINE_RELAY_OFF);
                            break;

                        case SignalConstants.PacketIds.MACHINE_RELAY:
                            AcknowledgmentController.getInstance().stopTimer();
                            Log.d(logCat, "Ack--  logout flow end : " );

                            break;

                        default:
                            AcknowledgmentController.getInstance().stopTimer();

                    }
                }
            }
        });
    }

    private void onLoginButtonClicked() {
        Log.d(logCat, "onLoginButtonClicked: ");
        sendLoginCommands(SignalConstants.AckIdentifier.ID_DCB_MODE, SignalConstants.Actions.COMMAND_MODE);
    }


    /**
     * Execute all command for login
     * and wait to receive ack of send packets
     *
     * @param ackId
     * @param action
     */
    private void sendLoginCommands(byte ackId, String action) {
        Log.d(logCat, "Ack--  sendLoginCommands : commandId --" + ackId);
        AcknowledgmentController.getInstance().dispatchAcknowledgment(ackId, action, new IPacketReceivedCallback() {
            @Override
            public void onPacketReceived(boolean isPacketReceived, SignalPacketDo signalPacketDo) {

                if (isPacketReceived && signalPacketDo != null) {
                    Log.d(logCat, "Ack--  received packet Id : " + signalPacketDo.getPacket_id());

                    switch (signalPacketDo.getPacket_id()) {
                        case SignalConstants.PacketIds.DCB_MODE:
                            AcknowledgmentController.getInstance().stopTimer();
                            if (signalPacketDo.getMachineRelay().equals(SignalConstants.Actions.COMMAND_MODE)) {
                                sendLoginCommands(SignalConstants.AckIdentifier.ID_MACHINE_RELAY, SignalConstants.Actions.MACHINE_RELAY_ON);
                            }
                            else {
                                Log.d(logCat, "Ack--  login flow end : " );
                            }

                            break;

                        case SignalConstants.PacketIds.MACHINE_RELAY:
                            AcknowledgmentController.getInstance().stopTimer();
                            sendLoginCommands(SignalConstants.AckIdentifier.ID_DCB_MODE, SignalConstants.Actions.PACKET_MODE);
                            break;
                    }
                }

            }
        });
    }

    @Override
    public void onDeviceConnected(String name, String address) {

    }

    @Override
    public void onDeviceDisconnected() {

    }

    @Override
    public void onDeviceConnectionFailed() {

    }

    @Override
    public void onDataReceived(byte[] data, String message) {
        packetRecTextView.setText(message);
        Log.d(logCat, "onDataReceived: " + message);
    }

    @Override
    public void setSignalStatus(SignalPacketDo signalPacketDo) {
//        Log.d("Kaush>>", "new setSignalStatus");
//        this.signalPacketDo = signalPacketDo;
        Log.d("Kaush>>", "counter - " + signalPacketDo.getCounter());
//        UiUpdateTask uiUpdateTask = new UiUpdateTask();
//        uiUpdateTask.execute(signalPacketDo);
    }

}
