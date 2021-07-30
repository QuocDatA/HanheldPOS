package com.diadiem.pos_config

import android.content.Context
import android.graphics.Typeface

object FontManagement {

    fun getFontFromAssets(context: Context, filePath: String?): Typeface? {
        return try {
            Typeface.createFromAsset(context.assets,filePath)
        }catch (e:Exception){
            e.printStackTrace()
            return null
        }

    }
}