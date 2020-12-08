package com.example.easystocksearch;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import io.github.luizgrp.sectionedrecyclerviewadapter.Section;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.example.easystocksearch.R.drawable.ic_baseline_trending_down_24;
import static com.example.easystocksearch.R.drawable.ic_twotone_trending_up_24;

public class FavouritesSection extends Section {

    private final String title;
    private  ArrayList<String> tickerlist;
    private  ArrayList<String> sharelist;
    private  ArrayList<String> favlist;
    private ArrayList<String> currpriceList;
    private ArrayList<Integer> trendList;
    private ArrayList<Integer> colorList;
    private ArrayList<String> changeList;

    private ArrayList<String> titleList;
    private ArrayList<String> favcurrpriceList;
    private ArrayList<Integer> favtrendList;
    private ArrayList<Integer> favcolorList;
    private ArrayList<String> favchangeList;
    Handler timerhandler = new Handler();
    Runnable runnable;
    Context context;


    FavouritesSection(Context context , @NonNull final String title, @NonNull ArrayList<String> favlist,@NonNull ArrayList<String>  tickerlist, @NonNull ArrayList<String>  sharelist,@NonNull ArrayList currpriceList,@NonNull ArrayList trendList,@NonNull ArrayList colorList,@NonNull ArrayList changeList,@NonNull ArrayList titleList,@NonNull ArrayList favcurrpriceList,@NonNull ArrayList favtrendList,@NonNull ArrayList favcolorList,@NonNull ArrayList favchangeList) {

        super(SectionParameters.builder()
                .itemResourceId(R.layout.section_portfolio_item)
                .headerResourceId(R.layout.section_header)
                .build());


        this.title = title;
        this.tickerlist =  tickerlist;
        this.sharelist = sharelist;
        this.favlist = favlist;
        this.currpriceList = currpriceList;
        this.trendList = trendList;
        this.colorList = colorList;
        this.changeList = changeList;
        this.titleList = titleList;

        this.favcurrpriceList = favcurrpriceList;
        this.favtrendList = favtrendList;
        this.favcolorList = favcolorList;
        this.favchangeList = favchangeList;

        this.context = context;

    }

    @Override
    public int getContentItemsTotal() {
        return favlist.size();
    }

   @Override
    public RecyclerView.ViewHolder getItemViewHolder(final View view) {
        return new ItemViewHolder(view);
    }



