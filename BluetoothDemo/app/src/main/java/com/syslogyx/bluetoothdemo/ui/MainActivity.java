package com.syslogyx.bluetoothdemo.ui;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.syslogyx.bluetoothdemo.R;
import com.syslogyx.bluetoothdemo.app.constant.IConstants;
import com.syslogyx.bluetoothdemo.app.perference.Preferences;
import com.syslogyx.bluetoothdemo.bluetooth.constant.BluetoothStateConstants;
import com.syslogyx.bluetoothdemo.bluetooth.controller.BluetoothMessageController;
import com.syslogyx.bluetoothdemo.bluetooth.dos.SignalPacketDo;
import com.syslogyx.bluetoothdemo.bluetooth.listeners.IBluetoothCallback;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener, IBluetoothCallback {

    //Constant for No device found
    private final String NO_DEVICE_FOUND = "No devices found";

    private ArrayAdapter<String> mPairedDevicesArrayAdapter;
    private Set<BluetoothDevice> pairedDevices;
    private BluetoothMessageController bluetoothMessageController;

    private ListView deviceListView;
    private Button scanNewDevicesButton, terminateBluetoothButton, signalButton;

    private TextView scanningTextView, statusTextView;
    private ProgressBar bluetoothProgressBar;

    //Temp bluetooth checker
    private List<BluetoothDevice> tmpBtChecker = new ArrayList<BluetoothDevice>();
    private Preferences preferences;
    private String logCat = "BT>>";

    private LocalBroadcastManager localBroadcastManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        registerViews();

        initComponents();
        checkBluetoothSupport();
        enableBluetooth();
        checkBluetoothConnection();
        setAdapter();
    }

    @Override
    public void onStart() {
        super.onStart();
        registerBroadcast();
    }


    @Override
    public void onStop() {
        super.onStop();
        //Unregister the broadcast in stop
        try {
            localBroadcastManager.unregisterReceiver(mReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Here we can register the broadcast for set the adapter after the bluetooth is enable
     */
    private void registerBroadcast() {
        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        localBroadcastManager.registerReceiver(mReceiver, new IntentFilter(IConstants.ACTION_SET_ADAPTER));
    }

    private void registerViews() {

        scanNewDevicesButton.setOnClickListener(this);

        deviceListView.setOnItemClickListener(this);
        terminateBluetoothButton.setOnClickListener(this);
        signalButton.setOnClickListener(this);
    }

    private void initViews() {

        deviceListView = findViewById(R.id.device_listView);
        scanNewDevicesButton = findViewById(R.id.scan_button);
        statusTextView = findViewById(R.id.status_textview);

        signalButton = findViewById(R.id.signal_button);
        terminateBluetoothButton = findViewById(R.id.terminate_bluetooth_button);
    }


    /**
     * Initialized all other Components
     */
    private void initComponents() {
        bluetoothMessageController = BluetoothMessageController.getInstance();
        bluetoothMessageController.registerICallback(this);
        bluetoothMessageController.initialize();

        mPairedDevicesArrayAdapter = new ArrayAdapter<String>(this, R.layout.bluetooth_device_name);
        preferences = new Preferences(this);
    }

    /**
     * Method which check the support of bluetooth in the device
     */
    private void checkBluetoothSupport() {

        if (!bluetoothMessageController.isBluetoothSupported()) {
            Log.d(logCat, "Bluetooth is not Supported");
            //Todo show proper error message visible to user on the screen.
            Toast.makeText(this, "Bluetooth Is Not Supported On This Device", Toast.LENGTH_SHORT).show();
//            getActivity().finish();
            //restrict to show all other information
        } else {
            Log.d(logCat, "Bluetooth is Supported");
        }
    }


    private void enableBluetooth() {


        bluetoothMessageController.enableBluetoothAndSetUpConnection();
    }

    /**
     * Check the bluetooth connection and get the connected device name
     */
    private void checkBluetoothConnection() {
        if (bluetoothMessageController.isBluetoothConnected()) {
            Log.d(logCat, "bluetooth is connected");
            statusTextView.setText(getResources().getString(R.string.bluetooth_connected) + bluetoothMessageController.getConnectedDeviceName());
        } else {
            Log.d(logCat, "bluetooth is not connected");
        }
    }


    /**
     * Here we set the array adapter for list of bluetooth devices
     */
    private void setAdapter() {

        try {
            if (mPairedDevicesArrayAdapter != null) {
                // Remove all element from the list
                mPairedDevicesArrayAdapter.clear();

                deviceListView.setAdapter(mPairedDevicesArrayAdapter);

                // Register for broadcasts when discovery has stared
                IntentFilter filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
                this.registerReceiver(mReceiver, filter);

                // Register for broadcasts when a device is discovered
                filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
                this.registerReceiver(mReceiver, filter);

                // Register for broadcasts when discovery has finished
                filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
                this.registerReceiver(mReceiver, filter);

                pairedDevices = bluetoothMessageController.getBondedDevices();

                addPairedDevicesToArrayAdapter();
            }
        } catch (Exception e) {
        }
    }

    /**
     * Method which add paired device into array adapter.
     */
    private void addPairedDevicesToArrayAdapter() {

        // If there are paired devices, add each one to the ArrayAdapter

        if (pairedDevices != null) {
            if (pairedDevices.size() > 0) {
                for (BluetoothDevice device : pairedDevices) {
//                    addDevicesIntoAdapter(device.getName(), device.getAddress());
                    addDevicesIntoAdapter(device.getName(), device.getAddress());
                }
            } else {
                mPairedDevicesArrayAdapter.add(NO_DEVICE_FOUND);
            }
        }
    }

    /**
     * Method which check if device name or address is null
     *
     * @param name
     * @param address
     */
    private void addDevicesIntoAdapter(String name, String address) {


        if (name != null && address != null) {
            mPairedDevicesArrayAdapter.add(name + " " + address);
        } else if (address != null) {
            mPairedDevicesArrayAdapter.add(address);
        } else if (name != null) {
            mPairedDevicesArrayAdapter.add(name);
        } else {
            mPairedDevicesArrayAdapter.add(NO_DEVICE_FOUND);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {

            case BluetoothStateConstants.REQUEST_CONNECT_DEVICE:
                if (resultCode == Activity.RESULT_OK) {
                    bluetoothMessageController.connect(data);
                }
                break;
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.scan_button:
                scanForNewDevices();
                break;

            case R.id.terminate_bluetooth_button:
                terminateBluetooth();
                break;

            case R.id.signal_button:
                launchActivity();
                break;
        }
    }

    private void launchActivity() {

        Intent intent = new Intent(this, SignalActivity.class);
        startActivity(intent);
    }


    /**
     * Remove connected device and disconnect the device from bluetooth
     */
    private void terminateBluetooth() {
        removeDeviceFromPreferences();
        if (bluetoothMessageController.isBluetoothConnected()) {
            bluetoothMessageController.disconnect();
        } else {
            Toast.makeText(this, "Device is not connected", Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * Remove device from the preferences.
     */
    private void removeDeviceFromPreferences() {
        if (preferences != null) {
            preferences.putString(IConstants.PREFERENCES.KEY_PREF_BLUETOOTH_DEVICE_ADDRESS, null);
            preferences.putString(IConstants.PREFERENCES.KEY_PREF_BLUETOOTH_DEVICE_NAME, null);
        }
    }


    private void scanForNewDevices() {
        Log.d(logCat, "scanForNewDevices");
        try {
            if (mPairedDevicesArrayAdapter != null) {
                // Remove all element from the list
                mPairedDevicesArrayAdapter.clear();

                addPairedDevicesToArrayAdapter();

                bluetoothProgressBar.setVisibility(View.VISIBLE);
                scanningTextView.setVisibility(View.VISIBLE);
                scanningTextView.setText(getResources().getString(R.string.scanning_text));

                // If we're already discovering, stop it
                if (bluetoothMessageController.isDiscovering()) {
                    bluetoothMessageController.cancelDiscovery();
                }

                // Request discover from BluetoothAdapter
                bluetoothMessageController.startDiscovery();
            }
        } catch (Exception e) {
        }
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        // Cancel discovery because it's costly and we're about to connect
        if (bluetoothMessageController.getBluetoothAdapter() != null) {
            bluetoothMessageController.getBluetoothAdapter().cancelDiscovery();

            if (bluetoothMessageController.isBluetoothConnected()) {
                Toast.makeText(this, "Already Connected please terminate bluetooth first", Toast.LENGTH_SHORT).show();
            } else {
                makingConnection(view);
            }
        }
    }

    private void makingConnection(View view) {

        if (!((TextView) view).getText().toString().equals(NO_DEVICE_FOUND)) {

            // Get the device MAC address, which is the last 17 chars in the View
            String info = ((TextView) view).getText().toString();
            String address = info.substring(info.length() - 17);
            view.setSelected(true);
            bluetoothMessageController.connect(address);

        }
    }


    @Override
    public void onDeviceConnected(String name, String address) {
        try {
            Log.d(logCat, "Connected To " + name);
            statusTextView.setText(getResources().getString(R.string.bluetooth_connected) + name + " - " + address);
            Toast.makeText(this, "Connected", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
        }
    }

    @Override
    public void onDeviceDisconnected() {
        Log.d(logCat, "Device Disconnected ");
        try {
            statusTextView.setText(getResources().getString(R.string.bluetooth_disconnected));
            Toast.makeText(this, "Disconnected", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
        }
    }

    @Override
    public void onDeviceConnectionFailed() {
        Log.d(logCat, "Device Fail To Connect ");
        try {
            statusTextView.setText(getResources().getString(R.string.bluetooth_fail_connected));
        } catch (Exception e) {
        }
    }

    @Override
    public void onDataReceived(byte[] data, String message) {
        try {
        } catch (Exception e) {
        }
    }

    @Override
    public void setSignalStatus(SignalPacketDo signalPacketDo) {
        try {
        } catch (Exception e) {
        }
    }

    // The BroadcastReceiver that listens for discovered devices and
    // changes the title when discovery is finished
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {

            if (intent != null) {

                String action = intent.getAction();

                if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
                    onDiscoveryStarted();

                } else if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                    onDeviceFound(intent);

                } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                    onDiscoveryFinish();

                } else if (IConstants.ACTION_SET_ADAPTER.equals(action)) {
                    setAdapter();
                }
            }
        }
    };

    // When discovery is finished, change the Activity title
    private void onDiscoveryFinish() {
        if (bluetoothProgressBar != null) {
            bluetoothProgressBar.setVisibility(View.GONE);
            scanningTextView.setVisibility(View.INVISIBLE);
        }
    }

    // When discovery start for device
    private void onDiscoveryStarted() {
        //clearing any existing list data
        tmpBtChecker.clear();

    }

    // When discovery finds a device
    private void onDeviceFound(Intent intent) {
        // Get the BluetoothDevice object from the Intent
        BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

        // If it's already paired, skip it, because it's been listed already
        if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
            if (mPairedDevicesArrayAdapter != null && !mPairedDevicesArrayAdapter.isEmpty()) {
                if (mPairedDevicesArrayAdapter.getItem(0).equals(NO_DEVICE_FOUND)) {
                    mPairedDevicesArrayAdapter.remove(NO_DEVICE_FOUND);
                }
                if (!tmpBtChecker.contains(device)) {
                    tmpBtChecker.add(device);
//                    mPairedDevicesArrayAdapter.add(device.getName() + " " + device.getAddress());
                    addDevicesIntoAdapter(device.getName(), device.getAddress());
                } else {
                    tmpBtChecker.remove(device);
                }
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        // If we're already discovering, stop it
        if (bluetoothMessageController.isDiscovering()) {
            bluetoothMessageController.cancelDiscovery();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (bluetoothMessageController != null) {
            //Removing the register callbacks
            bluetoothMessageController.removeICallback(this);
            // Make sure we're not doing discovery anymore
            if (bluetoothMessageController.getBluetoothAdapter() != null) {
                bluetoothMessageController.getBluetoothAdapter().cancelDiscovery();
            }

        }
        // Unregister broadcast listeners
        try {
            this.unregisterReceiver(mReceiver);
            this.finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
