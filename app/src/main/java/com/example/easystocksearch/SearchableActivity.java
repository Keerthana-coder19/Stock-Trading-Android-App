package com.example.easystocksearch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

public class SearchableActivity extends AppCompatActivity {

//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        Log.i("SA","inside onCreate");
//        super.onCreate(savedInstanceState);
//        handleIntent(getIntent());
//    }
//
//    @Override
//    protected void onNewIntent(Intent intent) {
//        Log.i("SA","inside onNewIntent");
//        super.onNewIntent(intent);
//        handleIntent(intent);
//    }
//
//    private void handleIntent(Intent intent) {
//
//        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
//            String query = intent.getStringExtra(SearchManager.QUERY);
//            //use the query to search your data somehow
//            Log.i("Query",query);
//        }
//    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);
        // Get the intent, verify the action and get the query
        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            Log.i("Query",query);
            doMySearch(query);

        }
    }
//
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//
//        Log.i("Stockapplication : SearchableActivity","inside onCreateOptionsMenu");
//        // Inflate the search menu action bar.
//        MenuInflater menuInflater = getMenuInflater();
//        menuInflater.inflate(R.menu.main_menu, menu);
//
//        // Get the search menu.
////        MenuItem searchMenu = menu.findItem(R.id.app_bar_search);
//
//        // Get SearchView object.
//
//        SearchView searchView = (SearchView) menu.findItem(R.id.app_bar_search).getActionView();
//        // Get SearchView autocomplete object.
//
//        SearchView.SearchAutoComplete searchAutoComplete = searchView.findViewById(androidx.appcompat.R.id.search_src_text);
////        final SearchView.SearchAutoComplete searchAutoComplete = (SearchView.SearchAutoComplete) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
//
//
//        // Create a new ArrayAdapter and add data to search auto complete object.
//        String dataArr[] = {"Apple", "Amazon", "Amd", "Microsoft", "Microwave", "MicroNews", "Intel", "Intelligence"};
//        ArrayAdapter<String> newsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, dataArr);
//        searchAutoComplete.setAdapter(newsAdapter);
//
//        // Listen to search view item on click event.
//        searchAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int itemIndex, long id) {
//                String queryString = (String) adapterView.getItemAtPosition(itemIndex);
//                searchAutoComplete.setText("" + queryString);
//
//            }
//        });



//        return super.onCreateOptionsMenu(menu);
//    }


    private void doMySearch(String query) {

    }
}