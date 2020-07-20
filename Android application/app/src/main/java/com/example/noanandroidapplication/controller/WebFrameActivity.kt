package com.example.noanandroidapplication.controller

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.widget.TextView
import com.example.noanandroidapplication.R

class WebFrameActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_frame)

        //we set the web page
        val myWebView = findViewById<View>(R.id.activity_web_frame_webview) as WebView
        myWebView.loadUrl("https://www.google.com")

    }
}