package com.sam_chordas.android.stockhawk.ui;

import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;

import java.lang.ref.WeakReference;

/**
 * Created by Amal Krishnan on 12-07-2016.
 */
public class AddStockBackground extends AsyncQueryHandler {
    private WeakReference<AsyncQueryListener> mListener;
    public AddStockBackground(ContentResolver cr) {
        super(cr);
        //mListener = new WeakReference<AsyncQueryListener>(listener);
    }

    /**
     * Interface to listen for completed query operations.
     */
    public interface AsyncQueryListener {
        void onQueryComplete(int token, Object cookie, Cursor cursor);
    }


    @Override
    protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
       // final AsyncQueryListener listener = mListener.get();
       // if (listener != null) {
       //     listener.onQueryComplete(token, cookie, cursor);
       // } else if (cursor != null) {
            cursor.close();
       // }
    }
}
