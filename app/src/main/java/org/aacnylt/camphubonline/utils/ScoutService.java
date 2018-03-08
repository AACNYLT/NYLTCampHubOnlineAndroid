package org.aacnylt.camphubonline.utils;

import org.aacnylt.camphubonline.models.Scout;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Aroon on 2/24/2018.
 */

public interface ScoutService {
    @GET("scouts")
    Call<ArrayList<Scout>> getAllScouts();

    @GET("scouts/{id}")
    Call<Scout> getScout(@Path("id") Integer ScoutID);

    @GET("authenticate")
    Call<Scout> authenticateScout(@Query("username") String username, @Query("password") String password);
}