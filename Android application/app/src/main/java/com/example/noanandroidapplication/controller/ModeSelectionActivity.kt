package com.example.noanandroidapplication.controller

import android.app.Activity
import android.os.Bundle
import android.widget.Button
import com.example.noanandroidapplication.R

class ModeActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mode)

        //we set listeners on the buttons
        val buttonBack = findViewById<Button>(R.id.activity_mode_back_btn)
        buttonBack?.setOnClickListener(){
            finish()
        }

    }



}