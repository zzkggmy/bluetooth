package com.example.bulutooth_util;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

public class ServerThread {
    private BluetoothAdapter bluetoothAdapter;
    private BluetoothServerSocket bluetoothServerSocket;
    private BluetoothSocket socket;
    private ReadThread readThread;
    private WriteThread writeThread;

    private static class SingleInstance {
        private static final ServerThread instance = new ServerThread();
    }

    void getBlueAdapter(String deviceName, UUID uuid, BluetoothAdapter bluetoothAdapter) {
        this.bluetoothAdapter = bluetoothAdapter;
        startConnect(deviceName, uuid);
    }

    private void startConnect(String deviceName, UUID uuid) {
        BluetoothServerSocket tmp = null;
        try {
            tmp = bluetoothAdapter.listenUsingRfcommWithServiceRecord(deviceName, uuid);
        } catch (IOException e) {
            e.printStackTrace();
        }
        bluetoothServerSocket = tmp;
    }

    public static synchronized ServerThread getInstance() {
        return SingleInstance.instance;
    }

    private class AcceptThread extends Thread {
        @Override
        public void run() {
            super.run();
            try {
                socket = bluetoothServerSocket.accept();
                readThread = new ReadThread();
                readThread.start();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private class ReadThread extends Thread {
        @Override
        public void run() {
            super.run();
            if (socket.isConnected()) {
                try {
                    InputStream inputStream = socket.getInputStream();
                    byte[] buff = new byte[2048];
                    int len = 0;
                    while (true) {
                        int size = inputStream.read(buff);
                        if (size > 0) {
                            Log.d("read_data",String.valueOf(buff));
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private class WriteThread extends Thread {
        @Override
        public void run() {
            super.run();
        }
    }

    public void closeServer() {
        try {
            bluetoothServerSocket.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
