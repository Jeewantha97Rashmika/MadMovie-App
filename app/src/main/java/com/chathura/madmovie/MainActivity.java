package com.chathura.madmovie;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.chathura.madmovie.classes.FirebasePushNotificationClass;
import com.chathura.madmovie.retrofit.ISuggestAPI;
import com.chathura.madmovie.retrofit.RetrofitClient;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mancj.materialsearchbar.MaterialSearchBar;

import org.json.JSONArray;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    //Search autocomplete API example
    // http://suggestqueries.google.com/complete/search?callback=JSON_CALLBACK&client=firefox&hl=en&q=the+100&ds=imdb.com
    // http://suggestqueries.google.com/complete/search?callback=JSON_CALLBACK&client=firefox&hl=en&q=spider&ds=imdb.com


    private DatabaseReference mDatabase;

    AppUpdateManager appUpdateManager;
    int requestUpdate = 1;
    MaterialSearchBar txtMovieTitle;
    ISuggestAPI myAPI;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    private List<String> suggestions = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Init autocomplete Suggestions
        myAPI = RetrofitClient.getInstance().create(ISuggestAPI.class);
        txtMovieTitle = findViewById(R.id.txtMovieTitle);
        txtMovieTitle.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {

            }

            @Override
            public void onSearchConfirmed(CharSequence text) {

            }

            @Override
            public void onButtonClicked(int buttonCode) {

            }
        });
        txtMovieTitle.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // set suggest list load from api
                getSuggestions(s.toString(),
                        "chrome",
                        "en",
                        "imdb.com");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //Firebase push notofication
        Intent intentBackgroundService = new Intent(this, FirebasePushNotificationClass.class);
        startService(intentBackgroundService);

        //check for update
        appUpdateManager = AppUpdateManagerFactory.create(getApplicationContext());
        appUpdateManager.getAppUpdateInfo().addOnSuccessListener(new OnSuccessListener<AppUpdateInfo>() {
            @Override
            public void onSuccess(AppUpdateInfo result) {
                if((result.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE)
                        && result.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE))
                {
                    //Toast.makeText(MainActivity.this, "Update Available", Toast.LENGTH_SHORT).show();
                    try{
                        appUpdateManager.startUpdateFlowForResult(
                                result,
                                AppUpdateType.IMMEDIATE,
                                MainActivity.this,
                                requestUpdate
                        );
                    }catch(IntentSender.SendIntentException e){
                        e.printStackTrace();
                    }
                }else{
                    //Toast.makeText(MainActivity.this, "Update Not Available", Toast.LENGTH_SHORT).show();
                }
            }
        });



        //firebase
        mDatabase = FirebaseDatabase.getInstance().getReference();

        TextView txtAbout = findViewById(R.id.txtAbout);

        Button btnSearch = findViewById(R.id.btnSearch);

        Intent intentAbout = new Intent(this, About.class);
        Intent intentMovieList = new Intent(this, MovieList.class);

        txtAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intentAbout);
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!txtMovieTitle.getText().toString().equals("")) {
                    intentMovieList.putExtra("movieTitle", txtMovieTitle.getText().toString());

                    Date date = new Date();
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd/HH");
                    String strDate = formatter.format(date);

                    mDatabase.child("searches").child(strDate).push().setValue(txtMovieTitle.getText().toString());
                    startActivity(intentMovieList);
                    txtMovieTitle.setText("");
                }else{
                    Toast.makeText(MainActivity.this, "Please Enter Movie Title", Toast.LENGTH_SHORT).show();
                }


            }
        });
    }


    private void getSuggestions(String query, String client, String ln, String yt) {
        compositeDisposable.add(
                myAPI.getObservable(query, client, ln, yt)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                       @Override
                       public void accept(String s) throws Exception {
                            // will retrieve the suggestions JSON
                           if(suggestions.size()>0)suggestions.clear();
                           JSONArray mainJson = new JSONArray(s);
                           suggestions = new Gson().fromJson(mainJson.getString(1),
                                new TypeToken<List<String>>(){}.getType());
                           txtMovieTitle.updateLastSuggestions(suggestions.subList(0,3));

                       }
                   }, new Consumer<Throwable>() {
                       @Override
                       public void accept(Throwable throwable) throws Exception {

                       }
                   }
        ));
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();

        appUpdateManager.getAppUpdateInfo().addOnSuccessListener(new OnSuccessListener<AppUpdateInfo>() {
            @Override
            public void onSuccess(AppUpdateInfo result) {
                if((result.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS))
                {
                    try{
                        appUpdateManager.startUpdateFlowForResult(
                                result,
                                AppUpdateType.IMMEDIATE,
                                MainActivity.this,
                                requestUpdate
                        );
                    }catch(IntentSender.SendIntentException e){
                        e.printStackTrace();
                    }
                }
            }
        });
    }


}

