package br.lauriavictor.wmb.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import br.lauriavictor.wmb.R;

public class CustomWindowViewAdapter implements GoogleMap.InfoWindowAdapter {

    private final View mWindow;
    private Context context;

    public CustomWindowViewAdapter(Context context) {
        this.context = context;
        mWindow = LayoutInflater.from(context).inflate(R.layout.custom_info_window, null);
    }

    //Setando texto para a view de informação
    private void setWindowText(Marker marker, View view) {
        String title = marker.getTitle();
        TextView tvTitle = (TextView) view.findViewById(R.id.titleInfo);

        if(!title.equals("")) {
            tvTitle.setText(title);
        }
        String snippet = marker.getSnippet();
        TextView tvSnippet = (TextView) view.findViewById(R.id.snippet);

        if(!snippet.equals("")) {
            tvSnippet.setText(snippet);
        }
    }

    @Override
    public View getInfoWindow(Marker marker) {
        setWindowText(marker, mWindow);
        return mWindow;
    }

    @Override
    public View getInfoContents(Marker marker) {
        setWindowText(marker, mWindow);
        return mWindow;
    }
}
