package org.iiitdharwad.navigation.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.iiitdharwad.navigation.Objects.FDPValue;
import org.iiitdharwad.navigation.R;

import java.util.List;

public class RecyclerViewCardViewAdapter extends RecyclerView.Adapter<RecyclerViewCardViewAdapter.ViewHolder> {

    Context context;
    public TextView fdpItem;

    List<FDPValue> fdpList;

    public RecyclerViewCardViewAdapter(List<FDPValue> getDataAdapter, Context context){

        super();

        this.fdpList = getDataAdapter;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        FDPValue data = fdpList.get(position);

        fdpItem.setText(data.getFDPName());
    }

    @Override
    public int getItemCount() {

        return fdpList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        Context c;


        public ViewHolder(View itemView) {
            super(itemView);
            c = itemView.getContext();
            fdpItem = itemView.findViewById(R.id.cardViewFDP);

        }

    }


}
