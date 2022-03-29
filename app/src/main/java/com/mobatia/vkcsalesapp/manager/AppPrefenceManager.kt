/**
 *
 *//*

package com.mobatia.vkcsalesapp.manager

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import org.json.JSONArray

*/
/**
 * Bibin
 *//*

*/
/*
 * Bibin Comment 4. Sales Head 5. Retailer 6. Dealer 7. Sub Dealer
 *//*

object AppPrefenceManager {
    const val PREFS_NAME = "VKC"
    const val PREFS_KEY_TYPE = "type"
    const val PREFS_KEY_OFFER = "offer"
    const val PREFS_KEY_SIZE = "size"
    const val PREFS_KEY_COLOR = "color"
    const val PREFS_KEY_PRICE = "price_range"
    const val PREFS_REG_ID = "Regid"
    const val PREFS_FILTER_DATACATEGORY = "category"
    const val PREFS_FILTER_DATASIZE = "sizefilter"
    const val PREFS_FILTER_DATAOFFER = "offerfilter"
    const val PREFS_FILTER_DATABRAND = "brandfilter"
    const val PREFS_FILTER_DATAPRICE = "pricefilter"
    const val PREFS_FILTER_DATACOLOR = "colorfilter"
    const val PREFS_BANNE_RESPONSE = "BannerResponse"
    const val PREFS_FILTER_DATASUBCATEGORY = "subcategory"
    const val PREFS_TOP_SLIDER = "top_slider"
    const val PREFS_BOTTOM_SLIDER = "bottom_slider"
    const val IDS_FOR_OFFER = "idsforoffer"
    const val OFFER_IDS = "offer_ids"
    const val LISTING_OPTION = "lising_option"
    const val BRAND_BANNER = "BrandBanner"
    const val LIST_TYPE = "ListType"
    const val BRAND_ID_FOR_SEARCH = "brand_id_for_search"
    const val MAIN_CATEGORY = "MAIN_CATEGORY"
    const val SUB_CATEGORY_ID = "SUB_CATEGORY_ID"
    const val PRODUCT_LIST_SORTOPTION = "PRODUCT_LIST_SORTOPTION"
    const val USERTYPE = "usertype"
    const val USERID = "userid"
    const val USERNAME = "username"

     fun getIsGroupMember(context: Context): String? {
        val settings: SharedPreferences
        val text: String?
        settings = context.getSharedPreferences(
            PREFS_NAME,
            Context.MODE_PRIVATE
        )
        text = settings.getString("is_grp_member", "")
        return text
    }

    fun saveIsGroupMember(context: Context, text: String) {
        val settings: SharedPreferences
        val editor: SharedPreferences.Editor
        settings = context.getSharedPreferences(
            PREFS_NAME,
            Context.MODE_PRIVATE
        )
        editor = settings.edit()
        editor.putString("is_grp_member", text)
        editor.commit()
    }

    fun getProductListSortOption(context: Context): String? {
        val settings: SharedPreferences
        val text: String?
        settings = context.getSharedPreferences(
            PREFS_NAME,
            Context.MODE_PRIVATE
        )
        text = settings.getString(PRODUCT_LIST_SORTOPTION, "0")
        return text
    }

    fun saveProductListSortOption(context: Context, text: String) {
        val settings: SharedPreferences
        val editor: SharedPreferences.Editor
        settings = context.getSharedPreferences(
            PREFS_NAME,
            Context.MODE_PRIVATE
        )
        editor = settings.edit()
        editor.putString(PRODUCT_LIST_SORTOPTION, text)
        editor.commit()
    }

    fun getSubCategoryId(context: Context): String {
        val settings: SharedPreferences
        val text: String?
        settings = context.getSharedPreferences(
            PREFS_NAME,
            Context.MODE_PRIVATE
        )
        text = settings.getString(SUB_CATEGORY_ID, "")
        return text.toString()
    }

    fun saveSubCategoryId(context: Context, text: String) {
        val settings: SharedPreferences
        val editor: SharedPreferences.Editor
        settings = context.getSharedPreferences(
            PREFS_NAME,
            Context.MODE_PRIVATE
        )
        editor = settings.edit()
        editor.putString(SUB_CATEGORY_ID, text)
        editor.commit()
    }

    fun getMainCategory(context: Context): String? {
        val settings: SharedPreferences
        val text: String?
        settings = context.getSharedPreferences(
            PREFS_NAME,
            Context.MODE_PRIVATE
        )
        text = settings.getString(MAIN_CATEGORY, "")
        return text
    }

    fun getDealerListCount(context: Context): String? {
        val settings: SharedPreferences
        val text: String?
        settings = context.getSharedPreferences(
            PREFS_NAME,
            Context.MODE_PRIVATE
        )
        text = settings.getString("dealer_count", "")
        return text
    }

    fun saveDealerListCount(context: Context, text: String) {
        val settings: SharedPreferences
        val editor: SharedPreferences.Editor
        settings = context.getSharedPreferences(
            PREFS_NAME,
            Context.MODE_PRIVATE
        )
        editor = settings.edit()
        editor.putString("dealer_count", text)
        editor.commit()
    }

    fun saveMainCategory(context: Context, text: String) {
        val settings: SharedPreferences
        val editor: SharedPreferences.Editor
        settings = context.getSharedPreferences(
            PREFS_NAME,
            Context.MODE_PRIVATE
        )
        editor = settings.edit()
        editor.putString(MAIN_CATEGORY, text)
        editor.commit()
    }

    // From Filter :2
    // From Offer Banner:1
    // From Home menu :0
    // For Search : 3
    // For Brand : 4
     fun saveListingOption(context: Context, text: String?) {
        val settings: SharedPreferences
        val editor: SharedPreferences.Editor
        settings = context.getSharedPreferences(
            PREFS_NAME,
            Context.MODE_PRIVATE
        ) // 1
        editor = settings.edit() // 2
        editor.putString(LISTING_OPTION, text) // 3
        editor.commit() // 4
    }

    fun getListingOption(context: Context): String? {
        val settings: SharedPreferences
        val text: String?
        settings = context.getSharedPreferences(
            PREFS_NAME,
            Context.MODE_PRIVATE
        ) // 1
        text = settings.getString(LISTING_OPTION, null) // 2
        return text
    }

    // Brand Id for listing
    fun saveBrandIdForSearch(context: Context, text: String?) {
        val settings: SharedPreferences
        val editor: SharedPreferences.Editor
        settings = context.getSharedPreferences(
            PREFS_NAME,
            Context.MODE_PRIVATE
        ) // 1
        editor = settings.edit() // 2
        editor.putString(BRAND_ID_FOR_SEARCH, text) // 3
        editor.commit() // 4
    }

    fun getBrandIdForSearch(context: Context): String? {
        val settings: SharedPreferences
        val text: String?
        settings = context.getSharedPreferences(
            PREFS_NAME,
            Context.MODE_PRIVATE
        ) // 1
        text = settings.getString(BRAND_ID_FOR_SEARCH, null) // 2
        return text
    }

    //
    fun saveOfferIDs(context: Context, text: String?) {
        val settings: SharedPreferences
        val editor: SharedPreferences.Editor
        settings = context.getSharedPreferences(
            PREFS_NAME,
            Context.MODE_PRIVATE
        ) // 1
        editor = settings.edit() // 2
        editor.putString(OFFER_IDS, text) // 3
        editor.commit() // 4
    }

    fun getOfferIDs(context: Context): String {
        val settings: SharedPreferences
        val text: String?
        settings = context.getSharedPreferences(
            PREFS_NAME,
            Context.MODE_PRIVATE
        ) // 1
        text = settings.getString(OFFER_IDS, "") // 2
        return text.toString()
    }

    fun saveIDsForOffer(context: Context, text: String?) {
        val settings: SharedPreferences
        val editor: SharedPreferences.Editor
        settings = context.getSharedPreferences(
            PREFS_NAME,
            Context.MODE_PRIVATE
        ) // 1
        editor = settings.edit() // 2
        editor.putString(IDS_FOR_OFFER, text) // 3
        editor.commit() // 4
    }

    fun getIDsForOffer(context: Context): String {
        val settings: SharedPreferences
        val text: String?
        settings = context.getSharedPreferences(
            PREFS_NAME,
            Context.MODE_PRIVATE
        ) // 1
        text = settings.getString(IDS_FOR_OFFER, null) // 2
        return text.toString()
    }

    fun saveJsonOfferResponse(context: Context, text: JSONArray) {
        val settings: SharedPreferences
        val editor: SharedPreferences.Editor
        settings = context.getSharedPreferences(
            PREFS_NAME,
            Context.MODE_PRIVATE
        ) // 1
        editor = settings.edit() // 2
        editor.putString(PREFS_KEY_OFFER, text.toString()) // 3
        editor.commit() // 4
    }

    fun getJsonOfferResponse(context: Context): String? {
        val settings: SharedPreferences
        val text: String?
        settings = context.getSharedPreferences(
            PREFS_NAME,
            Context.MODE_PRIVATE
        ) // 1
        text = settings.getString(PREFS_KEY_OFFER, null) // 2
        return text
    }

    fun saveJsonTypeResponse(context: Context, text: JSONArray) {
        val settings: SharedPreferences
        val editor: SharedPreferences.Editor
        settings = context.getSharedPreferences(
            PREFS_NAME,
            Context.MODE_PRIVATE
        ) // 1
        editor = settings.edit() // 2
        editor.putString(PREFS_KEY_TYPE, text.toString()) // 3
        editor.commit() // 4
    }

    fun getJsonTypeResponse(context: Context): String? {
        val settings: SharedPreferences
        val text: String?
        settings = context.getSharedPreferences(
            PREFS_NAME,
            Context.MODE_PRIVATE
        ) // 1
        text = settings.getString(PREFS_KEY_TYPE, null) // 2
        return text
    }

    fun saveJsonSizeResponse(context: Context, text: JSONArray) {
        val settings: SharedPreferences
        val editor: SharedPreferences.Editor
        settings = context.getSharedPreferences(
            PREFS_NAME,
            Context.MODE_PRIVATE
        ) // 1
        editor = settings.edit() // 2
        editor.putString(PREFS_KEY_SIZE, text.toString()) // 3
        editor.commit() // 4
    }

    fun getJsonSizeResponse(context: Context): String? {
        val settings: SharedPreferences
        val text: String?
        settings = context.getSharedPreferences(
            PREFS_NAME,
            Context.MODE_PRIVATE
        ) // 1
        text = settings.getString(PREFS_KEY_SIZE, null) // 2
        return text
    }

    fun saveJsonColorResponse(context: Context, text: JSONArray) {
        val settings: SharedPreferences
        val editor: SharedPreferences.Editor
        settings = context.getSharedPreferences(
            PREFS_NAME,
            Context.MODE_PRIVATE
        ) // 1
        editor = settings.edit() // 2
        editor.putString(PREFS_KEY_COLOR, text.toString()) // 3
        editor.commit() // 4
    }

    fun getJsonColorResponse(context: Context): String? {
        val settings: SharedPreferences
        val text: String?
        settings = context.getSharedPreferences(
            PREFS_NAME,
            Context.MODE_PRIVATE
        ) // 1
        text = settings.getString(PREFS_KEY_COLOR, null) // 2
        return text
    }

    fun saveJsonPriceResponse(context: Context, text: JSONArray) {
        val settings: SharedPreferences
        val editor: SharedPreferences.Editor
        settings = context.getSharedPreferences(
            PREFS_NAME,
            Context.MODE_PRIVATE
        ) // 1
        editor = settings.edit() // 2
        editor.putString(PREFS_KEY_PRICE, text.toString()) // 3
        editor.commit() // 4
    }

    fun getJsonPriceResponse(context: Context): String? {
        val settings: SharedPreferences
        val text: String?
        settings = context.getSharedPreferences(
            PREFS_NAME,
            Context.MODE_PRIVATE
        ) // 1
        text = settings.getString(PREFS_KEY_PRICE, null) // 2
        return text
    }

    */
