package com.hanheldpos.model

import com.hanheldpos.data.api.pojo.employee.EmployeeResp

object UserHelper {
    fun clearData() {
        curEmployee = null
    }

    fun getLocationGui() = DataHelper.getLocationGuidByDeviceCode() ?: ""

    fun getDeviceGui() = DataHelper.getDeviceGuidByDeviceCode() ?: ""

    fun getUserGui() = DataHelper.getUserGuidByDeviceCode() ?: ""


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