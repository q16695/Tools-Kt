package com.github.q16695

import java.math.BigInteger
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.*
import kotlin.experimental.and
import kotlin.experimental.inv
import kotlin.experimental.xor

object MD5Utils {
    val hexDigits = charArrayOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F')
    val hexDigitsLower = charArrayOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f')

    /**
     * 对字符串 MD5 无盐值加密
     *
     * @param plainText
     * 传入要加密的字符串
     * @return
     * MD5加密后生成32位(小写字母+数字)字符串
     */
    fun MD5Lower(plainText: String): String? {
        return try {
            // 获得MD5摘要算法的 MessageDigest 对象
            val md = MessageDigest.getInstance("MD5")

            // 使用指定的字节更新摘要
            md.update(plainText.toByteArray())

            // digest()最后确定返回md5 hash值，返回值为8位字符串。因为md5 hash值是16位的hex值，实际上就是8位的字符
            // BigInteger函数则将8位的字符串转换成16位hex值，用字符串来表示；得到字符串形式的hash值。1 固定值
            BigInteger(1, md.digest()).toString(16)
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
            null
        }
    }

    /**
     * 对字符串 MD5 加密
     *
     * @param plainText
     * 传入要加密的字符串
     * @return
     * MD5加密后生成32位(大写字母+数字)字符串
     */
    fun MD5Upper(plainText: String): String? {
        return try {
            // 获得MD5摘要算法的 MessageDigest 对象
            val md = MessageDigest.getInstance("MD5")

            // 使用指定的字节更新摘要
            md.update(plainText.toByteArray())

            // 获得密文
            val mdResult = md.digest()
            // 把密文转换成十六进制的字符串形式
            val j = mdResult.size
            val str = CharArray(j * 2)
            var k = 0
            for (i in 0 until j) {
                val byte0 = mdResult[i]
                str[k++] = hexDigits[byte0.toInt().ushr( 4 and 0xf)] // 取字节中高 4 位的数字转换, >>> 为逻辑右移，将符号位一起右移
                str[k++] = hexDigits[byte0.toInt().and(0xf)] // 取字节中低 4 位的数字转换
            }
            String(str)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /**
     * 对字符串 MD5 加盐值加密
     *
     * @param plainText
     * 传入要加密的字符串
     * @param saltValue
     * 传入要加的盐值
     * @return
     * MD5加密后生成32位(小写字母+数字)字符串
     */
    fun MD5Lower(plainText: String, saltValue: String): String? {
        return try {
            // 获得MD5摘要算法的 MessageDigest 对象
            val md = MessageDigest.getInstance("MD5")

            // 使用指定的字节更新摘要
            md.update(plainText.toByteArray())
            md.update(saltValue.toByteArray())

            // digest()最后确定返回md5 hash值，返回值为8位字符串。因为md5 hash值是16位的hex值，实际上就是8位的字符
            // BigInteger函数则将8位的字符串转换成16位hex值，用字符串来表示；得到字符串形式的hash值。1 固定值
            BigInteger(1, md.digest()).toString(16)
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
            null
        }
    }

    /**
     * 对字符串 MD5 加盐值加密
     *
     * @param plainText
     * 传入要加密的字符串
     * @param saltValue
     * 传入要加的盐值
     * @return
     * MD5加密后生成32位(大写字母+数字)字符串
     */
    fun MD5Upper(plainText: String, saltValue: String): String? {
        return try {
            // 获得MD5摘要算法的 MessageDigest 对象
            val md = MessageDigest.getInstance("MD5")

            // 使用指定的字节更新摘要
            md.update(plainText.toByteArray())
            md.update(saltValue.toByteArray())

            // 获得密文
            val mdResult = md.digest()
            // 把密文转换成十六进制的字符串形式
            val j = mdResult.size
            val str = CharArray(j * 2)
            var k = 0
            for (i in 0 until j) {
                val byte0 = mdResult[i]
                str[k++] = hexDigits[byte0.toInt() ushr 4 and 0xf]
                str[k++] = hexDigits[byte0.toInt() and 0xf]
            }
            String(str)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /**
     * MD5加密后生成32位(小写字母+数字)字符串
     * 同 MD5Lower() 一样
     */
    fun MD5(plainText: String): String? {
        return try {
            val mdTemp = MessageDigest.getInstance("MD5")
            mdTemp.update(plainText.toByteArray(charset("UTF-8")))
            val md = mdTemp.digest()
            val j = md.size
            val str = CharArray(j * 2)
            var k = 0
            for (i in 0 until j) {
                val byte0 = md[i]
                str[k++] = hexDigitsLower[byte0.toInt() ushr 4 and 0xf]
                str[k++] = hexDigitsLower[byte0.toInt() and 0xf]
            }
            String(str)
        } catch (e: Exception) {
            null
        }
    }

    /**
     * 校验MD5码
     *
     * @param text
     * 要校验的字符串
     * @param md5
     * md5值
     * @return 校验结果
     */
    fun valid(text: String, md5: String): Boolean {
        return md5 == MD5(text) || md5 == MD5(text)!!.uppercase(Locale.getDefault())
    }
}
