package com.example.bulutooth_util;

import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;

import java.io.IOException;
import java.util.UUID;

public class BlueConnect {

    private final BluetoothServerSocket bluetoothServerSocket;
    private UUID uuid;

    private BlueConnect() {
        BluetoothServerSocket tmp = null;
//        try {
//            tmp = Bluetooth.getInstance().bluetoothAdapter.listenUsingInsecureRfcommWithServiceRecord("", uuid);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        bluetoothServerSocket = tmp;
    }

    private static class SingleInstance {
        private static BlueConnect instance = new BlueConnect();
    }

    public BlueConnect instance() {
        return SingleInstance.instance;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    private void connect() {
        BluetoothSocket socket = null;
        // Keep listening until exception occurs or a socket is returned
        while (true) {
            try {
                socket = bluetoothServerSocket.accept();
            } catch (IOException e) {
                break;
            }
            // If a connection was accepted
            if (socket != null) {
                // Do work to manage the connection (in a separate thread)
//                manageConnectedSocket(socket);
                try {
                    bluetoothServerSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }

    public class ConnectThread extends Thread{
        @Override
        public void run() {
            super.run();
        }
    }
}
