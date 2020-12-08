package com.example.easystocksearch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.os.Message;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Formatter;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import io.github.luizgrp.sectionedrecyclerviewadapter.Section;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionAdapter;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;


public class MainActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private static final int TRIGGER_AUTO_COMPLETE = 100;
    private static final long AUTO_COMPLETE_DELAY = 300;
    private Handler handler;
    private AutoSuggestAdapter autoSuggestAdapter;
    private boolean validitemclicked = false;



    public static final String mypreference = "mypref";
    public static final String portfolio = "Portfoliolist";
    public static final String portfolioshares = "PortfolioSharelist";
    public static final String favourites = "Favouriteslist";
    public static final String NetWorth = "NetWorth";
    public static final String Uninvested_cash = "AvailableCash";
    public static final String currstocksval = "Currpval";



    ArrayList portfolioTickerList = new ArrayList<String>();
    ArrayList portfolioSharesList = new ArrayList<String>();
    ArrayList favouritesTickerList = new ArrayList<String>();

    ArrayList currpriceList = new ArrayList<String>();
    ArrayList changeList = new ArrayList<String>();
    ArrayList trendList = new ArrayList<Integer>();
    ArrayList colorList = new ArrayList<Integer>();

    ArrayList favcurrpriceList = new ArrayList<String>();
    ArrayList favchangeList = new ArrayList<String>();
    ArrayList favtitleList = new ArrayList<String>();
    ArrayList favtrendList = new ArrayList<Integer>();
    ArrayList favcolorList = new ArrayList<Integer>();


    String strportfolioTicker, strportfolioShares, strfavtickers;
    float uninvestedCash,currpval=(float) 0,nworth;
    String strUC;
    String portfolioTagName, favTagName;
    PortfolioSection pfSection;
    FavouritesSection favSection;
    private LinearLayout spinner;
    private ScrollView scrollView;
    public RecyclerView recyclerView;
    TextView dateview ;
    public SectionedRecyclerViewAdapter sectionAdapter;
    Handler timerhandler = new Handler();
    Runnable runnable;
    int delay = 15000;
    int pendingRequests=0;
    SharedPreferences sharedpreferences;
    int firsttaskcompleted = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setTheme(R.style.Theme_EasyStockSearch);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(mToolbar);
        scrollView = (ScrollView) findViewById(R.id.scrollviewmain);
        spinner = (LinearLayout)findViewById(R.id.progressBarmain);
        dateview = (TextView) findViewById(R.id.todaydate);

//        sharedpreferences=getSharedPreferences(mypreference, Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor =sharedpreferences.edit();
//        editor.clear();
//
//        portfolioTickerList.add("Net Worth");
//        portfolioTickerList.add("AAPL");
//        portfolioTickerList.add("TSLA");
//        BigDecimal bdnworth = new BigDecimal(20000).setScale(2, RoundingMode.HALF_UP);
//        float newbdnworth = bdnworth.floatValue();
//        portfolioSharesList.add(String.valueOf(newbdnworth));
//        portfolioSharesList.add("1.0");
//        portfolioSharesList.add("1.0");
//        favouritesTickerList.add("MSFT");
//        favouritesTickerList.add("NVDA");
//        favouritesTickerList.add("AAPL");
//        currpriceList.add("dummy");
//        changeList.add("dummy");
//        trendList.add(-1);
//        colorList.add(-1);
//        editor.putFloat(NetWorth, (float) 20000);
//        editor.putString(portfolio, String.join(",", portfolioTickerList));
//        editor.putString(portfolioshares, String.join(",", portfolioSharesList));
//        editor.putString(favourites,String.join(",", favouritesTickerList));
//
//
//        editor.putFloat(Uninvested_cash,(float) 0);
//        editor.putFloat(currstocksval,(float) 0);
//        editor.commit();

