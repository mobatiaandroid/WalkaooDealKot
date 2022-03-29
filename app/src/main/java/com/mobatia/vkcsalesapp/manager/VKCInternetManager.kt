package com.mobatia.vkcsalesapp.manager

import android.app.Activity
import android.content.Context
import android.util.Log
import com.android.volley.*
import com.android.volley.toolbox.StringRequest
import com.mobatia.vkcsalesapp.R
import com.mobatia.vkcsalesapp.controller.AppController
import com.mobatia.vkcsalesapp.customview.CustomProgressBar
import java.util.*

class VKCInternetManager(var mRequestUrl: String) {
    val TAG = "VKCInternetManager"
    var mResponseListener: ResponseListener? = null
    fun getResponseGET(activity: Activity?) {
        val tag_string_req = "string_req"

        // String url =
        // "http://www.androidhive.info/2014/05/android-working-with-volley-library-1/";
        val pDialog = CustomProgressBar(activity, R.drawable.loading)

        //pDialog.setMessage("Loading...");
        pDialog.show()
        val strReq = StringRequest(
            Request.Method.GET, mRequestUrl,
            { response ->
                pDialog.dismiss()
                Log.v(TAG, "$TAG $response")
                mResponseListener!!.responseSuccess(response)
            }) { error ->
            pDialog.dismiss()
            VolleyLog.d(
                TAG,
                TAG + " " + "Error: " + error.message
            )
            mResponseListener!!.responseFailure(error.message)

            //mResponseListener.responseSuccess(getResponseCache());
        }
        // set Volley cache enable true
        strReq.setShouldCache(true)
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req)
    }

    fun getResponse(activity: Activity?, responseListener: ResponseListener?) {
        mResponseListener = responseListener
        getResponseGET(activity)
    }

    fun getResponsePOST(
        activity: Activity?, name: Array<String>,
        value: Array<String>, responseListener: ResponseListener?
    ) {
        mResponseListener = responseListener
        getResponsePOST(activity, name, value)
    }



    fun getResponsePOST(
        context: Context?, name: Array<String>,
        value: Array<String>
    ) {
//		final CustomProgressBar pDialog = new CustomProgressBar(activity,R.drawable.loading);

        //pDialog.setMessage("Loading...");
//		pDialog.show();
        val tag_json_obj = "string_req"
        // RequestQueue queue = Volley.newRequestQueue(activity);
        val sr: StringRequest = object : StringRequest(
            Method.POST, mRequestUrl,
            Response.Listener { response -> // Log.v("LOG", "9122014 " + response);
                mResponseListener!!.responseSuccess(response)
                //						pDialog.dismiss();
                Log.v("LOG", "Response Sales Order $response")
            },
            Response.ErrorListener { error ->
                // Log.v("LOG", "9122014 " + error);
                VolleyLog.d(TAG, "Error: " + error.message)
                mResponseListener!!.responseFailure(error.message)
                //						pDialog.dismiss();
            }) {
            override fun getParams(): Map<String, String>? {
                val params: MutableMap<String, String> = HashMap()
                for (i in name.indices) {
                    params[name[i]] = value[i]
                }
                return params
            }

            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["Content-Type"] = "application/x-www-form-urlencoded"
                return params
            }
        }
        // queue.add(sr);

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(sr, tag_json_obj)
    }

    fun getResponsePOST(
        activity: Activity?, name: Array<String>,
        value: Array<String>
    ) {
        val pDialog = CustomProgressBar(activity, R.drawable.loading)

        //pDialog.setMessage("Loading...");
        pDialog.show()
        val tag_json_obj = "string_req"
        println("URL PRODUCT$mRequestUrl")
        // RequestQueue queue = Volley.newRequestQueue(activity);
        val sr: StringRequest = object : StringRequest(
            Method.POST, mRequestUrl,
            Response.Listener { response -> // Log.v("LOG", "9122014 " + response);
                mResponseListener!!.responseSuccess(response)
                pDialog.dismiss()
            },
            Response.ErrorListener { error -> // Log.v("LOG", "9122014 " + error);
                VolleyLog.d(TAG, "Error: " + error.message)
                mResponseListener!!.responseFailure(error.message)
                pDialog.dismiss()
            }) {
            override fun getParams(): Map<String, String>? {
                val params: MutableMap<String, String> = HashMap()
                for (i in name.indices) {
                    try {
                        params[name[i]] = value[i]
                    } catch (e: Exception) {
                    }
                    //System.out.println("Names:"+name[i]);
                    //System.out.println("Values:"+value[i]);
                }
                return params
            }

            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["Content-Type"] = "application/x-www-form-urlencoded"
                return params
            }
        }
        //Setting Timout Parameter : Bibin
        sr.retryPolicy = DefaultRetryPolicy(
            1800 * 1000,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(sr, tag_json_obj)
    }// Cached response doesn't exists. Make network call here

    // handle data, like converting it to xml, json, bitmap etc.,


    interface ResponseListener {
        fun responseSuccess(successResponse: String?)
        fun responseFailure(failureResponse: String?)
    }
}