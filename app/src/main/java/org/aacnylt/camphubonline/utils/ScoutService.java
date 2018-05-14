package org.aacnylt.camphubonline.utils;

import org.aacnylt.camphubonline.models.Course;
import org.aacnylt.camphubonline.models.Evaluation;
import org.aacnylt.camphubonline.models.Message;
import org.aacnylt.camphubonline.models.Scout;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
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

    @DELETE("scouts/{id}")
    Call<Message> deleteScout(@Path("id") Integer ScoutID);

    @PUT("scouts/{id}")
    Call<Message> updateScout(@Path("id") Integer ScoutID, @Body Scout scout);

    @GET("scouts/{id}/evaluations")
    Call<ArrayList<Evaluation>> getScoutEvaluations(@Path("id") Integer ScoutID);

    @POST("evaluations")
    Call<Message> insertEvaluation(@Body Evaluation evaluation);

    @GET("authenticate")
    Call<Scout> authenticateScout(@Query("username") String username, @Query("password") String password);

    @GET("courses")
    Call<ArrayList<Course>> getCourses();
}