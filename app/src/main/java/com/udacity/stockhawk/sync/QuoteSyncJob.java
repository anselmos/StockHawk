package com.udacity.stockhawk.sync;

import com.udacity.stockhawk.data.Contract;
import com.udacity.stockhawk.data.PrefUtils;
import com.udacity.stockhawk.data.MockedHistoricalQuote;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import timber.log.Timber;
import yahoofinance.Stock;
import yahoofinance.YahooFinance;
import yahoofinance.histquotes.HistoricalQuote;
import yahoofinance.histquotes.Interval;
import yahoofinance.quotes.stock.StockQuote;

public final class QuoteSyncJob {

    
    public final static String HISTORY_LINE_SPLITER = "\n";
    
    public final static String HISTORY_ROW_SPLITTER = ", ";
    
    private static final int ONE_OFF_ID = 2;
    
    public static final String ACTION_DATA_UPDATED = "com.udacity.stockhawk.ACTION_DATA_UPDATED";
    
    private static final int PERIOD = 300000;
    
    private static final int INITIAL_BACKOFF = 10000;
    
    private static final int PERIODIC_ID = 1;
    
    private static final int YEARS_OF_HISTORY = 2;
    
    
    
    private QuoteSyncJob() {
    }
    
    static void getQuotes(Context context) {
        
        Timber.d("Running sync job");
        
        Calendar from = Calendar.getInstance();
        Calendar to = Calendar.getInstance();
        from.add(Calendar.YEAR, -YEARS_OF_HISTORY);
        
        Set<String> stockPref = PrefUtils.getStocks(context);
        Set<String> stockCopy = new HashSet<>();
        stockCopy.addAll(stockPref);
        String[] stockArray = stockPref.toArray(new String[stockPref.size()]);
        
        Timber.d(stockCopy.toString());
        
        if (stockArray.length == 0) {
            return;
        }
        Map<String, Stock> quotes = null;
        try {
            quotes = YahooFinance.get(stockArray);
        } catch (IOException ioexc) {
            
        }
        Iterator<String> iterator = stockCopy.iterator();
        
        Timber.d(quotes.toString());
        
        ArrayList<ContentValues> quoteCVs = new ArrayList<>();
        
        while (iterator.hasNext()) {
            String symbol = iterator.next();
            
            Stock stock = quotes.get(symbol);
            
            if (stock.isValid()) {
                StockQuote quote = stock.getQuote();
                
                float price = quote.getPrice().floatValue();
                float change = quote.getChange().floatValue();
                float percentChange = quote.getChangeInPercent().floatValue();
                
                ContentValues quoteCV = new ContentValues();
                quoteCV.put(Contract.Quote.COLUMN_SYMBOL, symbol);
                quoteCV.put(Contract.Quote.COLUMN_PRICE, price);
                quoteCV.put(Contract.Quote.COLUMN_PERCENTAGE_CHANGE, percentChange);
                quoteCV.put(Contract.Quote.COLUMN_ABSOLUTE_CHANGE, change);
                List<HistoricalQuote> history = null;
                try {
                    history = stock.getHistory(from, to, Interval.WEEKLY);
                } catch (IOException ioexc) {
                    //Hence Yahoo icharts does not want to work anymore - noone knows when this issue will be fixed
                    //So I'm Mocking this Quotes
                    history = new MockedHistoricalQuote().getMockHistoryData(stock.getSymbol());
                }
                StringBuilder historyBuilder = new StringBuilder();
                for (HistoricalQuote it : history) {
                    historyBuilder.append(it.getDate().getTimeInMillis());
                    
                    historyBuilder.append(HISTORY_ROW_SPLITTER);
                    historyBuilder.append(it.getClose());
                    
                    historyBuilder.append(HISTORY_LINE_SPLITER);
                }
                quoteCV.put(Contract.Quote.COLUMN_HISTORY, historyBuilder.toString());
                quoteCVs.add(quoteCV);
            }
        }
        
        context.getContentResolver()
                .bulkInsert(
                        Contract.Quote.URI,
                        quoteCVs.toArray(new ContentValues[quoteCVs.size()]));
        
        Intent dataUpdatedIntent = new Intent(ACTION_DATA_UPDATED);
        context.sendBroadcast(dataUpdatedIntent);
    }
    
    private static void schedulePeriodic(Context context) {
        Timber.d("Scheduling a periodic task");
        
        JobInfo.Builder builder = new JobInfo.Builder(PERIODIC_ID, new ComponentName(context, QuoteJobService.class));
        
        builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                .setPeriodic(PERIOD)
                .setBackoffCriteria(INITIAL_BACKOFF, JobInfo.BACKOFF_POLICY_EXPONENTIAL);
        
        JobScheduler scheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        
        scheduler.schedule(builder.build());
    }
    
    public static synchronized void initialize(final Context context) {
        
        schedulePeriodic(context);
        syncImmediately(context);
    }
    
    public static synchronized void syncImmediately(Context context) {
        
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnectedOrConnecting()) {
            Intent nowIntent = new Intent(context, QuoteIntentService.class);
            context.startService(nowIntent);
        } else {
            
            JobInfo.Builder builder = new JobInfo.Builder(ONE_OFF_ID, new ComponentName(context, QuoteJobService.class));
            
            builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                    .setBackoffCriteria(INITIAL_BACKOFF, JobInfo.BACKOFF_POLICY_EXPONENTIAL);
            
            JobScheduler scheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
            
            scheduler.schedule(builder.build());
        }
    }
}
