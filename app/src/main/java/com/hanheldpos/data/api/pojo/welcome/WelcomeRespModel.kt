package com.hanheldpos.data.api.pojo.welcome

data class WelcomeRespModel(
    val Device_Code: List<DeviceCode>,
    val Pages_04: List<Pages04>,
    val Pass_Code: List<PassCode>,
    val Settings_Css: List<SettingsCs>,
    val Welcome: List<Welcome>
)