package org.iiitdharwad.navigation.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import org.iiitdharwad.navigation.R;
import org.iiitdharwad.navigation.schedule_code.scheduleValue;

import java.util.ArrayList;

public class RecyclerViewNotificationViewAdapter extends RecyclerView.Adapter<RecyclerViewNotificationViewAdapter.ViewHolderNotification> {

    Context context;
    public TextView timeView, titleView, descriptionView;

    ArrayList<scheduleValue> notificationArrayList;
    public RecyclerViewNotificationViewAdapter(ArrayList<scheduleValue> notificationsValues, Context c){
        super();
        this.context = c;
        this.notificationArrayList =notificationsValues;
    }

    @NonNull
    @Override
    public RecyclerViewNotificationViewAdapter.ViewHolderNotification onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_item_view, parent, false);

        ViewHolderNotification viewHolder = new ViewHolderNotification(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderNotification viewHolderNotification, int i) {
        scheduleValue data = notificationArrayList.get(i);
        int pos = getItemViewType(i);
        timeView.setText(data.startTime);
        titleView.setText(data.speaker);
        descriptionView.setText(data.topic);
//        linearLayout.setBackground();
    }

    @Override
    public int getItemCount() {
        return notificationArrayList.size();
    }

    @Override
    public int getItemViewType(int position){
        return position;
    }

    public class ViewHolderNotification extends RecyclerView.ViewHolder{

        Context c;


        public ViewHolderNotification(View itemView) {
            super(itemView);
            c = itemView.getContext();
            titleView = itemView.findViewById(R.id.notification_content_title);
            timeView = itemView.findViewById(R.id.notification_content_time);
            descriptionView = itemView.findViewById(R.id.notification_content_description);
        }
    }
}