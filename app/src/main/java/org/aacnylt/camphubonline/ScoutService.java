package org.aacnylt.camphubonline;

import org.aacnylt.camphubonline.models.Scout;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by Aroon on 2/24/2018.
 */

public interface ScoutService {
    @GET("scouts")
    Call<List<Scout>> getAllScouts();
}
