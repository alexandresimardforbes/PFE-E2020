package com.example.noanandroidapplication.controller

import android.app.Activity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import com.example.noanandroidapplication.R

class IpFrameActivity: Activity() {

    private lateinit var listView : ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ip_frame)

        //how to create a list view in kotlin
        //https://www.raywenderlich.com/155-android-listview-tutorial-with-kotlin
        listView = findViewById<ListView>(R.id.activity_ip_frame_listview)

        val list = generateIptvSource()
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, list)
        listView.adapter = adapter
    }

    fun generateIptvSource():MutableList<String>{
        val list: MutableList<String> = ArrayList()

        list.add("Channel SCTV")
        list.add("Channel theLobTv")
        list.add("Channel PittburgTv")
        list.add("Channel CBS")

        return list

    }
}