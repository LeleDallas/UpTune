package com.uptune.Search;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.uptune.Adapter.SongAdapter;
import com.uptune.R;
import com.uptune.Song.SongList;
import com.uptune.Web;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

public class SearchTracks extends Fragment {
    String name;
    ImageView bgImg;
    JSONArray arr;
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    ArrayList<SongList> cardContainers = new ArrayList<>();

    public SearchTracks(String name) {
        this.name = name;
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        recyclerView = view.findViewById(R.id.search_tracks_list);
        bgImg = view.findViewById(R.id.search_tracks_img);
        androidx.appcompat.widget.Toolbar toolbar = view.findViewById(R.id.search_tracks_toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(v -> getActivity().onBackPressed());
        toolbar.setTitle(name);
        renderCards(view);
        super.onViewCreated(view, savedInstanceState);
    }

    private void renderCards(View v) {
        try {
            arr = Web.getTracksFromName(name);
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        try {
            for (int i = 0; i < arr.length(); i++) {
                //popularity
                String name = arr.getJSONObject(i).getString("name");
                String id = arr.getJSONObject(i).getString("id");
                URL img = new URL(arr.getJSONObject(i).getJSONObject("album").getJSONArray("images").getJSONObject(0).getString("url"));
                if (i == 0) {
                    Bitmap bitmap = BitmapFactory.decodeStream((InputStream) img.getContent());
                    bgImg.setImageBitmap(bitmap);
                }
                String artists = "";
                for (int j = 0; j < arr.getJSONObject(i).getJSONArray("artists").length(); j++) {
                    String artistName = arr.getJSONObject(i).getJSONArray("artists").getJSONObject(j).getString("name");
                    if (j == arr.getJSONObject(i).getJSONArray("artists").length() - 1)
                        artists += artistName;
                    else
                        artists += artistName + ", ";
                }
                cardContainers.add(new SongList(name, id, img, artists));
            }
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        final int idV = View.generateViewId();
        v.setId(idV);
        adapter = new SongAdapter(cardContainers, idV);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 1, GridLayoutManager.VERTICAL, false);
        this.recyclerView.setLayoutManager(gridLayoutManager);
        this.recyclerView.setAdapter(adapter);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search_tracks, container, false);
    }
}