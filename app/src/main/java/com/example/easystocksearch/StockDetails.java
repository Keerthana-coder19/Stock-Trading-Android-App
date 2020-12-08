package com.example.easystocksearch;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigDecimal;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.gridlayout.widget.GridLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.RoundingMode;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class StockDetails extends AppCompatActivity {

    private Toolbar myChildToolbar;
    private GridLayout mygridLayout1;
    private GridView mygridView;
    String[] stat_values = {"Current Price:","Low:","Bid Price:","Open price:","Mid:","High:","Volume:"};
    private TextView textview1;
    private TextView textview2;
    private TextView textview3;
    private TextView textview4;
    private TextView about;
    private Button btShowmore;
    private CardView cardView;
    private WebView webView;
    final Context context = this;
    private LinearLayout spinner;
    private ScrollView scrollView;
    boolean removeSpinner;
    private Button trade;
    private TextView textview6;
    private TextView textview7;

    public static final String NetWorth = "NetWorth";
    public static final String Uninvested_cash = "AvailableCash";
    public static final String currstocksval = "Currpval";

    float uninvestedCash,currpval=(float) 0,nworth,currprice;


    Handler timerhandler = new Handler();
    Runnable runnable;
    int delay = 15000;
    String query;

    SharedPreferences sharedpreferences;
    public static final String mypreference = "mypref";
    public static final String portfolio = "Portfoliolist";
    public static final String portfolioshares = "PortfolioSharelist";
    public static final String favourites = "Favouriteslist";
//  String[] portfolioTickerList;
//    String[] portfolioSharesList;
//    String[] favouritesTickerList;

    ArrayList portfolioTickerList = new ArrayList<String>();
    ArrayList portfolioSharesList = new ArrayList<String>();
    ArrayList favouritesTickerList = new ArrayList<String>();

    float result,shareno,totalshare,marketvalue;

    String strportfolioTicker, strportfolioShares, strfavtickers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_details);

        Intent myIntent = getIntent(); // gets the previously created intent
        String fullquery = myIntent.getStringExtra("ticker");
        Log.i("fullquery",fullquery);
        String[] querylist = fullquery.split("-");
        query = querylist[0].replaceAll("\\s", "");
        Log.i("Query in stockdetails activity",query);
