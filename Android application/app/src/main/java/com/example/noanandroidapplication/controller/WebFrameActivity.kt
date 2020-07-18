package com.example.noanandroidapplication.controller

import android.app.Activity
import android.os.Bundle
import android.webkit.WebView
import com.example.noanandroidapplication.R

class WebFrameActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_frame)

        //we set the web page
        val myWebView = WebView(this)
        setContentView(myWebView)
        myWebView.loadUrl("https://www.google.com")

    }
}