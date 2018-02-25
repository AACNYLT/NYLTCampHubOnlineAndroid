package org.aacnylt.camphubonline

import android.app.ProgressDialog
import android.content.Context
import org.aacnylt.camphubonline.models.Scout
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by Aroon on 2/24/2018.
 */
internal object StaticScoutService {
    fun createRetrofitService(): ScoutService {
        val retrofit = Retrofit.Builder()
                .baseUrl(hostUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        return retrofit.create(ScoutService::class.java)
    }

    var hostUrl = ""

    lateinit var CurrentUser: Scout

    fun createProgressDialog(context: Context, message: String): ProgressDialog {
        val progressDialog = ProgressDialog(context)
        progressDialog.isIndeterminate = true
        progressDialog.setMessage(message)
        return progressDialog
    }
}