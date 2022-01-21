package com.hanheldpos.database

import com.hanheldpos.data.api.pojo.device.DeviceCodeResp
import com.hanheldpos.database.entities.DeviceCodeEntity
import com.hanheldpos.utils.GSonUtils

object DatabaseMapper {
    fun mappingDeviceCodeToEntity(deviceCode : DeviceCodeResp) : DeviceCodeEntity {
        return DeviceCodeEntity(
            id = "",
            deviceCodeJson = GSonUtils.toJson(deviceCode)
        )
    }

    fun mappingDeviceCodeFromEntity(deviceCodeEntity: DeviceCodeEntity) : DeviceCodeResp {
        return GSonUtils.toObject<DeviceCodeResp>(deviceCodeEntity.deviceCodeJson)!!;
    }
}