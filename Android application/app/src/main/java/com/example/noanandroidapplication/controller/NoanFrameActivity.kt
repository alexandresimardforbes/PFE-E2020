package com.example.noanandroidapplication.controller

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import com.example.noanandroidapplication.R

class NoanFrameActivity: Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_noan_frame)

        //we set the noan web page
        val myWebView = findViewById<View>(R.id.activity_noan_frame_webview) as WebView
        myWebView.loadUrl("https://www.google.com")
    }
}