//        spinner = (ProgressBar)findViewById(R.id.progressBar1);
        // my_child_toolbar is defined in the layout file
        myChildToolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(myChildToolbar);

        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);

        scrollView = findViewById(R.id.scrollView2);
        spinner = (LinearLayout)findViewById(R.id.progressBar1);
        removeSpinner = false;
        spinner.setVisibility(View.VISIBLE);
        scrollView.setVisibility(View.GONE);

        sharedpreferences = getSharedPreferences(mypreference, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();


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

        }
        if (sharedpreferences.contains(favourites)) {

            strfavtickers = sharedpreferences.getString(favourites, "");
            Log.i("strfavtickers",strfavtickers);
            String[] favelements = strfavtickers.split(",");
            List<String> fixedfavList = Arrays.asList(favelements);
            favouritesTickerList = new ArrayList<String>(fixedfavList);
        }

        uninvestedCash = sharedpreferences.getFloat(Uninvested_cash, (float) -1);
        currpval = sharedpreferences.getFloat(Uninvested_cash, (float)-1);

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        Log.i("in Stock Details activity",query);
        String details_url ="https://stock-search-nodejs-be.wl.r.appspot.com/details/" + query;
        String news_url = "https://stock-search-nodejs-be.wl.r.appspot.com/topnews/" + query;
        // Request a string response from the provided URL.
        StringRequest detailsrequest = new StringRequest(Request.Method.GET, details_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("on Response in Stock Details activity",query);
//                        List<String> stringList = new ArrayList<>();
                        mygridLayout1 = findViewById(R.id.gridlayout1);
                        textview1 = findViewById(R.id.textView1);
                        textview2 = findViewById(R.id.textView2);
                        textview3 = findViewById(R.id.textView3);
                        textview4 = findViewById(R.id.textView4);
                        btShowmore = findViewById(R.id.btShowmore);
                        mygridView = findViewById(R.id.statsgridview);
                        about = findViewById(R.id.textViewdesc);
                        textview6 = findViewById(R.id.textView6);
                        textview7 = findViewById(R.id.textView7);

                        btShowmore.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                if (btShowmore.getText().toString().equalsIgnoreCase("Show more..."))
                                {
                                    about.setMaxLines(Integer.MAX_VALUE);//your TextView
                                    btShowmore.setText("Show less");
                                }
                                else
                                {
                                    about.setMaxLines(2);//your TextView
                                    btShowmore.setText("Show more...");
                                }
                            }
                        });

                        trade = findViewById(R.id.tradebutton);
                        trade.setOnClickListener(new View.OnClickListener(){
                            @Override
                            public void onClick(View v) {
                                final Dialog dialog = new Dialog(context);
                                dialog.setContentView(R.layout.trade_dialog);

                                // set the custom dialog components - text, image and buttons
                                TextView title = (TextView) dialog.findViewById(R.id.dialogtitle);
                                EditText et = (EditText) dialog.findViewById(R.id.editTextNumber);
                                TextView dt = (TextView) dialog.findViewById(R.id.dialogtotal);
                                TextView dn = (TextView) dialog.findViewById(R.id.dialognote);

                                title.setText("Trade "+ textview3.getText() +" shares");
                                dt.setText("0 x "+ textview2.getText() +"/share = $0.00");
                                dn.setText( "$" + uninvestedCash + " available to buy " + query);


                                 class InputValidator implements TextWatcher {

                                    public void afterTextChanged(Editable s) {
                                        shareno = Float.parseFloat(s.toString());
                                        result = shareno*currprice; //Float.parseFloat(textview2.getText().toString())
                                        dt.setText(s+" x "+ textview2.getText()+ "/share =" + result );
                                    }
                                    public void beforeTextChanged(CharSequence s, int start, int count,
                                                                  int after) {

                                    }
                                    public void onTextChanged(CharSequence s, int start, int before,
                                                              int count) {

                                    }
                                }
                                et.addTextChangedListener(new InputValidator());



                                Button dialogButtonB = (Button) dialog.findViewById(R.id.buy);
                                Button dialogButtonS = (Button) dialog.findViewById(R.id.sell);
                                // if button is clicked, close the custom dialog
                                dialogButtonB.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if(portfolioTickerList.contains(query)){
                                            Log.i(query,"is present");
                                            int index = portfolioTickerList.indexOf(query);
                                            totalshare = Float.parseFloat(portfolioSharesList.get(index).toString());
                                            totalshare = totalshare + shareno;

                                            portfolioSharesList.set(index,String.valueOf(totalshare));


                                            Log.i("portfolioTickerlist after buying", String.join(",",portfolioTickerList));
                                            Log.i("portfoliosharelist after buying", String.join(",",portfolioSharesList));

                                        }
                                        else{
                                            Log.i(query,"not present in ticker list");
                                            portfolioTickerList.add(query);
                                            portfolioSharesList.add(String.valueOf(shareno));
                                            totalshare = shareno;
                                        }

                                        textview6.setText("Shares owned: "+ totalshare);
                                        textview7.setText("Market Value: $"+ totalshare*currprice);
                                        marketvalue = totalshare*currprice;

                                        currpval = sharedpreferences.getFloat(currstocksval,-1);
                                        currpval = currpval + result;
                                        uninvestedCash = sharedpreferences.getFloat(Uninvested_cash,-1);
                                        uninvestedCash = uninvestedCash - shareno*currprice;
                                        nworth = currpval + uninvestedCash;

                                        BigDecimal bduc = new BigDecimal(uninvestedCash).setScale(2, RoundingMode.HALF_UP);
                                        float newbduc = bduc.floatValue();
                                        editor.putFloat(Uninvested_cash,newbduc);
                                        BigDecimal bdp = new BigDecimal(currpval).setScale(2, RoundingMode.HALF_UP);
                                        float newbdp = bdp.floatValue();
                                        editor.putFloat(currstocksval,newbdp);
                                        BigDecimal bdnw = new BigDecimal(nworth).setScale(2, RoundingMode.HALF_UP);
                                        float newbdnw = bdnw.floatValue();

                                        portfolioSharesList.set(0,String.valueOf(newbdnw));
                                        strportfolioShares = String.join(",",portfolioSharesList);
                                        strportfolioTicker = String.join(",",portfolioTickerList);

                                        editor.putString(portfolioshares,strportfolioShares);
                                        editor.putString(portfolio,strportfolioTicker);
                                        editor.commit();

                                        dialog.dismiss();

                                    }

                                });

                                dialogButtonS.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        if(portfolioTickerList.contains(query)){
                                            Log.i(query,"is present");
                                            int index = portfolioTickerList.indexOf(query);
                                            totalshare = Float.parseFloat(portfolioSharesList.get(index).toString());
                                            totalshare = totalshare - shareno;
                                             if(totalshare == 0) {
                                                portfolioSharesList.remove(index);
                                                portfolioTickerList.remove(index);
                                            }
                                            else {
                                                 portfolioSharesList.set(index, String.valueOf(totalshare));
                                             }
                                            Log.i("portfolioTickerlist after selling", String.join(",",portfolioTickerList));
                                            Log.i("portfolioTickerlist after selling", String.join(",",portfolioSharesList));

                                        }
