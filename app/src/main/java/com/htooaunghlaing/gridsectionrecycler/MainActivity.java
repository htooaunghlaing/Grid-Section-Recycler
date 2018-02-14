package com.htooaunghlaing.gridsectionrecycler;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.htooaunghlaing.gridsectionrecycler.R;
import com.htooaunghlaing.gridsectionrecycler.util.Constants;
import com.htooaunghlaing.gridsectionrecycler.util.EqualSpacingItemDecoration;
import com.htooaunghlaing.gridsectionrecycler.util.Utality;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;
import io.github.luizgrp.sectionedrecyclerviewadapter.StatelessSection;

public class MainActivity extends AppCompatActivity {

    private SectionedRecyclerViewAdapter sectionAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sectionAdapter = new SectionedRecyclerViewAdapter();

        sectionAdapter.addSection(new MovieSection(getString(R.string.top_rated_movies_topic), getTopRatedMoviesList()));
        sectionAdapter.addSection(new MovieSection(getString(R.string.most_popular_movies_topic), getMostPopularMoviesList()));

        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.recyclerview);

        GridLayoutManager glm = new GridLayoutManager(MainActivity.this, Constants.NUM_OF_COLUMN);
        glm.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                switch(sectionAdapter.getSectionItemViewType(position)) {
                    case SectionedRecyclerViewAdapter.VIEW_TYPE_HEADER:
                        return Constants.NUM_OF_COLUMN;
                    default:
                        return 1;
                }
            }
        });

        if(glm.getSpanCount() != SectionedRecyclerViewAdapter.VIEW_TYPE_HEADER){
            recyclerView.addItemDecoration(new EqualSpacingItemDecoration(10, EqualSpacingItemDecoration.GRID));
        }

        recyclerView.setLayoutManager(glm);
        recyclerView.setAdapter(sectionAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (MainActivity.this instanceof AppCompatActivity) {
            AppCompatActivity activity = ((AppCompatActivity) MainActivity.this);
            if (activity.getSupportActionBar() != null)
                activity.getSupportActionBar().setTitle(getString(R.string.app_name));
        }
    }

    private List<Movie> getTopRatedMoviesList() {
        List<String> arrayList = new ArrayList<>(Arrays.asList(getResources()
                .getStringArray(R.array.top_rated_movies)));

        List<Movie> movieList = new ArrayList<>();

        for (String str : arrayList) {
            String[] array = str.split("\\|");
            movieList.add(new Movie(array[0], array[1]));
        }

        return movieList;
    }

    private List<Movie> getMostPopularMoviesList() {
        List<String> arrayList = new ArrayList<>(Arrays.asList(getResources()
                .getStringArray(R.array.most_popular_movies)));

        List<Movie> movieList = new ArrayList<>();

        for (String str : arrayList) {
            String[] array = str.split("\\|");
            movieList.add(new Movie(array[0], array[1]));
        }

        return movieList;
    }

    private class MovieSection extends StatelessSection {

        String title;
        List<Movie> list;

        MovieSection(String title, List<Movie> list) {
            super(new SectionParameters.Builder(R.layout.section_ex5_item)
                    .headerResourceId(R.layout.section_ex5_header)
                    .build());

            this.title = title;
            this.list = list;
        }

        @Override
        public int getContentItemsTotal() {
            return list.size();
        }

        @Override
        public RecyclerView.ViewHolder getItemViewHolder(View view) {
            ItemViewHolder holder =  new ItemViewHolder(view);
            view.getLayoutParams().height = (int) (Utality.getDeviceWidth(MainActivity.this) / Constants.NUM_OF_COLUMN * Constants.ITEM_RATIO);
            return holder;
        }

        @Override
        public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {
            final ItemViewHolder itemHolder = (ItemViewHolder) holder;

            String name = list.get(position).getName();

            itemHolder.tvItem.setText(name);
            itemHolder.rootView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(MainActivity.this, String.format("Clicked on position #%s of Section %s",
                            sectionAdapter.getPositionInSection(itemHolder.getAdapterPosition()), title),
                            Toast.LENGTH_SHORT).show();
                }
            });
        }

        @Override
        public RecyclerView.ViewHolder getHeaderViewHolder(View view) {
            return new HeaderViewHolder(view);
        }

        @Override
        public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder) {
            HeaderViewHolder headerHolder = (HeaderViewHolder) holder;

            headerHolder.tvTitle.setText(title);

            headerHolder.btnMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(MainActivity.this, String.format("Clicked on more button from the header of Section %s",
                            title),
                            Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private class HeaderViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvTitle;
        private final Button btnMore;

        HeaderViewHolder(View view) {
            super(view);

            tvTitle = (TextView) view.findViewById(R.id.tvTitle);
            btnMore = (Button) view.findViewById(R.id.btnMore);
        }
    }

    private class ItemViewHolder extends RecyclerView.ViewHolder {

        private final View rootView;
        private final TextView tvItem;

        ItemViewHolder(View view) {
            super(view);
            rootView = view;
            tvItem = (TextView) view.findViewById(R.id.item_label);
        }
    }

    private class Movie {
        String name;
        String category;

        Movie(String name, String category) {
            this.name = name;
            this.category = category;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

    }
}
