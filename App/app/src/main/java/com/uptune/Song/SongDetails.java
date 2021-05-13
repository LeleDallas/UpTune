package com.uptune.Song;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.UrlQuerySanitizer;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import com.uptune.R;
import com.uptune.Web;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import kaaes.spotify.webapi.android.models.Track;

public class SongDetails extends Fragment {
    private String title;
    private String id;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_chart, container, false);
        return root;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final Toolbar toolbar = view.findViewById(R.id.toolbar_song_details);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(v -> getFragmentManager().popBackStack());

        final Track track = Web.getSong(id);
        toolbar.setTitle(track.name);

        try {
            final String imagePath = track.album.images.get(0).url;
            final URL imageUrl = new URL(imagePath);
            Bitmap image = BitmapFactory.decodeStream(imageUrl.openConnection().getInputStream());
            //
            //  *********   SET IMAGE INTO IMAGE VIEW   *********
            //
        } catch (MalformedURLException e) {
            Log.e("ERROR", "No image found " + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            Log.e("ERROR", "No image found " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.title = getArguments().getString("title");
            this.id = getArguments().getString("url");
        }
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param title Title of the chart.
     * @param id   Url of the playlist.
     * @return A new instance of fragment charts_selector.
     */
    // TODO: Rename and change types and number of parameters
    public static SongDetails newInstance(String title, String id) {
        SongDetails fragment = new SongDetails();
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putString("id", id);
        fragment.setArguments(args);
        return fragment;
    }
}
