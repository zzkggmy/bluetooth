package com.example.bulutooth_util;

import android.bluetooth.BluetoothDevice;

public interface BlueCallBack {
    void startScan();

    void scanning(BluetoothDevice device);

    void scanFinish();
}
