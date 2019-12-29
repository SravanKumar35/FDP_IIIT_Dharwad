package org.iiitdharwad.navigation.schedule_code;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import org.iiitdharwad.navigation.R;

import java.util.List;

public class ScheduleAdapter extends ExpandableRecyclerViewAdapter<scheduleDateViewHolder, scheduleViewHolder> {

    public ScheduleAdapter(List<? extends ExpandableGroup> groups) {
        super(groups);
    }

    @Override
    public scheduleDateViewHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.schedule_title_item, parent, false);
        return new scheduleDateViewHolder(v, parent.getContext());
    }

    @Override
    public scheduleViewHolder onCreateChildViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.schedule_content_item, parent, false);
        return new scheduleViewHolder(v);
    }

    @Override
    public void onBindChildViewHolder(scheduleViewHolder holder, int flatPosition, ExpandableGroup group, int childIndex) {
        final scheduleValue sv = (scheduleValue) group.getItems().get(childIndex);
        int pos = getItemViewType(childIndex);
        holder.bind(sv);
    }

    @Override
    public void onBindGroupViewHolder(scheduleDateViewHolder holder, int flatPosition, ExpandableGroup group) {
        final scheduleDateValue sdv = (scheduleDateValue) group;
        holder.bind(sdv);

    }

    @Override
    public int getItemCount(){return super.getItemCount();}

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }


}
