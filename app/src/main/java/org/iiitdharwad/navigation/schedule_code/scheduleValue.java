package org.iiitdharwad.navigation.schedule_code;

import android.os.Parcel;
import android.os.Parcelable;

public class scheduleValue implements Parcelable {

    public final String startTime, endTime, speaker, speakerInfo, topic;


    public scheduleValue(String startTime, String endTime, String speaker, String speakerInfo, String topic) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.speaker = speaker;
        this.speakerInfo = speakerInfo;
        this.topic = topic;
    }

    protected scheduleValue(Parcel in) {
        startTime = in.readString();
        endTime = in.readString();
        speaker = in.readString();
        speakerInfo = in.readString();
        topic = in.readString();
    }

    public static final Creator<scheduleValue> CREATOR = new Creator<scheduleValue>() {
        @Override
        public scheduleValue createFromParcel(Parcel in) {
            return new scheduleValue(in);
        }

        @Override
        public scheduleValue[] newArray(int size) {
            return new scheduleValue[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(startTime);
        parcel.writeString(endTime);
        parcel.writeString(speaker);
        parcel.writeString(speakerInfo);
        parcel.writeString(topic);
    }
}
