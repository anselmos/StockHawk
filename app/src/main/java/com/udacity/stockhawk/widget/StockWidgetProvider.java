package com.udacity.stockhawk.widget;

import com.udacity.stockhawk.R;
import com.udacity.stockhawk.sync.QuoteSyncJob;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import timber.log.Timber;

/**
 * Created by anselmos on 19.05.17.
 */
public class StockWidgetProvider  extends AppWidgetProvider {
        
        private static final String ACTION_CLICK = "ACTION_CLICK";
        
        @Override
        public void onUpdate(Context context, AppWidgetManager appWidgetManager,
                int[] appWidgetIds) {
    
            Timber.d("OnUpdate Stock Provicer widget!! !!!");
            for (int appWidgetId : appWidgetIds) {
                RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
    
                views.setRemoteAdapter(R.id.widget_list_items,
                        new Intent(context, StockWidgetViewsService.class));
                
                appWidgetManager.updateAppWidget(appWidgetId, views);
            }
        }
    
    @Override
    public void onReceive(final Context context, final Intent intent) {
        super.onReceive(context, intent);
        
        if (QuoteSyncJob.ACTION_DATA_UPDATED.equals(intent.getAction())) {
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(
                    new ComponentName(context, getClass()));
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_list_items);
        }
    }
}