package com.hanheldpos.data.api.pojo.employee

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

//@Parcelize
//data class EmployeeResp(
//
//    @field:SerializedName("Message")
//    val message: String? = null,
//
//    @field:SerializedName("Model")
//    val model: List<EmployeeItem?>? = null,
//
//    @field:SerializedName("ErrorMessage")
//    val errorMessage: String? = null,
//
//    @field:SerializedName("DidError")
//    val didError: Boolean? = null
//) : Parcelable

@Parcelize
data class EmployeeResp(

    @field:SerializedName("Position")
    val position: String? = null,

    @field:SerializedName("Avatar")
    val avatar: String? = null,

    @field:SerializedName("Department")
    val department: String? = null,

    @field:SerializedName("PermissionName")
    val permissionName: String? = null,

    @field:SerializedName("Permission")
    val permission: Int? = null,

    @field:SerializedName("OwnershipType")
    val ownershipType: String? = null,

    @field:SerializedName("Email")
    val email: String? = null,

    @field:SerializedName("_rev")
    val rev: String? = null,

    @field:SerializedName("Token")
    val token: String? = null,

    @field:SerializedName("Gender")
    val gender: Int? = null,

    @field:SerializedName("_key")
    val key: Int? = null,

    @field:SerializedName("NameAcronymn")
    val nameAcronymn: String? = null,

    @field:SerializedName("CreateDate")
    val createDate: String? = null,

    @field:SerializedName("DateOfIssue")
    val dateOfIssue: String? = null,

    @field:SerializedName("Phone")
    val phone: String? = null,

    @field:SerializedName("Visible")
    val visible: Int? = null,

    @field:SerializedName("Street")
    val street: String? = null,

    @field:SerializedName("_Id")
    var id: String? = null,

    @field:SerializedName("Password")
    val password: String? = null,

    @field:SerializedName("IdentityCard")
    val identityCard: String? = null,

    @field:SerializedName("DateOfBirth")
    val dateOfBirth: String? = null,

    @field:SerializedName("FirstName")
    val firstName: String? = null,

    @field:SerializedName("PositionId")
    val positionId: Int? = null,

    @field:SerializedName("UserGuid")
    val userGuid: String? = null,

    @field:SerializedName("City")
    val city: String? = null,

    @field:SerializedName("PlaceOfIssue")
    val placeOfIssue: String? = null,

    @field:SerializedName("PassCode")
    val passCode: String? = null,

    @field:SerializedName("Ward")
    val ward: String? = null,

    @field:SerializedName("FullName")
    var fullName: String? = null,

    @field:SerializedName("Country")
    val country: String? = null,

    @field:SerializedName("StatusId")
    val statusId: String? = null,

    @field:SerializedName("LastName")
    val lastName: String? = null,

    @field:SerializedName("DepartmentId")
    val departmentId: Int? = null,

    @field:SerializedName("AddressType")
    val addressType: String? = null,

    @field:SerializedName("District")
    val district: String? = null,

    @field:SerializedName("Ethnic")
    val ethnic: String? = null,

    @field:SerializedName("NickName")
    val nickName: String? = null
) : Parcelable {

    fun getEmployeeNickName(): String? {
        if (!nickName.isNullOrBlank())
            return nickName
        if (!fullName.isNullOrBlank())
            return fullName

        return null
    }
}
