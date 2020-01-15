package com.cj.sdknet.utils

import android.text.TextUtils
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DecimalFormat
import java.util.*
import java.util.regex.Pattern

/**
 *  Create by chenjiao at 2020/1/15 0015
 *  描述: 例如：类型转化，数字相关表示，数字运算等
 */
object NumberUtil {
    /**
     * 支持小数点，向上取整
     *
     * @param str
     * @param defaultValue
     * @return
     */
    @JvmOverloads
    fun parseIntByCeil(str: String, defaultValue: Int = 0): Int {
        var number = defaultValue
        if (!TextUtils.isEmpty(str)) {
            try {
                var d: Double? = java.lang.Double.parseDouble(str)
                d = Math.ceil(d!!)
                number = d.toInt()
            } catch (e: Exception) {
                return number
            }

        }
        return number
    }

    /**
     * 支持小数点，向下取整
     *
     * @param str
     * @param defaultValue
     * @return
     */
    @JvmOverloads
    fun parseIntByFloor(str: String, defaultValue: Int = 0): Int {
        var number = defaultValue
        if (!TextUtils.isEmpty(str)) {
            try {
                var d: Double? = java.lang.Double.parseDouble(str)
                d = Math.floor(d!!)
                number = d.toInt()
            } catch (e: Exception) {
                return number
            }

        }
        return number
    }

    /**
     * 转成float类型，eg: 2 -> 2.0
     */
    fun parseFloat(str: String): Float {
        var number = 0f
        if (!TextUtils.isEmpty(str)) {
            try {
                number = java.lang.Float.parseFloat(str)
            } catch (e: Exception) {
                return 0f
            }

        }
        return number

    }

    /**
     * 转化成double类型， eg: 2 -> 2.0
     * @param str
     * @return
     */
    fun parseDouble(str: String): Double {
        var number = 0.0
        if (!TextUtils.isEmpty(str)) {
            try {
                number = java.lang.Double.parseDouble(str)
            } catch (e: NumberFormatException) {
                return 0.0
            }

        }
        return number
    }

    /**
     * 支持小数点，向上取整
     *
     * @param str
     * @return
     */
    fun parseLongByCeil(str: String): Long {
        var number: Long = 0
        if (!TextUtils.isEmpty(str)) {
            try {
                var d: Double? = java.lang.Double.parseDouble(str)
                d = Math.ceil(d!!)
                number = d.toLong()
            } catch (e: Exception) {
                return 0
            }

        }
        return number
    }

    /**
     * 支持小数点，向上取整
     *
     * @param str
     * @return
     */
    fun parseLongByFloor(str: String): Long {
        var number: Long = 0
        if (!TextUtils.isEmpty(str)) {
            try {
                var d: Double? = java.lang.Double.parseDouble(str)
                d = Math.floor(d!!)
                number = d.toLong()
            } catch (e: Exception) {
                return 0
            }

        }
        return number
    }

    /**
     * 末尾为0省略0,未四舍五入，保留以为小数
     * eg:1.50 -> 1.5
     * @param s
     * @return
     */
    fun getDoubleStrWithOneDecimal(s: String): String {
        var s = s
        try {
            if (s.indexOf(".") != -1) {
                s = s.substring(0, s.indexOf(".") + 2)
                if (s[s.length - 1] == '0') {
                    s = s.substring(0, s.indexOf("."))
                }
            }
        } catch (e: Exception) {
            return ""
        }

        return s
    }

    /**
     * 返回多少km, eg： "1120"返回"1.12km"
     */
    fun getDistance(s_distance: String): String {
        val distance = parseLongByCeil(s_distance)
        if (distance < 1000) {
            return "1km"
        } else if (distance < 10000 * 1000) {
            val s = BigDecimal(distance.toString()).divide(BigDecimal(1000.toString()), 2, BigDecimal.ROUND_HALF_UP).toString()
            return subZeroAndDot(s) + "km"
        } else {
            val s = BigDecimal(distance.toString()).divide(BigDecimal((10000 * 1000).toString()), 2, BigDecimal.ROUND_HALF_UP).toString()
            return subZeroAndDot(s) + "万km"
        }
    }

