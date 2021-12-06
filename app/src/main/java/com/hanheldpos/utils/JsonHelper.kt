package com.hanheldpos.utils

import com.google.gson.Gson
import java.util.*

object JsonHelper {
    fun stringify(value: Any): String {
        var json = Gson().toJson(value);
        json = json.replace("\\", "\\\\");
        json = json.replace("\"", "\\\"");
        json = json.replace("\b", "\\b");
        json = json.replace("\n", "\\n");
        json = json.replace("\r", "\\r");
        json = json.replace("\t", "\\t");
        return "\"${json}\"";
    }
}