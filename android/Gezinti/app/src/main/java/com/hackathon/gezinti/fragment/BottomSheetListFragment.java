package com.hackathon.gezinti.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.google.android.gms.maps.model.LatLng;
import com.hackathon.gezinti.EventDetailActivity;
import com.hackathon.gezinti.MainActivity;
import com.hackathon.gezinti.R;
import com.hackathon.gezinti.adapters.EventsAdapter;
import com.hackathon.gezinti.interfaces.EventCreateListener;
import com.hackathon.gezinti.models.common.Coordinates;
import com.hackathon.gezinti.models.common.Event;
import com.hackathon.gezinti.models.request.EventCreateRequest;
import com.hackathon.gezinti.models.response.EventCreateResponse;
import com.hackathon.gezinti.network.EventInteractor;
import com.hackathon.gezinti.utils.RecyclerItemListener;

import java.util.ArrayList;
import java.util.List;

public class BottomSheetListFragment extends Fragment implements View.OnClickListener{

    private View mDimBackground;
    private EventsAdapter mEventsAdapter;
    private RecyclerView mRecyclerView;
    private RelativeLayout mRelativeLayout;
    private EditText mEtTitle, mEtDesc;
    private Button btnCreate, btnCancel, btnRemoveMarker;
    private Spinner spEventTypes, spEventTimes;

    private MapFragment mMapFragment;

    private ArrayList<Event> mEvents;

    public BottomSheetListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_bottom_sheet_list, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.rvEvents);
        mRecyclerView.addOnItemTouchListener(new RecyclerItemListener(getContext(), mRecyclerView,
                new RecyclerItemListener.RecyclerTouchListener() {
            @Override
            public void onClickItem(View v, int position) {
                if(mEvents.size() == position){
                    //create
                    eventFormChanger(true);
                } else {
                    //list.get(position) için olan modeli yeni activity gönder
                    Intent intent = new Intent(getActivity(), EventDetailActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("EventDetail", mEvents.get(position));
                    intent.putExtras(bundle);
                    getActivity().startActivityForResult(intent, MainActivity.EVENT_DETAIL_REQUEST_CODE);
                }
            }
        }));
        mRelativeLayout = (RelativeLayout) view.findViewById(R.id.rl_event_create);
        mRelativeLayout.setVisibility(View.GONE);
        mEtTitle = (EditText) view.findViewById(R.id.et_event_title);
        mEtDesc = (EditText) view.findViewById(R.id.et_event_desc);
        btnCreate = (Button) view.findViewById(R.id.btn_event_create);
        btnCreate.setOnClickListener(this);
        btnCancel = (Button) view.findViewById(R.id.btn_event_cancel);
        btnCancel.setOnClickListener(this);
        btnRemoveMarker = (Button) view.findViewById(R.id.btn_marker_remove);
        btnRemoveMarker.setOnClickListener(this);
        spEventTypes = (Spinner) view.findViewById(R.id.sp_event_types);
        ArrayAdapter<CharSequence> eventAdapter = ArrayAdapter
                .createFromResource(getContext(), R.array.array_event_types,
                        android.R.layout.simple_spinner_item);
        eventAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spEventTypes.setAdapter(eventAdapter);

        spEventTimes = (Spinner) view.findViewById(R.id.sp_event_times);
        ArrayAdapter<CharSequence> timeAdapter = ArrayAdapter
                .createFromResource(getContext(), R.array.array_event_times,
                        android.R.layout.simple_spinner_item);
        timeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spEventTimes.setAdapter(timeAdapter);

        mMapFragment = MapFragment.getInstance();

        return view;
    }

    public void setDimBackground(View dimBackground) {
        this.mDimBackground = dimBackground;
        dimBackground.setVisibility(View.GONE);
    }

    public BottomSheetBehavior.BottomSheetCallback getBottomSheetCallback() {
        return bottomSheetCallback;
    }

    public void setEventsForPosition(ArrayList<Event> events) {
        mEvents = events;
        mEventsAdapter = new EventsAdapter(mEvents, getContext());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mEventsAdapter);
    }

    public void refreshEvents(ArrayList<Event> events) {
        mEvents = events;
        mEventsAdapter.setItems(events);
        mEventsAdapter.notifyDataSetChanged();
    }

    public void eventFormChanger(boolean isOpen){
        if (isOpen) {
            mRecyclerView.setVisibility(View.GONE);
            mRelativeLayout.setVisibility(View.VISIBLE);
            mMapFragment.openEventCreatorMode();
        } else {
            mRecyclerView.setVisibility(View.VISIBLE);
            mRelativeLayout.setVisibility(View.GONE);
            mMapFragment.closeEventCreatorMode();
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == btnCreate.getId()){
            //verileri ve latlng listini gönder, etkinlik oluştur
            ArrayList<Coordinates> list = mMapFragment.getCoordinatesList();
            if(list != null){
                EventCreateRequest eventCreateRequest = new EventCreateRequest();
                eventCreateRequest.setTitle(mEtTitle.getText().toString());
                eventCreateRequest.setDesc(mEtDesc.getText().toString());
                eventCreateRequest.setCoordinates(list);
                eventCreateRequest.setTime("TIME");
                eventCreateRequest.setEventTime(String.valueOf(spEventTimes.getSelectedItemPosition()));
                eventCreateRequest.setEventType(String.valueOf(spEventTypes.getSelectedItemPosition()));
                eventCreateRequest.setOwner("58d6bdb989f42f0544a8721d");

                EventInteractor eventInteractor = new EventInteractor(getContext());
                eventInteractor.eventCreate(eventCreateRequest, new EventCreateListener() {
                    @Override
                    public void onResponse(EventCreateResponse response) {
                        eventFormChanger(false);
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
        } else if (v.getId() == btnCancel.getId()){
            eventFormChanger(false);
        } else if (v.getId() == btnRemoveMarker.getId()){
            mMapFragment.popCoordinate();
        }
    }

    private BottomSheetBehavior.BottomSheetCallback bottomSheetCallback = new BottomSheetBehavior.BottomSheetCallback() {

        @Override
        public void onStateChanged(@NonNull View bottomSheet, int newState) {
        }

        @Override
        public void onSlide(@NonNull View bottomSheet, float slideOffset) {
            mDimBackground.animate().alpha(slideOffset).setDuration(0).start();

            if (slideOffset > 0) {
                mDimBackground.setVisibility(View.VISIBLE);
            } else {
                mDimBackground.setVisibility(View.GONE);
            }
        }
    };

}