/**
     * Save regid.
     *
     * @param Regid    the regid
     * @param mContext the m context
     *//*

    fun saveRegid(Regid: String?, mContext: Context) {
        val prefs = mContext.getSharedPreferences(
            PREFS_NAME,
            Context.MODE_PRIVATE
        )
        val editor = prefs.edit()
        editor.putString(PREFS_REG_ID, Regid)
        editor.commit()
    }

    */
/**
     * Gets the regid.
     *
     * @param mContext the m context
     * @return the regid
     *//*

    fun getRegid(mContext: Context): String? {
        var value: String?
        val prefs = mContext.getSharedPreferences(
            PREFS_NAME,
            Context.MODE_PRIVATE
        )
        value = prefs.getString(PREFS_REG_ID, "")
        return value
    }

    fun getFilterDataCategory(context: Context): String {
        val settings: SharedPreferences
        val text: String?
        settings = context.getSharedPreferences(
            PREFS_NAME,
            Context.MODE_PRIVATE
        ) // 1
        text = settings.getString(PREFS_FILTER_DATACATEGORY, "") // 2
        return text.toString()
    }

    fun saveFilterDataCategory(context: Context, text: String) {
        val settings: SharedPreferences
        val editor: SharedPreferences.Editor
        settings = context.getSharedPreferences(
            PREFS_NAME,
            Context.MODE_PRIVATE
        ) // 1
        editor = settings.edit() // 2
        editor.putString(PREFS_FILTER_DATACATEGORY, text) // 3
        editor.commit() // 4
    }

    fun getFilterDataSize(context: Context): String {
        val settings: SharedPreferences
        val text: String?
        settings = context.getSharedPreferences(
            PREFS_NAME,
            Context.MODE_PRIVATE
        ) // 1
        text = settings.getString(PREFS_FILTER_DATASIZE, "") // 2
        return text.toString()
    }

    fun saveFilterDataSize(context: Context, text: String) {
        val settings: SharedPreferences
        val editor: SharedPreferences.Editor
        settings = context.getSharedPreferences(
            PREFS_NAME,
            Context.MODE_PRIVATE
        ) // 1
        editor = settings.edit() // 2
        editor.putString(PREFS_FILTER_DATASIZE, text) // 3
        editor.commit() // 4
    }

    fun getFilterDataOffer(context: Context): String {
        val settings: SharedPreferences
        val text: String?
        settings = context.getSharedPreferences(
            PREFS_NAME,
            Context.MODE_PRIVATE
        ) // 1
        text = settings.getString(PREFS_FILTER_DATAOFFER, "") // 2
        return text.toString()
    }

    fun saveFilterDataOffer(context: Context, text: String) {
        val settings: SharedPreferences
        val editor: SharedPreferences.Editor
        settings = context.getSharedPreferences(
            PREFS_NAME,
            Context.MODE_PRIVATE
        ) // 1
        editor = settings.edit() // 2
        editor.putString(PREFS_FILTER_DATAOFFER, text) // 3
        editor.commit() // 4
    }

    fun getFilterDataBrand(context: Context): String {
        val settings: SharedPreferences
        val text: String?
        settings = context.getSharedPreferences(
            PREFS_NAME,
            Context.MODE_PRIVATE
        ) // 1
        text = settings.getString(PREFS_FILTER_DATABRAND, "") // 2
        return text.toString()
    }

    fun saveFilterDataBrand(context: Context, text: String) {
        val settings: SharedPreferences
        val editor: SharedPreferences.Editor
        settings = context.getSharedPreferences(
            PREFS_NAME,
            Context.MODE_PRIVATE
        ) // 1
        editor = settings.edit() // 2
        editor.putString(PREFS_FILTER_DATABRAND, text) // 3
        editor.commit() // 4
    }

    fun getFilterDataPrice(context: Context): String? {
        val settings: SharedPreferences
        val text: String?
        settings = context.getSharedPreferences(
            PREFS_NAME,
            Context.MODE_PRIVATE
        ) // 1
        text = settings.getString(PREFS_FILTER_DATAPRICE, "") // 2
        return text
    }

    fun saveFilterDataPrice(context: Context, text: String) {
        val settings: SharedPreferences
        val editor: SharedPreferences.Editor
        settings = context.getSharedPreferences(
            PREFS_NAME,
            Context.MODE_PRIVATE
        ) // 1
        editor = settings.edit() // 2
        editor.putString(PREFS_FILTER_DATAPRICE, text) // 3
        editor.commit() // 4
    }

    fun getFilterDataColor(context: Context): String? {
        val settings: SharedPreferences
        val text: String?
        settings = context.getSharedPreferences(
            PREFS_NAME,
            Context.MODE_PRIVATE
        ) // 1
        text = settings.getString(PREFS_FILTER_DATACOLOR, "") // 2
        return text
    }

    fun saveFilterDataColor(context: Context, text: String) {
        val settings: SharedPreferences
        val editor: SharedPreferences.Editor
        settings = context.getSharedPreferences(
            PREFS_NAME,
            Context.MODE_PRIVATE
        ) // 1
        editor = settings.edit() // 2
        editor.putString(PREFS_FILTER_DATACOLOR, text) // 3
        editor.commit() // 4
    }

    fun getNewArrivalBannerResponse(context: Context): String? {
        val settings: SharedPreferences
        val text: String?
        settings = context.getSharedPreferences(
            PREFS_NAME,
            Context.MODE_PRIVATE
        ) // 1
        text = settings.getString(PREFS_BANNE_RESPONSE, "") // 2
        return text
    }

    fun saveNewArrivalBannerResponse(context: Context, text: String) {
        val settings: SharedPreferences
        val editor: SharedPreferences.Editor
        settings = context.getSharedPreferences(
            PREFS_NAME,
            Context.MODE_PRIVATE
        ) // 1
        editor = settings.edit() // 2
        editor.putString(PREFS_BANNE_RESPONSE, text) // 3
        editor.commit() // 4
    }

    // public static String getTopSliderResponse(Context context) {
    // SharedPreferences settings;
    // String text;
    // settings = context.getSharedPreferences(PREFS_NAME,
    // Context.MODE_PRIVATE); // 1
    // text = settings.getString(PREFS_TOP_SLIDER, ""); // 2
    // return text;
    // }
    //
    // public static void saveTopSliderResponse(Context context, String text) {
    // SharedPreferences settings;
    // Editor editor;
    // settings = context.getSharedPreferences(PREFS_NAME,
    // Context.MODE_PRIVATE); // 1
    // editor = settings.edit(); // 2
    //
    // editor.putString(PREFS_TOP_SLIDER, text.toString()); // 3
    // editor.commit(); // 4
    // }
    fun getPopularProductSliderResponse(context: Context): String? {
        val settings: SharedPreferences
        val text: String?
        settings = context.getSharedPreferences(
            PREFS_NAME,
            Context.MODE_PRIVATE
        ) // 1
        text = settings.getString(PREFS_BOTTOM_SLIDER, "") // 2
        return text
    }

    fun savePopularProductSliderResponse(
        context: Context,
        text: String
    ) {
        val settings: SharedPreferences
        val editor: SharedPreferences.Editor
        settings = context.getSharedPreferences(
            PREFS_NAME,
            Context.MODE_PRIVATE
        ) // 1
        editor = settings.edit() // 2
        editor.putString(PREFS_BOTTOM_SLIDER, text) // 3
        editor.commit() // 4
    }

    // //////
    fun getBrandBannerResponse(context: Context): String? {
        val settings: SharedPreferences
        val text: String?
        settings = context.getSharedPreferences(
            PREFS_NAME,
            Context.MODE_PRIVATE
        ) // 1
        text = settings.getString(BRAND_BANNER, "") // 2
        return text
    }

    fun saveBrandBannerResponse(context: Context, text: String) {
        val settings: SharedPreferences
        val editor: SharedPreferences.Editor
        settings = context.getSharedPreferences(
            PREFS_NAME,
            Context.MODE_PRIVATE
        ) // 1
        editor = settings.edit() // 2
        editor.putString(BRAND_BANNER, text) // 3
        editor.commit() // 4
    }

    fun getListType(context: Context): String? {
        val settings: SharedPreferences
        val text: String?
        settings = context.getSharedPreferences(
            PREFS_NAME,
            Context.MODE_PRIVATE
        ) // 1
        text = settings.getString(LIST_TYPE, "") // 2
        return text
    }

    fun saveListType(context: Context, text: String) {
        val settings: SharedPreferences
        val editor: SharedPreferences.Editor
        settings = context.getSharedPreferences(
            PREFS_NAME,
            Context.MODE_PRIVATE
        ) // 1
        editor = settings.edit() // 2
        editor.putString(LIST_TYPE, text) // 3
        editor.commit() // 4
    }

    fun getUserType(context: Context): String {
        val settings: SharedPreferences
        val text: String?
        settings = context.getSharedPreferences(
            PREFS_NAME,
            Context.MODE_PRIVATE
        )
        text = settings.getString(USERTYPE, "")
        return text.toString()
    }

      fun saveUserType(context: Context, text: String?) { // 1-SalesPerson,2-Dealer,3-Retailer
        val settings: SharedPreferences
        val editor: SharedPreferences.Editor
        settings = context.getSharedPreferences(
            PREFS_NAME,
            Context.MODE_PRIVATE
        )
        editor = settings.edit()
        editor.putString(USERTYPE, text)
        editor.commit()
    }

    fun getUserId(context: Context): String {
        val settings: SharedPreferences
        val text: String?
        settings = context.getSharedPreferences(
            PREFS_NAME,
            Context.MODE_PRIVATE
        )
        text = settings.getString(USERID, "")
        return text.toString()
    }

    fun saveUserId(context: Context, text: String?) {
        val settings: SharedPreferences
        val editor: SharedPreferences.Editor
        settings = context.getSharedPreferences(
            PREFS_NAME,
            Context.MODE_PRIVATE
        )
        editor = settings.edit()
        editor.putString(USERID, text)
        editor.commit()
    }

    fun getCustomerId(context: Context): String {
        val settings: SharedPreferences
        val text: String?
        settings = context.getSharedPreferences(
            PREFS_NAME,
            Context.MODE_PRIVATE
        )
        text = settings.getString("custId", "")
        return text!!
    }

    fun saveCustomerId(context: Context, text: String?) {
        val settings: SharedPreferences
        val editor: SharedPreferences.Editor
        settings = context.getSharedPreferences(
            PREFS_NAME,
            Context.MODE_PRIVATE
        )
        editor = settings.edit()
        editor.putString("custId", text)
        editor.commit()
    }

    fun getUserName(context: Context): String? {
        val settings: SharedPreferences
        val text: String?
        settings = context.getSharedPreferences(
            PREFS_NAME,
            Context.MODE_PRIVATE
        )
        text = settings.getString(USERNAME, "")
        return text
    }

    fun saveUserName(context: Context, text: String?) {
        val settings: SharedPreferences
        val editor: SharedPreferences.Editor
        settings = context.getSharedPreferences(
            PREFS_NAME,
            Context.MODE_PRIVATE
        )
        editor = settings.edit()
        editor.putString(USERNAME, text)
        editor.commit()
    }

    fun getStateCode(context: Context): String {
        val settings: SharedPreferences
        val text: String?
        settings = context.getSharedPreferences(
            PREFS_NAME,
            Context.MODE_PRIVATE
        )
        text = settings.getString("statecode", "")
        return text.toString()
    }

    fun saveStateCode(context: Context, text: String?) {
        val settings: SharedPreferences
        val editor: SharedPreferences.Editor
        settings = context.getSharedPreferences(
            PREFS_NAME,
            Context.MODE_PRIVATE
        )
        editor = settings.edit()
        editor.putString("statecode", text)
        editor.commit()
    }

    fun getName(context: Context): String? {
        val settings: SharedPreferences
        val text: String?
        settings = context.getSharedPreferences(
            PREFS_NAME,
            Context.MODE_PRIVATE
        )
        text = settings.getString("name", "")
        return text
    }

    fun saveName(context: Context, text: String) {
        val settings: SharedPreferences
        val editor: SharedPreferences.Editor
        settings = context.getSharedPreferences(
            PREFS_NAME,
            Context.MODE_PRIVATE
        )
        editor = settings.edit()
        editor.putString("name", text)
        editor.commit()
    }

    fun getLoginStatusFlag(context: Context): String? {
        val settings: SharedPreferences
        val text: String?
        settings = context.getSharedPreferences(
            PREFS_NAME,
            Context.MODE_PRIVATE
        )
        text = settings.getString("loginstatus", "")
        return text
    }

    fun saveLoginStatusFlag(context: Context, text: String) {
        val settings: SharedPreferences
        val editor: SharedPreferences.Editor
        settings = context.getSharedPreferences(
            PREFS_NAME,
            Context.MODE_PRIVATE
        )
        editor = settings.edit()
        editor.putString("loginstatus", text)
        editor.commit()
    }

    fun getLoginCustomerName(context: Context): String? {
        val settings: SharedPreferences
        val text: String?
        settings = context.getSharedPreferences(
            PREFS_NAME,
            Context.MODE_PRIVATE
        )
        text = settings.getString("customer_name", "")
        return text
    }

    fun setLoginCustomerName(context: Context, text: String) {
        val settings: SharedPreferences
        val editor: SharedPreferences.Editor
        settings = context.getSharedPreferences(
            PREFS_NAME,
            Context.MODE_PRIVATE
        )
        editor = settings.edit()
        editor.putString("customer_name", text)
        editor.commit()
    }

    fun getLoginPlace(context: Context): String? {
        val settings: SharedPreferences
        val text: String?
        settings = context.getSharedPreferences(
            PREFS_NAME,
            Context.MODE_PRIVATE
        )
        text = settings.getString("log_place", "")
        return text
    }

    fun setLoginPlace(context: Context, text: String) {
        val settings: SharedPreferences
        val editor: SharedPreferences.Editor
        settings = context.getSharedPreferences(
            PREFS_NAME,
            Context.MODE_PRIVATE
        )
        editor = settings.edit()
        editor.putString("log_place", text)
        editor.commit()
    }

    fun getDealerCount(context: Context): String? {
        val settings: SharedPreferences
        val text: String?
        settings = context.getSharedPreferences(
            PREFS_NAME,
            Context.MODE_PRIVATE
        )
        text = settings.getString("dealer_count", "")
        return text
    }

    fun saveDealerCount(context: Context, text: String) {
        val settings: SharedPreferences
        val editor: SharedPreferences.Editor
        settings = context.getSharedPreferences(
            PREFS_NAME,
            Context.MODE_PRIVATE
        )
        editor = settings.edit()
        editor.putString("dealer_count", text)
        editor.commit()
    }

    fun getIsCredit(context: Context): String? {
        val settings: SharedPreferences
        val text: String?
        settings = context.getSharedPreferences(
            PREFS_NAME,
            Context.MODE_PRIVATE
        )
        text = settings.getString("is_credit", "")
        return text
    }

    fun saveIsCredit(context: Context, text: String?) {
        val settings: SharedPreferences
        val editor: SharedPreferences.Editor
        settings = context.getSharedPreferences(
            PREFS_NAME,
            Context.MODE_PRIVATE
        )
        editor = settings.edit()
        editor.putString("is_credit", text)
        editor.commit()
    }

    fun getCustomerCategory(context: Context): String {
        val settings: SharedPreferences
        val text: String?
        settings = context.getSharedPreferences(
            PREFS_NAME,
            Context.MODE_PRIVATE
        )
        text = settings.getString("cust_cat", "")
        return text.toString()
    }

    fun saveCustomerCategory(context: Context, text: String) {
        val settings: SharedPreferences
        val editor: SharedPreferences.Editor
        settings = context.getSharedPreferences(
            PREFS_NAME,
            Context.MODE_PRIVATE
        )
        editor = settings.edit()
        editor.putString("cust_cat", text)
        editor.commit()
    }

    fun getSelectedUserName(context: Context): String? {
        val settings: SharedPreferences
        val text: String?
        settings = context.getSharedPreferences(
            PREFS_NAME,
            Context.MODE_PRIVATE
        )
        text = settings.getString("sel_user", "")
        return text
    }

    fun saveSelectedUserName(context: Context, text: String) {
        val settings: SharedPreferences
        val editor: SharedPreferences.Editor
        settings = context.getSharedPreferences(
            PREFS_NAME,
            Context.MODE_PRIVATE
        )
        editor = settings.edit()
        editor.putString("sel_user", text)
        editor.commit()
    }

    fun getSelectedUserId(context: Context): String {
        val settings: SharedPreferences
        val text: String?
        settings = context.getSharedPreferences(
            PREFS_NAME,
            Context.MODE_PRIVATE
        )
        text = settings.getString("sel_user_id", "")
        return text!!
    }

    fun saveSelectedUserId(context: Context, text: String) {
        val settings: SharedPreferences
        val editor: SharedPreferences.Editor
        settings = context.getSharedPreferences(
            PREFS_NAME,
            Context.MODE_PRIVATE
        )
        editor = settings.edit()
        editor.putString("sel_user_id", text)
        editor.commit()
    }

    fun getFillTable(context: Context): String? {
        val settings: SharedPreferences
        val text: String?
        settings = context.getSharedPreferences(
            PREFS_NAME,
            Context.MODE_PRIVATE
        )
        text = settings.getString("set_fill_table", "")
        return text
    }

    fun setFillTable(context: Context, text: String) {
        val settings: SharedPreferences
        val editor: SharedPreferences.Editor
        settings = context.getSharedPreferences(
            PREFS_NAME,
            Context.MODE_PRIVATE
        )
        editor = settings.edit()
        editor.putString("set_fill_table", text)
        editor.commit()
    }

    fun getIsCallPendingAPI(context: Context): Boolean {
        val settings: SharedPreferences
        val text: Boolean
        settings = context.getSharedPreferences(
            PREFS_NAME,
            Context.MODE_PRIVATE
        )
        text = settings.getBoolean("call_pending", true)
        return text
    }

    fun setIsCallPendingAPI(context: Context, text: Boolean) {
        val settings: SharedPreferences
        val editor: SharedPreferences.Editor
        settings = context.getSharedPreferences(
            PREFS_NAME,
            Context.MODE_PRIVATE
        )
        editor = settings.edit()
        editor.putBoolean("call_pending", text)
        editor.commit()
    }

    fun getDate(context: Context): String? {
        val settings: SharedPreferences
        val text: String?
        settings = context.getSharedPreferences(
            PREFS_NAME,
            Context.MODE_PRIVATE
        )
        text = settings.getString("current_date", "")
        return text
    }

    fun setDate(context: Context, text: String?) {
        val settings: SharedPreferences
        val editor: SharedPreferences.Editor
        settings = context.getSharedPreferences(
            PREFS_NAME,
            Context.MODE_PRIVATE
        )
        editor = settings.edit()
        editor.putString("current_date", text)
        editor.commit()
    }

    fun getParentCatId(context: Context): String? {
        val settings: SharedPreferences
        val text: String?
        settings = context.getSharedPreferences(
            PREFS_NAME,
            Context.MODE_PRIVATE
        )
        text = settings.getString("p_id", "")
        return text
    }

    fun setParentCatId(context: Context, text: String?) {
        val settings: SharedPreferences
        val editor: SharedPreferences.Editor
        settings = context.getSharedPreferences(
            PREFS_NAME,
            Context.MODE_PRIVATE
        )
        editor = settings.edit()
        editor.putString("p_id", text)
        editor.commit()
    }

    fun getCatId(context: Context): String? {
        val settings: SharedPreferences
        val text: String?
        settings = context.getSharedPreferences(
            PREFS_NAME,
            Context.MODE_PRIVATE
        )
        text = settings.getString("pi_id", "")
        return text
    }

    fun setCatId(context: Context, text: String?) {
        val settings: SharedPreferences
        val editor: SharedPreferences.Editor
        settings = context.getSharedPreferences(
            PREFS_NAME,
            Context.MODE_PRIVATE
        )
        editor = settings.edit()
        editor.putString("pi_id", text)
        editor.commit()
    }

    fun getFilterDataSubCategory(context: Context): String? {
        val settings: SharedPreferences
        val text: String?
        settings = context.getSharedPreferences(
            PREFS_NAME,
            Context.MODE_PRIVATE
        ) // 1
        text = settings.getString(PREFS_FILTER_DATASUBCATEGORY, "") // 2
        return text
    }

    fun saveFilterDataSubCategory(context: Context, text: String) {
        val settings: SharedPreferences
        val editor: SharedPreferences.Editor
        settings = context.getSharedPreferences(
            PREFS_NAME,
            Context.MODE_PRIVATE
        ) // 1
        editor = settings.edit() // 2
        editor.putString(PREFS_FILTER_DATASUBCATEGORY, text) // 3
        editor.commit() // 4
    }

    fun getFCMID(context: Context): String {
        val settings: SharedPreferences
        val text: String?
        settings = context.getSharedPreferences(
            PREFS_NAME,
            Context.MODE_PRIVATE
        ) // 1
        text = settings.getString("FCM_ID", "") // 2
        return text.toString()
    }

    fun saveFCMID(context: Context, text: String) {
        val settings: SharedPreferences
        val editor: SharedPreferences.Editor
        settings = context.getSharedPreferences(
            PREFS_NAME,
            Context.MODE_PRIVATE
        ) // 1
        editor = settings.edit() // 2
        editor.putString("FCM_ID", text) // 3
        editor.commit() // 4
    }


}*/
