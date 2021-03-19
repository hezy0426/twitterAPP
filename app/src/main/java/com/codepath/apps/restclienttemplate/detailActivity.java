package com.codepath.apps.restclienttemplate;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.models.tweet;

import org.parceler.Parcels;

public class detailActivity extends AppCompatActivity {
    TextView name;
    TextView pName;
    TextView time;
    TextView body;
    ImageView pic;
    tweet tw;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        name = findViewById(R.id.dUserName);
        pName = findViewById(R.id.dProfileName);
        time = findViewById(R.id.dTime);
        body = findViewById(R.id.dTweetBody);
        pic = findViewById(R.id.dProfilePic);

        tw = Parcels.unwrap(getIntent().getParcelableExtra("twObj"));
        name.setText(tw.User.name);
        pName.setText(tw.User.screenName);
        time.setText(TimeFormatter.getTimeStamp(tw.createdAt));
        body.setText(tw.body);
        Glide.with(getApplicationContext()).load(tw.User.publicURL).into(pic);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.bar2);
/**        ImageButton button = findViewById(R.id.button1);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),timelineActivity.class);
                finish();
            }
        });
         */
    }
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}