package org.iiitdharwad.navigation.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import org.iiitdharwad.navigation.Objects.FDPValue;
import org.iiitdharwad.navigation.R;

import java.util.ArrayList;

public class RecyclerViewPersonViewAdapter extends RecyclerView.Adapter<RecyclerViewPersonViewAdapter.ViewHolderPerson> {

    Context context;
    public TextView personNameView, companyView, headerView;
    public ImageView personImage;
    public LinearLayout cardLayout;
    public android.support.v7.widget.CardView cardView;

    int resId;
    ArrayList<FDPValue.resourcePersonValue> resourcePersonValueArrayList;
    public RecyclerViewPersonViewAdapter(ArrayList<FDPValue.resourcePersonValue> resourcePersonValues, Context c, int resId){
        super();

        this.resId = resId;
        this.context = c;
        this.resourcePersonValueArrayList =resourcePersonValues;
    }

    @NonNull
    @Override
    public RecyclerViewPersonViewAdapter.ViewHolderPerson onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.person_card_view, parent, false);

        ViewHolderPerson viewHolder = new ViewHolderPerson(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderPerson viewHolderPerson, int i) {
        FDPValue.resourcePersonValue data = resourcePersonValueArrayList.get(i);
        int pos = getItemViewType(i);

        if(data.getCategory().equals("header")){
            headerView.setVisibility(View.VISIBLE);
            headerView.setText(data.getHeading());
//            cardView.setVisibility(View.GONE);
            cardLayout.setVisibility(View.GONE);
            personImage.setImageDrawable(null);
        }
        else{
            headerView.setVisibility(View.GONE);
            personNameView.setText(data.getPersonName());
            companyView.setText(data.getCompany());
            if(data.getResId() != 0){
                personImage.setImageResource(data.getResId());
            }
            else {
                personImage.setImageResource(resId);
            }
        }
    }

    @Override
    public int getItemCount() {
        return resourcePersonValueArrayList.size();
    }

    @Override
    public int getItemViewType(int position){
        return position;
    }

    public class ViewHolderPerson extends RecyclerView.ViewHolder implements View.OnClickListener {

        Context c;


        public ViewHolderPerson(View itemView) {
            super(itemView);
            c = itemView.getContext();
            personNameView = itemView.findViewById(R.id.resource_person_name);
            companyView = itemView.findViewById(R.id.resource_person_company);
            personImage = itemView.findViewById(R.id.resource_person_image);
            headerView = itemView.findViewById(R.id.resource_person_heading);
            cardLayout = itemView.findViewById(R.id.resource_person_layout);
            cardView = itemView.findViewById(R.id.icon_resource_person_card);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
        }
    }
}

