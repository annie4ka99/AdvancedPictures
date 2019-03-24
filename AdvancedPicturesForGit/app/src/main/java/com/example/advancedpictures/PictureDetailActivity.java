package com.example.advancedpictures;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

public class PictureDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture_detail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        if (savedInstanceState == null) {
            Bundle arguments = new Bundle();
            arguments.putParcelable(ExtraValues.EXTRA_PICTURE, getIntent().getParcelableExtra(ExtraValues.EXTRA_PICTURE));
            PictureDetailFragment fragment = new PictureDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.picture_detail_container, fragment)
                    .commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //Log.d("!!!!!!!!!!!!!!!!!!", "ITEM SELECTED");
        int id = item.getItemId();
        if (id == android.R.id.home) {
            //!!!!!!!!!!!!!
            /*
            if (getIntent().getBooleanExtra("DB", false)) {
                //Log.d("!!!!!!!!!!!!!!!!!!", "DAAAAAAAAAA");
                //Intent i = new Intent(this, PictureListActivity.class);
                //i.putExtra("DB", true);
                //navigateUpTo(i);
            } else {
                //Log.d("!!!!!!!!!!!!!!!!!!", "NEEEeeeEET");
                //Intent i = new Intent(this, PictureListActivity.class);
                //i.putExtra("DB", false);
                //navigateUpTo(i);
            }
            */
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}