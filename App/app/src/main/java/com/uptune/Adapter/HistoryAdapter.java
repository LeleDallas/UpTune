package com.uptune.Adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.uptune.Adapter.Card.HistoryElement;
import com.uptune.Artist.ArtistStuff;
import com.uptune.History.History;
import com.uptune.R;
import com.uptune.Used.Tag;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.FeatureViewHolder> {

    private List<HistoryElement> items;
    private int[] colors;

    //template method
    public void onClick(View view, HistoryAdapter.FeatureViewHolder fvh, HistoryElement s) {

    }

    public HistoryAdapter(List<HistoryElement> items) { this.items = items; }

    @NotNull
    @Override
    public FeatureViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_history, parent, false);
        final HistoryAdapter.FeatureViewHolder fvh = new HistoryAdapter.FeatureViewHolder(view);

        view.setOnClickListener(e -> {
            int position = fvh.getAdapterPosition();
            HistoryElement s = items.get(position);
            // template method actuation
            onClick(view, fvh, s);
        });

        return fvh;
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryAdapter.FeatureViewHolder holder, int position) {
        HistoryElement element = items.get(position);

        holder.name.setText(element.getName());
        holder.date.setText(element.getDate());
        holder.price.setText("€" + element.getPrice());
        holder.constraintLayout.setBackgroundColor(element.getColor());

        try {
            final Bitmap image = BitmapFactory.decodeStream(element.getImg().openStream());
            holder.img.post(() -> holder.img.setImageBitmap(image));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class FeatureViewHolder extends RecyclerView.ViewHolder {
        TextView name, date, price;
        ImageView img;
        View cardView;
        ConstraintLayout constraintLayout;

        public FeatureViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.historyName);
            date = itemView.findViewById(R.id.historyDate);
            price = itemView.findViewById(R.id.historyPrice);
            img = itemView.findViewById(R.id.historyImg);
            constraintLayout = itemView.findViewById(R.id.historyConstraintLayout1);
//            cardView = itemView.findViewById(R.id.historyCard);
        }
    }
}
