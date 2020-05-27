package com.example.ftn_android_10.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.ftn_android_10.R;
import com.example.ftn_android_10.adapters.SearchAdapter;

public class MainActivity extends AppCompatActivity implements SearchAdapter.OnItemClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );
    }

    @Override
    public void onItemClick(int position) {

    }
}
