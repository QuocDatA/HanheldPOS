package com.hanheldpos.data.api.pojo.device

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ViewItemMode(
    val PictureMode: List<PictureMode>,
    val ViewMode: List<ViewMode>
) : Parcelable