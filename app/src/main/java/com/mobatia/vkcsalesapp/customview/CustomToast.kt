/**
 *
 */
package com.mobatia.vkcsalesapp.customview

import android.app.Activity
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.mobatia.vkcsalesapp.R

/**
 * Bibin
 */
class CustomToast(var mActivity: Activity) {
    var mTextView: TextView? = null
    var mToast: Toast? = null
    fun init() {
        val inflater = mActivity.layoutInflater
        val layouttoast = inflater.inflate(R.layout.custom_toast, null)
        mTextView = layouttoast.findViewById<View>(R.id.texttoast) as TextView
        mToast = Toast(mActivity)
        mToast!!.view = layouttoast
    }

    fun show(errorCode: Int) {
        if (errorCode == 0) {
            mTextView!!.text = mActivity.resources.getString(
                R.string.common_error_message
            )
        }
        if (errorCode == 1) {
            mTextView!!.text = "Successfully logged in."
        }
        if (errorCode == 2) {
            mTextView!!.text = "Login failed.Please try again later"
        }
        if (errorCode == 3) {
            mTextView!!.text = "Please select size and color"
        }
        if (errorCode == 4) {
            mTextView!!.text = "Please select size,color and quantity"
        }
        if (errorCode == 5) {
            mTextView!!.text = "Succesfully added to cart"
        }
        if (errorCode == 6) {
            mTextView!!.text = "Successfully submitted login request"
        }
        if (errorCode == 7) {
            mTextView!!.text = "Registration failed.Try again later"
        }
        if (errorCode == 8) {
            mTextView!!.text = "Email Already Exists"
        }
        if (errorCode == 9) {
            mTextView!!.text = "No items in the cart"
        }
        if (errorCode == 10) {
            mTextView!!.text = "Please enter valid username and password"
        }
        if (errorCode == 11) {
            mTextView!!.text = "All fields are mandatory"
        }
        if (errorCode == 12) {
            mTextView!!.text = "Complaint send successfully"
        }
        if (errorCode == 13) {
            mTextView!!.text = "Failed.Try again later"
        }
        if (errorCode == 14) {
            mTextView!!.text = "Feedback send successfully"
        }
        if (errorCode == 15) {
            mTextView!!.text = "Salesorder submitted successfully"
        }
        if (errorCode == 16) {
            mTextView!!.text = "No enough values for placing sales order"
        }
        if (errorCode == 17) {
            mTextView!!.text = "Sorry, no result found"
        }
        if (errorCode == 18) {
            mTextView!!.text = "Please select any Category"
        }
        if (errorCode == 19) {
            mTextView!!.text = "Please select any Sub Category in filter"
        }
        if (errorCode == 20) {
            mTextView!!.text = "Please select state"
        }
        if (errorCode == 21) {
            mTextView!!.text = "Please select district"
        }
        if (errorCode == 22) {
            mTextView!!.text = "Please select place"
        }
        if (errorCode == 23) {
            mTextView?.setText("Login failed.Invalid login credentials or account not approved")
        }
        if (errorCode == 24) {
            mTextView!!.text = "Sorry, cannot add this item to cart"
        }
        if (errorCode == 25) {
            mTextView!!.text = "Cannot enter value greater than existing quantity"
        }
        if (errorCode == 26) {
            mTextView!!.text = "Order updated successfully"
        }
        if (errorCode == 27) {
            mTextView!!.text = "Cannot enter a quantity greater than order quantity"
        }
        if (errorCode == 28) {
            mTextView!!.text = "Updated successfully !"
        }
        if (errorCode == 29) {
            mTextView!!.text = "Quantity cannot be empty !"
        }
        if (errorCode == 30) {
            mTextView!!.text = "Please fill all the quantity fields correctly"
        }
        if (errorCode == 31) {
            mTextView!!.text = "Dealers added successfully !"
        }
        if (errorCode == 32) {
            mTextView!!.text = "There are no pending orders"
        }
        if (errorCode == 33) {
            mTextView!!.text = "State value missing"
        }
        if (errorCode == 34) {
            mTextView!!.text = "District value missing"
        }
        if (errorCode == 35) {
            mTextView!!.text = "Product already exist in cart adding quantity"
        }
        if (errorCode == 36) {
            mTextView!!.text = "Please enter keyword to search"
        }
        if (errorCode == 37) {
            mTextView!!.text = "No results found"
        }
        if (errorCode == 38) {
            mTextView!!.text = "Please enter a keyword to search"
        }
        if (errorCode == 39) {
            mTextView!!.text = "Please select a customer category"
        }
        if (errorCode == 40) {
            mTextView!!.text = "Please fill the quantity value before you proceed"
        }
        if (errorCode == 41) {
            mTextView!!.text =
                "Please choose customer category and customer name from cart before you proceed"
        }
        if (errorCode == 42) {
            mTextView!!.text = "No details found"
        }
        if (errorCode == 43) {
            mTextView!!.text = "No results found"
        }
        if (errorCode == 44) {
            mTextView!!.text = "No records to display"
        }
        if (errorCode == 45) {
            mTextView!!.text = "Please enter the article number"
        }
        if (errorCode == 46) {
            mTextView!!.text = "Unable to add to cart as your cart value exceeds credit limit !"
        }
        if (errorCode == 47) {
            mTextView!!.text = "Please check the size value !"
        }
        if (errorCode == 48) {
            mTextView!!.text = "Issue point should not be greater than your points!"
        }
        if (errorCode == 49) {
            mTextView!!.text = "Please select user type !"
        }
        if (errorCode == 50) {
            mTextView!!.text = "Successfully issued points"
        }
        if (errorCode == 51) {
            mTextView!!.text = "Please select a user"
        }
        if (errorCode == 52) {
            mTextView!!.text = "Please enter point value"
        }
        if (errorCode == 53) {
            mTextView!!.text = "Please enter email Id"
        }
        if (errorCode == 54) {
            mTextView!!.text = "Invalid email Id"
        }
        if (errorCode == 55) {
            mTextView!!.text = "Cart has been cleared successfully"
        }
        if (errorCode == 56) {
            mTextView!!.text = "Please select dealer to load the cart data"
        }
        if (errorCode == 57) {
            mTextView!!.text =
                "Unable to load transaction history, since there is no scheme running in your state"
        }
        if (errorCode == 58) {
            mTextView!!.text =
                "Unable to load report, since there is no scheme running in your state"
        }
        if (errorCode == 59) {
            mTextView!!.text = "Please grant permission to continue"
        }
        mToast!!.duration = Toast.LENGTH_SHORT
        mToast!!.show()
    } /*
	 * CustomToast toast = new CustomToast(mActivity); toast.show(18);
	 */

    init {
        init()
    }
}