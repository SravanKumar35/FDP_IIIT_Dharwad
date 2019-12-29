package org.iiitdharwad.navigation.schedule_code;

import android.view.View;
import android.widget.TextView;

import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder;

import org.iiitdharwad.navigation.MainActivity;
import org.iiitdharwad.navigation.R;

public class scheduleViewHolder extends ChildViewHolder {

    private TextView timePeriodView, speakerView, speakerInfoView, topicView;

    public scheduleViewHolder(View itemView) {
        super(itemView);
        timePeriodView = itemView.findViewById(R.id.schedule_content_time);
        speakerView = itemView.findViewById(R.id.schedule_content_speaker);
        speakerInfoView = itemView.findViewById(R.id.schedule_content_speaker_info);
        topicView = itemView.findViewById(R.id.schedule_content_topic);
    }

    public void bind(scheduleValue sv){
        timePeriodView.setText(sv.startTime + " - " + sv.endTime);
        speakerInfoView.setVisibility(View.VISIBLE);
        topicView.setVisibility(View.VISIBLE);
        speakerView.setText(sv.speaker);
        if(sv.speaker.equals("Inaugration") || sv.speaker.equals("Valedictory")){
            speakerInfoView.setTypeface(MainActivity.tf3);
            speakerInfoView.setTextSize(16);
            speakerInfoView.setPaddingRelative(0, 10, 0, 0);
        }
        if (sv.speakerInfo.equals("")){
            speakerInfoView.setVisibility(View.GONE);
        }
        else{
            speakerInfoView.setText(sv.speakerInfo);
            speakerInfoView.setVisibility(View.VISIBLE);
        }
        if(sv.topic.equals("")){
            topicView.setVisibility(View.GONE);
        }
        else {
            topicView.setText("TOPIC: " + sv.topic);
            topicView.setVisibility(View.VISIBLE);
        }
    }

}
