package com.example.easystocksearch;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import io.github.luizgrp.sectionedrecyclerviewadapter.Section;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters;

import static com.example.easystocksearch.R.*;
import static com.example.easystocksearch.R.drawable.ic_baseline_trending_down_24;
import static com.example.easystocksearch.R.drawable.ic_twotone_trending_up_24;


public class PortfolioSection extends Section {

    private final String title;
    private ArrayList<String> tickerlist;
    private ArrayList<String> sharelist;
    private ArrayList<String> currpriceList;
    private ArrayList<Integer> trendList;
    private ArrayList<Integer> colorList;
    private ArrayList<String> changeList;
    Context context;
    Handler timerhandler = new Handler();
    Runnable runnable;


    PortfolioSection(Context context , @NonNull final String title, @NonNull ArrayList tickerlist, @NonNull ArrayList sharelist,@NonNull ArrayList currpriceList,@NonNull ArrayList trendList,@NonNull ArrayList colorList,@NonNull ArrayList changeList) {

        super(SectionParameters.builder()
                .itemResourceId(layout.section_portfolio_item)
                .headerResourceId(layout.section_header)
                .build());


        this.title = title;
        this.tickerlist = tickerlist;
        this.sharelist = sharelist;
        this.currpriceList = currpriceList;
        this.trendList = trendList;
        this.colorList = colorList;
        this.changeList = changeList;
        this.context = context;

    }

    @Override
    public int getContentItemsTotal() {

        return tickerlist.size();
    }

    @Override
    public RecyclerView.ViewHolder getItemViewHolder(final View view) {
        return new ItemViewHolder(view);
    }


    @Override
    public void onBindItemViewHolder(final RecyclerView.ViewHolder holder, final int position) {

        final ItemViewHolder itemHolder = (ItemViewHolder) holder;

        if(position == 0){

            itemHolder.name.setText(tickerlist.get(position));
            itemHolder.name.setTextSize((float) 25);
            itemHolder.name.setTextColor(Color.parseColor("#000000"));
            itemHolder.rightarrow.setVisibility(View.GONE);
            itemHolder.shares.setText(sharelist.get(position));
            itemHolder.shares.setTextSize((float) 25);
            itemHolder.shares.setTypeface(Typeface.DEFAULT_BOLD);
            itemHolder.shares.setTextColor(Color.parseColor("#000000"));

        }
        else {
            itemHolder.name.setText(tickerlist.get(position)); // .replaceAll("\\s", "")
            itemHolder.name.setTypeface(Typeface.DEFAULT_BOLD);
            itemHolder.name.setTextSize((float) 15);
            itemHolder.curr.setTextSize((float) 15);
            itemHolder.name.setTextColor(Color.parseColor("#000000"));
            itemHolder.shares.setText("Shares:" + sharelist.get(position));
            itemHolder.curr.setText(currpriceList.get(position));
            itemHolder.curr.setTextColor(Color.parseColor("#000000"));
            itemHolder.curr.setTypeface(Typeface.DEFAULT_BOLD);
            itemHolder.change.setText(changeList.get(position));
//            ContextCompat.getDrawable(context,trendList.get(position));
            itemHolder.trendimg.setImageResource(trendList.get(position));
            itemHolder.change.setTextColor(ContextCompat.getColor(context, colorList.get(position)));

//            RequestQueue queue = Volley.newRequestQueue(context);
//
//            timerhandler.postDelayed(runnable = new Runnable() {
//                public void run() {
////                timerhandler.postDelayed(runnable, 15000);
////                Toast.makeText(context, "This method is run every 15 seconds", Toast.LENGTH_SHORT).show();
//                Log.i("This method is run every 15 seconds","in Main Activity");
//                                    String url = "https://stock-search-nodejs-be.wl.r.appspot.com/details/" + tickerlist.get(position); // .replaceAll("\\s", "")
//
//
//                                    StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
//                                            new Response.Listener<String>() {
//                                                @Override
//                                                public void onResponse(String response) {
//                                                    try {
//                                                        Log.i("onResponse received in portfolio section","--insideresponse--");
//                                                        JSONObject responseObject = new JSONObject(response);
//                                                        itemHolder.curr.setText(responseObject.getString("last"));
//
//                                                        double change = (responseObject.getDouble("last") - responseObject.getDouble("prevClose"));
//                                                        BigDecimal bdchange = new BigDecimal(change).setScale(2, RoundingMode.HALF_UP);
//                                                        double newbdchange = bdchange.doubleValue();
//                                                        itemHolder.change.setText(String.valueOf(Math.abs(newbdchange)));
//
//                                                        if (newbdchange < 0) {
//                                                            itemHolder.trendimg.setImageResource(ic_baseline_trending_down_24);
//                                                            itemHolder.change.setTextColor(ContextCompat.getColor(context, R.color.red));
//                                                        } else if (newbdchange > 0) {
//                                                            itemHolder.trendimg.setImageResource(ic_twotone_trending_up_24);
//                                                            itemHolder.change.setTextColor(ContextCompat.getColor(context, R.color.green));
//                                                        }
//
//
//                                                    } catch (JSONException e) {
//                                                        e.printStackTrace();
//                                                    }
//
//                                                }
//                                            }, new Response.ErrorListener() {
//                                        @Override
//                                        public void onErrorResponse(VolleyError error) {
//
//                                        }
//                                    });
//
//                                    // Add the request to the RequestQueue.
//                                    queue.add(stringRequest);
//                    }
//                    }, 15000);


            itemHolder.rightarrow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent myIntent = new Intent(v.getContext(), StockDetails.class);
                    myIntent.putExtra("ticker", tickerlist.get(position)); //.replaceAll("\\s", "")
                    v.getContext().startActivity(myIntent);
                }
            });
        }
    }

    @Override
    public RecyclerView.ViewHolder getHeaderViewHolder(final View view) {
        return new HeaderViewHolder(view);
    }

    @Override
    public void onBindHeaderViewHolder(final RecyclerView.ViewHolder holder) {
        final HeaderViewHolder headerHolder = (HeaderViewHolder) holder;

        headerHolder.tvTitle.setText(title);
    }

    public void refreshData(ArrayList<String> tlist,ArrayList<String> slist,ArrayList<String> pricelist,ArrayList<Integer> trendlist,ArrayList<Integer> colorlist,ArrayList<String> changelist) {
        tickerlist = tlist;
        sharelist = slist;
        currpriceList = pricelist;
        trendList = trendlist;
        colorList = colorlist;
        changeList = changelist;

    }


    final class ItemViewHolder extends RecyclerView.ViewHolder {

        final View rootView;
        final TextView name;
        final TextView shares;
        final TextView curr;
        final TextView change;
        final ImageView trendimg;
        final ImageButton rightarrow;

        ItemViewHolder(@NonNull View view) {
            super(view);

            rootView = view;
            name = view.findViewById(id.tickername);
            shares = view.findViewById(id.noofshares);
            curr = view.findViewById(id.currprice);
            change = view.findViewById(id.change);
            trendimg = view.findViewById(id.upordownimg);
            rightarrow = view.findViewById(id.arrowbutton);
        }
    }



    final class HeaderViewHolder extends RecyclerView.ViewHolder {

        final TextView tvTitle;

        HeaderViewHolder(@NonNull View view) {
            super(view);

            tvTitle = view.findViewById(id.tvTitle);
        }
    }
}
