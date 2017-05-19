package com.udacity.stockhawk.ui;

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
            
            // Get all ids
            ComponentName thisWidget = new ComponentName(context,
                    StockWidgetProvider.class);
            int[] allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
            for (int widgetId : allWidgetIds) {
                // create some random data
                int number = (new Random().nextInt(100));
                
                RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
                        R.layout.widget_layout);
                // Set the text
                remoteViews.setTextViewText(R.id.update, String.valueOf("UPDATE" + String.valueOf(number)));
                
                // Register an onClickListener
                Intent intent = new Intent(context, StockWidgetProvider.class);
                
                intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
                intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
            }
        }
    }