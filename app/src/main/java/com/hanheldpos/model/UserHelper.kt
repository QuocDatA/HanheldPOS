package com.hanheldpos.model

import com.hanheldpos.data.api.pojo.employee.EmployeeResp

object UserHelper {
    fun clearData() {
        curEmployee = null
    }

    fun getLocationGuid() = OrderHelper.getLocationGuidByDeviceCode() ?: ""

    fun getDeviceGuid() = OrderHelper.getDeviceGuidByDeviceCode() ?: ""

    fun getUserGuid() = OrderHelper.getUserGuidByDeviceCode() ?: ""


    //Employee

    var curEmployee: EmployeeResp? = null

    fun getEmployeeGuid(): String {
        return if (curEmployee?._Id != null) {
            curEmployee?._Id!!
        } else {
            NullPointerException("getEmployeeGui null value").printStackTrace()
            ""
        }
    }

    //endregion
}