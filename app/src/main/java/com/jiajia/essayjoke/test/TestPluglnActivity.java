package com.jiajia.essayjoke.test;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.jiajia.essayjoke.R;

public class TestPluglnActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_plugln);

        Log.w("TestPluglnActivity", "TestPluglnActivity");
    }
}