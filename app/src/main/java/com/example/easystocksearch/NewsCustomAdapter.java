package com.example.easystocksearch;

import android.app.Dialog;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import java.util.ArrayList;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;


public class NewsCustomAdapter extends RecyclerView.Adapter<NewsCustomAdapter.ViewHolder> {

    ArrayList newsTitles;
    ArrayList newsImagesrc;
    ArrayList newsSources;
    ArrayList newsTimesAgo;
    ArrayList newsUrls;
    Context context;

    public NewsCustomAdapter(Context context, ArrayList newsTitles, ArrayList newsImagesrc, ArrayList newsSources, ArrayList newsTimesAgo,ArrayList newsUrls) {
        this.context = context;
        this.newsImagesrc = newsImagesrc;
        this.newsTitles = newsTitles;
        this.newsSources = newsSources;
        this.newsTimesAgo = newsTimesAgo;
        this.newsUrls = newsUrls;
    }


    @Override
    public ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v); // pass the view to View Holder
        return vh;

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.source.setText((CharSequence) newsSources.get(position));
        holder.title.setText((CharSequence) newsTitles.get(position));
        holder.timesago.setText((CharSequence) newsTimesAgo.get(position));
        GlideApp.with(context).load(newsImagesrc.get(position)).transform(new CenterCrop(),new RoundedCorners(75)).into(holder.image);
        // implement setOnClickListener event on item view.
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse((String) newsUrls.get(position))));
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                // custom dialog
                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.news_dialog);

                // set the custom dialog components - text, image and buttons
                TextView text = (TextView) dialog.findViewById(R.id.textView);
                text.setText((CharSequence)newsTitles.get(position));
                ImageView image = (ImageView) dialog.findViewById(R.id.image);
                GlideApp.with(context).load(newsImagesrc.get(position)).transform(new CenterCrop()).into(image);

                ImageButton dialogButtonT = (ImageButton) dialog.findViewById(R.id.button);
                ImageButton dialogButtonC = (ImageButton) dialog.findViewById(R.id.button2);
                // if button is clicked, close the custom dialog
                dialogButtonT.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/intent/tweet?text=" + (CharSequence)newsTitles.get(position) + Uri.parse("&url=") + newsUrls.get(position))));
                    }
                });

                dialogButtonC.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse((String) newsUrls.get(position))));
                    }
                });

                dialog.show();
                return false;
            }
        });
    }


    @Override
    public int getItemCount() {
        return newsTitles.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView source;
        TextView title;
        TextView timesago;
        ImageView image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            source = (TextView) itemView.findViewById(R.id.news_source);
            image = (ImageView) itemView.findViewById(R.id.news_img);
            title = (TextView) itemView.findViewById(R.id.news_title);
            timesago = (TextView) itemView.findViewById(R.id.news_timeago);
        }
    }
}
