package com.example.advancedpictures;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;

public class PictureDetailFragment extends Fragment {

    Picture picture;
    View rootView;
    ImageView imageView;
    TextView textView;
    Context context;
    Boolean isFavourite;
    FavouritesDBHelper helper = null;
    Button addRemoveButton;
    CompositeDisposable compositeDisposable;

    public PictureDetailFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        compositeDisposable = new CompositeDisposable();
        if (getArguments().containsKey(ExtraValues.EXTRA_PICTURE)) {
            //Log.d("*****", "image got");
            picture = getArguments().getParcelable(ExtraValues.EXTRA_PICTURE);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.picture_detail, container, false);
        helper = MyApp.getDataBase();
        addRemoveButton = rootView.findViewById(R.id.add_remove_button);
        addRemoveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFavourite) {
                    //Log.d("FAVOURITE IS ", "REMOVED");
                    addRemoveButton.setText(getString(R.string.FavAdd));
                    compositeDisposable.add(helper.delete(picture.getDescription(), picture.getUrl()).subscribe(new Consumer<Boolean>() {
                        @Override
                        public void accept(Boolean isDeleted){}
                    }));
                    isFavourite = false;
                } else {
                    //Log.d("FAVOURITE IS ", "ADDED");
                    addRemoveButton.setText(getString(R.string.FavRemoved));
                    compositeDisposable.add(helper.add(picture.getDescription(), picture.getUrl()).subscribe(new Consumer<Boolean>() {
                        @Override
                        public void accept(Boolean isAdded){}
                    }));
                    isFavourite = true;
                }
            }
        });
        compositeDisposable.add(helper.check(picture.getDescription(), picture.getUrl()).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean isExists) throws Exception {
                isFavourite = isExists;
                if (!isFavourite) {
                    addRemoveButton.setText(getString(R.string.FavAdd));
                } else {
                    addRemoveButton.setText(getString(R.string.FavRemoved));
                }
            }
        }));
        imageView = rootView.findViewById(R.id.image_view);
        textView = rootView.findViewById(R.id.image_description);
        Picasso.get().load(picture.getUrl()).into(imageView);
        imageView.setContentDescription(picture.getDescription());
        textView.setText(picture.getDescription());
        return rootView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}