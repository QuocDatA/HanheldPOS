package com.hanheldpos.utils


import android.text.TextUtils
import java.lang.Exception
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

object DateTimeUtils {
    fun doubleToDate(value: Double, format: String?): String {
        val dateFormat = SimpleDateFormat(format, Locale.ENGLISH)
        return dateFormat.format(Date(value.toLong()))
    }

    fun strToDate(dateStr: String?, format: String?): Date? {
        if (TextUtils.isEmpty(dateStr)) return null
        try {
            val dateFormat = SimpleDateFormat(format, Locale.ENGLISH)
            return dateFormat.parse(dateStr)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    fun strToStr(dateStr: String?, format: String?, formatNew: String?): String {
        val date = strToDate(dateStr, format)
        return dateToString(date, formatNew)
    }

    fun strToCalendar(dateStr: String?, format: String?): Calendar? {
        val date = strToDate(dateStr, format) ?: return null
        val calendar = Calendar.getInstance()
        calendar.time = date
        return calendar
    }

    fun utcTimeToDateStr(dateUtc: String?, currFormat: String?, newFormat: String?): String? {
        if (TextUtils.isEmpty(dateUtc)) return null
        try {
            val dateFormat = SimpleDateFormat(currFormat, Locale.ENGLISH)
            dateFormat.timeZone = TimeZone.getTimeZone("UTC")
            val createdDate = dateFormat.parse(dateUtc)
            val createdDateInLocalTimeZone = TimeUnit.MILLISECONDS.toSeconds(createdDate.time)
            return parseMillisToDateStr(createdDateInLocalTimeZone, newFormat)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    fun utcTimeToDate(dateUtc: String?, format: String?): Date? {
        if (TextUtils.isEmpty(dateUtc)) return null
        try {
            val dateFormat = SimpleDateFormat(format, Locale.ENGLISH)
            dateFormat.timeZone = TimeZone.getTimeZone("UTC")
            val createdDate = dateFormat.parse(dateUtc)
            val createdDateInLocalTimeZone = TimeUnit.MILLISECONDS.toSeconds(createdDate.time)
            return parseMillisToDate(createdDateInLocalTimeZone, format)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    fun parseMillisToDateStr(timeInMillis: Long, dateFormat: String?): String {
        val shortDateFormat = SimpleDateFormat(dateFormat, Locale.ENGLISH)
        return shortDateFormat.format(Date(timeInMillis))
    }

    @Throws(ParseException::class)
    fun parseMillisToDate(timeInMillis: Long, dateFormat: String?): Date {
        val shortDateFormat = SimpleDateFormat(dateFormat, Locale.ENGLISH)
        val dateStr = shortDateFormat.format(Date(timeInMillis))
        return shortDateFormat.parse(dateStr)
    }

    fun getPassTimeInMin(nextDate: Date?): Long {
        return if (nextDate == null) 0 else getPassTimeInMin(curDate, nextDate)
    }

    fun getPassTimeInMin(date: Date?, nextDate: Date?): Long {
        if (date == null || nextDate == null) return 0
        val difference = date.time - nextDate.time
        return TimeUnit.MINUTES.convert(difference, TimeUnit.MILLISECONDS)
    }

    /**
     * Format current date to new date
     *
     * @param currDate
     * @param currFormat
     * @param newFormat
     * @return
     */
    fun formatDate(currDate: String?, currFormat: String?, newFormat: String?): String? {
        if (TextUtils.isEmpty(currDate)) return null
        val currDateFormat = SimpleDateFormat(currFormat, Locale.ENGLISH)
        val newDateFormat = SimpleDateFormat(newFormat, Locale.ENGLISH)
        var date: Date? = null
        try {
            date = currDateFormat.parse(currDate)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return if (date != null) newDateFormat.format(date) else null
    }

    fun formatTimeZone(currDate: String?, newFormat: String?): String? {
        if (TextUtils.isEmpty(currDate)) return null
        val newDateFormat = SimpleDateFormat(newFormat, Locale.ENGLISH)
        val utcFormat: DateFormat = SimpleDateFormat(Format.FULL_DATE_UTC_Z, Locale.ENGLISH)
        utcFormat.timeZone = TimeZone.getTimeZone("UTC")
        var date: Date? = null
        try {
            date = utcFormat.parse(currDate)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return if (date != null) newDateFormat.format(date) else null
    }

    /**
     * Convert date to String
     *
     * @param date
     * @param dateFormat
     * @return
     */
    fun dateToString(date: Date?, dateFormat: String?): String {
        date?:return "";
        val sdf = SimpleDateFormat(dateFormat, Locale.ENGLISH)
        return sdf.format(date)
    }

    val curDate: Date
        get() = Calendar.getInstance().time
    val curDateAsCalendar: Calendar
        get() = Calendar.getInstance()

    fun getCurDateAsStr(dateFormat: String): String? {
        try {
            val sdf = SimpleDateFormat(dateFormat, Locale.ENGLISH)
            val curDay = curDate
            return sdf.format(curDay)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    fun checkValidDateStr(strDateNeedCheck: String?, dateFormat: String?): Boolean {
        if (strDateNeedCheck == null) return true
        val currDateFormat = SimpleDateFormat(dateFormat, Locale.ENGLISH)
        currDateFormat.isLenient = false
        var date: Date? = null
        try {
            date = currDateFormat.parse(strDateNeedCheck)
            if (date != null) {
                return true
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
        return false
    }

    object Format {
        const val FULL_DATE_UTC_NOT_MILI = "yyyy-MM-dd'T'HH:mm:ss"
        const val FULL_DATE_UTC_Z = "yyyy-MM-dd'T'HH:mm:ss'Z'"
        const val FULL_DATE_UTC_TIMEZONE = "yyyy-MM-dd'T'HH:mm:ssZ"
        const val DD_MM_YYYY = "dd/MM/yyyy"
        const val DD_MM = "dd/MM"
        const val DD_MM_HH_MM_aa = "dd/MM hh:mm aa"
        const val MM_DD_YYYY_HH_MM_SS_aa = "MM/dd/yyyy hh:mm:ss aa"
        const val MM_DD_YYYY_HH_MM_SS = "MM/dd/yyyy HH:mm:ss"
        const val DD_MM_YYYY_HH_MM_AA = "dd/MM/yyyy hh:mm aa"
        const val DD_MM_YYYY_HH_MM = "dd/MM/yyyy HH:mm"
        const val MM_DD_YYYY_HH_MM = "MM/dd/yyyy HH:mm"
        const val YYYY_MM_DD_HH_MM = "yyyy/MM/dd HH:mm"
        const val YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss"
        const val HH_mm = "HH:mm"
        const val hh_mm_aa = "hh:mm aa" // with AM/PM
        const val EEEE_MMMMM_d = "EEEE, MMMM dd"
        const val EEEE_dd_MMM_yyyy = "EEEE, dd MMM yyyy"
        const val EEE_MMM_d = "EEE, MMM dd"
        const val EEEE = "EEEE"
        const val MMM_dd_yyyy = "MMM dd, yyyy"
        const val yy_MMMMM_dd = "yyyy MMMM dd"
        const val MM_DD_YYYY = "MM/dd/yyyy"
        const val YYYY_MM_DD = "yyyy-MM-dd"
        const val YYYY_MM_DD_18 = "yyyy/MM/dd"
        const val REPORT_TIME = "MM/dd hh:mm aa"
        const val HH_MM = "HH:mm"
        const val DD_MMM_YYYY = "MMM dd YYYY"
        const val dd_MMM_YYYY = "dd MMM YYYY"
    }
}