package com.example.noanandroidapplication.controller

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.example.noanandroidapplication.R

class MainActivity2 : Activity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity2_main)

        //set listeners on buttons
        val buttonModes = findViewById<Button>(R.id.activity_main_modes_btn)
        buttonModes?.setOnClickListener(){
            Toast.makeText(this@MainActivity2,  R.string.app_name, Toast.LENGTH_LONG).show()

            //on lance l'activité grâce à un intent
            val modeActivityIntent = Intent(this, ModeActivity::class.java)
            startActivity(modeActivityIntent)


        }

        val buttonExit = findViewById<Button>(R.id.activity_main_exit_btn)
        buttonExit?.setOnClickListener(){
            finish()
        }

    }
}