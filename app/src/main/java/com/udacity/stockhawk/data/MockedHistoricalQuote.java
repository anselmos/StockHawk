package com.udacity.stockhawk.data;

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
public class MockedHistoricalQuote {
    
    public List<HistoricalQuote> getMockHistoryData(String symbol) {
        ArrayList<HistoricalQuote> output = new ArrayList<>();
        for (int i = 0; i<= 10; i++) {
            HistoricalQuote randomQuote = new HistoricalQuote(
                    symbol,
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
    

    
    public Calendar calendar() {
        SimpleDateFormat dfDateTime = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault());
        int year = 2017;
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
