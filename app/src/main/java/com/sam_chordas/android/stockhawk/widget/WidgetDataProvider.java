package com.sam_chordas.android.stockhawk.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.sam_chordas.android.stockhawk.R;
import com.sam_chordas.android.stockhawk.data.QuoteColumns;
import com.sam_chordas.android.stockhawk.data.QuoteProvider;

/**
 * Created by Amal Krishnan on 06-07-2016.
 */
public class WidgetDataProvider implements RemoteViewsService.RemoteViewsFactory {

    private Context context;
    private Cursor cursor;
    private Intent intent;

    public WidgetDataProvider(Context context1,Intent intent1)
    {
        context=context1;
        intent=intent1;
        //.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,AppWidgetManager.INVALID_APPWIDGET_ID);
    }

    @Override
    public void onCreate() {
        initData();
    }

    @Override
    public void onDataSetChanged() {
           // initData();
    }

    @Override
    public void onDestroy() {
        if(cursor!=null)
            cursor.close();
    }

    @Override
    public int getCount() {
        return cursor.getCount();
    }

    @Override
    public RemoteViews getViewAt(int position) {

        String StockSymbol="";
        String StockBid="";
        String StockChange="";
        int isUp=1;

        if(cursor.moveToPosition(position)){

            StockSymbol=cursor.getString(cursor.getColumnIndex(QuoteColumns.SYMBOL));
            StockBid=cursor.getString(cursor.getColumnIndex(QuoteColumns.BIDPRICE));
            StockChange=cursor.getString(cursor.getColumnIndex(QuoteColumns.CHANGE));
            isUp=cursor.getInt(cursor.getColumnIndex(QuoteColumns.ISUP));

            Log.d(WidgetDataProvider.class.getSimpleName(), "getViewAt: "+StockSymbol+StockBid+StockChange+isUp);

        }

        RemoteViews remoteViews=new RemoteViews(context.getPackageName(), R.layout.widget_item);

        remoteViews.setTextViewText(R.id.stock_symbol2,StockSymbol);
        remoteViews.setTextViewText(R.id.bid_price2,StockBid);
        remoteViews.setTextViewText(R.id.change2,StockChange);


        if (isUp == 1) {
            remoteViews.setInt(R.id.change2, "setBackgroundResource", R.drawable.percent_change_pill_green);
        } else {
            remoteViews.setInt(R.id.change2, "setBackgroundResource", R.drawable.percent_change_pill_red);
        }

        //Intent fillInIntent = new Intent();


        //fillInIntent.putExtra("SYMBOL",StockSymbol);

        //remoteViews.setOnClickFillInIntent(R.id.widget_item, fillInIntent);

        return remoteViews;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    public void initData(){
        if(cursor!=null)
            cursor.close();

        cursor=context.getContentResolver().query(QuoteProvider.Quotes.CONTENT_URI,
                new String[]{ QuoteColumns._ID, QuoteColumns.SYMBOL, QuoteColumns.BIDPRICE,
                        QuoteColumns.PERCENT_CHANGE, QuoteColumns.CHANGE, QuoteColumns.ISUP},
                QuoteColumns.ISCURRENT + " = ?",
                new String[]{"1"},
                null);
    }
}
