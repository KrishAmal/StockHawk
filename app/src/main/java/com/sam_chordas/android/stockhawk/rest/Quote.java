
package com.sam_chordas.android.stockhawk.rest;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Quote {

    @SerializedName("Date")
    @Expose
    private String date;
    @SerializedName("Close")
    @Expose
    private String close;

    /**
     * 
     * @return
     *     The date
     */
    public String getDate() {
        return date;
    }

    /**
     * 
     * @param date
     *     The Date
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * 
     * @return
     *     The close
     */
    public String getClose() {
        return close;
    }

    /**
     * 
     * @param close
     *     The Close
     */
    public void setClose(String close) {
        this.close = close;
    }

}
