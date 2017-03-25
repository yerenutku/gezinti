package com.hackathon.gezinti;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.hackathon.gezinti.models.EventResponse;

public class EventDetailActivity extends AppCompatActivity {

    private EventResponse mEventResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);

        Bundle bundle = this.getIntent().getExtras();
        if(bundle == null) finish();
        mEventResponse = (EventResponse) bundle.getSerializable("EventDetail");

        Log.d("EventDetail", mEventResponse.getText() + " - " + mEventResponse.getLatitude() + " " + mEventResponse.getLongitude());

    }
}