    @Override
    public void onBindItemViewHolder(final RecyclerView.ViewHolder holder, final int position) {


        final FavouritesSection.ItemViewHolder itemHolder = (FavouritesSection.ItemViewHolder) holder;

        itemHolder.name.setText(favlist.get(position)); //.replaceAll("\\s", "")
        itemHolder.name.setTypeface(Typeface.DEFAULT_BOLD);
        itemHolder.name.setTextSize((float) 15);

        itemHolder.name.setTextColor(Color.parseColor("#000000"));

        int index = tickerlist.indexOf(favlist.get(position)); //.replaceAll("\\s", "")
        Log.i("index",String.valueOf(index));
        if (index >= 0){
            itemHolder.sharesORtitle.setText("Shares:" + sharelist.get(index));
            itemHolder.curr.setText(currpriceList.get(index));
            itemHolder.curr.setTextSize((float) 15);
            itemHolder.curr.setTextColor(Color.parseColor("#000000"));

            itemHolder.change.setText(changeList.get(index));
            itemHolder.trendimg.setImageResource(trendList.get(index));
            itemHolder.change.setTextColor(ContextCompat.getColor(context, colorList.get(index)));

        }
        else {
            itemHolder.sharesORtitle.setText(titleList.get(position));
            itemHolder.curr.setText(favcurrpriceList.get(position));
            itemHolder.curr.setTextSize((float) 15);
            itemHolder.curr.setTextColor(Color.parseColor("#000000"));

            itemHolder.change.setText(favchangeList.get(position));
            itemHolder.trendimg.setImageResource(favtrendList.get(position));
            itemHolder.change.setTextColor(ContextCompat.getColor(context, favcolorList.get(position)));
        }

//        timerhandler.postDelayed(runnable = new Runnable() {
//            public void run() {
//                timerhandler.postDelayed(runnable, 15000);
//
////         Instantiate the RequestQueue.
//        RequestQueue queue = Volley.newRequestQueue(context);
//
//        String url ="https://stock-search-nodejs-be.wl.r.appspot.com/details/" + favlist.get(position); //.replaceAll("\\s", "")
//
//        // Request a string response from the provided URL.
//        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        try {
//                            JSONObject responseObject = new JSONObject(response);
//                            if(index < 0) {
//                                itemHolder.sharesORtitle.setText(responseObject.getString("name"));
//                            }
//                            itemHolder.curr.setText(responseObject.getString("last"));
//
//                            double change = (responseObject.getDouble("last") - responseObject.getDouble("prevClose"));
//                            BigDecimal bdchange = new BigDecimal(change).setScale(2, RoundingMode.HALF_UP);
//                            double newbdchange = bdchange.doubleValue();
//                            itemHolder.change.setText(String.valueOf(Math.abs(newbdchange)));
//
//                            if (newbdchange < 0) {
//                                itemHolder.trendimg.setImageResource(ic_baseline_trending_down_24);
//                                itemHolder.change.setTextColor(ContextCompat.getColor(context, R.color.red));
//                            }
//                            else if(newbdchange > 0){
//                                itemHolder.trendimg.setImageResource(ic_twotone_trending_up_24);
//                                itemHolder.change.setTextColor(ContextCompat.getColor(context, R.color.green));
//                            }
//
//
//
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//
//            }
//        });
//
//        // Add the request to the RequestQueue.
//        queue.add(stringRequest);
//            }
//        }, 15000);

        itemHolder.rightarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent myIntent = new Intent(v.getContext(), StockDetails.class);
                myIntent.putExtra("ticker",favlist.get(position)); //.replaceAll("\\s", "")
                v.getContext().startActivity(myIntent);
            }
        });
            }

    public void removeItem(int position) {
        favlist.remove(position);

    }

    public void refreshData(ArrayList<String> tlist,ArrayList<String> slist,ArrayList<String> flist,ArrayList<String> pricelist,ArrayList<Integer> trendlist,ArrayList<Integer> colorlist,ArrayList<String> changelist,ArrayList<String> titlelist,@NonNull ArrayList favcurrpricelist,@NonNull ArrayList favtrendlist,@NonNull ArrayList favcolorlist,@NonNull ArrayList favchangelist) {
        tickerlist = tlist;
        sharelist = slist;
        favlist = flist;
        currpriceList = pricelist;
        trendList = trendlist;
        colorList = colorlist;
        changeList = changelist;
        titleList = titlelist;
        favcurrpriceList =favcurrpricelist;
        favchangeList = favchangelist;
        favcolorList = favcolorlist;
        favtrendList = favtrendlist;



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


    final class ItemViewHolder extends RecyclerView.ViewHolder {

        final View rootView;
        final TextView name;
        final TextView sharesORtitle;
        final TextView curr;
        final TextView change;
        final ImageView trendimg;
        final ImageButton rightarrow;

        ItemViewHolder(@NonNull View view) {
            super(view);

            rootView = view;
            name = view.findViewById(R.id.tickername);
            sharesORtitle = view.findViewById(R.id.noofshares);
            curr = view.findViewById(R.id.currprice);
            change = view.findViewById(R.id.change);
            trendimg = view.findViewById(R.id.upordownimg);
            rightarrow = view.findViewById(R.id.arrowbutton);
        }
    }

    final class HeaderViewHolder extends RecyclerView.ViewHolder {

        final TextView tvTitle;

        HeaderViewHolder(@NonNull View view) {
            super(view);

            tvTitle = view.findViewById(R.id.tvTitle);
        }
    }

}
