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
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import br.lauriavictor.wmb.R;
import br.lauriavictor.wmb.model.FetchData;
import br.lauriavictor.wmb.model.RecyclerViewAdapter;

public class SugestionPlacesFragment extends Fragment {

    private View view;
    private Button mLoadSugestionButton;
    public static TextView mContent;

    public SugestionPlacesFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.sugestion_fragment, container, false);

        mLoadSugestionButton = (Button) view.findViewById(R.id.buttonShowJson);
        mContent = (TextView) view.findViewById(R.id.jsonContent);

        mLoadSugestionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FetchData fetchData = new FetchData();
                fetchData.execute();
            }
        });

        return view;
    }
}
