package com.azuredev.android;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.VideoView;

import com.example.noanandroidapplication.R;

import java.util.UUID;

public class MainActivity extends Activity {
    private WebView myWebView;
    private TVClientImpl client = new TVClientImpl();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main_web);
        WebView.setWebContentsDebuggingEnabled(true);
        myWebView = (WebView) findViewById(R.id.webView);
        WebSettings webSettings = myWebView.getSettings();

        webSettings.setJavaScriptEnabled(true);

        SimulatedHAL fake = new SimulatedHAL();
        myWebView.addJavascriptInterface(new JSInterface(this, fake), "Android"); //You will access this via Android.method(args);

        myWebView.setBackgroundColor(Color.TRANSPARENT);
        //myWebView.loadUrl("http://192.168.0.16:5000"); // localhost (Charles)
        myWebView.loadUrl("https://pfe-e2020-noan.herokuapp.com/"); // site heroku

        VideoView videoView = (VideoView) findViewById(R.id.videoView);
        client.onCreate((Context) this, videoView);

//        myWebView.loadUrl("javascript: var result = window.Android.FetchJavaData(); window.DoStuff(result)");

        hideSystemUI();
        updateUI();
        generateUniqueID();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        myWebView.loadUrl("http://192.168.2.81:5000");
        return false;
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        hideSystemUI();
    }

    @Override
    public void onResume() {
        super.onResume();
        hideSystemUI();
    }

    public void updateUI() {
        final View decorView = getWindow().getDecorView();
        decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                hideSystemUI();
            }
        });
    }

    private void hideSystemUI() {
        // Enables regular immersive mode.
        // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
        // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        // Set the content to appear under the system bars so that the
                        // content doesn't resize when the system bars hide and show.
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        // Hide the nav bar and status bar
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    public void generateUniqueID() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String value = sharedPreferences.getString("uuid_key", "");
        if (TextUtils.isEmpty(value)) {
            String uuid = UUID.randomUUID().toString();
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("uuid_key", uuid);
            editor.commit();
        }
    }
}