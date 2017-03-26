package com.hackathon.gezinti.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.hackathon.gezinti.R;
import com.hackathon.gezinti.interfaces.GeneralSuccessListener;
import com.hackathon.gezinti.models.common.Coordinates;
import com.hackathon.gezinti.models.common.Event;
import com.hackathon.gezinti.models.common.User;
import com.hackathon.gezinti.network.EventInteractor;

public class EventDetailActivity extends BaseActivity {

    private Event mEvent;
    private TextView tvTitle, tvOwner, tvTel, tvDesc, tvLocations, tvMembers, tvEventTime, tvEventType;
    private Button btJoin, btLeave, btDelete;
    public static final String hardcodedUserID = "58d6bdb989f42f0544a8721d";
    private boolean isJoined = false, isOwner = false;
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

        tvTel = (TextView) findViewById(R.id.tvTel);
        tvTel.setText(getString(R.string.event_detail_tel, mEvent.getOwner().getTel()));

        tvDesc = (TextView) findViewById(R.id.tvDesc);
        tvDesc.setText(getString(R.string.event_detail_desc, mEvent.getDesc()));

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

        tvEventTime = (TextView) findViewById(R.id.tvEventTime);
        String[] eventTimes = getResources().getStringArray(R.array.array_event_times);
        tvEventTime.setText(getString(R.string.event_detail_time, eventTimes[Integer.valueOf(mEvent.getEventTime())]));

        String[] eventTypes = getResources().getStringArray(R.array.array_event_types);
        tvEventType = (TextView) findViewById(R.id.tvEventType);
        tvEventType.setText(getString(R.string.event_detail_eventtype, eventTypes[Integer.valueOf(mEvent.getEventType())]));

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
                        showErrorMessage(errorMessage);
                    }

                    @Override
                    public void onBeforeRequest() {
                        showWaitingDialog();
                    }

                    @Override
                    public void onAfterRequest() {
                        dismissWaitingDialog();
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
                        showErrorMessage(errorMessage);
                    }

                    @Override
                    public void onBeforeRequest() {
                        showWaitingDialog();
                    }

                    @Override
                    public void onAfterRequest() {
                        dismissWaitingDialog();
                    }
                });
            }
        });
        btDelete = (Button) findViewById(R.id.btDeleteEvent);
        btDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mInteractor.deleteEvent(mEvent.getId(), new GeneralSuccessListener() {
                    @Override
                    public void onSuccess() {
                        setResult(RESULT_OK);
                        finish();
                    }

                    @Override
                    public void onError(String errorMessage) {
                        Log.d("DetailError", errorMessage);
                    }

                    @Override
                    public void onBeforeRequest() {
                        Log.d("DetailBefore", "");
                    }

                    @Override
                    public void onAfterRequest() {
                        Log.d("DetailAfter", "");
                    }
                });
            }
        });

        isJoined = false;
        isOwner = false;
        for (User user : mEvent.getMembers()) {
            if (user.getId().equals(hardcodedUserID)) isJoined = true;
        }
        if (mEvent.getOwner().getId().equals(hardcodedUserID)) isOwner = true;
        checkButtonsVisibility();

    }

    private void checkButtonsVisibility() {
        if (isOwner) {
            btJoin.setVisibility(View.GONE);
            btLeave.setVisibility(View.GONE);
            btDelete.setVisibility(View.VISIBLE);
        } else {
            if (isJoined) {
                btJoin.setVisibility(View.GONE);
                btLeave.setVisibility(View.VISIBLE);
                btDelete.setVisibility(View.GONE);
            } else {
                btJoin.setVisibility(View.VISIBLE);
                btLeave.setVisibility(View.GONE);
                btDelete.setVisibility(View.GONE);
            }
        }
    }
}
