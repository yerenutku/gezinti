package com.hackathon.gezinti;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.hackathon.gezinti.interfaces.GeneralSuccessListener;
import com.hackathon.gezinti.models.common.Coordinates;
import com.hackathon.gezinti.models.common.Event;
import com.hackathon.gezinti.models.common.User;
import com.hackathon.gezinti.network.EventInteractor;

public class EventDetailActivity extends AppCompatActivity {

    private Event mEvent;
    private TextView tvTitle, tvOwner, tvDesc, tvTime, tvLocations, tvMembers;
    private Button btJoin, btLeave;
    public static final String hardcodedUserID = "58d6cff97909d41fd4fc6e02";
    private boolean isJoined = false;
    private EventInteractor mInteractor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);
        setResult(RESULT_CANCELED);
        Bundle bundle = this.getIntent().getExtras();
        if (bundle == null) finish();
        mEvent = (Event) bundle.getSerializable("EventDetail");
        mInteractor = new EventInteractor(this);

        Log.d("EventDetail", mEvent.getTitle() + " - " + mEvent.getCoordinatesList().get(0).getLat() + " " + mEvent.getCoordinatesList().get(0).getLon());

        initViews();

    }

    private void initViews() {
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvTitle.setText(getString(R.string.event_detail_title, mEvent.getTitle()));

        tvOwner = (TextView) findViewById(R.id.tvOwner);
        tvOwner.setText(getString(R.string.event_detail_owner, mEvent.getOwner().getName()));

        tvDesc = (TextView) findViewById(R.id.tvDesc);
        tvDesc.setText(getString(R.string.event_detail_desc, mEvent.getDesc()));

        tvTime = (TextView) findViewById(R.id.tvTime);
        tvTime.setText(getString(R.string.event_detail_time, mEvent.getTime()));

        tvLocations = (TextView) findViewById(R.id.tvLocations);
        String locations = "";
        for (Coordinates coordinate : mEvent.getCoordinatesList()) {
            locations = locations + coordinate.getLat() + ", " + coordinate.getLon() + "\n";
        }
        tvLocations.setText(getString(R.string.event_detail_locations, locations));

        tvMembers = (TextView) findViewById(R.id.tvMembers);
        String members = "";
        for (User user : mEvent.getMembers()) {
            members = members + user.getName() + "\n";
        }
        tvMembers.setText(getString(R.string.event_detail_members, members));

        btJoin = (Button) findViewById(R.id.btJoin);
        btLeave = (Button) findViewById(R.id.btLeave);
        btJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mInteractor.joinEvent(mEvent.getId(), hardcodedUserID, new GeneralSuccessListener() {
                    @Override
                    public void onSuccess() {
                        setResult(RESULT_OK);
                        isJoined = true;
                        checkButtonsVisibility();
                    }

                    @Override
                    public void onError(String errorMessage) {

                    }

                    @Override
                    public void onBeforeRequest() {

                    }

                    @Override
                    public void onAfterRequest() {

                    }
                });
            }
        });
        btLeave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mInteractor.leaveEvent(mEvent.getId(), hardcodedUserID, new GeneralSuccessListener() {
                    @Override
                    public void onSuccess() {
                        setResult(RESULT_OK);
                        isJoined = false;
                        checkButtonsVisibility();
                    }

                    @Override
                    public void onError(String errorMessage) {

                    }

                    @Override
                    public void onBeforeRequest() {

                    }

                    @Override
                    public void onAfterRequest() {

                    }
                });
            }
        });
        isJoined = false;
        for (User user : mEvent.getMembers()) {
            if (user.getId().equals(hardcodedUserID)) isJoined = true;
        }
        checkButtonsVisibility();

    }

    private void checkButtonsVisibility() {
        if (isJoined) {
            btJoin.setVisibility(View.GONE);
            btLeave.setVisibility(View.VISIBLE);
        } else {
            btJoin.setVisibility(View.VISIBLE);
            btLeave.setVisibility(View.GONE);
        }
    }
}
