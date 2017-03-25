package com.hackathon.gezinti;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.hackathon.gezinti.models.common.Event;

public class EventDetailActivity extends AppCompatActivity {

    private Event mEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);

        Bundle bundle = this.getIntent().getExtras();
        if(bundle == null) finish();
        mEvent = (Event) bundle.getSerializable("EventDetail");

        Log.d("EventDetail", mEvent.getTitle() + " - " + mEvent.getCoordinatesList().get(0).getLat() + " " + mEvent.getCoordinatesList().get(0).getLon());

    }
}
