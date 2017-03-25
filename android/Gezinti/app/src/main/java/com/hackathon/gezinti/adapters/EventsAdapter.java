package com.hackathon.gezinti.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hackathon.gezinti.R;
import com.hackathon.gezinti.models.common.Event;

import java.util.ArrayList;

public class EventsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private ArrayList<Event> mEvents;
    private Context mContext;
    private static final int FOOTER_VIEW = 1;

    public EventsAdapter(ArrayList<Event> events, Context context) {
        mEvents = events;
        mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == FOOTER_VIEW) {
            View itemView = LayoutInflater.from(mContext)
                    .inflate(R.layout.item_add_event, parent, false);
            return new FooterViewHolder(itemView);
        } else {
            View itemView = LayoutInflater.from(mContext)
                    .inflate(R.layout.item_recyclerview_response, parent, false);
            return new MyViewHolder(itemView);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MyViewHolder){
            MyViewHolder myViewHolder = (MyViewHolder) holder;
//            myViewHolder.tvText.setText(mEventCreateResponseList.get(position).getText() + position);
        }
    }

    @Override
    public int getItemCount() {
        if (mEvents == null) {
            return 0;
        }
        if (mEvents.size() == 0) {
            //Return 1 here to show nothing
            return 1;
        }
        // Add extra view to show the footer view
        return mEvents.size() + 1;
    }

    public void setItems(ArrayList<Event> events){
        mEvents = events;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView tvText;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvText = (TextView) itemView.findViewById(R.id.tv_text);
        }
    }

    public class FooterViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout llRow;

        public FooterViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //TODO ADD EVENT
                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == mEvents.size()) {
            // This is where we'll add footer.
            return FOOTER_VIEW;
        }

        return super.getItemViewType(position);
    }
}