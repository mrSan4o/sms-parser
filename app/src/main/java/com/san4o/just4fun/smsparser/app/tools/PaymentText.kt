package com.san4o.just4fun.smsparser.app.tools

import com.san4o.just4fun.smsparser.app.data.PaymentType
import com.san4o.just4fun.smsparser.app.utils.isPrice
import com.san4o.just4fun.smsparser.app.utils.toPrice
import java.util.*

class PaymentText(val text: String) {

    companion object {
        val declineRegex = Regex("^(ОТКАЗ|отказ|Отказ).*")
        //        val declineRegex = Regex("ОТКАЗ (\\([а-я ]\\))? (покупка|выдача) ${Price.regex.pattern} [A-Z 0-9]+")
        val serviceDeclineRegex = Regex("^услуга.*отключена.$")

        private val mobileBankRegex =
            Regex("оплата Мобильного банка за (\\d){2}/(\\d){2}/(\\d){4}-(\\d){2}/(\\d){2}/(\\d){4}")
        private val mobileBank2Regex =
            Regex("мобильный банк за (\\d){2}.(\\d){2}-(\\d){2}.(\\d){2}")

    }

    private val tokens: MutableList<String> = ArrayList<String>(text.split(" "))

    fun firstToken() = if (tokens.size == 0) {
        ""
    } else {
        tokens[0]
    }

    fun secondToken() = if (tokens.size == 0) {
        ""
    } else {
        tokens[1]
    }

    fun lastToken() = if (tokens.size == 0) {
        ""
    } else {
        tokens[tokens.size - 1]
    }

    fun removeFirstToken() {
        tokens.removeAt(0)
    }


    fun isExclude(): Boolean {
        val newtext = buildText()
        return newtext.matches(PaymentText.serviceDeclineRegex)
                || newtext.matches(PaymentText.declineRegex)
                || text.contains("пополнение Копилки на")
    }

    private fun buildText(): String {
        return tokens.joinToString(" ")
    }


    fun parseDate(calendarDate: CalendarDate) {
        val firstToken = firstToken()
        val secondToken = secondToken()

        val dateFirstToken = isDateToken(firstToken)
        val timeFirstToken = isTimeToken(firstToken)


        if (dateFirstToken) {

            parseDateToken(firstToken)?.let { calendarDate.setDate(it) }

            if (dateFirstToken && isTimeToken(secondToken)) {
                parseTimeToken(secondToken)?.let { calendarDate.setTime(it) }
            }
        }
        if (timeFirstToken) {
            parseTimeToken(firstToken)?.let { calendarDate.setTime(it) }
        }
    }

    fun parseBalance(): Double {
        val text = buildText()
        val balanceLabel = "Баланс:"
        val index = text.indexOf(balanceLabel)
        var balance = 0.0
        if (index != -1) {
            val cuttedText = text.substringAfter(balanceLabel).trim()
            if (cuttedText.isPrice()) {
                balance = cuttedText.toPrice()

                val balanceLabelIndex = tokens.indexOf(balanceLabel)
                tokens.removeAt(balanceLabelIndex + 1)
                tokens.removeAt(balanceLabelIndex)
            }
        } else {
            val lastToken = lastToken()
            if (lastToken.isPrice()) {
                balance = lastToken.toPrice()
                tokens.removeAt(tokens.size - 1)
                tokens.removeAt(tokens.size - 1)
            }
        }
        return balance
    }

    fun parseFlowType(): PaymentTypeData {
        val text = buildText()

        val firstToken = firstToken().toLowerCase()
        val secondToken = secondToken().toLowerCase()
        val firstAndSecondTokens = "$firstToken $secondToken"

        return when {
            firstToken == "покупка" && secondToken.isPrice() -> {
                parseToken(firstToken, PaymentType.OUTFLOW)
            }
            firstToken == "списание" && secondToken.isPrice() -> {
                parseToken(firstToken, PaymentType.OUTFLOW)
            }
            firstAndSecondTokens == "отмена покупки" && tokens[2].isPrice() -> {
                parseTwoTokens(firstAndSecondTokens, PaymentType.INFLOW)
            }
            firstToken == "выдача" && secondToken.isPrice() -> {
                parseToken(firstToken, PaymentType.OUTFLOW)
            }
            firstAndSecondTokens == "выдача наличных" && tokens[2].isPrice() -> {
                parseTwoTokens(firstToken, PaymentType.OUTFLOW)
            }
            firstAndSecondTokens == "зачисление зарплаты" && tokens[2].isPrice() -> {
                parseTwoTokens(firstAndSecondTokens, PaymentType.INFLOW)
            }
            firstToken == "зачисление" && secondToken.isPrice() -> {
                parseToken(firstToken, PaymentType.INFLOW)
            }
            firstToken == "оплата" && secondToken.isPrice() -> {
                parseToken(firstToken, PaymentType.OUTFLOW)
            }
            firstToken == "перевод" && secondToken.isPrice() -> {
                parseToken(firstToken, PaymentType.OUTFLOW)
            }
            firstAndSecondTokens == "возврат покупки" && tokens[2].isPrice() -> {
                parseTwoTokens(firstAndSecondTokens, PaymentType.INFLOW)
            }
            text.contains(mobileBankRegex) -> {
                parseMobileBankPayment(text, mobileBankRegex)
            }
            text.contains(mobileBank2Regex) -> {
                parseMobileBankPayment(text, mobileBank2Regex)
            }
            firstAndSecondTokens == "оплата услуг" && tokens[2].isPrice() -> {
                parseTwoTokens(firstAndSecondTokens, PaymentType.OUTFLOW)
            }

            else -> {
                PaymentTypeData(PaymentType.UNKNOWN, "unknown")
            }
        }

    }

    fun parseDestination(): String {
        val text = buildText()
        return if (!text.isEmpty()) {

            removeFirstToken()
            return text
        } else {
            ""
        }
    }

    fun parseSum(): Double {
        val textPrice = firstToken()
        return if (textPrice.isPrice()) {

            removeFirstToken()
            return textPrice.toPrice()
        } else {
            0.0
        }
    }

    private fun parseMobileBankPayment(text: String, regex: Regex): PaymentTypeData {
        val result = regex.find(text)
        var typeDescription = ""
        var type = PaymentType.UNKNOWN
        var typeName = "оплата Мобильного банка"
        if (result != null) {
            val value = result.value
            typeDescription = value
            type = PaymentType.OUTFLOW

            value.split(" ").forEach {
                tokens.remove(it)
            }
        }

        return PaymentTypeData(type, typeName, typeDescription)
    }


    private fun parseToken(firstToken: String, paymentType: PaymentType): PaymentTypeData {
        removeFirstToken()

        return PaymentTypeData(paymentType, firstToken)
    }

    private fun parseTwoTokens(firstAndSecondTokens: String, paymentType: PaymentType): PaymentTypeData {
        removeFirstToken()
        removeFirstToken()

        return PaymentTypeData(paymentType, firstAndSecondTokens)
    }

    private fun isDateToken(token: String): Boolean = token.matches(Payment.dateRegex)
    private fun isTimeToken(token: String): Boolean = token.matches(Payment.timeRegex)

    private fun parseDateToken(token: String): Date? {
        val date = Payment.textDateFormat.parse(token)
        if (date != null) {
            tokens.remove(token)
            return date
        }
        return null
    }

    private fun parseTimeToken(token: String): Date? {
        val date = Payment.textTimeFormat.parse(token)
        if (date != null) {
            tokens.remove(token)
            return date
        }
        return null
    }


    data class PaymentTypeData(val type: PaymentType, val name: String, val description: String = "")
}