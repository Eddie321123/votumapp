package com.votum.app;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class NotificationRecyclerViewAdapter extends RecyclerView.Adapter<NotificationRecyclerViewAdapter.ViewHolder> {
    private static final String TAG = "RecyclerView Adapter";

    private ArrayList<String> mInformation;
    private ArrayList<String> mTime;
    private ArrayList<String> mTitle;

    public NotificationRecyclerViewAdapter(ArrayList<String> information, ArrayList<String> time, ArrayList<String> title) {
        mInformation = information;
        mTime = time;
        mTitle = title;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification, parent , false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called");

        holder.information.setText(mInformation.get(position));
        holder.time.setText(mTime.get(position));
        holder.title.setText(mTitle.get(position));
    }

    @Override
    public int getItemCount() {
        return mInformation.size();

    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView information;
        TextView time;
        TextView title;

        public ViewHolder(View itemView) {
            super(itemView);
            information = itemView.findViewById(R.id.information);
            time = itemView.findViewById(R.id.time);
            title = itemView.findViewById(R.id.title);

        }
    }
}
