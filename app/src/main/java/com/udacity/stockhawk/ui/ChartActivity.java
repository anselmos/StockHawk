package com.udacity.stockhawk.ui;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.EntryXComparator;
import com.udacity.stockhawk.R;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import timber.log.Timber;

import static com.udacity.stockhawk.sync.QuoteSyncJob.HISTORY_LINE_SPLITER;
import static com.udacity.stockhawk.sync.QuoteSyncJob.HISTORY_ROW_SPLITTER;

/**
 * Created by anselmos on 19.05.17.
 */
public class ChartActivity extends AppCompatActivity {
    final static String LINE_DATA_SET_LABEL = "Stock : ";
    final static String parcelName = "symbol";
    final static String historyName = "history";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chart_layout);
        
        String symbol = getIntent().getStringExtra(parcelName);
        String history = getIntent().getStringExtra(historyName);
        LineChart chart = (LineChart) findViewById(R.id.chart);
        List<Entry> entries = getEntries(history);
    
        
        LineDataSet dataSet = new LineDataSet(entries, LINE_DATA_SET_LABEL + symbol);
        LineData lineData = new LineData(dataSet);
        chart.setData(lineData);
        chart.invalidate(); // refresh
    }
    
    @NonNull
    private List<Entry> getEntries(String history) {
        List<Entry> entries = new ArrayList<>();
        if (!history.isEmpty()){
        
            for(String row: history.split(HISTORY_LINE_SPLITER)){
                float x = Float.valueOf(row.split(HISTORY_ROW_SPLITTER)[0]);
                float y = Float.valueOf(row.split(HISTORY_ROW_SPLITTER)[1]);
                Timber.d("x = " + String.valueOf(x) + " y = "+ String.valueOf(y));
                entries.add(new Entry(x, y));
            }
        }
        //Since entries might not be sorted by x,
        // you may end-up with raising error of NegativeArraySizeException, so sorting:
        Collections.sort(entries, new EntryXComparator());
        return entries;
    }
}
