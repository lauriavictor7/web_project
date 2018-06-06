package br.lauriavictor.wmb.model;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

import br.lauriavictor.wmb.R;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

    Context context;
    List<PlaceInfo> placeInfos;

    public RecyclerViewAdapter(Context context, List<PlaceInfo> placeInfos) {
        this.context = context;
        this.placeInfos = placeInfos;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(context).inflate(R.layout.item_place, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.mName.setText(placeInfos.get(position).getName());
        holder.mAddress.setText(placeInfos.get(position).getAddress());
        holder.mPhoneNumber.setText(placeInfos.get(position).getPhoneNumber());
    }

    @Override
    public int getItemCount() {
        return placeInfos.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView mName;
        private TextView mPhoneNumber;
        private TextView mAddress;

        public MyViewHolder(View itemView) {
            super(itemView);

            mName = (TextView) itemView.findViewById(R.id.namePlaceList);
            mPhoneNumber = (TextView) itemView.findViewById(R.id.phonePlaceList);
            mAddress = (TextView)  itemView.findViewById(R.id.addressPlaceList);
        }
    }
}
