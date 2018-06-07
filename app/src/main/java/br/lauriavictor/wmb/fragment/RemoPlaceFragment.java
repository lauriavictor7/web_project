package br.lauriavictor.wmb.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import br.lauriavictor.wmb.R;
import br.lauriavictor.wmb.model.DatabaseController;
import br.lauriavictor.wmb.model.PlaceInfo;

public class RemoPlaceFragment extends Fragment {

    private View view;
    private Button mButtonRemovePlace;
    private EditText mPlaceItem;
    private DatabaseController mDatabaseController;
    private PlaceInfo mPlaceInfo;


    public RemoPlaceFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.remove_place_fragment, container, false);

        mButtonRemovePlace = (Button) view.findViewById(R.id.buttonRemove);
        mPlaceItem = (EditText) view.findViewById(R.id.editTextPlaceItem);

        mButtonRemovePlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    mDatabaseController = new DatabaseController(getContext());
                    mPlaceInfo = new PlaceInfo();
                    mPlaceInfo.id = Integer.parseInt(String.valueOf(mPlaceItem.getText()));
                    mDatabaseController.removePlace(mPlaceInfo);
                    Toast.makeText(getContext(), "Local removido!", Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    Toast.makeText(getContext(), "Erro ao remover local!", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        });
        return view;
    }

}
