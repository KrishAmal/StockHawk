package com.sam_chordas.android.stockhawk.rest;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Amal Krishnan on 08-07-2016.
 */
public interface GraphAPI {


    @GET("/v1/public/yql?&format=json&diagnostics=true&env=store://datatables.org/alltableswithkeys&callback=")
    Call<GraphJSON> loadQuotes(@Query("q") String q);

}
