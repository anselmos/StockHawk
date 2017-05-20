package com.udacity.stockhawk.widget;

import com.udacity.stockhawk.R;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

import java.util.Random;

/**
 * Created by anselmos on 19.05.17.
 */
public class StockWidgetProvider  extends AppWidgetProvider {
        
        private static final String ACTION_CLICK = "ACTION_CLICK";
        
        @Override
        public void onUpdate(Context context, AppWidgetManager appWidgetManager,
                int[] appWidgetIds) {
    
            for (int appWidgetId : appWidgetIds) {
                RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
    
                views.setRemoteAdapter(R.id.widget_list_items,
                        new Intent(context, StockWidgetViewsService.class));
            }
        }
    }