package com.github.q16695

import com.github.q16695.MD5Utils.MD5Upper
import java.io.IOException
import java.util.*

object MachineCodeUtil {
    const val LINUX_OS_NAME = "LINUX"
    const val SYSTEM_PROPERTY_OS_NAME = "os.name"
    @JvmStatic
    fun main(args: Array<String>) {
        println(thisMachineCode)
        println(thisMachineCodeMd5)
    }

    /**
     * 获取机器唯一识别码（CPU ID + BIOS UUID）
     *
     * @return 机器唯一识别码
     */
    val thisMachineCode: String
        get() {
            try {
                return cpuId + biosUuid
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return ""
        }

    /**
     * 获取机器码进行MD5摘要后的字符串
     *
     * @return
     */
    val thisMachineCodeMd5: String?
        get() {
            try {
                val thisMachineCode = thisMachineCode
                return MD5Upper(thisMachineCode)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return ""
        }// 获取当前操作系统名称

    /**
     * 获取当前系统CPU序列，可区分linux系统和windows系统
     */
    @get:Throws(IOException::class)
    val cpuId: String
        get() {
            val cpuId: String?
            // 获取当前操作系统名称
            var os = System.getProperty(SYSTEM_PROPERTY_OS_NAME)
            os = os.uppercase(Locale.getDefault())
            cpuId = if (LINUX_OS_NAME == os) {
                getLinuxDmidecodeInfo("dmidecode -t processor | grep 'ID'", "ID", ":")
            } else {
                windowsCpuId
            }
            return cpuId!!.uppercase(Locale.getDefault()).replace(" ", "")
        }

    /**
     * 获取linux系统
     * dmidecode
     * 命令的信息
     */
    @Throws(IOException::class)
    fun getLinuxDmidecodeInfo(cmd: String?, record: String?, symbol: String?): String? {
        val execResult = executeLinuxCmd(cmd)
        val infos = execResult.split("\n").toTypedArray()
        for (info in infos) {
            val info = info.trim { it <= ' ' }
            if (info.contains(record!!)) {
                info.replace(" ", "")
                val sn = info.split(symbol!!).toTypedArray()
                return sn[1]
            }
        }
        return null
    }

    /**
     * 执行Linux 命令
     *
     * @param cmd Linux 命令
     * @return 命令结果信息
     * @throws IOException 执行命令期间发生的IO异常
     */
    @Throws(IOException::class)
    fun executeLinuxCmd(cmd: String?): String {
        val run = Runtime.getRuntime()
        val process: Process
        process = run.exec(cmd)
        val processInputStream = process.inputStream
        val stringBuilder = StringBuilder()
        val b = ByteArray(8192)
        var n: Int
        while (processInputStream.read(b).also { n = it } != -1) {
            stringBuilder.append(String(b, 0, n))
        }
        processInputStream.close()
        process.destroy()
        return stringBuilder.toString()
    }

    /**
     * 获取windows系统CPU序列
     */
    @get:Throws(IOException::class)
    val windowsCpuId: String
        get() {
            val process =
                Runtime.getRuntime().exec(arrayOf("wmic", "cpu", "get", "ProcessorId"))
            process.outputStream.close()
            val sc = Scanner(process.inputStream)
            sc.next()
            return sc.next()
        }// 获取当前操作系统名称

    /**
     * 获取 BIOS UUID
     *
     * @return BIOS UUID
     * @throws IOException 获取BIOS UUID期间的IO异常
     */
    @get:Throws(IOException::class)
    val biosUuid: String
        get() {
            val cpuId: String?
            // 获取当前操作系统名称
            var os = System.getProperty("os.name")
            os = os.uppercase(Locale.getDefault())
            cpuId = if ("LINUX" == os) {
                getLinuxDmidecodeInfo("dmidecode -t system | grep 'UUID'", "UUID", ":")
            } else {
                windowsBiosUUID
            }
            return cpuId!!.uppercase(Locale.getDefault()).replace(" ", "")
        }

    /**
     * 获取windows系统 bios uuid
     *
     * @return
     * @throws IOException
     */
    @get:Throws(IOException::class)
    val windowsBiosUUID: String
        get() {
            val process = Runtime.getRuntime()
                .exec(arrayOf("wmic", "path", "win32_computersystemproduct", "get", "uuid"))
            process.outputStream.close()
            val sc = Scanner(process.inputStream)
            sc.next()
            return sc.next()
        }
}
