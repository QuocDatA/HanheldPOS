package com.hanheldpos.database

import com.hanheldpos.data.api.pojo.device.DeviceCodeResp
import com.hanheldpos.data.api.pojo.discount.CouponResp
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

    fun mappingDiscountToEntity(discount: DiscountResp): DiscountEntity {
        return DiscountEntity(id = discount._id, discountJson = GSonUtils.toJson(discount))
    }

    fun mappingDiscountFromEntity(discountEntity: DiscountEntity): DiscountResp {
        return GSonUtils.toObject<DiscountResp>(discountEntity.discountJson)!!
    }

    fun mappingDiscountDetailToEntity(discountDetail: CouponResp): DiscountDetailEntity {
        return DiscountDetailEntity(
            id = discountDetail._id,
            discountDetailJson = GSonUtils.toJson(discountDetail)
        )
    }

    fun mappingPaymentToEntity(paymentMethodResp: PaymentMethodResp): PaymentMethodEntity {
        return PaymentMethodEntity(
            id = paymentMethodResp._id,
            paymentMethodJson = GSonUtils.toJson(paymentMethodResp)
        )
    }

    fun mappingPaymentFromEntity(paymentMethodEntity: PaymentMethodEntity): PaymentMethodResp {
        return GSonUtils.toObject<PaymentMethodResp>(paymentMethodEntity.paymentMethodJson)!!
    }

//    fun mappingAddressTypeToEntity(addressTypeResp: AddressTypeResp): AddressTypeEntity {
//        return AddressTypeEntity(
//            id = addressTypeResp.AddressTypeId.toString(),
//            addressTypeJson = GSonUtils.toJson(addressTypeResp)
//        )
//    }
//
//    fun mappingAddressTypeFromEntity(addressTypeEntity: AddressTypeEntity): AddressTypeResp{
//        return GSonUtils.toObject<AddressTypeResp>(addressTypeEntity.addressTypeJson)!!
//    }

    fun mappingDiscountDetailFromEntity(discountDetailEntity: DiscountDetailEntity): CouponResp {
        return GSonUtils.toObject<CouponResp>(discountDetailEntity.discountDetailJson)!!
    }

    fun <T> mappingClassToEntity(classOff: T): Any {
        when (classOff) {
            is MenuResp -> {
                return MenuEntity(
                    id = "menu",
                    menu_Json = GSonUtils.toJson(classOff)
                )
            }
            is DeviceCodeResp -> {
                return DeviceCodeEntity(
                    id = "device_code",
                    deviceCodeJson = GSonUtils.toJson(classOff)
                )
            }
            is PaymentMethodResp -> {
                return PaymentMethodEntity(
                    id = "payment_method",
                    paymentMethodJson = GSonUtils.toJson(classOff)
                )
            }
            is OrderSettingResp -> {
                return OrderSettingEntity(
                    id = "order_setting",
                    orderSettingJson = GSonUtils.toJson(classOff)
                )
            }
            is FeeResp -> {
                return FeeEntity(
                    id = "fee",
                    feeJson = GSonUtils.toJson(classOff)
                )
            }
            is FloorResp -> {
                return FloorEntity(id = "floor", floorJson = GSonUtils.toJson(classOff))
            }
            is OrderReq -> {
                return OrderCompletedEntity(
                    id = "order",
                    orderCompletedJson = GSonUtils.toJson(classOff)
                )
            }
            else -> {
                throw Exception("No Entity Found!")
            }
        }
    }

    fun  mappingClassFromEntity(classOff: Any): Any {
        when (classOff) {
            is MenuEntity -> {
                val menuEntity = classOff as MenuEntity
                return GSonUtils.toObject<MenuResp>(menuEntity.menu_Json)!!;
            }
            is DeviceCodeEntity -> {
                val deviceCodeEntity = classOff as DeviceCodeEntity
                return GSonUtils.toObject<DeviceCodeResp>(deviceCodeEntity.deviceCodeJson)!!;
            }
            is PaymentMethodEntity -> {
                val paymentMethodEntity = classOff as PaymentMethodEntity
                return GSonUtils.toObject<MenuResp>(paymentMethodEntity.paymentMethodJson)!!;
            }
            is OrderSettingEntity -> {
                val orderSettingEntity = classOff as OrderSettingEntity
                return GSonUtils.toObject<MenuResp>(orderSettingEntity.orderSettingJson)!!;
            }
            is FeeEntity -> {
                val feeEntity = classOff as FeeEntity
                return GSonUtils.toObject<MenuResp>(feeEntity.feeJson)!!;
            }
            is FloorEntity -> {
                val floorEntity = classOff as FloorEntity
                return GSonUtils.toObject<MenuResp>(floorEntity.floorJson)!!;
            }
            is OrderCompletedEntity -> {
                val orderCompletedEntity = classOff as OrderCompletedEntity
                return GSonUtils.toObject<MenuResp>(orderCompletedEntity.orderCompletedJson)!!;
            }
            else -> {
                throw Exception("No Entity Found!")
            }
        }
    }
}