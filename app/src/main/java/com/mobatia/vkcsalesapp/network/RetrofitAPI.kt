package com.vkc.loyaltyapp.api

import com.mobatia.vkcsalesapp.constants.VKCUrlConstants
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitAPI {

    private var retrofit: Retrofit? = null
    fun getClient(): APIInterface {

        // change your base URL
        if (retrofit == null) {
            retrofit = Retrofit.Builder()
                .baseUrl(VKCUrlConstants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        //Creating object for our interface
        return retrofit!!.create<APIInterface>(APIInterface::class.java) // return the APIInterface object
    }
}