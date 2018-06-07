package br.lauriavictor.wmb.model;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import java.util.List;

import br.lauriavictor.wmb.R;


public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

    private final static String TAG = "RecyclerViewAdaper";

    Context context;
    List<PlaceInfo> placeInfos;
    Dialog mDialog;

    public RecyclerViewAdapter(Context context, List<PlaceInfo> placeInfos) {
        this.context = context;
        this.placeInfos = placeInfos;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(context).inflate(R.layout.item_place, parent, false);
        final MyViewHolder myViewHolder = new MyViewHolder(view);

        Log.d(TAG, "onCreateViewHolder: inicializando viewholder e dialog");

        mDialog = new Dialog(context);
        mDialog.setContentView(R.layout.dialog_places);
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        myViewHolder.mItemPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView mNameDialog = (TextView) mDialog.findViewById(R.id.dialog_place_name);
                TextView mPhoneDialog = (TextView) mDialog.findViewById(R.id.dialog_place_phone);
                TextView mAddressDialog = (TextView) mDialog.findViewById(R.id.dialog_place_address);
                mNameDialog.setText(placeInfos.get(myViewHolder.getAdapterPosition()).getName());
                mPhoneDialog.setText(placeInfos.get(myViewHolder.getAdapterPosition()).getPhoneNumber());
                mAddressDialog.setText(placeInfos.get(myViewHolder.getAdapterPosition()).getAddress());

                Log.d(TAG, "onClick: chamando dialog");
                mDialog.show();
            }
        });
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

        private LinearLayout mItemPlace;
        private TextView mName;
        private TextView mPhoneNumber;
        private TextView mAddress;

        public MyViewHolder(View itemView) {
            super(itemView);

            mItemPlace = (LinearLayout) itemView.findViewById(R.id.place_item);
            mName = (TextView) itemView.findViewById(R.id.namePlaceList);
            mPhoneNumber = (TextView) itemView.findViewById(R.id.phonePlaceList);
            mAddress = (TextView)  itemView.findViewById(R.id.addressPlaceList);
        }
    }
}
