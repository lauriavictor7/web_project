package br.lauriavictor.wmb.fragment;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import br.lauriavictor.wmb.R;
import br.lauriavictor.wmb.model.DatabaseController;
import br.lauriavictor.wmb.model.DatabaseHelper;
import br.lauriavictor.wmb.model.PlaceInfo;
import br.lauriavictor.wmb.model.RecyclerViewAdapter;

public class ListPlaceFragment extends Fragment {

    private View view;
    private RecyclerView recyclerView;
    private List<PlaceInfo> placeInfoList;

    public ListPlaceFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.list_fragment, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerViewPlace);
        RecyclerViewAdapter recyclerViewAdapter = new RecyclerViewAdapter(getContext(), placeInfoList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(recyclerViewAdapter);
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DatabaseController databaseController = new DatabaseController(getContext());
        this.placeInfoList = databaseController.listAllPlaces();
    }
}
