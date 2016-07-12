
package com.sam_chordas.android.stockhawk.rest;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Results {

    @SerializedName("quote")
    @Expose
    private List<Quote> quote = new ArrayList<Quote>();

    /**
     * 
     * @return
     *     The quote
     */
    public List<Quote> getQuote() {
        return quote;
    }

    /**
     * 
     * @param quote
     *     The quote
     */
    public void setQuote(List<Quote> quote) {
        this.quote = quote;
    }

}
