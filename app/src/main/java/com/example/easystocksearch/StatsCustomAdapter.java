package com.example.easystocksearch;

import android.widget.BaseAdapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class StatsCustomAdapter extends BaseAdapter{
    Context context;
    String[] stat_values;
    LayoutInflater inflater;
    public StatsCustomAdapter(Context applicationContext,String[] stat_values ) {
        this.context = applicationContext;
        this.stat_values = stat_values;
        inflater = (LayoutInflater.from(applicationContext));
    }
    @Override
    public int getCount() {
        return stat_values.length;
    }
    @Override
    public Object getItem(int i) {
        return null;
    }
    @Override
    public long getItemId(int i) {
        return 0;
    }
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.stats_gridview, null); // inflate the layout
        TextView tv = (TextView) view.findViewById(R.id.statvalue); // get the reference of ImageView
        tv.setText(stat_values[i]);
        return view;
    }
}
