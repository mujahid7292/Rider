package com.sand_corporation.www.uthaopartner.GetGoogleDirection;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

/**
 * Created by HP on 11/15/2017.
 */

public interface IGoogleAPI {

    @GET
    Call<String> getPath(@Url String url);


}
