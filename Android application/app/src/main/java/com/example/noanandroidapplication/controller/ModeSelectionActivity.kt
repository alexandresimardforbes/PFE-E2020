package com.example.noanandroidapplication.controller

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import com.example.noanandroidapplication.R

class ModeSelectionActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mode_selection)

        //we set listeners on the buttons

        //back button
        val buttonBack = findViewById<Button>(R.id.activity_mode_back_btn)
        buttonBack?.setOnClickListener(){
            finish()
        }

        //analog button
        val buttonAnalogFrame = findViewById<Button>(R.id.activity_mode_analog_btn)
        buttonAnalogFrame?.setOnClickListener(){
            val analogFrameIntent = Intent(this, AnalogFrameActivity::class.java)
            startActivity(analogFrameIntent)
        }

        //iptv button
        val buttonIptvFrame = findViewById<Button>(R.id.activity_mode_iptv_btn)
        buttonIptvFrame?.setOnClickListener(){
            val iptvFrameIntent = Intent(this, IpFrameActivity::class.java)
            startActivity(iptvFrameIntent)
        }

        //noan button
        val buttonNoanFrame = findViewById<Button>(R.id.activity_mode_noan_btn)
        buttonNoanFrame?.setOnClickListener(){
            val noanFrameIntent = Intent(this, NoanFrameActivity::class.java)
            startActivity(noanFrameIntent)
        }

        //web button
        val buttonWebFrame = findViewById<Button>(R.id.activity_mode_web_btn)
        buttonWebFrame?.setOnClickListener(){
            val webFrameIntent = Intent(this, WebFrameActivity::class.java)
            startActivity(webFrameIntent)
        }


    }



}