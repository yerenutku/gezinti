package com.hackathon.gezinti;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.hackathon.gezinti.ui.MapFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fl_map_container, new MapFragment(), "")
                    .commit();
        }

    }
}
