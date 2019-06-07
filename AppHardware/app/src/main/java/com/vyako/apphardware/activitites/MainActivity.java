package com.vyako.apphardware.activitites;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.vyako.apphardware.R;
import com.vyako.apphardware.bluetooth.BluetoothSPP;
import com.vyako.apphardware.bluetooth.BluetoothState;
import com.vyako.apphardware.daos.SignalStatusDao;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String logCat = "Logs Main--";

    private static final String[] SIGNAL_STATUS = {"on", "off"};
    private static final String SEND_ACK = "ACK";
    private static final int SENDER_CONNECTED = 9;
    private static final int COUNTER_MAX_COUNT = 999999;

    private final String[] signalArray = new String[]{"sig 1", "sig 2", "sig 3", "sig 4", "sig 5", "sig 6", "sig 7", "sig 8", "sig 9", "sig 10", "sig 11"};

    private Button sendButton, stopButton, disconnectButton, resetButton;
    private AppCompatButton b1, b2, b3, b4, b5, b6, b7, b8, b9, changeMode;
    private EditText delayEditText;
    private TextView statusTextView, ackTextView;

    private BluetoothSPP bluetoothSPP;
    private Timer timer;
    private Gson gson;
    private long delay;
    private final String signalStartChar = "@";
    private final String signalEndChar = "#";
    private int counterInc = 0;
    private int packetCounter = 0;

    private String mode = "01";
    private ArrayList<SignalStatusDao> signalStatusDaoArrayList;
    //det sensor data
    String sensor = "111111111";
    boolean stopSendingSignals = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        initComponents();

        checkBluetoothSupport();
        manageBluetoothConnection();

        receivedSignalAck();
    }


    /**
     * Method which check the support of bluetooth in the device
     */
    private void checkBluetoothSupport() {
        if (!bluetoothSPP.isBluetoothAvailable()) {
            Log.d(logCat, "Bluetooth is not available");
            Toast.makeText(getApplicationContext(), "Bluetooth is not available", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void initComponents() {
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        bluetoothSPP = new BluetoothSPP(this);
        gson = new Gson();
    }

    private void initViews() {

        statusTextView = (TextView) findViewById(R.id.status_textView);
        ackTextView = (TextView) findViewById(R.id.ack_textView);
        delayEditText = (EditText) findViewById(R.id.delay_editText);

        sendButton = (Button) findViewById(R.id.send_button);
        stopButton = (Button) findViewById(R.id.stop_button);
        disconnectButton = (Button) findViewById(R.id.disconnect_button);
        resetButton = (Button) findViewById(R.id.reset_button);
        b1 = (AppCompatButton) findViewById(R.id.b1);
        b2 = (AppCompatButton) findViewById(R.id.b2);
        b3 = (AppCompatButton) findViewById(R.id.b3);
        b4 = (AppCompatButton) findViewById(R.id.b4);
        b5 = (AppCompatButton) findViewById(R.id.b5);
        b6 = (AppCompatButton) findViewById(R.id.b6);
        b7 = (AppCompatButton) findViewById(R.id.b7);
        b8 = (AppCompatButton) findViewById(R.id.b8);
        b9 = (AppCompatButton) findViewById(R.id.b9);
        changeMode = (AppCompatButton) findViewById(R.id.changeMode);

        sendButton.setOnClickListener(this);
        stopButton.setOnClickListener(this);
        disconnectButton.setOnClickListener(this);
        resetButton.setOnClickListener(this);
        b1.setOnClickListener(this);
        b2.setOnClickListener(this);
        b3.setOnClickListener(this);
        b4.setOnClickListener(this);
        b5.setOnClickListener(this);
        b6.setOnClickListener(this);
        b7.setOnClickListener(this);
        b8.setOnClickListener(this);
        b9.setOnClickListener(this);
        changeMode.setOnClickListener(this);
    }

    private void manageBluetoothConnection() {

        bluetoothSPP.setBluetoothConnectionListener(new BluetoothSPP.BluetoothConnectionListener() {

            @Override
            public void onDeviceConnected(String name, String address) {
                Log.d(logCat, "Connected--" + name + "-" + address);
                Toast.makeText(MainActivity.this, "Connected-- " + name + " " + address, Toast.LENGTH_SHORT).show();
                statusTextView.setText("Connected To " + name + " " + address);

//                PrepareToSendCountOfSignals();
            }

            @Override
            public void onDeviceDisconnected() {
                Toast.makeText(MainActivity.this, "Device Disconnected", Toast.LENGTH_SHORT).show();
                Log.d(logCat, "Device Disconnected");
                statusTextView.setText("Device Disconnected");

            }

            @Override
            public void onDeviceConnectionFailed() {
                statusTextView.setText("Device Connection Failed");
            }
        });

    }

    private void PrepareToSendCountOfSignals() {

        //Convert the String array into Json Array
        String totalSignal = gson.toJson(signalArray);
        Log.d(logCat, "Signal JsonArray String -- " + totalSignal);
//      Commented by vickram for now send signal button will only send the signals
//        sendSignals(totalSignal);

//        Type type = new TypeToken<String[]>() {}.getType();
//        String[] str = gson.fromJson(totalSignal, type);
//        Log.d(logCat, "Signal Array : "+str.length);

    }

    public void onDestroy() {
        super.onDestroy();
        bluetoothSPP.stopService();
    }


    /**
     * WE check for bluetooth enable if not then get enable it.
     */
    public void onStart() {
        super.onStart();
        if (!bluetoothSPP.isBluetoothEnabled()) {
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(intent, BluetoothState.REQUEST_ENABLE_BT);

        } else {
            if (!bluetoothSPP.isServiceAvailable()) {
                Log.d(logCat, "Bluetooth is already Enable ");
                setupConnection();
            }
        }
    }

    /**
     * Here we can set and start the bluetooth services
     */
    private void setupConnection() {
        bluetoothSPP.setupService();
        bluetoothSPP.startService(BluetoothState.DEVICE_ANDROID);
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.send_button:
                prepareToSendSignals();
                break;

            case R.id.stop_button:
                stopSendingSignal();
                break;

            case R.id.disconnect_button:
                disconnectBluetooth();
                break;

            case R.id.reset_button:
                resetAllData();
                break;
            case R.id.b1:
                sensor = "011111111";
                stopSendingSignals = true;
                break;
            case R.id.b2:
                sensor = "101111111";
                stopSendingSignals = true;
                break;
            case R.id.b3:
                sensor = "110111111";
                stopSendingSignals = true;
                break;
            case R.id.b4:
                sensor = "11101111";
                stopSendingSignals = true;
                break;
            case R.id.b5:
                sensor = "111101111";
                stopSendingSignals = true;
                break;
            case R.id.b6:
                sensor = "111110111";
                stopSendingSignals = true;
                break;
            case R.id.b7:
                sensor = "111111011";
                stopSendingSignals = true;
                break;
            case R.id.b8:
                sensor = "111111101";
                stopSendingSignals = true;
                break;
            case R.id.b9:
                sensor = "111111110";
                stopSendingSignals = true;
                break;
            case R.id.changeMode:
                onChangeModeClick(changeMode);
                break;

        }
    }


    private void resetAllData() {

        ackTextView.setText("");
        delayEditText.setText("");
        counterInc = 0;
    }

    private void disconnectBluetooth() {

        if (bluetoothSPP.getServiceState() == BluetoothState.STATE_CONNECTED) {
            bluetoothSPP.disconnect();
        } else {
            Toast.makeText(this, "Already Disconnected", Toast.LENGTH_SHORT).show();
        }
    }

    private void stopSendingSignal() {

        if (timer != null) {
            timer.cancel();
            Log.d(logCat, "Timer Thread Stopped");

        } else {
            Toast.makeText(this, "Timer is already stop ", Toast.LENGTH_SHORT).show();
        }
    }


    private void prepareToSendSignals() {

        /**
         * Check timer is null or not if not then stop the timer before sending data
         * it stop the running timer
         */
        if (timer != null) {
            timer.cancel();
            Log.d(logCat, "Timer Thread Stopped");
        }

        /**
         * Get value from edit text if value is empty then set default delay 1 sec.
         */
        if (delayEditText.getText().toString().isEmpty()) {
            delay = 1000;
        } else {
            delay = Long.parseLong(delayEditText.getText().toString());
        }

        timer = new Timer();

        /**
         * Check bluetooth connection before to send data
         */
        if (bluetoothSPP.getServiceState() == BluetoothState.STATE_CONNECTED) {

            TimerTask timerTask = new TimerTask() {
                public void run() {
                    //Prepare 45 byte signal
                    String signals = getSignalData();
                    Log.d(logCat, "Signal Json Array -- " + signals);
                    sendSignals(signals);
                    if (stopSendingSignals) {
                        timer.cancel();
                        stopSendingSignals = false;
                        sensor = "111111111";
                    }
                }
            };
            Log.d(logCat, "delay interval-- " + delay);
            Log.d(logCat, "Timer Thread Started");
            timer.scheduleAtFixedRate(timerTask, 0, delay); // in milli-sec

        } else {
            stopSendingSignals = false;
            Toast.makeText(this, "Devices are not Connected", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Returns the signal generated by hardware
     *
     * @return
     */
    private String getSignalData() {

        String dcd_uid = "STPL025431";
        //Generate current time stamp in seconds
        long timeStamp = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis());

        //Logic to generate 3 digit packet id
        packetCounter++;
        if (packetCounter > 20) {
            packetCounter = 1;
        }
        DecimalFormat df = new DecimalFormat("000");
        String packet_id = df.format(packetCounter);
//        String packet_id = "001";

        //Logic to generate six digit counter value
        int min = 0;
        int max = 999999;
        Random random = new Random();
        //int randomCounter = random.nextInt((max - min) + 1) + min;
        counterInc++;
        if (counterInc > COUNTER_MAX_COUNT) {
            counterInc = 1;
        }
        df = new DecimalFormat("000000");
        String counter = df.format(counterInc);

        //det sensor data
//        sensor = "111111111";

//        String sensor = getSensorsValue();

        //logic to generate machine relay
        min = 0;
        max = 1;
        int machineRelayStatus = random.nextInt((max - min) + 1) + min;

        //Logic to generate tower signal
        String signalTowerLignt = "GBYO";
        char towerSignal = signalTowerLignt.charAt(random.nextInt(signalTowerLignt.length()));

        String finalSignal = dcd_uid + timeStamp + packet_id + counter + mode + sensor + machineRelayStatus + towerSignal;

        return finalSignal;
    }

    public void onChangeModeClick(View view) {
        updateMode(view);
    }

    /**
     * Updates mode here
     */
    private void updateMode(View view) {
        if (mode.equals("01")) {
            mode = "10";
            ((AppCompatButton) view).setText("Change to Manual");
        } else {
            mode = "01";
            ((AppCompatButton) view).setText("Change to Auto");

        }
    }

    /**
     * Generate signal status randomly
     *
     * @return
     */
    private String getSensorsValue() {
        String sensorSignalData = "";
        final int min = 0;
        final int max = 1;
        Random random = new Random();
        for (int index = 0; index < SENDER_CONNECTED; index++) {
            int signalStatus = random.nextInt((max - min) + 1) + min;
            sensorSignalData += signalStatus;
        }
        return sensorSignalData;
    }


    private String getJsonSignalsFromDao() {

        signalStatusDaoArrayList = new ArrayList<SignalStatusDao>();

        for (int i = 0; i < signalArray.length; i++) {

            SignalStatusDao statusDao = new SignalStatusDao();
            statusDao.setSignalId(signalArray[i]);
            statusDao.setSignalValue(getSignalStatus());
            signalStatusDaoArrayList.add(statusDao);
        }

        return gson.toJson(signalStatusDaoArrayList);
    }


    /**
     * @return random Status of signal
     */
    public String getSignalStatus() {
        Random random = new Random();
        // randomly selects an index from the arr
        int select = random.nextInt(SIGNAL_STATUS.length);
        // prints out the value at the randomly selected index

//        Log.d(logCat, "Random Selected Signal : " + SIGNAL_STATUS[select]);

        return SIGNAL_STATUS[select];
    }

    private void sendSignals(String message) {

        Log.d(logCat, "Msg Original Send--" + message);
        message = signalStartChar + message + signalEndChar;
//        message = "*" + message + "#";
        Log.d(logCat, "Msg Encrypt send--" + message);
        bluetoothSPP.send(message, true);
    }

    private void receivedSignalAck() {

        bluetoothSPP.setOnDataReceivedListener(new BluetoothSPP.OnDataReceivedListener() {
            @Override

            public void onDataReceived(byte[] data, String message) {
                Log.d(logCat, "Received ACK --" + message);

                if (message.startsWith(signalStartChar) && message.endsWith(signalEndChar)) {
                    message = message.substring(1, message.length() - 1);
                    Log.d(logCat, "Received ACK Decrypt--" + message);
                    checkAcknowledgement(message);
                } else {
                    Toast.makeText(MainActivity.this, "Not a valid signal.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void checkAcknowledgement(String message) {
//        if (message.contains(SEND_ACK)) {
        ackTextView.setText(message);
//        } else {
//            ackTextView.setText("Not ACK -- " + message);
//        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d(logCat, "requestCode--" + requestCode);
        Log.d(logCat, "resultCode--" + resultCode);

        switch (requestCode) {

            case BluetoothState.REQUEST_ENABLE_BT:
                if (resultCode == Activity.RESULT_OK) {
                    setupConnection();
                    Log.d(logCat, "Turning the Bluetooth ON ");
                } else {
                    Log.d(logCat, "Bluetooth is not enabled.");
                    finish();
                }
        }

    }
}
