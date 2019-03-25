package com.example.advancedpictures;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private boolean fromDB;

    private final PictureListActivity mParentActivity;
    private final ArrayList<Picture> mValues = new ArrayList<>();
    private final boolean mTwoPane;
    private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Picture picture = (Picture) view.getTag();
            if (mTwoPane) {
                Bundle arguments = new Bundle();
                arguments.putParcelable(ExtraValues.EXTRA_PICTURE, picture);
                PictureDetailFragment fragment = new PictureDetailFragment();
                fragment.setArguments(arguments);
                mParentActivity.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.picture_detail_container, fragment)
                        .commit();
            } else {
                Context context = view.getContext();
                Intent intent = new Intent(context, PictureDetailActivity.class);
                intent.putExtra(ExtraValues.EXTRA_PICTURE, picture);

                //!!!!!!!!!!
                intent.putExtra("DB", fromDB);
                //!!!!!!!!!!

                context.startActivity(intent);
            }
        }
    };

    RecyclerViewAdapter(PictureListActivity parent, boolean twoPane) {
        mParentActivity = parent;
        mTwoPane = twoPane;
    }

    void setElement(Picture[] pictures, boolean DB) {
        //!!!!!!!!!
        fromDB = DB;
        //if (fromDB) {
        //    Log.d("!!!!!!!!!!!!!!!!!!", "YEEEEEEEEEEES");
        //} else  {
        //    Log.d("!!!!!!!!!!!!!!!!!!", "NOOOOOOOOOOOO");
        //}
        //!!!!!!!!!!
        mValues.addAll(Arrays.asList(pictures));
        notifyDataSetChanged();
    }


    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.picture_list_content, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        holder.mIdView.setText(mValues.get(position).getId());
        holder.mContentView.setText(mValues.get(position).getDescription());
        holder.itemView.setTag(mValues.get(position));
        holder.itemView.setOnClickListener(mOnClickListener);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final TextView mIdView;
        final TextView mContentView;
        ViewHolder(View view) {
            super(view);
            mIdView = view.findViewById(R.id.id_text);
            mContentView = view.findViewById(R.id.content);
        }
    }
}