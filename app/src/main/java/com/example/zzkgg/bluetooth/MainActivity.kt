package com.example.zzkgg.bluetooth

import android.bluetooth.BluetoothDevice
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.bulutooth_util.BlueCallBack
import com.example.bulutooth_util.Bluetooth
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private val devices: ArrayList<BluetoothDevice> = ArrayList()
    private var adapter: BlueAdapter = BlueAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Bluetooth.getInstance().withBluetooth(this)
        rv_main.layoutManager = LinearLayoutManager(this)
        tv_open_blu.setOnClickListener {
            Bluetooth.getInstance().openBlue()
        }
        adapter.setOnItemClickListener(object : BlueAdapter.SetOnItemClickListener {
            override fun onItemClick(view: View, position: Int) {
                Toast.makeText(this@MainActivity, "$position", Toast.LENGTH_SHORT).show()
                Bluetooth.getInstance().connect(devices[position].name,devices[position].uuids[0].uuid)
            }
        })
        tv_close_blu.setOnClickListener { Bluetooth.getInstance().cancelScanBlue() }
        tv_scan_blu.setOnClickListener {
            Bluetooth.getInstance().scanBlue(object : BlueCallBack {
                override fun startScan() {
                    Toast.makeText(this@MainActivity, "扫描开始", Toast.LENGTH_SHORT).show()
                }

                override fun scanning(device: BluetoothDevice) {
                    Toast.makeText(this@MainActivity, "正在扫描", Toast.LENGTH_SHORT).show()
                    try {
                        Log.d("result_data", device.name)
                    } catch (e: NullPointerException) {
                        e.printStackTrace()
                    }
                    devices.add(device)
                    adapter.getContext(this@MainActivity)
                    adapter.setData(devices)
                    rv_main.adapter = adapter
                }

                override fun scanFinish() {
                    Toast.makeText(this@MainActivity, "扫描结束", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

}
