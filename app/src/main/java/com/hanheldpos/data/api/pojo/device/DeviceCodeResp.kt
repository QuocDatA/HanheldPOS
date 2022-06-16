package com.hanheldpos.data.api.pojo.device

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DeviceCodeResp(
    val Device: List<Device>,
    val DeviceInfoGuid: String,
    val Employees: List<Employee>,
    val ListDeviceType: List<DeviceType>,
    val ListSettingsId: List<SettingsId>,
    val Region: List<Region>,
    val SystemSettingsType: List<SystemSettingsType>,
    val SystemTimeZoneInfo: List<SystemTimeZoneInfo>,
    val Users: Users,
    val ViewItemMode: List<ViewItemMode>,
    val domain_images: String
) : Parcelable {
    fun getEmployeeByPasscode(passcode : String?) : Employee? {
         return Employees.find { it.PassCode == passcode }
    }
    fun getEmployeeById(idEmployee: String?) : Employee? {
        idEmployee?: return null
        return  Employees.find { it._id == idEmployee }
    }
}