    /**
     * 返回鱼丸的重量
     * eg： "11"返回"11g"; "1120"返回"1.12kg"; "1120000"返回"1.12t"
     *
     */
    fun getYuWanCount(dw: String): String {
        val wight = parseLongByCeil(dw)
        if (wight < 1000) {
            return wight.toString() + "g"
        } else if (wight < 1000 * 1000) {
            val s = BigDecimal(dw).divide(BigDecimal(1000.toString()), 3, BigDecimal.ROUND_FLOOR).toString()
            return subZeroAndDot(s) + "kg"
        } else {
            val s = BigDecimal(dw).divide(BigDecimal((1000 * 1000).toString()), 2, BigDecimal.ROUND_FLOOR).toString()
            return subZeroAndDot(s) + "t"
        }
    }

    /**
     * 千位表示法, eg : 10000 -> 10,000
     *
     * @param num
     * @return
     */
    fun numberWithDelimiter(num: Long): String {
        val numStr = StringBuilder()
        var len = numStr.append(num).length
        if (len <= 3) return numStr.toString()   //如果长度小于等于3不做处理
        while ((len - 3) > 0) { //从个位开始倒序插入
            numStr.insert(len, ",")
        }
        return numStr.toString()
    }

    /**
     * 人数大于万，显示万，eg: 1000000 -> 100.0万
     */
    fun getOnLineNum(num: Int): String {
        if (num >= 100000000) {
            val d = num.toDouble() / 100000000.toDouble()
            return String.format("%2.1f", d) + "亿"
        } else if (num >= 10000) {
            val d = num.toDouble() / 10000.toDouble()
            return String.format("%2.1f", d) + "万"
        } else {
            return num.toString()
        }
    }

    /**
     * 使用java正则表达式去掉多余的.与0
     * eg : 11.0 -> 11
     * @param s
     * @return
     */
    fun subZeroAndDot(s: String): String {
        var s = s
        if (TextUtils.isEmpty(s)) {
            return ""
        }
        if (s.indexOf(".") > 0) {
            s = s.replace("0+?$".toRegex(), "") //去掉多余的0
            s = s.replace("[.]$".toRegex(), "") //如最后一位是.则去掉
        }
        return s
    }

    /**
     * 如果是个位数，前面自动补全0, eg: 1 -> 01
     * @param i
     * @return
     */
    fun formatNum(i: Int): String {
        if (i < 0) {
            return "00"
        }

        when (i) {
            0 -> return "00"
            1 -> return "01"
            2 -> return "02"
            3 -> return "03"
            4 -> return "04"
            5 -> return "05"
            6 -> return "06"
            7 -> return "07"
            8 -> return "08"
            9 -> return "09"
            else -> return "" + i
        }
    }

    /**
     * @return 获得区间内的一个随机数,包括最小值和最大值
     */
    fun randomInt(min: Int, max: Int): Int {
        val computeMax = Math.max(min, max)
        val computeMin = Math.min(min, max)
        val rand = Random()
        return rand.nextInt(computeMax - computeMin + 1) + computeMin
    }

    /**
     * 格式化times 超过一万以万为单位 保留一位小数 不超过一万返回原计数
     * 计算规则和 [DYNumberUtils.getOnLineNum]
     * @param times
     * @return
     */
    fun formateTimes(times: Long): String {
        if (times < 10000) {
            return times.toString()
        } else if (times < 100000000) {
            val d = times.toDouble() / 10000.toDouble()
            return String.format("%2.1f", d) + "万"
        } else {
            val d = times.toDouble() / 100000000.toDouble()
            return String.format("%2.1f", d) + "亿"
        }
    }

    /**
     * 金额转化为,例如12000转成120,000
     *
     * @param data
     * @return
     */
    fun formatTosepara(data: String): String {
        val amount = parseDouble(data)
        val df = DecimalFormat("#,###.##")
        return df.format(amount)
    }

