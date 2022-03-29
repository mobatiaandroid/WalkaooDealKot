package com.mobatia.vkcsalesapp.constants

interface VKCUrlConstants {

    companion object {
        /*
     * Bibin Comment VKC USERS 4. Sales Head 5. Retailer 6. Dealer 7. Sub Dealer
     */
        // Live AWS------------------------------------------------
        const val BASE_URL = "https://mobile.walkaroo.in/vkc/"

        // Dev URL
        //public String BASE_URL = "http://dev.mobatia.com/vkc2.5/";
        /* Settings Api */
        const val SETTINGS_URL = BASE_URL + "apiv2/getsettings"

        /* Popularity count Api */
        const val POPULARITY_COUNT_URL = BASE_URL + "apiv2/popular"

        /* Product Detail Api */
        const val PRODUCT_DETAIL_URL = BASE_URL + "apiv2/getdetails"
        const val PRODUCT_DETAIL_NEW_URL = BASE_URL + "apiv2/getdetails_new"
        const val PRODUCT_DETAIL__PAGINATED_URL = (BASE_URL
                + "apiv2/getdetailsPaginated")

        /* Search Api */
        const val SEARCH_PRODUCT_URL = BASE_URL + "apiv2/searchcontent"

        /* Login Req Api */
        const val LOGIN_REQUEST_URL = BASE_URL + "apiv2/signup"

        /* SignIn Api */
        const val LOGIN__URL = BASE_URL + "apiv2/login"

        /* Submit My Dealer */
        const val SUBMIT_MY_DEALER_URL = BASE_URL + "apiv2/addMyDealers"

        /* List My Dealers */
        const val LIST_MY_DEALERS_URL = BASE_URL + "apiv2/listMyDealers"

        /* Salesorder Api */
        const val LIST_MY_DEALERS_SALES_HEAD_URL = (BASE_URL
                + "apiv2/listSalesHeadDealers")
        const val PRODUCT_SALESORDER_SUBMISSION = BASE_URL + "apiv2/salesOrder"

        /* SubDealer Order Details */
        const val SUBDEALER_ORDER_DETAILS = BASE_URL + "apiv2/getOrderDetails"
        const val SUBDEALER_ORDER_URL = BASE_URL + "apiv2/getOrders"
        const val SUBDEALER_ORDER_URL_LIST = (BASE_URL
                + "apiv2/getSubdealerOrders")

        /* SalesorderStatus Api */
        const val PRODUCT_SALESORDER_STATUS = (BASE_URL
                + "apiv2/salesOrderStatus")

        /* Feedback Api */
        const val PRODUCT_FEEDBACK = BASE_URL + "apiv2/feedback"

        /* Complaint Api */
        const val PRODUCT_COMPLAINT = BASE_URL + "apiv2/compliant"

        /* State Api */
        const val DEALERS_GETSTATE = BASE_URL + "apiv2/getstate"

        /* Retailers Api */
        const val DEALERS_GETDISTRICT = BASE_URL + "apiv2/getdistrict"
        const val GET_RETAILERS = BASE_URL + "apiv2/getretailers"

        /* Dealers Api */
        const val GET_DEALERS = BASE_URL + "apiv2/getdealers"

        /* Approve,Reject Order */
        const val SET_ORDER_STATUS_API = BASE_URL + "apiv2/setOrderStatus"

        /* GCMIDregistration Api */
        const val GCM_INITIALISATION = BASE_URL + "apiv2/appinit"

        /* PaymentStatus Api */
        const val GET_PAYMENT_STATUS = BASE_URL + "apiv2/creditstatus"
        const val GET_SALES_ORDER_STATUS = (BASE_URL
                + "apiv2/productSalesOrderStatus")
        const val PRODUCT_SALESORDER_DETAILS = (BASE_URL
                + "apiv2/salesOrderStatusDetails")
        const val UPDATE_ORDER_STATUS = BASE_URL + "apiv2/setOrderStatus"

        /* Get Subdealer Orders */
        const val GET_SUBDEALER_ORDER_LIST = (BASE_URL
                + "apiv2/getSubdealerOrders")

        /* Reorder Product */
        const val SUBMIT_REORDER_URL = BASE_URL + "apiv2/salesReorder"

        /* Like Product API */
        const val LIKE_PRODUCT_URL = BASE_URL + "apiv2/likeproduct"

        /* Get Like Count */
        const val LIKE_COUNT_URL = BASE_URL + "apiv2/getproductlikes"

        /* Recent Orders List */
        const val GET_RECENT_ORDERS = BASE_URL + "apiv2/getRecentOrders"

        /* Sales Head Orders List */
        const val SALES_HEAD_ORDERS_URL = (BASE_URL
                + "apiv2/getSalesExecutiveOrders")
        const val URL_ARTICLE_SEARCH_PRODUCT = BASE_URL + "apiv2/searchProduct"
        const val URL_GET_PRODUCT_DETAIL = BASE_URL + "apiv2/getProductDetail?"
        const val URL_GET_PENDING_ORDER_CART = (BASE_URL
                + "apiv2/getPendingCartItems")
        const val URL_GET_APP_VERSION = "apiv2/appversion"

        /* Notification List */
        const val NOTIFICATION_LIST_URL = BASE_URL + "apiv2/push"
        const val NOTIFICATION_DELETE_URL = BASE_URL + "apiv2/pushdelete"
        const val GET_ARTICLE_NUMBERS_URL = (BASE_URL
                + "apiv2/getarticlenumbers")
        const val GET_PRODUCT_DETAILS_URL = (BASE_URL
                + "apiv2/getQuickOrderProductDetails")
        const val GET_DISPATCH_ORDERS_URL = (BASE_URL
                + "apiv2/getDispatchorders")
        const val GET_QUICK_ORDER_CREDIT_URL = (BASE_URL
                + "apiv2/getcreditvalue")
        const val GET_QUICK_ARTICLE_NO_URL = (BASE_URL
                + "apiv2/getquickorderarticlenumbers")
        const val GET_CART_VALUE_URL = BASE_URL + "apiv2/getcartvalue"
        const val GET_CATEGORY_URL = BASE_URL + "apiv2/getcategorylist"
        const val GET_SUBDEALER_RECENT_ORDERS = (BASE_URL
                + "apiv2/getRecentOrderDetails")
        const val SUBDEALER_NEW_ORDER_DETAILS = (BASE_URL
                + "apiv2/getOrderDetails_new")
        const val GET_DEALER_POINTS = BASE_URL + "apiv2/getLoyalityPoints"
        const val GET_USERS = BASE_URL + "apiv2/getUsers"
        const val ISSUE_POINTS = BASE_URL + "apiv2/issueLoyalityPoints"
        const val TRANSACTION_HISTORY = BASE_URL + "apiv2/transaction_history"
        const val GET_DATA = BASE_URL + "apiv2/fetchUserData"
        const val GET_CUSTOMERS = BASE_URL + "apiv2/getMyCustomers"
        const val GET_REDEEM_LIST = BASE_URL + "apiv2/Redeemed_gifts"
        const val GET_MEMBERS_LIST = BASE_URL + "apiv2/getGroupMembers"
        const val GET_REDEEM_REPORT_APP = BASE_URL + "apiv2/RedeemReportForApp"
        const val GET_GIFT_REWARD_REPORT_APP = (BASE_URL
                + "apiv2/GiftRewardReportForApp")
        const val ADD_TO_CART_API = BASE_URL + "apiv2/saveCartitems"
        const val DELETE_CART_ITEM_API = BASE_URL + "apiv2/deleteCartitems"
        const val EDIT_CART_ITEM_API = BASE_URL + "apiv2/editCartitems"
    }
}