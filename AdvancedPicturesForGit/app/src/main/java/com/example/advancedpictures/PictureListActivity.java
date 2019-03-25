package com.example.advancedpictures;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.fasterxml.jackson.databind.JsonNode;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


public class PictureListActivity extends AppCompatActivity {
    private boolean mTwoPane;
    RecyclerView recyclerView;
    private RecyclerViewAdapter adapter;

    EditText search;
    ImageButton searchButton;
    ImageButton favButton;

    Picture[] savedList;
    CompositeDisposable compositeDisposable;
    private static final int MAX_SIZE = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("ACTION: ", "CREATING PictureListActivity");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture_list);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());
        search = findViewById(R.id.search_text);

        if (findViewById(R.id.picture_detail_container) != null) {
            mTwoPane = true;
        }

        recyclerView = findViewById(R.id.picture_list);
        assert recyclerView != null;
        setupRecyclerView(recyclerView);

        compositeDisposable = new CompositeDisposable();

        favButton = findViewById(R.id.fav_button);
        searchButton = findViewById(R.id.search_button);
    }


    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        adapter = new RecyclerViewAdapter(this, mTwoPane);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (savedList != null) {
            Log.d("ACTION: ", "PictureListActivity SAVED STATE");
            outState.putParcelableArray(ExtraValues.EXTRA_ARRAY, savedList);
        }
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.d("ACTION: ", "PictureListActivityRESTORING STATE");
        if (savedInstanceState != null) {
            savedList = (Picture[]) savedInstanceState.getParcelableArray(ExtraValues.EXTRA_ARRAY);
            if (savedList != null) {
                adapter.setElement(savedList, false);
            }
        }
    }


    @Override
    protected void onNewIntent(Intent intent) {
        Log.d("!!!!!!!!!!!!!!", "NEW INTENT");
        //super.onNewIntent(intent);
        if (intent.getBooleanExtra("DB", false)) {
            compositeDisposable.add(MyApp.getDataBase().getPictures().subscribe(new Consumer<Picture[]>() {
                @Override
                public void accept(Picture[] pictures){
                    adapter.setElement(pictures, true);
                }
            }));
        }
    }

    public void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }

    public void onSearchClick(View view) {
        setupRecyclerView(recyclerView);
        compositeDisposable.add(MyApp.getApi().getData(search.getText().toString(), "json", "1")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<JsonNode>() {
                    @Override
                    public void accept(JsonNode result) throws Exception {
                        savedList = new Picture[MAX_SIZE];
                        JsonNode pictures = (result.path("items"));
                        if (pictures == null)
                            return;
                        try {
                            for (int i = 0; i < pictures.size(); i++) {
                                savedList[i] = new Picture(i + 1, pictures.get(i));
                            }
                            Log.d("ACTION: ", "LIST LOADED");
                        } catch (Exception e) {
                            Log.d("ACTION: ", "COULDN'T LOAD LIST");
                            e.printStackTrace();
                        }
                        setupRecyclerView(recyclerView);
                        adapter.setElement(savedList, false);
                    }
                }));
    }

    public void onFavClick(View view) {
        setupRecyclerView(recyclerView);
        compositeDisposable.add(MyApp.getDataBase().getPictures().subscribe(new Consumer<Picture[]>() {
            @Override
            public void accept(Picture[] pictures){
                adapter.setElement(pictures, true);
            }
        }));
    }
}