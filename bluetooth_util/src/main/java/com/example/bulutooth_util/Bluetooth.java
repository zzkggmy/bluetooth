package com.example.bulutooth_util;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

public class Bluetooth {
    private Context mContext;
    private BlueReceiver receiver;
    private final BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    private BlueCallBack callBack;
    private ArrayList<BluetoothDevice> bondedDevices = new ArrayList<>();
    private UUID uuid;

    private static class SingleInstance {
        @SuppressLint("StaticFieldLeak")
        private static final Bluetooth instance = new Bluetooth();
    }

    public static synchronized Bluetooth getInstance() {
        return SingleInstance.instance;
    }

    public void withBluetooth(Context context) {
        this.mContext = context;
    }

    public void openBlue() {
        if (bluetoothAdapter == null) {
            Toast.makeText(mContext, "您的设备不支持蓝牙!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!bluetoothAdapter.isEnabled()) {
            bluetoothAdapter.enable();
//            Intent discoverableIntent = new Intent
//                    (BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
//
//            discoverableIntent.putExtra(
//                    BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 0);
//            mContext.startActivity(discoverableIntent);
        }
    }

    public void scanBlue(BlueCallBack callBack) {
        this.callBack = callBack;
        if (!bluetoothAdapter.isEnabled()) {
            Toast.makeText(mContext, "请打开蓝牙！", Toast.LENGTH_SHORT).show();
        } else {
            if (bluetoothAdapter.isDiscovering()) {
                bluetoothAdapter.cancelDiscovery();
            } else {
                bluetoothAdapter.startDiscovery();
                getBlueList();
            }
        }
    }

    public void cancelScanBlue() {
        if (bluetoothAdapter == null) {
            Toast.makeText(mContext, "您的设备不支持蓝牙！", Toast.LENGTH_SHORT).show();
        } else {
            bluetoothAdapter.cancelDiscovery();
            if (receiver != null) {
                mContext.unregisterReceiver(receiver);
            }
        }
    }

    private void getBlueList() {
        receiver = new BlueReceiver();
        IntentFilter filter1 = new IntentFilter(android.bluetooth.BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        IntentFilter filter2 = new IntentFilter(android.bluetooth.BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        IntentFilter filter3 = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        mContext.registerReceiver(receiver, filter1);
        mContext.registerReceiver(receiver, filter2);
        mContext.registerReceiver(receiver, filter3);
    }

    public ArrayList<BluetoothDevice> getBondedDevices() {
        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
        if (pairedDevices.size() > 0) {
            bondedDevices.addAll(pairedDevices);
        }
        return bondedDevices;
    }


    private class BlueReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            switch (Objects.requireNonNull(intent.getAction())) {
                case android.bluetooth.BluetoothAdapter.ACTION_DISCOVERY_STARTED:
                    callBack.startScan();
                    break;
                case android.bluetooth.BluetoothAdapter.ACTION_DISCOVERY_FINISHED:
                    callBack.scanFinish();
                    break;
                case BluetoothDevice.ACTION_FOUND:
                    callBack.scanning(device);
                    break;
            }
        }
    }

    public void connect(String deviceName,UUID uuid) {
        ServerThread.getInstance().getBlueAdapter(deviceName,uuid,bluetoothAdapter);
    }

//    private class AcceptThread extends Thread {
//        private final BluetoothServerSocket mmServerSocket;
//
//        public AcceptThread() {
//            BluetoothServerSocket tmp = null;
//            try {
//                tmp = bluetoothAdapter.listenUsingRfcommWithServiceRecord("", uuid);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            mmServerSocket = tmp;
//        }
//
//        public void run() {
//            BluetoothSocket socket;
//            while (true) {
//                try {
//                    socket = mmServerSocket.accept();
//                } catch (IOException e) {
//                    break;
//                }
//                if (socket != null) {
//                    manageConnectedSocket(socket);
//                    try {
//                        mmServerSocket.close();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                    break;
//                }
//            }
//        }
//
//        public void cancel() {
//            try {
//                mmServerSocket.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    private class ConnectThread extends Thread {
//
//        private final BluetoothSocket mmSocket;
//        private final BluetoothDevice mmDevice;
//
//        public ConnectThread(BluetoothDevice device) {
//            BluetoothSocket tmp = null;
//            mmDevice = device;
//            try {
//                tmp = device.createRfcommSocketToServiceRecord(uuid);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            mmSocket = tmp;
//        }
//
//        public void run() {
//            // Cancel discovery because it will slow down the connection
//            bluetoothAdapter.cancelDiscovery();
//
//            try {
//                // Connect the device through the socket. This will block
//                // until it succeeds or throws an exception
//                mmSocket.connect();
//            } catch (IOException connectException) {
//                // Unable to connect; close the socket and get out
//                try {
//                    mmSocket.close();
//                } catch (IOException closeException) {
//                    closeException.printStackTrace();
//                }
//                return;
//            }
//
//            manageConnectedSocket(mmSocket);
//        }
//
//        public void cancel() {
//            try {
//                mmSocket.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    private class ConnectedThread extends Thread {
//        private final BluetoothSocket mmSocket;
//        private final InputStream mmInStream;
//        private final OutputStream mmOutStream;
//
//        public ConnectedThread(BluetoothSocket socket) {
//            mmSocket = socket;
//            InputStream tmpIn = null;
//            OutputStream tmpOut = null;
//            try {
//                tmpIn = socket.getInputStream();
//                tmpOut = socket.getOutputStream();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//            mmInStream = tmpIn;
//            mmOutStream = tmpOut;
//        }
//
//        public void run() {
//            byte[] buffer = new byte[1024];
//            int bytes;
//
//            while (true) {
//                try {
//
//                    bytes = mmInStream.read(buffer);
//
//                    mHandler.obtainMessage(MESSAGE_READ, bytes, -1, buffer)
//                            .sendToTarget();
//                } catch (IOException e) {
//                    break;
//                }
//            }
//        }
//
//        public void write(byte[] bytes) {
//            try {
//                mmOutStream.write(bytes);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//
//        public void cancel() {
//            try {
//                mmSocket.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }
}
