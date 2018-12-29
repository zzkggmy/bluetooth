package com.example.bulutooth_util;

public interface ConnectCallBack {
    void startConnect();

    void connectSuccess();

    void connectFailed();

    void finishConnect();
}
