package com.example.vulnapp;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class DeepLinkActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TextView tv = new TextView(this);
        tv.setText("Deep Link triggered with: " + getIntent().getDataString());
        setContentView(tv);
    }
}
