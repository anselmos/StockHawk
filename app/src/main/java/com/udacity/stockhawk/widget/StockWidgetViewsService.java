package com.udacity.stockhawk.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

/**
 * Created by anselmos on 20.05.17.
 */
public class StockWidgetViewsService extends RemoteViewsService {
    
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new StockWidgetViewsFactory(this);
    }
}