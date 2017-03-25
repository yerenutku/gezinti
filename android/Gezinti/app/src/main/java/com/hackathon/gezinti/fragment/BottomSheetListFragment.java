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

import com.hackathon.gezinti.EventCreationActivity;
import com.hackathon.gezinti.EventDetailActivity;
import com.hackathon.gezinti.R;
import com.hackathon.gezinti.adapters.EventsAdapter;
import com.hackathon.gezinti.models.common.Event;
import com.hackathon.gezinti.utils.RecyclerItemListener;

import java.util.ArrayList;

public class BottomSheetListFragment extends Fragment {

    private View mDimBackground;
    private EventsAdapter mEventsAdapter;
    private RecyclerView mRecyclerView;

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
                    Intent intent = new Intent(getActivity(), EventCreationActivity.class);
                    startActivity(intent);
                } else {
                    //list.get(position) için olan modeli yeni activity gönder
                    Intent intent = new Intent(getActivity(), EventDetailActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("EventDetail", mEvents.get(position));
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        }));
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
