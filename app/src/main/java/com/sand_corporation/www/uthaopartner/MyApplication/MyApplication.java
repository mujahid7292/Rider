package com.sand_corporation.www.uthaopartner.MyApplication;

import android.app.Application;
import android.content.Context;

import com.sand_corporation.www.uthaopartner.LanguageChange.LocalHelper;

//import com.facebook.drawee.backends.pipeline.Fresco;

/**
 * Created by HP on 1/13/2018.
 */

public class MyApplication extends Application {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocalHelper.onAttach(base,"en"));
    }

    @Override
    public void onCreate() {
        super.onCreate();
//        Fresco.initialize(this);
    }

}
