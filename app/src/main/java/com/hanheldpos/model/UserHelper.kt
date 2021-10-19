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
        return if (curEmployee?.id != null) {
            curEmployee?.id!!
        } else {
            NullPointerException("getEmployeeGui null value").printStackTrace()
            ""
        }
    }

    fun getEmployeeUserGui(): String {
        return if (curEmployee?.id != null) {
            curEmployee?.userGuid!!
        } else {
            NullPointerException("getEmployeeUserGui null value").printStackTrace()
            ""
        }
    }

    //endregion
}