//                                        else{
//                                            Log.i(query,"not present in ticker list");
//                                            portfolioTickerList.add(query);
//                                            portfolioSharesList.add(String.valueOf(shareno));
//                                            totalshare = shareno;
//                                        }

                                        textview6.setText("Shares owned: "+ totalshare);
                                        textview7.setText("Market Value: $"+ totalshare*currprice);
                                        marketvalue = totalshare*currprice;

                                        currpval = sharedpreferences.getFloat(currstocksval,-1);
                                        currpval = currpval - result;
                                        uninvestedCash = sharedpreferences.getFloat(Uninvested_cash,-1);
                                        uninvestedCash = uninvestedCash + shareno*currprice;
                                        nworth = currpval + uninvestedCash;

                                        BigDecimal bduc = new BigDecimal(uninvestedCash).setScale(2, RoundingMode.HALF_UP);
                                        float newbduc = bduc.floatValue();
                                        editor.putFloat(Uninvested_cash,newbduc);
                                        BigDecimal bdp = new BigDecimal(currpval).setScale(2, RoundingMode.HALF_UP);
                                        float newbdp = bdp.floatValue();
                                        editor.putFloat(currstocksval,newbdp);
                                        BigDecimal bdnw = new BigDecimal(nworth).setScale(2, RoundingMode.HALF_UP);
                                        float newbdnw = bdnw.floatValue();

                                        portfolioSharesList.set(0,String.valueOf(newbdnw));
                                        strportfolioShares = String.join(",",portfolioSharesList);
                                        strportfolioTicker = String.join(",",portfolioTickerList);

                                        editor.putString(portfolioshares,strportfolioShares);
                                        editor.putString(portfolio,strportfolioTicker);
                                        editor.commit();

                                        dialog.dismiss();
                                    }
                                });

                                dialog.show();

                            }
                        });

                        try {
                            Log.i("String Object is", response);
                            JSONObject responseObject = new JSONObject(response);

                            Locale locale = new Locale("en", "US");
                            NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(locale);
                            BigDecimal bdcurr = new BigDecimal(responseObject.getDouble("last")).setScale(2, RoundingMode.HALF_UP);
                            double newbdcurr = bdcurr.doubleValue();
                            currprice = (float) newbdcurr;
                            textview1.setText(responseObject.getString("ticker"));
                            textview2.setText(currencyFormatter.format(newbdcurr));
                            textview3.setText(responseObject.getString("name"));

                            double change = (responseObject.getDouble("last") - responseObject.getDouble("prevClose"));
                            BigDecimal bdchange = new BigDecimal(change).setScale(2, RoundingMode.HALF_UP);
                            double newbdchange = bdchange.doubleValue();

                            textview4.setText(currencyFormatter.format(newbdchange));
                            if(newbdchange < 0){
                                textview4.setTextColor(ContextCompat.getColor(context,R.color.red ));
                            }
                            else if(newbdchange > 0){
                                textview4.setTextColor(ContextCompat.getColor(context,R.color.green ));
                            }
                            else{
                                textview4.setTextColor(ContextCompat.getColor(context,R.color.black ));
                            }

                            if(responseObject.getString("last") == "null")
                                stat_values[0] += " 0.0";
                            else
                                stat_values[0] += " " + responseObject.getString("last");
                            if(responseObject.getString("low") == "null")
                                stat_values[1] += " 0.0";
                            else
                                stat_values[1] += " " + responseObject.getString("low");
                            if(responseObject.getString("bidPrice") == "null")
                                stat_values[2] += " 0.0";
                            else
                                stat_values[2] += " " + responseObject.getString("bidPrice");
                            if(responseObject.getString("open") == "null")
                                stat_values[3] += " 0.0";
                            else
                                stat_values[3] += " " + responseObject.getString("open");
                            if(responseObject.getString("mid") == "null")
                                stat_values[4] += " 0.0";
                            else
                                stat_values[4] += " " + responseObject.getString("mid");
                            if(responseObject.getString("high") == "null")
                                stat_values[5] += " 0.0";
                            else
                                stat_values[5] += " " + responseObject.getString("high");
                            if(responseObject.getString("volume") == "null")
                                stat_values[6] += " 0.0";
                            else
                                stat_values[6] += " " + responseObject.getString("volume");

                            StatsCustomAdapter customAdapter = new StatsCustomAdapter(getApplicationContext(), stat_values);
                            mygridView.setAdapter(customAdapter);
                            about.setText(responseObject.getString("description"));

                            int index = portfolioTickerList.indexOf(query);
                            if(index > 0){
                                textview6.setText("Shares owned: "+ portfolioSharesList.get(index));
                                textview7.setText("Market Value: $"+ Float.parseFloat(String.valueOf(portfolioSharesList.get(index)))* Float.parseFloat(responseObject.getString("last")));
                            }
                            else{
                                textview6.setText("You have 0 shares of "+ query);
                                textview7.setText("Start trading!");
                            }


                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });



        // Request a string response from the provided URL.
        StringRequest newsRequest = new StringRequest(Request.Method.GET, news_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        CardView cardView = findViewById(R.id.card_view);
                        webView = findViewById(R.id.webview);

                        webView.getSettings().setJavaScriptEnabled(true);
                        webView.loadUrl("file:///android_asset/index.html");
                        webView.setWebViewClient(new WebViewClient(){
                            public void onPageFinished(WebView view, String url){
                                webView.loadUrl("javascript:init('" + query + "')");
                            }
                        });

                        ArrayList newsTitles = new ArrayList<>();
                        ArrayList newsImagesrc = new ArrayList<>();
                        ArrayList newsTimesago = new ArrayList<>();
                        ArrayList newsSources = new ArrayList<>();
                        ArrayList newsUrls = new ArrayList<>();

                        TextView newsrc = findViewById(R.id.news1_source);
                        TextView newstitle = findViewById(R.id.news1_title);
                        TextView newstimesago = findViewById(R.id.news1_timeago);
                        ImageView newsimg = findViewById(R.id.news1_img);
                        try{
                            JSONObject responseObject = new JSONObject(response);
                            JSONArray array = responseObject.getJSONArray("articles");

                            int c = 0;
                            for(;c<array.length();c++) {
                                JSONObject row0 = array.getJSONObject(c);
                                if (row0.has("title") && row0.has("urlToImage") && row0.has("publishedAt") && row0.has("source") && row0.has("url")) {
                                    JSONObject source = new JSONObject(row0.getString("source"));
                                    newsrc.setText(source.getString("name"));
                                    newstitle.setText(row0.getString("title"));
                                    String newsurl = row0.getString("url");
                                    String row0title = row0.getString("title");
                                    LocalDateTime localDateTime = LocalDateTime.parse((row0.getString("publishedAt")).substring(0, (row0.getString("publishedAt")).length() - 1));
                                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                                    String formatDateTime = localDateTime.format(formatter);
                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                    Date d1 = sdf.parse(formatDateTime);
                                    Date d2 = sdf.parse(sdf.format(new Date()));
                                    long difference_In_Time = d2.getTime() - d1.getTime();
                                    long difference_In_minutes = difference_In_Time / (1000 * 60);
                                    String timesAgo;
                                    if (difference_In_minutes == (long) 60)
                                        timesAgo = "1 hour ago";
                                    else if (difference_In_minutes < (long) 60)
                                        timesAgo = difference_In_minutes + " minutes ago";
                                    else if ((long) (difference_In_minutes / 60) < (long) 24)
                                        timesAgo = (difference_In_minutes / 60) + " minutes ago";
                                    else if ((long) (difference_In_minutes / 60) == (long) 24)
                                        timesAgo = "1 day ago";
                                    else
                                        timesAgo = difference_In_minutes / (60 * 24) + " days ago";

                                    newstimesago.setText(timesAgo);
                                    GlideApp.with(getApplicationContext()).load(row0.getString("urlToImage")).transform(new CenterCrop(), new RoundedCorners(75)).into(newsimg);

                                    cardView.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(newsurl)));
                                        }
                                    });

                                    cardView.setOnLongClickListener(new View.OnLongClickListener() {
                                        @Override
                                        public boolean onLongClick(View v) {

                                            final Dialog dialog = new Dialog(context);
                                            dialog.setContentView(R.layout.news_dialog);

                                            // set the custom dialog components - text, image and buttons
                                            TextView text = (TextView) dialog.findViewById(R.id.textView);
                                            ImageView image = (ImageView) dialog.findViewById(R.id.image);


                                            try
                                            {
                                                text.setText(row0.getString("title"));
                                                GlideApp.with(getApplicationContext()).load(row0.getString("urlToImage")).transform(new CenterCrop()).into(image);
                                            }
                                            catch (JSONException e)
                                            {
                                                e.printStackTrace();
                                            }


                                            ImageButton dialogButtonT = (ImageButton) dialog.findViewById(R.id.button);
                                            ImageButton dialogButtonC = (ImageButton) dialog.findViewById(R.id.button2);
                                            // if button is clicked, close the custom dialog
                                            dialogButtonT.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/intent/tweet?text=" + row0title + Uri.parse("&url=") + newsurl)));
                                                }
                                            });

                                            dialogButtonC.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(newsurl)));
                                                }
                                            });

                                            dialog.show();
                                            return false;
                                        }
                                    });


                                    break;
                                }
                            }
                            for (int i = c+1; i < array.length(); i++) {
                                JSONObject row = array.getJSONObject(i);
                                if(row.has("title") && row.has("urlToImage") && row.has("publishedAt") && row.has("source") && row.has("url")) {
                                    newsTitles.add(row.getString("title"));
                                    newsImagesrc.add(row.getString("urlToImage"));
                                    newsUrls.add(row.getString("url"));
                                    LocalDateTime localDateTime = LocalDateTime.parse((row.getString("publishedAt")).substring(0,(row.getString("publishedAt")).length()-1));
                                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                                    String formatDateTime = localDateTime.format(formatter);
                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                    Date d1 = sdf.parse(formatDateTime);
                                    Date d2 = sdf.parse(sdf.format(new Date()));
                                    long difference_In_Time = d2.getTime() - d1.getTime();
                                    long difference_In_minutes = difference_In_Time / (1000 * 60);
                                    String timesAgo;
                                    if (difference_In_minutes == (long)60)
                                        timesAgo = "1 hour ago";
                                    else if (difference_In_minutes < (long)60)
                                        timesAgo = difference_In_minutes + " minutes ago";
                                    else if ((long)(difference_In_minutes / 60) < (long)24)
                                        timesAgo = (difference_In_minutes / 60) + " minutes ago";
                                    else if ((long)(difference_In_minutes / 60) == (long)24)
                                        timesAgo = "1 day ago";
                                    else
                                        timesAgo = difference_In_minutes / (60*24) + " days ago";

                                    newsTimesago.add(timesAgo);
                                    JSONObject source = new JSONObject(row.getString("source"));
                                    newsSources.add(source.getString("name"));
                                    }
                                }



                            // get the reference of RecyclerView
                            RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
                            // set a LinearLayoutManager with default orientation
                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
                            recyclerView.setLayoutManager(linearLayoutManager); // set LayoutManager to RecyclerView
                            // call the constructor of CustomAdapter to send the reference and data to Adapter
                            NewsCustomAdapter customAdapter = new NewsCustomAdapter(StockDetails.this, newsTitles,newsImagesrc,newsSources,newsTimesago,newsUrls);
                            recyclerView.setAdapter(customAdapter); // set the Adapter to RecyclerView

                            Thread.sleep(5000);
                            spinner.setVisibility(View.GONE);
                            removeSpinner = true;
                            invalidateOptionsMenu();
                            scrollView.setVisibility(View.VISIBLE);
                            }
                        catch (Exception e) {
                            e.printStackTrace();
                        }

                        }
                    }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

    // Add the request to the RequestQueue.
        queue.add(detailsrequest);
        queue.add(newsRequest);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.stock_details_menu,menu);
        MenuItem item = menu.findItem(R.id.action_favorite);

        if (removeSpinner) {

            strfavtickers = sharedpreferences.getString(favourites,"");
            String[] favelements = strfavtickers.split(",");
            List<String> fixedfavList = Arrays.asList(favelements);
            favouritesTickerList = new ArrayList<String>(fixedfavList);
            if(favouritesTickerList.contains(query)){ //check if error occurs..........................!
                item.setIcon(R.drawable.ic_baseline_star_24);

            }
            else{
                item.setIcon(R.drawable.ic_baseline_star_border_24);
            }
            item.setVisible(true);
        }else{
            item.setVisible(false);
        }

        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu){

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                onBackPressed();
                return true;

            case R.id.action_favorite:
                // User chose the "Favorite" action, mark the current item as a favorite...
                if(item.getIcon().getConstantState() == ContextCompat.getDrawable(context,R.drawable.ic_baseline_star_24).getConstantState()){

                    strfavtickers = sharedpreferences.getString(favourites,"");
                    String[] favelements = strfavtickers.split(",");
                    List<String> fixedfavList = Arrays.asList(favelements);
                    favouritesTickerList = new ArrayList<String>(fixedfavList);
                    favouritesTickerList.remove(query);
                    Toast.makeText(StockDetails.this, "\""+query+"\"" + " was removed from favourites", Toast.LENGTH_SHORT).show();
                    strfavtickers = String.join(",",favouritesTickerList);
                    Log.i("after removing ticker from favlist",strfavtickers);
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString(favourites,strfavtickers);
                    editor.commit();
                    invalidateOptionsMenu();

                }

                if(item.getIcon().getConstantState() == ContextCompat.getDrawable(context,R.drawable.ic_baseline_star_border_24).getConstantState()){

                    strfavtickers = sharedpreferences.getString(favourites,"");
                    String[] favelements = strfavtickers.split(",");
                    List<String> fixedfavList = Arrays.asList(favelements);
                    favouritesTickerList = new ArrayList<String>(fixedfavList);
                    favouritesTickerList.add(query);
                    Toast.makeText(StockDetails.this, "\""+query+"\"" + " was added to favourites", Toast.LENGTH_SHORT).show();
                    strfavtickers = String.join(",",favouritesTickerList);
                    Log.i("after adding ticker to favlist",strfavtickers);
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString(favourites,strfavtickers);
                    editor.commit();
                    invalidateOptionsMenu();

                }

                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }
}

