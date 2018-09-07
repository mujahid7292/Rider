package com.sand_corporation.www.uthaopartner.GetGoogleRoads;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

/**
 * Created by HP on 2/4/2018.
 */

public interface IGoogleRoadAPI {

    @GET
    Call<String> getRoad(@Url String url);
}
