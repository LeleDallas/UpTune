package com.uptune.Catalog;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.uptune.Adapter.SongAdapter;
import com.uptune.R;
import com.uptune.Song.SongList;
import com.uptune.Web;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;


public class CardDetails extends Fragment {
    private String id, title;
    RecyclerView songList;
    RecyclerView.Adapter adapter;
    URL img;

    public CardDetails(String title, URL img, String id) {
        this.id = id;
        this.title = title;
        this.img = img;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        androidx.appcompat.widget.Toolbar toolbar = view.findViewById(R.id.toolbar_card_details);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(v -> getActivity().onBackPressed());
        toolbar.setTitle(title);

        Fragment fragment = getActivity().getSupportFragmentManager().findFragmentByTag(String.valueOf(R.id.categories_details));
        if (fragment != null)
            getActivity().getSupportFragmentManager().beginTransaction().remove(fragment).commit();

        ImageView imgView = view.findViewById(R.id.details_song_img);

        try {
            imgView.setImageBitmap(BitmapFactory.decodeStream(img.openStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        songList = view.findViewById(R.id.details_song_list);
        songList.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 1, GridLayoutManager.VERTICAL, false);
        ArrayList<SongList> setData = new ArrayList<>();

        try {
            JSONArray arr = Web.getAlbum(id);
            for (int i = 0; i < arr.length(); i++) {
                JSONObject current = arr.getJSONObject(i);
                String name = current.getString("name");
                String id = current.getString("id");
                String artists = "";
                for (int j = 0; j < current.getJSONArray("artists").length(); j++) {
                    String artistName=current.getJSONArray("artists").getJSONObject(j).getString("name");
                    if (j == current.getJSONArray("artists").length()-1)
                        artists += artistName;
                    else
                        artists +=artistName +", ";
                }

                setData.add(new SongList(name, id, img, artists));
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        final int idV = View.generateViewId();
        view.setId(idV);
        adapter = new SongAdapter(setData, idV);
        songList.setLayoutManager(gridLayoutManager);
        songList.setAdapter(adapter);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_card_details, container, false);
    }
}