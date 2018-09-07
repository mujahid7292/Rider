package com.sand_corporation.www.uthaopartner.DeviceToDeviceNotification;

import com.sand_corporation.www.uthaopartner.DeviceToDeviceNotification.ModelClass.FCMResponse;
import com.sand_corporation.www.uthaopartner.DeviceToDeviceNotification.ModelClass.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by HP on 1/30/2018.
 */

public interface IFCMService {
    //This interface for Retrofit client
    //This interface will be used for Firebase Cloud Messaging
    //HTTP protocol

    @Headers({
            "content-type:application/json",
            "Authorization:key=AAAAmsBJGgw:APA91bGOQWdaIQE6RA6u1hzx65UbKh89ew9gQYp2afAFGmY1Gs9CqXNmDMBCjANNYOl8ZqUYFQYCEbiYQ0mfzO4OcdmOPTZ4h5m3c3_Fj9XSaIKzjFfujQSGsvZM31IIgJwXwHVf9YAY"
    })
    //Above Authorization Key is the Firebase Cloud messaging Authorization Key


    @POST("fcm/send")
    Call<FCMResponse>sendMessage(@Body Sender body);
}
