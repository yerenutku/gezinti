package com.hackathon.gezinti.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hackathon.gezinti.R;
import com.hackathon.gezinti.models.EventResponse;

import java.util.List;

// temp adapter
public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.MyViewHolder>{

    private List<EventResponse> responseList;
    private Context mContext;

    @Override
    public EventsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext)
                .inflate(R.layout.item_recyclerview_response, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.tvText.setText(responseList.get(position).getText()+position);
    }

    @Override
    public int getItemCount() {
        return responseList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        private TextView tvText;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvText = (TextView) itemView.findViewById(R.id.tv_text);
        }
    }
}


