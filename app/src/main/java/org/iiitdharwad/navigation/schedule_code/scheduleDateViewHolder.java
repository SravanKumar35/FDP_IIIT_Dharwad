package org.iiitdharwad.navigation.schedule_code;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder;

import org.iiitdharwad.navigation.R;

import static android.view.animation.Animation.RELATIVE_TO_SELF;

public class scheduleDateViewHolder extends GroupViewHolder {

    TextView dateView;
    ImageView arrow;

    public scheduleDateViewHolder(final View itemView, final Context context) {
        super(itemView);
        dateView = itemView.findViewById(R.id.schedule_title);
        arrow = itemView.findViewById(R.id.arrowImg);
        arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Please Tap On Date To View", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void bind(scheduleDateValue sdv){
        dateView.setText(sdv.getTitle());
    }


    @Override
    public void expand() {
        animateExpand();
    }
//
    @Override
    public void collapse() {
//        arrow.setImageResource(MainActivity.arrowDown);
        animateCollapse();

    }

    private void animateExpand() {
        RotateAnimation rotate =
                new RotateAnimation(360, 180, RELATIVE_TO_SELF, 0.5f, RELATIVE_TO_SELF, 0.5f);
        rotate.setStartOffset(0);
        rotate.setDuration(1000);
        rotate.setFillAfter(true);

        arrow.startAnimation(rotate);
    }

    private void animateCollapse() {
//        arrow.setImageResource(MainActivity.arrowUp);
        RotateAnimation rotate =
                new RotateAnimation(180, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(1000);
        rotate.setFillAfter(true);

        arrow.startAnimation(rotate);
    }
}

