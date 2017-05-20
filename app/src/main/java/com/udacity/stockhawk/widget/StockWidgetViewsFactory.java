package com.udacity.stockhawk.widget;

import com.udacity.stockhawk.R;
import com.udacity.stockhawk.data.Contract;

import android.content.Context;
import android.database.Cursor;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import timber.log.Timber;

/**
 * Created by anselmos on 20.05.17.
 */
public class StockWidgetViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    protected Context context = null;
    protected Cursor data;
    
    StockWidgetViewsFactory(Context context){
        this.context = context;
    }
    @Override
    public void onCreate() {
        
    }
    
    @Override
    public void onDataSetChanged() {
        Timber.d("OnDatasetChanged");
        this.data = this.context.getContentResolver().query(Contract.Quote.URI, null, null, null, null);
    }
    
    @Override
    public void onDestroy() {
        if(this.data != null && !this.data.isClosed()){
            this.data.close();
            this.data = null;
        }
        
    }
    
    @Override
    public int getCount() {
        return this.data != null? this.data.getCount() : 0;
    }
    
    @Override
    public RemoteViews getViewAt(final int position) {
        if(this.data != null && this.data.moveToPosition(position)) {
            RemoteViews remoteViews = new RemoteViews(this.context.getPackageName(), R.layout.widget_item_layout);
            remoteViews.setTextViewText(R.id.widget_item_symbol, this.data.getString(Contract.Quote.POSITION_SYMBOL));
            remoteViews.setTextViewText(R.id.widget_item_price, this.data.getString(Contract.Quote.POSITION_PRICE));
            return remoteViews;
        }
        return null;
    }
    
    @Override
    public RemoteViews getLoadingView() {
        return new RemoteViews(this.context.getPackageName(), R.layout.widget_item_layout);
    }
    
    @Override
    public int getViewTypeCount() {
        return 1;
    }
    
    @Override
    public long getItemId(final int position) {
        return position;
    }
    
    @Override
    public boolean hasStableIds() {
        return true;
    }
}
