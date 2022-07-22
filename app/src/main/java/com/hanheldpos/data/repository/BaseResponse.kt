package com.hanheldpos.data.repository

data class BaseResponse<T> (
    val Message: String?,
    val DidError: Boolean,
    val ErrorMessage: String?,
    val Model: T?,
)
