package com.hanheldpos.data.api.pojo.device

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
data class Employee(
    val Avatar: String,
    val City: String,
    val Country: String,
    val DateOfBirth: String,
    val Department: String,
    val Email: String,
    val FirstName: String,
    val FullName: String,
    val FullName_Ansi: String,
    val LastName: String,
    val NameAcronymn: String,
    val NickName: String,
    val PassCode: String,
    val Permission: Int,
    val PermissionName: String,
    val Phone: String,
    val Position: String,
    val ReferenceId: String,
    val RoleName: String,
    val RoleTypeId: Int,
    val Token: String,
    val Ward: String,
    val _id: String,
    val _key: Int,
    val _rev: String
) : Parcelable