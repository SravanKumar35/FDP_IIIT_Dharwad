package org.iiitdharwad.navigation.schedule_code;

import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.List;

public class scheduleDateValue extends ExpandableGroup<scheduleValue> {

    public scheduleDateValue(String title, List<scheduleValue> items) {
        super(title, items);
    }

    @Override
    public String getTitle() {
        return super.getTitle();
    }
}