package com.mobatia.vkcsalesapp.manager

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.net.ConnectivityManager
import android.provider.Settings.Secure
import android.telephony.TelephonyManager
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.core.app.ActivityCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.mobatia.vkcsalesapp.customview.CustomToast
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern

object VKCUtils {
    fun setImageFromUrl(
        activity: Activity?, Url: String?,
        imageView: ImageView?, progressBar: ProgressBar
    ) {
        progressBar.visibility = View.VISIBLE
        Glide.with(activity!!)
            .load(Url).centerCrop()
            .listener(object : RequestListener<Drawable?> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any,
                    target: Target<Drawable?>,
                    isFirstResource: Boolean
                ): Boolean {
                    progressBar.visibility = View.GONE
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any,
                    target: Target<Drawable?>,
                    dataSource: DataSource,
                    isFirstResource: Boolean
                ): Boolean {
                    progressBar.visibility = View.GONE
                    return false
                }
            })
            .into(imageView!!)
    }

    fun setImageFromUrlGrid(
        activity: Activity?, Url: String?,
        imageView: ImageView?, progressBar: ProgressBar
    ) {
        Glide.with(activity!!)
            .load(Url).centerCrop()
            .listener(object : RequestListener<Drawable?> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any,
                    target: Target<Drawable?>,
                    isFirstResource: Boolean
                ): Boolean {
                    progressBar.visibility = View.GONE
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any,
                    target: Target<Drawable?>,
                    dataSource: DataSource,
                    isFirstResource: Boolean
                ): Boolean {
                    progressBar.visibility = View.GONE
                    return false
                }
            })
            .into(imageView!!)
    }

    fun formatDateWithInput(
        date: String?, formatOutput: String?,
        formatInput: String?
    ): String {
        var lastLoginTimeFormat = ""
        val calenderDate = Calendar.getInstance()
        try {
            val sdf = SimpleDateFormat(formatInput, Locale.US)
            sdf.timeZone = calenderDate.timeZone
            calenderDate.time = sdf.parse(date)
            sdf.applyLocalizedPattern(formatOutput)
            lastLoginTimeFormat = sdf.format(calenderDate.time)
        } catch (e: ParseException) {
            // TODO Auto-generated catch block
            e.printStackTrace()
        }
        return lastLoginTimeFormat
    }

    fun setImageFromUrlBaseTransprant(
        activity: Activity?,
        Url: String?, imageView: ImageView, progressBar: ProgressBar
    ) {
        Glide.with(activity!!)
            .load(Url).fitCenter()
            .listener(object : RequestListener<Drawable?> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any,
                    target: Target<Drawable?>,
                    isFirstResource: Boolean
                ): Boolean {
                    progressBar.visibility = View.GONE
                    imageView.visibility = View.INVISIBLE
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any,
                    target: Target<Drawable?>,
                    dataSource: DataSource,
                    isFirstResource: Boolean
                ): Boolean {
                    progressBar.visibility = View.GONE
                    return false
                }
            })
            .into(imageView)
    }

    /**
     * Gets the device id.
     *
     * @param context the context
     * @return the device id
     */
    fun getDeviceID(context: Context): String {
        val telephonyManager = context
            .getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        return if (telephonyManager != null) {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.READ_PHONE_STATE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                telephonyManager.deviceId
            } else telephonyManager.deviceId
        } else {
            Secure.getString(
                context.contentResolver,
                Secure.ANDROID_ID
            )
        }
    }

    /**
     * Show toast message
     *
     * @param activity the activity,message
     * @return void
     */
    fun showtoast(activity: Activity?, errorType: Int) {
        val toast = CustomToast(activity!!)
        toast.show(errorType)
    }

    /**
     * Check internet connection.
     *
     * @param context the context
     * @return true, if successful
     */
    fun checkInternetConnection(context: Context): Boolean {
        val connec = context
            .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val netInfo = connec.activeNetworkInfo
        return if (netInfo != null && netInfo.isConnectedOrConnecting) {
            true
        } else {
            false
        }
    }

    /**
     * Hexa to Color value
     *
     * @param colorString hexa value
     * @return int value, if successful
     */
    fun parseColor(colorString: String): Int {
        return if (colorString.startsWith("#")) {
            Color.parseColor(colorString)
        } else {
            Color.parseColor("#FFFFFFFF")
        }
    }

    fun hideKeyBoard(context: Context) {
        val imm = context
            .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (imm.isAcceptingText) {
            imm.hideSoftInputFromWindow(
                (context as Activity).currentFocus
                    ?.getWindowToken(), 0
            )
        }
    }

    /**
     * Sets the error for edit text.
     *
     * @param edt the edt
     * @param msg the msg
     */
    fun setErrorForEditText(edt: EditText, msg: String?) {
        edt.error = msg
    }

    /**
     * Text watcher for edit text.
     *
     * @param edt the edt
     * @param msg the msg
     */
    fun textWatcherForEditText(
        edt: EditText,
        msg: String?
    ) {
        edt.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(
                s: CharSequence, start: Int, before: Int,
                count: Int
            ) {
                if (s.length == 0 || s == "") {
                    setErrorForEditText(edt, msg)
                }
            }

            override fun beforeTextChanged(
                s: CharSequence, start: Int, count: Int,
                after: Int
            ) {
                if (s == "") {
                    setErrorForEditText(edt, msg)
                }
            }

            override fun afterTextChanged(s: Editable) {
                if (s != null && s.length > 0 && edt.error != null) {
                    edt.error = null
                } else if (s.length == 0 || s.equals("")) {
                    setErrorForEditText(edt, msg)
                }
            }
        })
    }

    /**
     * Checks if is valid email.
     *
     * @param email the email
     * @return the boolean
     */
    fun isValidEmail(email: String?): Boolean {
        var isValid = false
        val expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$"
        val pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE)
        val matcher = pattern.matcher(email)
        if (matcher.matches()) {
            isValid = true
        }
        return isValid
    }
}