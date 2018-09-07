package com.sand_corporation.www.uthaopartner.Volley;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by HP on 7/28/2017.
 */

public class MySingelTon {
    private static MySingelTon mInsatance;
    private static Context mContext;
    private RequestQueue requestQueue;

    private MySingelTon(Context context){
        mContext = context;
        requestQueue = getRequestQueue();
    }

    private RequestQueue getRequestQueue(){
        if (requestQueue == null){
            requestQueue = Volley.newRequestQueue(mContext.getApplicationContext());
        }
        return requestQueue;
    }

    public static synchronized MySingelTon getmInsatance(Context context){
        if (mInsatance == null){
            mInsatance = new MySingelTon(context);
        }
        return mInsatance;
    }

    public<T> void addToRequestQue(Request<T> request){
        requestQueue.add(request);
    }
}
