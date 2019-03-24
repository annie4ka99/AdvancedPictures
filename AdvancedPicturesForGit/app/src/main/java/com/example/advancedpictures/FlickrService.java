package com.example.advancedpictures;

import com.fasterxml.jackson.databind.JsonNode;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface FlickrService {
    @GET("services/feeds/photos_public.gne")
    Observable<JsonNode> getData(@Query("tags") String tags, @Query("format") String format, @Query("nojsoncallback") String jsoncb);
}
