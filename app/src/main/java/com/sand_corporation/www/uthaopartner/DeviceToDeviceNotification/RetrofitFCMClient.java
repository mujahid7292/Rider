package com.sand_corporation.www.uthaopartner.DeviceToDeviceNotification;

import com.sand_corporation.www.uthaopartner.BuildConfig;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by HP on 1/30/2018.
 */

public class RetrofitFCMClient {

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
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okhttp.build())
                    .build();
        }
        return retrofit;
    }
}
