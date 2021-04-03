package com.chathura.madmovie;

import androidx.appcompat.app.AppCompatActivity;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.chathura.madmovie.classes.Movie;
import com.chathura.madmovie.classes.MovieAdapter;
import com.chathura.madmovie.classes.SingletonRequest;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

public class MovieList extends AppCompatActivity {

    private ListView listView;
    private MovieAdapter mAdapter;
    RequestQueue mQueue;
    private AdView mAdView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Bundle bundle = getIntent().getExtras();
        String movieTitle = bundle.getString("movieTitle");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mQueue = Volley.newRequestQueue(MovieList.this);

        //admob
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);



        listView = (ListView) findViewById(R.id.movies_list);
        ArrayList<Movie> moviesList = new ArrayList<>();

        final String API_KEY = "29769d9e";
        final String URL = "https://www.omdbapi.com/?s="+movieTitle+"&apikey="+API_KEY;

        ProgressDialog dialog = new ProgressDialog(this);

        dialog.setMessage("Please wait...");
        dialog.setCancelable(true);
        dialog.show();


        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if(response!=null){
                    dialog.dismiss();
                    try {
                        JSONArray jsonArray = response.getJSONArray("Search");

                        if (jsonArray.length()!=0){
                            for(int i=0; i<jsonArray.length();i++) {
                                JSONObject object = jsonArray.getJSONObject(i);
                                String title = object.getString("Title");
                                String year = object.getString("Year");
                                String imdbID = object.getString("imdbID");
                                String type = object.getString("Type");
                                String poster = object.getString("Poster");
                                poster.replace("SX300","SX100");

                                moviesList.add(new Movie(title, imdbID, year, type, poster));
                            }
                        }else{
                            moviesList.add(new Movie("Movie Not Found", "-", "-", "-", ""));
                        }




                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(MovieList.this, "Error Occured", Toast.LENGTH_SHORT).show();
                        moviesList.add(new Movie("Movie Not Found", "-", "-", "-", ""));

                    }
                }else{
                    Toast.makeText(MovieList.this, "response is empty", Toast.LENGTH_SHORT).show();
                    moviesList.add(new Movie("Movie Not Found", "-", "-", "-", ""));
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MovieList.this, "Error response", Toast.LENGTH_SHORT).show();
            }
        });

        //RequestQueue queue = Volley.newRequestQueue(this);
        SingletonRequest singletonRequest = new SingletonRequest(this);
        RequestQueue queue = singletonRequest.getRequestQueue();

        queue.add(request);

        Collections.sort(moviesList);
        mAdapter = new MovieAdapter(this,moviesList);
        listView.setAdapter(mAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
                Intent intentMovieDetails = new Intent(getApplicationContext(), MovieDetails.class);
                intentMovieDetails.putExtra("imdbId", moviesList.get((int)id).getImdbID());
                startActivity(intentMovieDetails);

            }
        });

    }



}

