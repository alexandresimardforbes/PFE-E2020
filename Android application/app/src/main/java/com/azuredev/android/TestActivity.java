package com.azuredev.android;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.TextView;

import com.example.noanandroidapplication.R;

public class TestActivity extends Activity {
    private TextView text;
    public String keyValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        text =  (TextView) findViewById(R.id.textView);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        text.setText("" + event.getKeyCode());
        keyValue =  "" +event.getKeyCode();
//        return super.dispatchKeyEvent(event);
        return  false;
    }

    public String getRemoteValue() {
        return this.keyValue;
    }
}