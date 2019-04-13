package com.san4o.just4fun.smsparser.app.tools

data class SmsParseItem(val text: String, val time: Long) {
    companion object {
        fun parse(lines: List<String>): List<SmsParseItem> = lines.map { parse(it) }
        fun parse(line: String): SmsParseItem {
            return SmsParseItem(
                text = line.substringAfter("\"").substringBeforeLast("\"")
//                .substring(9) // repalce example:"VISA7066 "
                    .trim(),
                time = if (line.contains(",")) {
                    line.substringAfterLast(",").trim().toLong()
                } else {
                    -1
                }
            )
        }

    }
}