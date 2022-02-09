package com.hanheldpos.database

import android.os.Parcelable
import com.hanheldpos.data.api.pojo.device.DeviceCodeResp
import com.hanheldpos.data.api.pojo.discount.DiscountResp
import com.hanheldpos.data.api.pojo.fee.FeeResp
import com.hanheldpos.data.api.pojo.floor.FloorResp
import com.hanheldpos.data.api.pojo.order.menu.MenuResp
import com.hanheldpos.data.api.pojo.order.settings.OrderSettingResp
import com.hanheldpos.data.api.pojo.payment.PaymentMethodResp
import com.hanheldpos.database.entities.*
import com.hanheldpos.model.order.OrderReq
import com.hanheldpos.utils.GSonUtils

object DatabaseMapper {
    fun mappingDeviceCodeToEntity(deviceCode: DeviceCodeResp): DeviceCodeEntity {
        return DeviceCodeEntity(
            id = "",
            deviceCodeJson = GSonUtils.toJson(deviceCode)
        )
    }

    fun mappingDeviceCodeFromEntity(deviceCodeEntity: DeviceCodeEntity): DeviceCodeResp {
        return GSonUtils.toObject<DeviceCodeResp>(deviceCodeEntity.deviceCodeJson)!!;
    }

    fun <T : Parcelable> mappingClassToEntity(classOff: Class<T>?): Any {
        when (classOff) {
            MenuResp::class.java -> {
                return MenuEntity(
                    id = "",
                    menu_Json = GSonUtils.toJson(classOff)
                )
            }
            DeviceCodeResp::class.java -> {
                return DeviceCodeEntity(
                    id = "",
                    deviceCodeJson = GSonUtils.toJson(classOff)
                )
            }
            PaymentMethodResp::class.java -> {
                return PaymentMethodEntity(
                    id = "",
                    paymentMethodJson = GSonUtils.toJson(classOff)
                )
            }
            OrderSettingResp::class.java -> {
                return OrderSettingEntity(
                    id = "",
                    orderSettingJson = GSonUtils.toJson(classOff)
                )
            }
            FeeResp::class.java -> {
                return FeeEntity(
                    id = "",
                    feeJson = GSonUtils.toJson(classOff)
                )
            }
            FloorResp::class.java -> {
                return FloorEntity(id = "", floorJson = GSonUtils.toJson(classOff))
            }
            DiscountResp::class.java -> {
                return DiscountEntity(
                    id = "",
                    discountJson = GSonUtils.toJson(classOff)
                )
            }
            OrderReq::class.java -> {
                return OrderCompletedEntity(
                    id = "",
                    orderCompletedJson = GSonUtils.toJson(classOff)
                )
            }
            else -> {
                return DiscountDetailEntity(
                    id = "",
                    discountDetailJson = GSonUtils.toJson(classOff)
                )
            }
        }
    }

    fun  mappingClassFromEntity(classOff: Any): Any {
        when (classOff) {
            MenuEntity::class.java -> {
                val menuEntity = classOff as MenuEntity
                return GSonUtils.toObject<MenuResp>(menuEntity.menu_Json)!!;
            }
            DeviceCodeEntity::class.java -> {
                val deviceCodeEntity = classOff as DeviceCodeEntity
                return GSonUtils.toObject<DeviceCodeResp>(deviceCodeEntity.deviceCodeJson)!!;
            }
            PaymentMethodEntity::class.java -> {
                val paymentMethodEntity = classOff as PaymentMethodEntity
                return GSonUtils.toObject<MenuResp>(paymentMethodEntity.paymentMethodJson)!!;
            }
            OrderSettingEntity::class.java -> {
                val orderSettingEntity = classOff as OrderSettingEntity
                return GSonUtils.toObject<MenuResp>(orderSettingEntity.orderSettingJson)!!;
            }
            FeeEntity::class.java -> {
                val feeEntity = classOff as FeeEntity
                return GSonUtils.toObject<MenuResp>(feeEntity.feeJson)!!;
            }
            FloorEntity::class.java -> {
                val floorEntity = classOff as FloorEntity
                return GSonUtils.toObject<MenuResp>(floorEntity.floorJson)!!;
            }
            DiscountEntity::class.java -> {
                val discountEntity = classOff as DiscountEntity
                return GSonUtils.toObject<MenuResp>(discountEntity.discountJson)!!;
            }
            OrderCompletedEntity::class.java -> {
                val orderCompletedEntity = classOff as OrderCompletedEntity
                return GSonUtils.toObject<MenuResp>(orderCompletedEntity.orderCompletedJson)!!;
            }
            else -> {
                val discountDetailEntity = classOff as DiscountDetailEntity
                return GSonUtils.toObject<MenuResp>(discountDetailEntity.discountDetailJson)!!;
            }

        }
    }
}