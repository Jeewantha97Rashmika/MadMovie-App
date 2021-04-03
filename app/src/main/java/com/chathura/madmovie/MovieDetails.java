package com.chathura.madmovie;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class MovieDetails extends AppCompatActivity {

    TextView lblTitle, lblYear, lblGenre, lblActors, lblPlot, lblRating, lblType, lblRuntime, lblDirector, lblCountry, lblAwards;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle bundle = getIntent().getExtras();
        String imdbId = bundle.getString("imdbId");

        //Toast.makeText(this, imdbId, Toast.LENGTH_SHORT).show();

        final String API_KEY = "29769d9e";
        final String URL = "https://www.omdbapi.com/?i="+imdbId+"&apikey="+API_KEY;





        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please wait...");
        dialog.setCancelable(true);
        dialog.show();

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if(response!=null){
                    dialog.dismiss();
                    lblTitle = findViewById(R.id.lblTitle);
                    lblYear = findViewById(R.id.lblYear);
                    lblRating = findViewById(R.id.lblRating);
                    lblGenre = findViewById(R.id.lblGenre);
                    lblActors = findViewById(R.id.lblActors);
                    lblPlot = findViewById(R.id.lblPlot);
                    lblType = findViewById(R.id.lblType);
                    lblRuntime = findViewById(R.id.lblRuntime);
                    lblDirector = findViewById(R.id.lblDirector);
                    lblCountry = findViewById(R.id.lblCountry);
                    lblAwards = findViewById(R.id.lblAwards);

                    try {
                        lblTitle.setText(response.getString("Title"));
                        lblYear.setText(response.getString("Year"));
                        lblRating.setText(response.getString("imdbRating"));
                        lblGenre.setText(response.getString("Genre"));
                        lblActors.setText(response.getString("Actors"));
                        lblPlot.setText(response.getString("Plot"));
                        lblType.setText(response.getString("Type"));
                        lblRuntime.setText(response.getString("Runtime"));
                        lblDirector.setText(response.getString("Director"));
                        lblCountry.setText(response.getString("Country"));
                        lblAwards.setText(response.getString("Awards"));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else{
                    //Toast.makeText(MovieList.this, "response is empty", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);

    }
}