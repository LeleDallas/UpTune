package com.uptune.Catalog;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.uptune.Helper.CardAdapter;
import com.uptune.Helper.SongAdapter;
import com.uptune.Helper.CardContainer;
import com.uptune.R;
import com.uptune.Web;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class Catalog extends Fragment {
    RecyclerView bestCateg, mostList, mostList2;
    RecyclerView.Adapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_catalog, container, false);
        return root;
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bestCateg = view.findViewById(R.id.bestCateg);
        mostList = view.findViewById(R.id.mostList);
        mostList2 = view.findViewById(R.id.mostList2);
        renderBestCateg();
    }

    private void renderBestCateg() {
        bestCateg.setHasFixedSize(true);
        bestCateg.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        mostList.setHasFixedSize(true);
        mostList.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        mostList2.setHasFixedSize(true);
        mostList2.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        ArrayList<CardContainer> setBestCat = new ArrayList<>();
        ArrayList<CardContainer> setArtistCard = new ArrayList<>();
        ArrayList<CardContainer> setNewRelease = new ArrayList<>();

        try {
            JSONArray arr = Web.getCategories();
            for (int i = 0; i < arr.length(); i++) {
                JSONObject current = arr.getJSONObject(i);
                String name = current.getString("name");
                String id = current.getString("id");
                URL img = new URL(current.getJSONArray("icons").getJSONObject(0).getString("url"));
                setBestCat.add(new CardContainer(name, img, id));
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        try {
            JSONArray arr = Web.getArtistId();
            JSONObject current = arr.getJSONObject(0);
            for (int i = 0; i < 20; i++) {
                String id = current.getJSONObject(i + "").getString("id");
                //prendo img e dati qui
                JSONObject artist = Web.getArtist(id);
                String name = artist.getString("name");
                String popularity = artist.getString("popularity");
                URL img = new URL(artist.getJSONArray("images").getJSONObject(0).getString("url"));
                setNewRelease.add(new CardContainer(name, img, id, popularity));
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        try {
            JSONArray arr = Web.getNewRelease();
            for (int i = 0; i < arr.length(); i++) {
                JSONObject current = arr.getJSONObject(i);
                String name = current.getString("name");
                String id = current.getString("id");
                URL img = new URL(current.getJSONArray("images").getJSONObject(0).getString("url"));
                setArtistCard.add(new CardContainer(name, img, id));
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        adapter = new CardAdapter(setArtistCard, 0);
        bestCateg.setAdapter(adapter);
        adapter = new CardAdapter(setNewRelease, 1);
        mostList.setAdapter(adapter);
        adapter = new CardAdapter(setBestCat, 2);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false);
        mostList2.setLayoutManager(gridLayoutManager);
        mostList2.setAdapter(adapter);
    }

}