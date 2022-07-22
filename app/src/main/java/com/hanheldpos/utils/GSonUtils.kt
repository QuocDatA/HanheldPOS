package com.hanheldpos.utils

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object GSonUtils {
    val gson = Gson()

    fun toJson(obj: Any?): String {
        return gson.toJson(obj)
    }

    fun toServerJson(obj: Any?): String {
        return toServerFormat(gson.toJson(obj))
    }


    /**
     *  Server designer is drunk so we have this method
     */
    private fun toServerFormat(str: String): String {
        var json = str
        json = json.replace("\\", "\\\\")
        json = json.replace("\"", "\\\"")
        json = json.replace("\b", "\\b")
        json = json.replace("\n", "\\n")
        json = json.replace("\r", "\\r")
        json = json.replace("\t", "\\t")
        return "\"${json}\""
    }

    fun <T> mapToObject(any: Any?, type: Class<T>): T? {
        if (any == null) return null
        val map = any as Map<String, Any?>
        val json = gson.toJson(map)
        return gson.fromJson(json, type)
    }

    inline fun <reified T> toObject(str: String?): T? {
        return gson.fromJson(str, T::class.java)
    }

    /**
     *  Convert str to list Item
     *  Ex: GSonUtils.toList<List<DATACLASS>>(str)
     */
    inline fun <reified T> toList(str: String?): T? {
        if (str == null) return null
        return Gson().fromJson(str, object : TypeToken<T>() {}.type)
    }
}