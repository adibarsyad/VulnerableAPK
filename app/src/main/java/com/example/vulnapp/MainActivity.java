package com.example.vulnapp;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);  // Ensure this XML includes buttons: btn_load, btn_deeplink, btn_scheme, btn_broadcast

        // 🧪 Write fake secret to internal storage (for adb backup or run-as)
        String secret = "top_secret_api_key=abcd1234";
        try {
            FileOutputStream fos = openFileOutput("secret.txt", Context.MODE_PRIVATE);
            fos.write(secret.getBytes());
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 📲 Find UI buttons
        Button btnLoad = findViewById(R.id.btn_load);
        Button btnDeepLink = findViewById(R.id.btn_deeplink);
        Button btnScheme = findViewById(R.id.btn_scheme);
        Button btnBroadcast = findViewById(R.id.btn_broadcast);

        // 🌐 Launch WebViewActivity with URL from intent
        btnLoad.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setClass(MainActivity.this, WebViewActivity.class);
            intent.setData(android.net.Uri.parse("http://10.0.2.2/real.html"));  // gets passed to WebViewActivity via getIntent().getDataString()
            startActivity(intent);
        });

        // 🔗 Deep link intent
        btnDeepLink.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(android.net.Uri.parse("vulnapp://deeplink?param=test"));
            startActivity(intent);
        });

        // 📡 Custom scheme handler
        btnScheme.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(android.net.Uri.parse("vulnapp://anydata"));
            startActivity(intent);
        });

        // 🚨 Send vulnerable broadcast
        btnBroadcast.setOnClickListener(view -> {
            Intent intent = new Intent("com.example.vulnapp.VULNERABLE_ACTION");
            intent.putExtra("data", "reboot");

            // optional: make it explicit for Android 8+
            intent.setComponent(new ComponentName("com.example.vulnapp", "com.example.vulnapp.VulnReceiver"));

            sendBroadcast(intent);
        });
    }
}
