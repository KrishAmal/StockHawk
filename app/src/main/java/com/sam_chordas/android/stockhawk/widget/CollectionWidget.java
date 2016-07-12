package com.sam_chordas.android.stockhawk.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.widget.RemoteViews;

import com.sam_chordas.android.stockhawk.R;
import com.sam_chordas.android.stockhawk.ui.MyStocksActivity;
import com.sam_chordas.android.stockhawk.ui.MyStocksGraph;

/**
 * Created by Amal Krishnan on 06-07-2016.
 */
public class CollectionWidget extends AppWidgetProvider {
/*
    @Override
    public void onReceive(@NonNull Context context,@NonNull Intent intent){
            super.onReceive(context,intent);

    }
*/
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for(int appWidgetId:appWidgetIds){
            Intent intent=new Intent(context,WidgetService.class);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,appWidgetId);
            intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));

            RemoteViews remoteViews=new RemoteViews(context.getPackageName(), R.layout.widget);
            remoteViews.setRemoteAdapter(appWidgetId,R.id.listview,intent);
            //remoteViews.setEmptyView(R.id.listview,R.id.emptyListview);

            appWidgetManager.updateAppWidget(appWidgetId,remoteViews);
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }
}
