package com.codepath.apps.restclienttemplate;

import androidx.activity.ComponentActivity;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import okhttp3.Headers;

public class composeActivity extends AppCompatActivity {
    Button bt;
    EditText tweetText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);
        bt = findViewById(R.id.sendButton);
        tweetText = findViewById(R.id.etCompose);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Compose a message");

        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String temp = tweetText.getText().toString();
                if(temp.isEmpty()){
                    Toast.makeText(getApplicationContext(),"You haven't written anything",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(temp.length()>140){
                    Toast.makeText(composeActivity.this, "Your text is too long", Toast.LENGTH_SHORT).show();
                    return;
                }
                new twitterClient(getApplicationContext()).postNewTweet(new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Headers headers, JSON json) {
                        Toast.makeText(composeActivity.this, "Sending tweet", Toast.LENGTH_SHORT).show();
                        Log.i("compose","message sent");
                        finish();
                    }

                    @Override
                    public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                        Log.e("compose",response);
                    }
                },temp);
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}