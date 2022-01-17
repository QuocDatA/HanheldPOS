package com.hanheldpos.data.api.pojo.device

import android.os.Parcelable
import com.hanheldpos.data.api.pojo.employee.EmployeeResp
import kotlinx.parcelize.Parcelize

@Parcelize
data class DeviceCodeResp(
    val Device: List<Device>,
    val DeviceInfoGuid: String?,
    val EmployeeList: List<EmployeeResp>,
//    val ListDeviceType: Any,
    val Region: Region,
    val SettingsId: List<SettingsId>,
    val SystemSettingsType: Int?,
    val SystemTimeZoneInfo: String?,
    val Users: Users,
    val ViewItemMode: Int?,
    val domain_images: String?
) : Parcelable