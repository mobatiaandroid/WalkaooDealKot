package com.mobatia.vkcsalesapp.model

class SalesRepOrderModel {
    private var mOrderNo: String? = null
    private var mOrderDate: String? = null
    private var mOrderQty: String? = null
    private var mPendingQty: String? = null
    private var mCompany: String? = null
    private var mCustomerName: String? = null
    private var mMaterialDesc: String? = null
    private var mPrdctName: String? = null
    private var mPrdctDesc: String? = null

    /**
     * @return the mPrdctDesc
     */
    fun getmPrdctDesc(): String? {
        return mPrdctDesc
    }

    /**
     * @param mPrdctDesc the mPrdctDesc to set
     */
    fun setmPrdctDesc(mPrdctDesc: String?) {
        this.mPrdctDesc = mPrdctDesc
    }

    /**
     * @return the mPrdctName
     */
    fun getmPrdctName(): String? {
        return mPrdctName
    }

    /**
     * @param mPrdctName the mPrdctName to set
     */
    fun setmPrdctName(mPrdctName: String?) {
        this.mPrdctName = mPrdctName
    }

    fun getmOrderNo(): String? {
        return mOrderNo
    }

    fun setmOrderNo(mOrderNo: String?) {
        this.mOrderNo = mOrderNo
    }

    fun getmOrderDate(): String? {
        return mOrderDate
    }

    fun setmOrderDate(mOrderDate: String?) {
        this.mOrderDate = mOrderDate
    }

    fun getmOrderQty(): String? {
        return mOrderQty
    }

    fun setmOrderQty(mOrderQty: String?) {
        this.mOrderQty = mOrderQty
    }

    fun getmPendingQty(): String? {
        return mPendingQty
    }

    fun setmPendingQty(mPendingQty: String?) {
        this.mPendingQty = mPendingQty
    }

    fun getmCompany(): String? {
        return mCompany
    }

    fun setmCompany(mCompany: String?) {
        this.mCompany = mCompany
    }

    fun getmCustomerName(): String? {
        return mCustomerName
    }

    fun setmCustomerName(mCustomerName: String?) {
        this.mCustomerName = mCustomerName
    }

    fun getmMaterialDesc(): String? {
        return mMaterialDesc
    }

    fun setmMaterialDesc(mMaterialDesc: String?) {
        this.mMaterialDesc = mMaterialDesc
    }
}