package br.lauriavictor.wmb.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import br.lauriavictor.wmb.R;
import br.lauriavictor.wmb.model.DatabaseController;
import br.lauriavictor.wmb.model.RecyclerViewAdapter;
import br.lauriavictor.wmb.model.User;

public class ListUserFragment extends Fragment {

    private View view;
    private RecyclerView recyclerView;
    private List<User> userList;

    public ListUserFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.user_fragment, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerViewUser);
        RecyclerViewAdapter recyclerViewAdapter = new RecyclerViewAdapter(getContext(), userList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(recyclerViewAdapter);
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DatabaseController databaseController = new DatabaseController(getContext());
        this.userList = databaseController.listAll();
    }
}
