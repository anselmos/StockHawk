package com.udacity.stockhawk.widget;

import com.udacity.stockhawk.R;

import android.content.Context;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import timber.log.Timber;

/**
 * Created by anselmos on 20.05.17.
 */
public class StockWidgetViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    protected Context context = null;
    
    StockWidgetViewsFactory(Context context){
        this.context = context;
    }
    @Override
    public void onCreate() {
        
    }
    
    @Override
    public void onDataSetChanged() {
        Timber.d("OnDatasetChanged!!!!");
        
    }
    
    @Override
    public void onDestroy() {
        
    }
    
    @Override
    public int getCount() {
        //TODO remove this hardcoded value!
        return 2;
    }
    
    @Override
    public RemoteViews getViewAt(final int position) {
        RemoteViews remoteViews = new RemoteViews(this.context.getPackageName(), R.layout.widget_item_layout);
        remoteViews.setTextViewText(R.id.widget_item_symbol, "AAAPT");
        remoteViews.setTextViewText(R.id.widget_item_price, "3");
        return remoteViews;
    }
    
    @Override
    public RemoteViews getLoadingView() {
        return new RemoteViews(this.context.getPackageName(), R.layout.widget_item_layout);
    }
    
    @Override
    public int getViewTypeCount() {
        return 0;
    }
    
    @Override
    public long getItemId(final int position) {
        return position;
    }
    
    @Override
    public boolean hasStableIds() {
        return false;
    }
}