    /**
     * float保留4位小数
     * eg: 3.1415926 -> 3.1416
     * @param number
     * @return
     */
    fun get4bitFloat(number: Float): Float {
        val df = DecimalFormat("#.####")
        return java.lang.Float.valueOf(df.format(number.toDouble()))
    }
    //    /**
    //     * 已万未单位，四舍五入, eg: 100000 -> 10万
    //     *
    //     * @param number
    //     * @return
    //     */
    //    public static String tenThousands(int number) {
    //        if (number > 10000) {
    //            String s = new BigDecimal(String.valueOf(number)).divide(new BigDecimal(String.valueOf(10000)), 1, BigDecimal.ROUND_HALF_UP).toString();
    //            return subZeroAndDot(s) + "万";
    //        } else {
    //            return String.valueOf(number);
    //        }
    //    }
    /**
     * 已万未单位，无四舍五入(主播等级经验值显示法)
     * eg:tenThousands(11111, 1) -> 1.1万
     * @param number:需要转成万的数字
     * @param fraction:万后面保留几位小数
     * @return
     */
    fun tenThousands(number: Double, fraction: Int): String {
        val formater = DecimalFormat()
        formater.maximumFractionDigits = fraction
        formater.groupingSize = 0
        formater.roundingMode = RoundingMode.FLOOR
        if (number >= 10000) {
            val value = formater.format(number / 10000)
            return value + "万"
        } else {
            return formater.format(number)
        }
    }

    /**
     * 判断字符串是否全为数字
     *
     * @param str
     * @return
     */
    fun isNumeric(str: String): Boolean {
        if (TextUtils.isEmpty(str)) {
            return false
        }
        val pattern = Pattern.compile("[0-9]*")
        return pattern.matcher(str).matches()
    }

    /**
     * 超过最大值显示 num++
     * eg : (101, 100) 显示"100+"
     * @param number
     * @return
     */
    fun showNumMaxJJ(number: Int, max: Int): String {
        return if (max <= 0 || number <= max) {
            number.toString() + ""
        } else "$max+"

    }

    /**
     * 对传入参数除100,默认整除时保留小数位
     *
     * @param num :需要除100的数
     * @param decNum 保留的小数位
     * @param decNum 末位为.0...时是否保留小数位
     * @return
     */
    fun divideHundred(num: Long, decNum: Int, retainDec: Boolean): String {
        val d = num.toDouble() / 100.toDouble()
        val result = BigDecimal(d.toString() + "").setScale(if (decNum >= 0) decNum else 0, BigDecimal.ROUND_FLOOR)
        return if (!retainDec) {
            getDoubleStrWithOneDecimal(result.toString())
        } else {
            result.toString()
        }
    }

    /**
     * 获取房间列表单个房间热度，传参为String型
     * 向上取整,数字过亿以亿为单位，数字过万不足亿以万为单位，数字不足万不作处理
     * @param hn
     * @return
     */
    fun getHotNum(hn: String): String {
        val hotNum = parseLongByCeil(hn)
        return formateTimes(hotNum)
    }

    /**
     * 此方法只支持整数，同时处理范围扩大为long范围
     * @param str
     * @return
     */
    @JvmOverloads
    fun parseRealLong(str: String, defaultValue: Long = 0L): Long {
        var number = defaultValue
        if (!TextUtils.isEmpty(str)) {
            try {
                number = java.lang.Long.parseLong(str)
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
        return number
    }

    /**
     * 将String除以100，采用地板除
     * @param value
     * @return 如果value长度小于等于2，返回0；<br></br>
     * 如果没有值，返回""；<br></br>
     * 如果value长度大于2，返回地板除100后的值<br></br>
     */
    fun divide100(value: String): String {
        var result = ""
        if (!TextUtils.isEmpty(value)) {
            try {
                val valueS = value.trim { it <= ' ' }
                if (valueS.length <= 2) {
                    result = "0"
                } else {
                    result = valueS.substring(0, valueS.length - 2)
                }
            } catch (e: Exception) {
            }

        }
        return result
    }
}