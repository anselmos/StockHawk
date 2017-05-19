package com.udacity.stockhawk.ui;

import com.google.common.collect.ArrayListMultimap;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.udacity.stockhawk.R;
import com.udacity.stockhawk.data.MockedHistoricalQuote;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import yahoofinance.histquotes.HistoricalQuote;

/**
 * Created by anselmos on 19.05.17.
 */
public class ChartActivity extends AppCompatActivity {
    final static String LINE_DATA_SET_LABEL = "Stock : ";
    final static String parcelName = "symbol";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chart_layout);
        
        final String symbol = getIntent().getParcelableExtra(parcelName);
        LineChart chart = (LineChart) findViewById(R.id.chart);
        List<Entry> entries = getEntries(symbol);
    
        
        LineDataSet dataSet = new LineDataSet(entries, LINE_DATA_SET_LABEL + symbol);
        LineData lineData = new LineData(dataSet);
        chart.setData(lineData);
        chart.invalidate(); // refresh
    }
    
    @NonNull
    private List<Entry> getEntries(final String symbol) {
        //TODO change this mocked behaviour to a db gatther!!!
        ArrayList<HistoricalQuote> data = (ArrayList<HistoricalQuote>) new MockedHistoricalQuote().getMockHistoryData(symbol);
        List<Entry> entries = new ArrayList<>();
        
        for(HistoricalQuote quote: data){
            entries.add(new Entry(quote.getDate().getTime().getTime(), quote.getVolume().floatValue()));
        }
        return entries;
    }
}
