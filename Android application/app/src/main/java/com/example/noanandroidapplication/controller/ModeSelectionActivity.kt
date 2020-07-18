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
        val buttonBack = findViewById<Button>(R.id.activity_mode_back_btn)
        buttonBack?.setOnClickListener(){
            finish()
        }

        val buttonWebFrame = findViewById<Button>(R.id.activity_mode_web_btn)
        buttonWebFrame?.setOnClickListener(){
            val webFrameIntent = Intent(this, WebFrameActivity::class.java)
            startActivity(webFrameIntent)
        }

    }



}