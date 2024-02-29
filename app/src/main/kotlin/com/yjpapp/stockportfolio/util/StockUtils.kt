package com.yjpapp.stockportfolio.util

import com.yjpapp.stockportfolio.ui.common.StockConfig
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

/**
 * 개발에 필요한 함수들
 *
 * @author Yoon Jae-park
 * @since 2020.07
 */
object StockUtils {
    const val DATEFORMAT = "yyyy.MM.dd HH:mm"
    const val DATEFORMAT2 = "yyyy.MM.dd HH:mm:ss"
    const val DATEFORMAT3 = "yyyy-MM-dd"
    const val DATEFORMAT4 = "yyyyMMdd"
    //yyyymmdd로 변환
    fun getTodayYYYYMMDD(): String {
        val currentTime: Long = System.currentTimeMillis()
        val todayDate = Date(currentTime)
        val sdformat = SimpleDateFormat(DATEFORMAT4)
        return sdformat.format(todayDate)
    }

    //어제 날짜를 yyyymmdd로 return
    fun getYesterday(): String {
        val calendar = Calendar.getInstance()
        val today = Date()
        calendar.time = today
        calendar.add(Calendar.DATE, -1)
        val sdformat = SimpleDateFormat(DATEFORMAT4)
        return sdformat.format(calendar.time)
    }

    //yyyy.mm.dd로 변환
    fun convertYYYY_MM_DD(yyyymmdd: String): String {
        val result = if (yyyymmdd.length == 8) {
            StringBuffer().apply {
                append(yyyymmdd.substring(0, 4))
                append(".")
                append(yyyymmdd.substring(4, 6))
                append(".")
                append(yyyymmdd.substring(6, 8))
            }.toString()
        } else {
            yyyymmdd
        }
        return result
    }

    //현재 yyyy.mm 반환
    fun getTodayYYMMDD(): List<String> {
        val currentTime: Long = System.currentTimeMillis()
        val todayDate = Date(currentTime)
        val sdformat = SimpleDateFormat(DATEFORMAT3)
        return sdformat.format(todayDate).split("-")
    }

    //5,000,000 => 5000000 변환
    fun getNumDeletedComma(num: String): String {
        if (num.isEmpty()) return "0"
        if (!num.contains(",")) return num
        val result = StringBuffer()
        val split = num.split(",")
        for (i in split.indices) {
            result.append(split[i])
        }
        return result.toString()
    }

    //5000000 => 5,000,000 변환
    fun getNumInsertComma(num: String): String {
        if (num.isEmpty()) return "0"
        val decimalFormat = DecimalFormat("###,###")
        return decimalFormat.format(num.replace(",", "").toBigDecimal())
    }

    //5000000 => $5,000,000 변환
    fun getPriceNum(num: String): String {
        if (num.isEmpty()) return "0"
        val result = StringBuffer().apply {
            append(StockConfig.koreaMoneySymbol)
            append(getNumInsertComma(num))
        }
        return result.toString()
    }

    //14% => 14 변환.
    fun getNumDeletedPercent(num: String): String {
        var result: String = ""
        if (num.isEmpty()) return result
        val split = num.split("%")
        for (i in split.indices) {
            result += split[i]
        }
        return result
    }

    //2020.11.18 =>20201118 변환.
    fun getNumDeletedDot(num: String): String {
        var result: String = ""
        val split = num.split(".")
        for (i in split.indices) {
            result += split[i]
        }
        return result
    }

    //수익률 계산
    fun calculateGainPercent(purchasePrice: Double, sellPrice: Double): Double {
//        val purchasePriceNum = getNumDeletedComma(purchasePrice)
//        val sellPriceNum = getNumDeletedComma(sellPrice)
        var result = (((sellPrice / purchasePrice) -1) * 100)
        if (result.equals(Double.NaN)) result = 0.00
        return result
    }

    //퍼센티지 붙이기
    fun getRoundsPercentNumber(number: Double): String{
        var result = ""
        result = try{
            String.format("%.2f", number)
        } catch (e: Exception) {
            "0"
        }
        result += "%"
        return result
    }

    fun parseNewsDate(date: String): String {
        return try {
            val dateSplit = date.split(" ", ",").filter { it != "" && it != "09:00" }
            if (dateSplit.size >= 5) {
                if (dayOfMonthHashMap[dateSplit[2]] == null || dayOfWeeksKoreaHashMap[dateSplit[0]] == null) {
                    return date
                }
                "${dateSplit[3]}.${dayOfMonthHashMap[dateSplit[2]]}.${dateSplit[1]} ${dayOfWeeksKoreaHashMap[dateSplit[0]]} ${dateSplit[4]}"
            } else {
                date
            }
        } catch (e: Exception) {
            date
        }
    }
}

val dayOfWeeksKoreaHashMap = hashMapOf(
    "Mon" to "월요일",
    "Tue" to "화요일",
    "Wen" to "수요일",
    "Wed" to "수요일",
    "Thu" to "목요일",
    "Fri" to "금요일",
    "Sat" to "토요일",
    "Sun" to "일요일",
)
val dayOfMonthHashMap = hashMapOf(
    "Jan" to "01", "Feb" to "02", "Mar" to "03",
    "Apr" to "04", "May" to "05", "Jun" to "06",
    "Jul" to "07", "Aug" to "08", "Sep" to "09",
    "Oct" to "10", "Nov" to "11", "Dec" to "12"
)