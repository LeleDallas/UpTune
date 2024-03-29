package com.uptune.Chart;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.uptune.R;
import com.uptune.Web;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

public class Chart extends Fragment {
    private RecyclerView recyclerView;
    private String title;
    private String playlistUrl;
    String img;
    ImageView imgBg;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_chart, container, false);
        return root;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final Toolbar toolbar = view.findViewById(R.id.toolbar_charts);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(v -> getFragmentManager().popBackStack());
        toolbar.setTitle(this.title);
        imgBg = view.findViewById(R.id.charts_img);
        try {
            Bitmap bitmap = BitmapFactory.decodeStream((InputStream) new URL(img).getContent());
            imgBg.setImageBitmap(bitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }
        recyclerView = view.findViewById(R.id.topTracksGlobal);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setItemViewCacheSize(20);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        final List<ChartItem> items = Web.getCachedPlaylist(this.playlistUrl);

        final int id = View.generateViewId();
        view.setId(id);
        this.recyclerView.setAdapter(new ChartAdapter(items, id));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.title = getArguments().getString("title");
            this.playlistUrl = getArguments().getString("url");
            this.img = getArguments().getString("img");
        }
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param title Title of the chart.
     * @param url   Url of the playlist.
     * @return A new instance of fragment charts_selector.
     */
    // TODO: Rename and change types and number of parameters
    public static Chart newInstance(String title, String url, String img) {
        Chart fragment = new Chart();
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putString("url", url);
        args.putString("img", img);

        fragment.setArguments(args);
        return fragment;
    }
}