//        if(sharedpreferences.contains(Uninvested_cash)){
//            uninvestedCash = sharedpreferences.getFloat(Uninvested_cash, (float) -1);
//            strUC = String.valueOf(uninvestedCash);
//        }
//
//        if(sharedpreferences.contains(Uninvested_cash)){
//            currpval = sharedpreferences.getFloat(currstocksval, (float) -1);
//
//        }
        Log.i("size of portfoliotickerlist on create", String.valueOf(portfolioTickerList.size()));
        Log.i("size of currpricelist on create", String.valueOf(currpriceList.size()));
        sectionAdapter = new SectionedRecyclerViewAdapter();
        pfSection = new PortfolioSection(getApplicationContext(),"PORTFOLIO",portfolioTickerList,portfolioSharesList,currpriceList,trendList,colorList,changeList);
        portfolioTagName = sectionAdapter.addSection(pfSection);
        favSection = new FavouritesSection(getApplicationContext(),"FAVORITES",favouritesTickerList,portfolioTickerList,portfolioSharesList,currpriceList,trendList,colorList,changeList,favtitleList,favcurrpriceList,favtrendList,favcolorList,favchangeList);
        favTagName = sectionAdapter.addSection(favSection);
        recyclerView = (RecyclerView) findViewById(R.id.mainrecyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setAdapter(sectionAdapter);

        enableSwipeToDelete();


        new Task().execute();

        timerhandler.postDelayed(runnable = new Runnable() {
            public void run() {
                Log.i("This method is run every 15 seconds","in Main Activity");
//                Toast.makeText(MainActivity.this, "This method is run every 15 seconds", Toast.LENGTH_SHORT).show();
//                new ResumeTask(1000).execute();
                timerhandler.postDelayed(runnable, delay);
            }
        }, delay);

    }

    private void enableSwipeToDelete(){
        SwipeToDeleteCallback swipeToDeleteCallback = new SwipeToDeleteCallback(this) {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

                final int position = viewHolder.getAdapterPosition();
                Map<String,Section> sectionmap = sectionAdapter.getCopyOfSectionsMap();
                Section section = sectionAdapter.getSectionForPosition(position);
                Section favsection = sectionmap.get(favTagName);
                if(section != favsection){return;}

                int posinadapter = sectionAdapter.getPositionInSection(position);
                Log.i("posin adapter",String.valueOf(posinadapter));

                strfavtickers = sharedpreferences.getString(favourites, "");
                String[] favelements = strfavtickers.split(",");
                List<String> fixedfavList = Arrays.asList(favelements);
                favouritesTickerList = new ArrayList<String>(fixedfavList);

                favSection.removeItem(posinadapter);
                sectionAdapter.notifyDataSetChanged();

                favouritesTickerList.remove(posinadapter);
                strfavtickers = String.join(",",favouritesTickerList);
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString(favourites,strfavtickers);
                editor.commit();



            }
        };

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeToDeleteCallback);
        itemTouchhelper.attachToRecyclerView(recyclerView);
    }

    class Task extends AsyncTask<String, Integer, Boolean> {
        @Override
        protected void onPreExecute() {

            spinner.setVisibility(View.VISIBLE);
            scrollView.setVisibility(View.GONE);
            sharedpreferences=getSharedPreferences(mypreference, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor =sharedpreferences.edit();
            editor.clear();

        portfolioTickerList.add("Net Worth");
        portfolioTickerList.add("AAPL");
        portfolioTickerList.add("TSLA");
        BigDecimal bdnworth = new BigDecimal(20000).setScale(2, RoundingMode.HALF_UP);
        float newbdnworth = bdnworth.floatValue();
        portfolioSharesList.add(String.valueOf(newbdnworth));
        portfolioSharesList.add("1.0");
        portfolioSharesList.add("1.0");
        favouritesTickerList.add("MSFT");
        favouritesTickerList.add("NVDA");

        currpriceList.add("dummy");
            currpriceList.add("dummy");
            currpriceList.add("dummy");
        changeList.add("dummy");
            changeList.add("dummy");
            changeList.add("dummy");
        trendList.add(-1);
            trendList.add(-1);
            trendList.add(-1);
        colorList.add(-1);
            colorList.add(-1);
            colorList.add(-1);

            favcurrpriceList.add("dummy");
            favcurrpriceList.add("dummy");
           favtitleList.add("dummy");
            favtitleList.add("dummy");

            favtrendList.add(-1);
            favtrendList.add(-1);
            favchangeList.add("dummy");
            favchangeList.add("dummy");
            favcolorList.add(-1);
            favcolorList.add(-1);



        editor.putFloat(NetWorth, (float) 20000);
            editor.putString(portfolio, String.join(",", portfolioTickerList));
            editor.putString(portfolioshares, String.join(",", portfolioSharesList));
            editor.putString(favourites,String.join(",", favouritesTickerList));


            editor.putFloat(Uninvested_cash,(float) 0);
            editor.putFloat(currstocksval,(float) 0);
            editor.commit();

            if (sharedpreferences.contains(portfolio) && sharedpreferences.contains(portfolioshares)) {


                strportfolioTicker = sharedpreferences.getString(portfolio, "");
                Log.i("strportfolioTicker", strportfolioTicker);
                String[] tickerelements = strportfolioTicker.split(",");
                List<String> fixedtickerList = Arrays.asList(tickerelements);
                portfolioTickerList = new ArrayList<String>(fixedtickerList);

                strportfolioShares = sharedpreferences.getString(portfolioshares, "");
                Log.i("strportfolioshares", strportfolioShares);
                String[] tickershareelements = strportfolioShares.split(",");
                List<String> fixedtickersharesList = Arrays.asList(tickershareelements);
                portfolioSharesList = new ArrayList<String>(fixedtickersharesList);
            }
            currpriceList.clear();
            changeList.clear();
            trendList.clear();
            colorList.clear();

            for(int z=0; z<portfolioTickerList.size();z++) {
                currpriceList.add("dummy");
                changeList.add("dummy");
                trendList.add(-1);
                colorList.add(-1);

            }



            if (sharedpreferences.contains(favourites)) {


                strfavtickers = sharedpreferences.getString(favourites, "");
                Log.i("strfavtickers",strfavtickers);
                String[] favelements = strfavtickers.split(",");
                List<String> fixedfavList = Arrays.asList(favelements);
                favouritesTickerList = new ArrayList<String>(fixedfavList);
            }


            favcurrpriceList.clear();
            favtitleList.clear();
            favchangeList.clear();
            favtrendList.clear();
            favcolorList.clear();

            for(int z=0; z<favouritesTickerList.size();z++) {


                favtitleList.add("dummy");
                favcurrpriceList.add("dummy");
                favchangeList.add("dummy");
                favtrendList.add(-1);
                favcolorList.add(-1);
            }
            if(sharedpreferences.contains(Uninvested_cash)){
                uninvestedCash = sharedpreferences.getFloat(Uninvested_cash, (float) -1);
                strUC = String.valueOf(uninvestedCash);
            }

            if(sharedpreferences.contains(currstocksval)){
                currpval = sharedpreferences.getFloat(currstocksval, (float) -1);

            }
            Log.i("size of portfolio ticker list in pre execute", String.valueOf(portfolioTickerList.size()));
            Log.i("size of currpricelist in pre execute", String.valueOf(currpriceList.size()));
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Boolean result) {

            Log.i("size of currpricelist on post execute", String.valueOf(currpriceList.size()));
            Log.i("size of trendlist on post execute", String.valueOf(trendList.size()));


            nworth = Float.parseFloat(String.valueOf(portfolioSharesList.get(0)));
            for(int k = 1 ;k< portfolioSharesList.size();k++){
                currpval = currpval+ (Float.parseFloat(String.valueOf(portfolioSharesList.get(k))) * Float.parseFloat(String.valueOf(currpriceList.get(k))));
            }
            uninvestedCash = nworth-currpval;
            BigDecimal bdUC = new BigDecimal(uninvestedCash).setScale(2, RoundingMode.HALF_UP);
            float newbdUC = bdUC.floatValue();
            Log.i("initial Net worth",String.valueOf(nworth));
            Log.i("initial UC",String.valueOf(newbdUC));
            SharedPreferences.Editor editor =sharedpreferences.edit();
            editor.putFloat(Uninvested_cash,newbdUC);
            BigDecimal bdcurrpval = new BigDecimal(currpval).setScale(2, RoundingMode.HALF_UP);
            float newbdcurrpval = bdcurrpval.floatValue();
            Log.i("initial stock p val",String.valueOf(newbdcurrpval));
            editor.putFloat(currstocksval,newbdcurrpval);
            editor.commit();

            spinner.setVisibility(View.GONE);
            scrollView.setVisibility(View.VISIBLE);

                pfSection.refreshData(portfolioTickerList, portfolioSharesList, currpriceList, trendList, colorList, changeList);
                favSection.refreshData(portfolioTickerList, portfolioSharesList, favouritesTickerList, currpriceList, trendList, colorList, changeList, favtitleList, favcurrpriceList, favtrendList, favcolorList, favchangeList);

                sectionAdapter.notifyDataSetChanged();

                firsttaskcompleted = 1;

//                timerhandler.postDelayed(runnable = new Runnable() {
//                public void run() {
//                timerhandler.postDelayed(runnable, delay);
//                Toast.makeText(MainActivity.this, "This method is run every 15 seconds", Toast.LENGTH_SHORT).show();
//                    new ResumeTask(6000).execute();
//                    }
//                    }, delay);

            super.onPostExecute(result);
        }

        @Override
        protected Boolean doInBackground(String... params) {
            int i,j;
            Locale locale = new Locale("en", "US");
            String pattern = "MMMM dd, yyyy";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
            String date = simpleDateFormat.format(new Date());
            dateview.setText(date);


            if (sharedpreferences.contains(portfolio) && sharedpreferences.contains(portfolioshares)) {


                strportfolioTicker = sharedpreferences.getString(portfolio, "");
                Log.i("strportfolioTicker",strportfolioTicker);
                String[] tickerelements = strportfolioTicker.split(",");
                List<String> fixedtickerList = Arrays.asList(tickerelements);
                portfolioTickerList = new ArrayList<String>(fixedtickerList);

                strportfolioShares = sharedpreferences.getString(portfolioshares, "");
                Log.i("strportfolioshares",strportfolioShares);
                String[] tickershareelements = strportfolioShares.split(",");
                List<String> fixedtickersharesList = Arrays.asList(tickershareelements);
                portfolioSharesList = new ArrayList<String>(fixedtickersharesList);


                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                Log.i("size of portfolioTickerList", String.valueOf(portfolioTickerList.size()));
                for(i=1; i<portfolioTickerList.size();i++) {


                    String url = "https://stock-search-nodejs-be.wl.r.appspot.com/details/" + portfolioTickerList.get(i); // .replaceAll("\\s", "")
                    Log.i("url is ",url);
                    // Request a string response from the provided URL.
                    int finalI = i;

                    StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {

                                    try {

                                        Log.i("onResponse received for portfolio", String.valueOf(finalI));
                                        JSONObject responseObject = new JSONObject(response);

                                        currpriceList.set(finalI,responseObject.getString("last"));
                                        double change = (responseObject.getDouble("last") - responseObject.getDouble("prevClose"));
                                        BigDecimal bdchange = new BigDecimal(change).setScale(2, RoundingMode.HALF_UP);
                                        double newbdchange = bdchange.doubleValue();
                                        changeList.set(finalI,String.valueOf(Math.abs(newbdchange)));

                                        if (newbdchange < 0) {
                                            trendList.set(finalI,R.drawable.ic_baseline_trending_down_24);
                                            colorList.set(finalI,R.color.red);

                                        } else if (newbdchange > 0) {
                                            trendList.set(finalI,R.drawable.ic_twotone_trending_up_24);
                                            colorList.set(finalI,R.color.green);
                                        }


                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.i("volley error", String.valueOf(error));
                        }
                    });

                    // Add the request to the RequestQueue.
                    queue.add(stringRequest);
                }
            }
            if (sharedpreferences.contains(favourites)) {


                strfavtickers = sharedpreferences.getString(favourites, "");
                Log.i("strfavtickers",strfavtickers);
                String[] favelements = strfavtickers.split(",");
                List<String> fixedfavList = Arrays.asList(favelements);
                favouritesTickerList = new ArrayList<String>(fixedfavList);


                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                Log.i("size of favlist", String.valueOf(favouritesTickerList.size()));
                for(j=0; j<favouritesTickerList.size();j++) {

                    String url = "https://stock-search-nodejs-be.wl.r.appspot.com/details/" + favouritesTickerList.get(j); // .replaceAll("\\s", "")
                    Log.i("url is ",url);
                    // Request a string response from the provided URL.
                    int finalI = j;

                    StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {

                                    try {

                                        Log.i("onResponse received for fav", String.valueOf(finalI));
                                        JSONObject responseObject = new JSONObject(response);

                                        favcurrpriceList.set(finalI,responseObject.getString("last"));
                                        favtitleList.set(finalI,responseObject.getString("name"));
                                        double change = (responseObject.getDouble("last") - responseObject.getDouble("prevClose"));
                                        BigDecimal bdchange = new BigDecimal(change).setScale(2, RoundingMode.HALF_UP);
                                        double newbdchange = bdchange.doubleValue();
                                        favchangeList.set(finalI,String.valueOf(Math.abs(newbdchange)));

                                        if (newbdchange < 0) {
                                            favtrendList.set(finalI, R.drawable.ic_baseline_trending_down_24);
                                            favcolorList.set(finalI,R.color.red);

                                        } else if (newbdchange > 0) {
                                            favtrendList.set(finalI,R.drawable.ic_twotone_trending_up_24);
                                            favcolorList.set(finalI,R.color.green);
                                        }


                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.i("volley error", String.valueOf(error));
                        }
                    });

                    // Add the request to the RequestQueue.
                    queue.add(stringRequest);
                }
            }
            try {
                Thread.sleep(7000);
            } catch (Exception e) {
                e.printStackTrace();
            }

            Log.i("size of currpricelist in background before exiting", String.valueOf(currpriceList.size()));
            Log.i("exited in background fav","bye bye");
            return null;
        }
    }

    public void onClick(View v){
        this.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.tiingo.com/")));
    }


    @Override
    protected void onResume() {

        Log.i("inside Resume", "--on Resume--");

        if(firsttaskcompleted == 1){
            Log.i("first task completed at this point","in Resume phase");
            new ResumeTask(3000).execute();
            //set the callback here
            timerhandler.postDelayed(runnable = new Runnable() {
            public void run() {
                Log.i("This method is run every 15 seconds","in Main Activity");

                timerhandler.postDelayed(runnable, delay);
                    }
                    }, delay);
        }

        //create task to update the screen for new/removed favourites or portfolios


            super.onResume();

    }

    @Override
    protected void onPause() {
        handler.removeCallbacks(runnable); //stop handler when activity not visible
        super.onPause();
    }

    class ResumeTask extends AsyncTask<String, Integer, Boolean> {

        private int delay_dur;
        ResumeTask(int d){
            this.delay_dur = d;
        }
        @Override
        protected void onPreExecute() {
            if (delay_dur == 3000){
                spinner.setVisibility(View.VISIBLE);
                scrollView.setVisibility(View.GONE);
            }
            super.onPreExecute();
        }


        @Override
        protected void onPostExecute(Boolean result) {

            Log.i("calculating net worth on post execute in resume activity","from uc and curr stock val");
            uninvestedCash = sharedpreferences.getFloat(Uninvested_cash,-1);
//            currpval = sharedpreferences.getFloat(currstocksval,-1);
//            nworth = Float.parseFloat(String.valueOf(portfolioSharesList.get(0)));
            strportfolioTicker = sharedpreferences.getString(portfolio, "");
            Log.i("POST EXECUTE IN RESUME : strportflioshareslist is",strportfolioShares);
            Log.i("POST EXECUTE IN RESUME : currlist", String.join(",",currpriceList));
            float tempval = (float) 0;
            for(int k = 1 ;k< portfolioSharesList.size();k++){
                tempval = tempval + (Float.parseFloat(String.valueOf(portfolioSharesList.get(k))) * Float.parseFloat(String.valueOf(currpriceList.get(k))));
            }
            BigDecimal bdtempval = new BigDecimal(tempval).setScale(2, RoundingMode.HALF_UP);
            float newbdtempval = bdtempval.floatValue();
            currpval = newbdtempval;
            nworth = currpval + uninvestedCash;
            BigDecimal bdnworth = new BigDecimal(nworth).setScale(2, RoundingMode.HALF_UP);
            float newbdnworth = bdnworth.floatValue();
            SharedPreferences.Editor editor = sharedpreferences.edit();
            portfolioSharesList.set(0,String.valueOf(newbdnworth));
            editor.putString(portfolioshares,String.join(",",portfolioSharesList));
            editor.commit();

            if (delay_dur == 3000){
                spinner.setVisibility(View.GONE);
                scrollView.setVisibility(View.VISIBLE);
            }
            Log.i(" Resume:  inside post execute ", String.valueOf(currpriceList.size()));
            Log.i(" Resume : size of currepricelist on post execute ", String.valueOf(currpriceList.size()));
            Log.i(" Resume : size of trendlist on post execute ", String.valueOf(trendList.size()));
            pfSection.refreshData(portfolioTickerList, portfolioSharesList, currpriceList, trendList, colorList, changeList);
            favSection.refreshData(portfolioTickerList, portfolioSharesList, favouritesTickerList, currpriceList, trendList, colorList, changeList, favtitleList, favcurrpriceList, favtrendList, favcolorList, favchangeList);

            sectionAdapter.notifyDataSetChanged();
            super.onPostExecute(result);
        }

        @Override
        protected Boolean doInBackground(String... params) {
            int i,j;

            if (sharedpreferences.contains(portfolio) && sharedpreferences.contains(portfolioshares)) {


                strportfolioTicker = sharedpreferences.getString(portfolio, "");
                Log.i("strportfolioTicker",strportfolioTicker);
                String[] tickerelements = strportfolioTicker.split(",");
                List<String> fixedtickerList = Arrays.asList(tickerelements);
                portfolioTickerList = new ArrayList<String>(fixedtickerList);

                strportfolioShares = sharedpreferences.getString(portfolioshares, "");
                Log.i("strportfolioshares",strportfolioShares);
                String[] tickershareelements = strportfolioShares.split(",");
                List<String> fixedtickersharesList = Arrays.asList(tickershareelements);
                portfolioSharesList = new ArrayList<String>(fixedtickersharesList);

                currpriceList.clear();
                changeList.clear();
                trendList.clear();
                colorList.clear();

                for(int z=0; z<portfolioTickerList.size();z++) {
                    currpriceList.add("dummy");
                    changeList.add("dummy");
                    trendList.add(-1);
                    colorList.add(-1);

                }

                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                Log.i("size of portfolioTickerList", String.valueOf(portfolioTickerList.size()));
                for(i=1; i<portfolioTickerList.size();i++) {


                    String url = "https://stock-search-nodejs-be.wl.r.appspot.com/details/" + portfolioTickerList.get(i); // .replaceAll("\\s", "")
                    Log.i("url is ",url);
                    // Request a string response from the provided URL.
                    int finalI = i;

                    StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {

                                    try {

                                        Log.i("onResponse received for portfolio", String.valueOf(finalI));
                                        JSONObject responseObject = new JSONObject(response);

                                        currpriceList.set(finalI,responseObject.getString("last"));
                                        double change = (responseObject.getDouble("last") - responseObject.getDouble("prevClose"));
                                        BigDecimal bdchange = new BigDecimal(change).setScale(2, RoundingMode.HALF_UP);
                                        double newbdchange = bdchange.doubleValue();
                                        changeList.set(finalI,String.valueOf(Math.abs(newbdchange)));

                                        if (newbdchange < 0) {
                                            trendList.set(finalI,R.drawable.ic_baseline_trending_down_24);
                                            colorList.set(finalI,R.color.red);

                                        } else if (newbdchange > 0) {
                                            trendList.set(finalI,R.drawable.ic_twotone_trending_up_24);
                                            colorList.set(finalI,R.color.green);
                                        }
                                        else{
                                            trendList.set(finalI,R.drawable.whitebg);
                                            colorList.set(finalI,R.color.lightgrey);
                                        }


                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.i("volley error", String.valueOf(error));
                        }
                    });

                    // Add the request to the RequestQueue.
                    queue.add(stringRequest);
                }
            }
            if (sharedpreferences.contains(favourites)) {


                strfavtickers = sharedpreferences.getString(favourites, "");
                Log.i("strfavtickers",strfavtickers);
                String[] favelements = strfavtickers.split(",");
                List<String> fixedfavList = Arrays.asList(favelements);
                favouritesTickerList = new ArrayList<String>(fixedfavList);
                Log.i("size of favlist in resume", String.valueOf(favouritesTickerList.size()));

                favcurrpriceList.clear();
                favtitleList.clear();
                favchangeList.clear();
                favtrendList.clear();
                favcolorList.clear();

                for(int z=0; z<favouritesTickerList.size();z++) {


                    favtitleList.add("dummy");
                  favcurrpriceList.add("dummy");
                   favchangeList.add("dummy");
                    favtrendList.add(-1);
                    favcolorList.add(-1);
                }

                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

                for(j=0; j<favouritesTickerList.size();j++) {

                    String url = "https://stock-search-nodejs-be.wl.r.appspot.com/details/" + favouritesTickerList.get(j); // .replaceAll("\\s", "")
                    Log.i("url is ",url);
                    // Request a string response from the provided URL.
                    int finalI = j;

                    StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {

                                    try {

                                        Log.i("onResponse received for fav", String.valueOf(finalI));
                                        JSONObject responseObject = new JSONObject(response);

                                        favcurrpriceList.set(finalI,responseObject.getString("last"));
                                        favtitleList.set(finalI,responseObject.getString("name"));
                                        double change = (responseObject.getDouble("last") - responseObject.getDouble("prevClose"));
                                        BigDecimal bdchange = new BigDecimal(change).setScale(2, RoundingMode.HALF_UP);
                                        double newbdchange = bdchange.doubleValue();
                                        favchangeList.set(finalI,String.valueOf(Math.abs(newbdchange)));

                                        if (newbdchange < 0) {
                                            favtrendList.set(finalI, R.drawable.ic_baseline_trending_down_24);
                                            favcolorList.set(finalI,R.color.red);

                                        } else if (newbdchange > 0) {
                                            favtrendList.set(finalI,R.drawable.ic_twotone_trending_up_24);
                                            favcolorList.set(finalI,R.color.green);
                                        }
                                        else{
                                            favtrendList.set(finalI,R.drawable.whitebg);
                                            favcolorList.set(finalI,R.color.lightgrey);
                                        }


                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.i("volley error", String.valueOf(error));
                        }
                    });

                    // Add the request to the RequestQueue.
                    queue.add(stringRequest);
                }
            }

            try {
                Thread.sleep(delay_dur);
            } catch (Exception e) {
                e.printStackTrace();
            }

            Log.i(" Resume:  bg ", String.valueOf(currpriceList.size()));
            Log.i(" Resume : size of currepricelist before exiting bg ", String.valueOf(currpriceList.size()));
            Log.i(" Resume : size of trendlist before exiting bg ", String.valueOf(trendList.size()));
            return null;
        }
    }

    @SuppressLint("RestrictedApi")
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        Log.i("MainActivity","--onCreateOptionsMenu--");
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main_menu,menu);

        MenuItem item = menu.findItem(R.id.app_bar_search);
        SearchView searchView = (SearchView) item.getActionView();
        final SearchView.SearchAutoComplete autoCompleteTextView = (SearchView.SearchAutoComplete)searchView.findViewById(androidx.appcompat.R.id.search_src_text);
