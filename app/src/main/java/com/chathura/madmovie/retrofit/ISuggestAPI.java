package com.chathura.madmovie.retrofit;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ISuggestAPI {
//    @GET("complete/search")
//    Observable<String> getSuggestionFromYouTube(@Query("q") String query,
//                                                @Query("client") String client,
//                                                @Query("hl") String language,
//                                                @Query("ds") String restrict);
    @GET("complete/search")
    Observable<String> getObservable(@Query("q") String query,
                                                @Query("client") String client,
                                                @Query("hl") String language,
                                                @Query("ds") String restrict);
}
