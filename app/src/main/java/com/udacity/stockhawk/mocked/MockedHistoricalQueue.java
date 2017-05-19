package com.udacity.stockhawk.mocked;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import yahoofinance.histquotes.HistoricalQuote;

/**
 * Created by anselmos on 19.05.17.
 */
public class MockedHistoricalQueue {
    
    public List<HistoricalQuote> getMockHistoryData() {
        ArrayList<HistoricalQuote> output = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            HistoricalQuote randomQuote = new HistoricalQuote(
                    this.symbol(),
                    this.calendar(),
                    this.decimal(),
                    this.decimal(),
                    this.decimal(),
                    this.decimal(),
                    this.decimal(),
                    this.longer()
            );
            output.add(randomQuote);
        }
        return output;
    }
    
    public String symbol() {
        ArrayList<String> symbols = new ArrayList<>();
        symbols.add("Name1");
        symbols.add("Name2");
        symbols.add("Name3");
        return symbols.get(new Random().nextInt(symbols.size()));
    }
    
    public Calendar calendar() {
        SimpleDateFormat dfDateTime = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault());
        int year = new Random().nextInt(1900);
        int month = new Random().nextInt(11);
        int hour = new Random().nextInt(23);
        int min = new Random().nextInt(59);
        int sec = new Random().nextInt(59);
        Calendar output = new GregorianCalendar();
        output.set(year, month, hour, hour, min, sec);
        return output;
    }
    
    public BigDecimal decimal() {
        return new BigDecimal(new Random().nextDouble());
    }
    
    public Long longer() {
        return new Random().nextLong();
    }
}
