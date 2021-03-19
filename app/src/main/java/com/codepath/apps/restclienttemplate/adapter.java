package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.models.tweet;

import org.parceler.Parcels;

import java.util.List;

public class adapter extends RecyclerView.Adapter<adapter.ViewHolder>{
    List<tweet> arr;
    Context context;
    int i;
    public adapter(Context context, List<tweet> arr) {
        this.arr = arr;
        this.context = context;
        i = 0;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_tweet,parent,false);
        Log.i("inflates",String.format("%d",i++));
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        tweet tw= arr.get(position);
        holder.bind(tw);
    }

    @Override
    public int getItemCount() {
        return arr.size();
    }

    public void clear(){
        arr.clear();
        notifyDataSetChanged();

    }

    public void addAll(List<tweet> tweetList){
        arr.addAll(tweetList);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView iv;
        TextView body;
        TextView profileName;
        TextView userName;
        TextView timeStamp;
        RelativeLayout container;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            iv = itemView.findViewById(R.id.profilePic);
            body = itemView.findViewById(R.id.tweetBody);
            profileName = itemView.findViewById(R.id.profileName);
            userName = itemView.findViewById(R.id.userName);
            timeStamp = itemView.findViewById(R.id.timeStamp);
            container = itemView.findViewById(R.id.container);
        }

        public void bind(final tweet tw) {
            body.setText(tw.body);
            profileName.setText(tw.User.screenName);
            userName.setText(tw.User.name);
            timeStamp.setText(tw.getFormattedTimestamp(tw));
            Glide.with(context).load(tw.User.publicURL).into(iv);
            Log.i("binding","tweet bound");

            container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context, detailActivity.class);
                    i.putExtra("twObj", Parcels.wrap(tw));
                    context.startActivity(i);
                }
            });

            //Since autoLink is enabled, the textView can't be selected when there is link
            //This if statement will satisfy only when it's not an autolink text, so normal text can trigger intent
            body.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (body.getSelectionStart() == -1 && body.getSelectionEnd() == -1){
                        Intent i = new Intent(context, detailActivity.class);
                        i.putExtra("twObj", Parcels.wrap(tw));
                        context.startActivity(i);
                    }
                }
            });

        }
    }
}
