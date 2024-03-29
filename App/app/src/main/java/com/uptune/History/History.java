package com.uptune.History;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.uptune.Adapter.Card.HistoryElement;
import com.uptune.Adapter.HistoryAdapter;
import com.uptune.R;
import com.uptune.SessionAccount;
import com.uptune.Used.Tag;
import com.yalantis.filter.adapter.FilterAdapter;
import com.yalantis.filter.listener.FilterListener;
import com.yalantis.filter.widget.Filter;
import com.yalantis.filter.widget.FilterItem;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class History extends AppCompatActivity {
    private ArrayList<HistoryElement> setCards = new ArrayList<>();
    private ArrayList<HistoryElement> defaultCards = new ArrayList<>();
    private String[] mTitles = {"Default", "Newest", "Oldest", "Album", "Song", "Used", "Price"};
    private int[] mColors;
    ImageButton btnBack;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private Filter<Tag> mFilter;
    private FilterListener<Tag> filterListener;
    ArrayList<Tag> oldTags = new ArrayList<>();

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        btnBack = findViewById(R.id.review_back);
        btnBack.setOnClickListener(v -> finish());
        this.recyclerView = findViewById(R.id.historyRecyclerView);
        this.mColors = getResources().getIntArray(R.array.colors);
        this.mFilter = findViewById(R.id.historyFilter);

        this.filterListener = new MyFilterAdapter();
        mFilter.setAdapter(new History.Adapter(getTags()));
        mFilter.setListener(this.filterListener);
        mFilter.setNoSelectedItemText("All Items");
        mFilter.build();
        getData();
    }


    private void getData() {
        FirebaseDatabase rootNode = FirebaseDatabase.getInstance();
        DatabaseReference reference = rootNode.getReference("history").child(SessionAccount.getUsername());
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                setCards = new ArrayList<>();
                for (DataSnapshot d : dataSnapshot.getChildren()) {
                    HistoryElement ele = d.getValue(HistoryElement.class);
                    ele.setId(d.getKey());
                    setCards.add(ele);
                    defaultCards.add(ele);

                }
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
                recyclerView.setItemViewCacheSize(20);
                recyclerView.setDrawingCacheEnabled(true);
                recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
                adapter = new HistoryAdapter(setCards);
                recyclerView.setAdapter(adapter);
                filterListener.onNothingSelected();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }


    private List<Tag> getTags() {
        List<Tag> tags = new ArrayList<>();
        for (int i = 0; i < mTitles.length; ++i)
            tags.add(new Tag(mTitles[i], mColors[i]));
        return tags;
    }

    private class Adapter extends FilterAdapter<Tag> {

        Adapter(@NotNull List<? extends Tag> items) {
            super(items);
        }

        @NotNull
        @Override
        public FilterItem createView(int position, Tag item) {
            FilterItem filterItem = new FilterItem(getApplicationContext());
            filterItem.setStrokeColor(mColors[0]);
            filterItem.setTextColor(mColors[0]);
            filterItem.setCornerRadius(14);
            filterItem.setCheckedTextColor(ContextCompat.getColor(getApplicationContext(), android.R.color.white));
            filterItem.setColor(ContextCompat.getColor(getApplicationContext(), android.R.color.white));
            filterItem.setCheckedColor(mColors[position]);
            filterItem.setText(item.getText());
            filterItem.deselect();
            return filterItem;
        }
    }

    class MyFilterAdapter implements FilterListener<Tag> {
        @Override
        public void onFilterDeselected(Tag tag) {

        }

        @Override
        public void onFilterSelected(Tag tag) {
            switch (tag.getText()) {
                case "Default":
                    mFilter.deselectAll();
                    mFilter.collapse();
                    setCards.clear();
                    setCards.addAll(defaultCards);
                    adapter.notifyDataSetChanged();
                    break;
            }
        }

        @Override
        public void onFiltersSelected(@NotNull ArrayList<Tag> arrayList) {
            if (arrayList == oldTags)
                return;
            oldTags = arrayList;
            setCards.clear();

            if ((arrayList.contains("Song")) && (arrayList.contains("Used")) && (arrayList.contains("Album")))
                for (HistoryElement el : defaultCards) {
                    setCards.add(el);
                }
            else {
                for (Tag tag : arrayList) {
                    for (HistoryElement el : defaultCards) {
                        if (el.getType().equalsIgnoreCase(tag.getText())) {
                            setCards.add(el);
                        }
                    }
                }
            }
            for (Tag tag : arrayList) {
                switch (tag.getText()) {
                    case "Oldest":
                        Collections.sort(setCards, HistoryElement.dateComparatorOldest);
                        break;
                    case "Newest":
                        Collections.sort(setCards, HistoryElement.dateComparatorNewest);
                        break;
                    case "Price":
                        Collections.sort(setCards);
                        break;
                }
            }

            adapter.notifyDataSetChanged();
        }

        @Override
        public void onNothingSelected() {
            if (adapter != null) {
                setCards.clear();
                setCards.addAll(defaultCards);
                adapter.notifyDataSetChanged();
            }
        }

    }
}