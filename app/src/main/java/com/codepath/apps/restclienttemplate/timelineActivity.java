package com.codepath.apps.restclienttemplate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.ActionBar;
import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Toast;

import com.codepath.apps.restclienttemplate.models.tweet;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

public class timelineActivity extends AppCompatActivity {
    twitterClient client;
    List<tweet> arr;
    RecyclerView rv;
    adapter Adapter;
    Context context;
    SwipeRefreshLayout swipeContainer;
    EndlessRecyclerViewScrollListener scrollListener;
    SwipeRefreshLayout.OnRefreshListener swipeListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_line);

        client = twitterAPP.getRestClient(this);

        //Swipe down to refresh
        swipeContainer = findViewById(R.id.swipeContainer);
        swipeContainer.setColorSchemeColors(
                getResources().getColor(android.R.color.holo_blue_bright),
                getResources().getColor(android.R.color.holo_green_light),
                getResources().getColor(android.R.color.holo_orange_light),
                getResources().getColor(android.R.color.holo_red_light)
        );

        //Tell it what to do when gesture occurs
        swipeListener = new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.i("timeline","fetching new data");
                populateTimeline(); //This method calls setRefreshing to false
                rv.smoothScrollToPosition(0);
            }
        };
        swipeContainer.setOnRefreshListener(swipeListener);

        //Modify the action bar
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setLogo(R.drawable.ic_action_name);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        //Using recyclerView
        rv = findViewById(R.id.rvTweets);
        rv.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        arr = new ArrayList<>();
        context = this;
        Adapter = new adapter(context,arr);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        rv.setLayoutManager(layoutManager);
        rv.setAdapter(Adapter);

        //Scroll down to load more
        scrollListener = new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                Log.i("onloadmore",String.format("%d",page));
                loadMoreData();
            }
        };
        rv.addOnScrollListener(scrollListener);
        populateTimeline();
    }

    //Adding the icon for writting tweet
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }

    //Adding functionality to the icon
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.compose){
            Toast.makeText(this,"Compose new message", Toast.LENGTH_SHORT).show();
        }
        Intent i = new Intent(context,composeActivity.class);
        startActivity(i);
        swipeContainer.setRefreshing(true);
        populateTimeline();
        rv.smoothScrollToPosition(0);
        return true;
    }

    //Load more pages
    private void loadMoreData() {
        client.getNextPageOfTweets(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                Log.i("time/LMD","onSuccess for loadMoreData");
                JSONArray array = json.jsonArray;
                try {
                    Adapter.addAll( tweet.fromJsonArray(array));
                    Log.i("time/LMD","onSuccess add all");
                }
                catch(JSONException e){
                    Log.e("timeline/failed", String.valueOf(e));
                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.e("time/LMD","onFailre for loadMoreData "+statusCode);
            }
        },arr.get(arr.size()-1).id);
    }

    //Populate the first page
    private void populateTimeline() {
        client.getHomeTimeline(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                Log.i("timeline","onSuccess" + json.toString());
                JSONArray array = json.jsonArray;
                try {
                    Adapter.clear();
                    Adapter.addAll( tweet.fromJsonArray(array));
                    swipeContainer.setRefreshing(false); //Stop the animation
                    Log.i("timeline","try block");
                }
                catch(JSONException e){
                    Log.e("timeline/failed", String.valueOf(e));
                }

            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.i("timeline","onFailure "+ response);
            }
        });
    }
}