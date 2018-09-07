package com.sand_corporation.www.uthaopartner.GetGoogleRoads;

import com.sand_corporation.www.uthaopartner.BuildConfig;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by HP on 2/4/2018.
 */

public class RetrofitRoadClient {
    private static Retrofit retrofit = null;


    public static Retrofit getClient(String baseUrl){
        if (retrofit == null){
            OkHttpClient.Builder okhttp = new OkHttpClient.Builder();
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            if (BuildConfig.DEBUG){
                okhttp.addInterceptor(interceptor);
            }
            retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .client(okhttp.build())
                    .build();
        }
        return retrofit;
    }
}
