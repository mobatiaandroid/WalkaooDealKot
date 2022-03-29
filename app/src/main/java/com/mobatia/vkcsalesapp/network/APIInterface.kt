package com.vkc.loyaltyapp.api

import com.google.gson.JsonObject
import com.mobatia.vkcsalesapp.model.LoginResponseModel
import com.mobatia.vkcsalesapp.model.ProductSettingsResponseModel
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface APIInterface {



    @FormUrlEncoded
    @POST("apiv2/login")
    fun getLogin(@Field("email") email: String,@Field("password") password:String,@Field("imei_no") imei_no:String): Call<LoginResponseModel>

    @POST("apiv2/getsettings")
    fun getProductData():Call<ProductSettingsResponseModel>
    /* @POST("getstate")
     fun getStateResponse(): Call<StateResponseModel>

     @FormUrlEncoded
     @POST("getdistrict")
     fun getDistrictResponse(@Field("state") stateId: String): Call<DistrictResponseModel>

     @FormUrlEncoded
     @POST("getuserdetailswithMobile")
     fun getUserData(
         @Field("mobile") mMobile: String,
         @Field("customer_id") mCusId: String,
         @Field("imei_no") mImei: String
     ): Call<UserDataResponseModel>


     @FormUrlEncoded
     @POST("resend_otp")
     fun sendOTPRequest(
         @Field("role") role: String,
         @Field("cust_id") custId: String,
         @Field("sms_key") key: String
     ): Call<SendOTPModel>

     @FormUrlEncoded
     @POST("newRegRequest")
     fun newRegistration(
         @Field("customer_id") customer_id: String,
         @Field("shop_name") shop_name: String,
         @Field("state_name") state_name: String,
         @Field("district") district: String,
         @Field("city") city: String,
         @Field("pincode") pincode: String,
         @Field("contact_person") contact_person: String,
         @Field("phone") phone: String,
         @Field("door_no") door_no: String,
         @Field("address_line1") address_line1: String,
         @Field("landmark") landmark: String,
         @Field("user_type") user_type: String
     ): Call<NewRegistrationResponseModel>

     @FormUrlEncoded
     @POST("OTP_verification")
     fun verifyOTP_API(
         @Field("otp") otp: String,
         @Field("role") role: String,
         @Field("cust_id") cust_id: String,
         @Field("phone") phone: String,
         @Field("isnewMobile") isnewMobile: String,
         @Field("imei_no") imei_no: String
     ): Call<OTPVerificationResponseModel>

     @FormUrlEncoded
     @POST("registration")
     fun registerAPI(
         @Field("phone") phone: String,
         @Field("role") role: String,
         @Field("cust_id") cust_id: String,
         @Field("contact_person") contact_person: String,
         @Field("city") city: String
     ): Call<RegisterResponseModel>

     @FormUrlEncoded
     @POST("OTP_verification")
     fun updateMobileAPI(
         @Field("otp") otp: String,
         @Field("role") role: String,
         @Field("cust_id") cust_id: String,
         @Field("phone") phone: String,
         @Field("isnewMobile") isnewMobile: String
     ): Call<UpdateMobileResponseModel>

     @FormUrlEncoded
     @POST("getDealerswithState")
     fun getDealers(
         @Field("cust_id") cust_id: String,
         @Field("role") role: String,
         @Field("search_key") search_key: String
     ): Call<JsonObject>

     @FormUrlEncoded
     @POST("assignDealers")
     fun assignDealers(
         @Field("cust_id") cust_id: String,
         @Field("role") role: String,
         @Field("dealer_id") dealer_id: String
     ): Call<DealerAssignResponse>


     @FormUrlEncoded
     @POST("getLoyalityPoints")
     fun getLoyaltyPoints(
         @Field("cust_id") cust_id: String,
         @Field("role") role: String
     ): Call<MyPointsResponse>

     @FormUrlEncoded
     @POST("getGifts")
     fun getGifts(@Field("cust_id") cust_id: String): Call<GiftsResponseModel>

     @POST("loyalty_appversion")
     fun getAppVersion(): Call<AppVersionResponseModel>

     @FormUrlEncoded
     @POST("device_registration")
     fun deviceRegister(
         @Field("cust_id") cust_id: String,
         @Field("role") role: String,
         @Field("device_id") device_id: String
     ): Call<DeviceRegisterResponseModel>

     @FormUrlEncoded
     @POST("GiftcartList")
     fun getCartData(@Field("cust_id") cust_id: String): Call<CartItemResponseModel>

     @FormUrlEncoded
     @POST("addGiftsCartItem")
     fun addToCart(
         @Field("cust_id") cust_id: String,
         @Field("gift_id") gift_id: String,
         @Field("quantity") quantity: String,
         @Field("gift_type") gift_type: String
     ): Call<AddToCartResponseModel>

     @FormUrlEncoded
     @POST("giftCartCount")
     fun getCartCount(@Field("cust_id") cust_id: String): Call<CartCountResponseModel>

     @FormUrlEncoded
     @POST("myDealers_Subdealers")
     fun getDealers(
         @Field("cust_id") cust_id: String,
         @Field("role") role: String
     ): Call<DealerCartListResponseModel>

     @FormUrlEncoded
     @POST("updateGiftCart")
     fun editCart(
         @Field("cust_id") cust_id: String,
         @Field("id") id: String,
         @Field("quantity") quantity: String
     ): Call<EditCartResponseModel>

     @FormUrlEncoded
     @POST("deleteGiftCart")
     fun deleteCart(
         @Field("cust_id") cust_id: String,
         @Field("ids") ids: String
     ): Call<DeleteCartResponseModel>

     @FormUrlEncoded
     @POST("giftsPlaceorder")
     fun placeOrder(
         @Field("cust_id") cust_id: String,
         @Field("dealer_id") dealer_id: String,
         @Field("role") role: String,
         @Field("ids") ids: String
     ): Call<PlaceOrderResponseModel>

     @FormUrlEncoded
     @POST("redeem_history")
     fun getRedeemData(@Field("cust_id") cust_id: String): Call<RedeemHistoryResponseModel>

     @FormUrlEncoded
     @POST("getProfile")
     fun getProfile(
         @Field("cust_id") cust_id: String,
         @Field("role") role: String
     ): Call<ProfileResponseModel>

     @FormUrlEncoded
     @POST("phoneUpdateOTP")
     fun updateMobile(
         @Field("cust_id") cust_id: String,
         @Field("role") role: String,
         @Field("phone") phone: String
     ): Call<UpdateMobileResponseModel>

     @Multipart
     @POST("profile_updation")

     fun updateProfile(
         @Part("cust_id") cust_id: RequestBody,
         @Part("role") role: RequestBody,
         @Part("phone") phone: RequestBody,
         @Part("contact_person") contact_person: RequestBody,
         @Part("city") city: RequestBody,
         @Part("phone2") phone2: RequestBody,
         @Part("email") email: RequestBody, @Part image: MultipartBody.Part?
     ): Call<UpdateMobileResponseModel>

     @FormUrlEncoded
     @POST("last_uploaded_image")
     fun getImage(
         @Field("cust_id") cust_id: String,
         @Field("role") role: String
     ): Call<ShopGetImageResponseModel>

     @FormUrlEncoded
     @POST("uploaded_images_history")
     fun getImageHistory(
         @Field("cust_id") cust_id: String,
         @Field("role") role: String
     ): Call<ImageHistoryResponseModel>

     @FormUrlEncoded
     @POST("delete_uploaded_images")
     fun deleteShopImage(@Field("id") id: String): Call<UpdateMobileResponseModel>

     @Multipart
     @POST("upload_shop_images")
     fun uploadImage(
         @Part("cust_id") cust_id: RequestBody,
         @Part("role") role: RequestBody,
         @Part image: MultipartBody.Part?
     ): Call<UpdateMobileResponseModel>*/

}
