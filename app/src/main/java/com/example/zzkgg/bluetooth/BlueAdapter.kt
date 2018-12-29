package com.example.zzkgg.bluetooth

import android.bluetooth.BluetoothDevice
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.blu_item.view.*

class BlueAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private lateinit var context: Context
    private var devices: ArrayList<BluetoothDevice> = ArrayList()
    private lateinit var setOnItemClickListener: SetOnItemClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        BlueHolder(LayoutInflater.from(context).inflate(R.layout.blu_item, parent, false))

    override fun getItemCount() = devices.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder.itemView.ntv_name_blu.text = devices[position].name
        holder.itemView.tv_mac.text = devices[position].address
        holder.itemView.cl_blue.setOnClickListener {
            setOnItemClickListener.onItemClick(holder.itemView, position)
        }
    }

    fun setData(data: ArrayList<BluetoothDevice>) {
        this.devices = data
    }

    fun setOnItemClickListener(setOnItemClickListener: SetOnItemClickListener) {
        this.setOnItemClickListener = setOnItemClickListener
    }

    fun getContext(context: Context) {
        this.context = context
    }

    class BlueHolder(view: View) : RecyclerView.ViewHolder(view)

    interface SetOnItemClickListener {
        fun onItemClick(view: View, position: Int)
    }
}