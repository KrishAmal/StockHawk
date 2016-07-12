package com.sam_chordas.android.stockhawk.ui;

import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.db.chart.model.LineSet;
import com.db.chart.model.Point;
import com.db.chart.view.AxisController;
import com.db.chart.view.ChartView;
import com.db.chart.view.LineChartView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sam_chordas.android.stockhawk.R;
import com.sam_chordas.android.stockhawk.rest.GraphAPI;
import com.sam_chordas.android.stockhawk.rest.GraphJSON;
import com.sam_chordas.android.stockhawk.rest.Results;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Amal Krishnan on 15-06-2016.
 */
public class MyStocksGraph extends AppCompatActivity implements Callback<GraphJSON>{
    String LOGTAG=MyStocksGraph.class.getSimpleName();
    String[] labels={};
    float[] values={};
    Call<GraphJSON> call;
    float close;
    String Date,CurrentDate,PrevDate;
    String query,diagnostics,env,format;
    String Sym;
    LineSet dataset=new LineSet(labels,values) ;
    TextView NoHistory;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_line_graph);

        Sym = getIntent().getStringExtra("StockSym").toUpperCase();
        Gson gson =new GsonBuilder().create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://query.yahooapis.com/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        GraphAPI service = retrofit.create(GraphAPI.class);

        SimpleDateFormat sd=new SimpleDateFormat("yyyy-MM-dd");
        CurrentDate=sd.format(Calendar.getInstance().getTime());

        Calendar cd=Calendar.getInstance();
        cd.add(Calendar.DAY_OF_YEAR,-6);
        PrevDate=sd.format(cd.getTime());
        Log.d(LOGTAG, "onCreate: Date:"+ CurrentDate +"and Prev:"+PrevDate);

        query="select * from yahoo.finance.historicaldata where symbol = '"+Sym+"' and startDate = '"+PrevDate+"' and endDate = '"+CurrentDate+"'";
        diagnostics="true";
        env="store://datatables.org/alltableswithkeys&callback=";
        format="json";
        call = service.loadQuotes(query);
        call.enqueue(this);

        TextView Symbol= (TextView)findViewById(R.id.Sym);
        NoHistory=(TextView) findViewById(R.id.noStockHis);
        NoHistory.setVisibility(View.GONE);
        Log.d(LOGTAG, "onCreate: "+Sym);
        try {
            Symbol.setText(Sym);
        }
        catch (NullPointerException e)
        {
            Log.d(LOGTAG, "NULL value");
        }
    }

    public void showgraph(int step){

        Paint p=new Paint();
        p.setColor(ContextCompat.getColor(this,R.color.grey));

        LineChartView data=(LineChartView)findViewById(R.id.linechart);
        dataset.setSmooth(false);
        dataset.setColor(getResources().getColor(R.color.material_blue_500));
        dataset.setDotsColor(Color.RED);
        dataset.setDotsRadius(8);
        dataset.setThickness(4);
        data.setAxisColor(ContextCompat.getColor(this,R.color.grey));

        data.setStep(step);
        data.setGrid(ChartView.GridType.FULL,p);
        data.setXAxis(true);
        data.setYAxis(true);
        data.setBorderSpacing(30);
        data.setTopSpacing(10);

        data.setLabelsColor(ContextCompat.getColor(this,R.color.white));
        dataset.setShadow(2,3,3,R.color.blue);
        data.addData(dataset);
        data.show();

    }


    @Override
    public void onResponse(Call<GraphJSON> call, Response<GraphJSON> response) {
        int code = response.code();


        if (code == 200) {
            Log.d(LOGTAG, "onResponse: Successful");
        } else {
            Log.d(LOGTAG, "onResponse: Unsuccessful");;
        }

        if(response.body().getQuery().getResults()!=null) {
            for (int i = 0; i < response.body().getQuery().getCount(); i++) {
                close = Float.parseFloat(response.body().getQuery().getResults().getQuote().get(i).getClose());
                Date = response.body().getQuery().getResults().getQuote().get(i).getDate();
                dataset.addPoint(Date, close);
            }

            int step;
            if(Float.parseFloat(response.body().getQuery().getResults().getQuote().get(0).getClose())<500.0)
                step=10;
            else
                step=100;

            showgraph(step);
        }
        else
        {
            NoHistory.setVisibility(View.VISIBLE);
            Toast.makeText(MyStocksGraph.this,R.string.no_stock_history, Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onFailure(Call<GraphJSON> call, Throwable t) {
        Log.e("onFailure: ",t.getMessage());
        Toast.makeText(this, R.string.failed_data_fetch, Toast.LENGTH_LONG).show();
    }
}