//        searchView.setDropDownAnchor(R.id.app_bar_search);
        autoCompleteTextView.setDropDownHeight(1100);
        autoCompleteTextView.setDropDownWidth(1000);

        //Setting up the adapter for AutoSuggest
        autoSuggestAdapter = new AutoSuggestAdapter(this ,android.R.layout.simple_dropdown_item_1line);
        autoCompleteTextView.setThreshold(3);
        autoCompleteTextView.setAdapter(autoSuggestAdapter);
        autoCompleteTextView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        validitemclicked = true;
                        autoCompleteTextView.setText(autoSuggestAdapter.getObject(position));

                    }
                });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(validitemclicked == true){
                    Log.i("query string is",query);
                    Intent myIntent = new Intent(getApplicationContext(), StockDetails.class);
                    myIntent.putExtra("ticker",query);
                    startActivity(myIntent);
                }

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        autoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.i("TAG","--onTextChanged--");
                handler.removeMessages(TRIGGER_AUTO_COMPLETE);
                handler.sendEmptyMessageDelayed(TRIGGER_AUTO_COMPLETE, AUTO_COMPLETE_DELAY);
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if (msg.what == TRIGGER_AUTO_COMPLETE) {
                    if (!TextUtils.isEmpty(autoCompleteTextView.getText())) {
                        Editable q = autoCompleteTextView.getText();
                        Log.i("TAG", String.valueOf(q));
                        makeApiCall(autoCompleteTextView.getText().toString());
                    }
                }
                return false;
            }
        });


        return true;
    }

    private void makeApiCall(String text) {
        ApiCall.make(this, text, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("TAG","Inside --onResponse--");
                //parsing logic, please change it as per your requirement
                List<String> stringList = new ArrayList<>();
                try {
                    Log.i("String Object is", response);
//                    JSONObject responseObject = new JSONObject(response);

                    JSONArray array = new JSONArray(response);
//                    JSONArray array = responseObject.getJSONArray("results");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject row = array.getJSONObject(i);
                        stringList.add(row.getString("ticker") + " - " + row.getString("name"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //IMPORTANT: set data here and notify
                autoSuggestAdapter.setData(stringList);
                autoSuggestAdapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
    }
}