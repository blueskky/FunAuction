package com.`fun`.auction.dimen

import java.io.File
import java.io.FileOutputStream
import java.math.BigDecimal

class MakeUtils {


    companion object {
        val XML_HEADER = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\r\n"
        val XML_RESOURCE_START = "<resources>\r\n"
        val XML_RESOURCE_END = "</resources>\r\n"
        val XML_DIMEN_TEMPLETE = "<dimen name=\"px_%1\$d\">%2$.2fdp</dimen>\r\n"


        val XML_BASE = "<string name=\"base_dpi\">%ddp</string>\r\n"
        val XML_NAME = "dimens.xml";

        val max = 1080


        fun makeAll(designWidth: Int, widthType: Int, dir: String) {
            val floderName = "values-sw${widthType}dp"
            val file = File(dir + File.separator + floderName)
            if (!file.exists()) {
                file.mkdirs()
            }
            val out = FileOutputStream(file.absolutePath + File.separator + XML_NAME)
            out.write(makeAllDimens(widthType, designWidth).toByteArray())
            out.flush()
            out.close()
        }


        fun makeAllDimens(valueType: Int, width: Int): String {
            val buffer = StringBuffer()
            buffer.append(XML_HEADER)
            buffer.append(XML_RESOURCE_START)

            val format = String.format(XML_BASE, valueType)
            buffer.append(format)
            for (i in 0..max) {
                val dp = px2dip(i, valueType, width)
                val temp = String.format(XML_DIMEN_TEMPLETE, i, dp)
                buffer.append(temp)
            }
            buffer.append(XML_RESOURCE_END)

            return buffer.toString()
        }


        fun px2dip(pxValue: Int, sw: Int, designWidth: Int): Float {
            val dp = (pxValue.toFloat() / designWidth.toFloat()) * sw
            val big = BigDecimal(dp.toString())
            return big.setScale(2, BigDecimal.ROUND_HALF_UP).toFloat()
        }
    }


}