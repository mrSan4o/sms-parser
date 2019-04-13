package com.san4o.just4fun.smsparser.app.tools

import com.san4o.just4fun.smsparser.app.data.PaymentType
import com.san4o.just4fun.smsparser.app.utils.isPrice
import com.san4o.just4fun.smsparser.app.utils.toPrice
import java.util.*
import kotlin.collections.ArrayList

class PaymentBuilder(private val text: String, private val date: Date) {


    companion object {
        val declineRegex = Regex("^(ОТКАЗ|отказ|Отказ).*")
        //        val declineRegex = Regex("ОТКАЗ (\\([а-я ]\\))? (покупка|выдача) ${Price.regex.pattern} [A-Z 0-9]+")
        val serviceDeclineRegex = Regex("^услуга.*отключена.$")
    }

    val tokens: MutableList<String> = ArrayList<String>(text.split(" "))
    private val textDate = CalendarDate(date)
    private var exclude = false
    private var balance = 0.0
    private var price = 0.0
    private var destination = ""
    private var type = PaymentType.UNKNOWN
    private var typeName = "unknown"
    private var typeDescription = ""

    private val mobileBankRegex =
        Regex("оплата Мобильного банка за (\\d){2}/(\\d){2}/(\\d){4}-(\\d){2}/(\\d){2}/(\\d){4}")
    private val mobileBank2Regex =
        Regex("мобильный банк за (\\d){2}.(\\d){2}-(\\d){2}.(\\d){2}")


    init {
        parseDate()


        val newtext = text()
        exclude = newtext.matches(serviceDeclineRegex)
                || newtext.matches(declineRegex)
                || text.contains("пополнение Копилки на")

        if (!exclude) {
            parseBalance()

            parseFlowType()

            parseSum()

            parseDestination()
        }
    }

    private fun parseDate() {
        val firstToken = tokens[0]
        val secondToken = tokens[1]

        val dateFirstToken = isDateToken(firstToken)
        val timeFirstToken = isTimeToken(firstToken)
        if (dateFirstToken) {
            parseDateToken(firstToken)

            if (dateFirstToken && isTimeToken(secondToken)) {
                parseTimeToken(secondToken)
            }
        }
        if (timeFirstToken) {
            parseTimeToken(firstToken)
        }
    }

    private fun parseDestination() {
        val text = text()
        if (!text.isEmpty()) {
            destination = text;
            removeFirstToken()
        }
    }

    private fun parseSum() {
        val textPrice = firstToken()
        if (textPrice.isPrice()) {
            price = textPrice.toPrice()
            removeFirstToken()
        }
    }

    private fun parseFlowType() {
        val text = text()

        val firstToken = firstToken().toLowerCase()
        val secondToken = secondToken().toLowerCase()
        val firstAndSecondTokens = "$firstToken $secondToken"
        when {
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
        }

    }

    private fun parseToken(firstToken: String, paymentType: PaymentType) {
        type = paymentType
        typeName = firstToken
        removeFirstToken()
    }

    private fun parseTwoTokens(firstAndSecondTokens: String, paymentType: PaymentType) {
        type = paymentType
        typeName = firstAndSecondTokens
        removeFirstToken()
        removeFirstToken()
    }

    private fun removeFirstToken() {
        tokens.removeAt(0)
    }

    private fun parseMobileBankPayment(text: String, regex: Regex) {
        val result = regex.find(text)
        if (result != null) {
            val value = result.value
            typeDescription = value
            type = PaymentType.OUTFLOW
            typeName = "оплата Мобильного банка"
            value.split(" ").forEach {
                tokens.remove(it)
            }
        }
    }

    private fun parseBalance() {
        val text = text()
        val balanceLabel = "Баланс:"
        val index = text.indexOf(balanceLabel)
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
    }

    fun isExclude() = exclude

    fun sourceText() = text
    fun text() = tokens.joinToString(" ")

    fun firstToken() = if (tokens.size == 0) { "" } else { tokens[0] }
    fun secondToken() = if (tokens.size == 0) { "" } else { tokens[1] }
    fun lastToken() = if (tokens.size == 0) { "" } else { tokens[tokens.size - 1] }
    fun balance() = balance
    fun date() = textDate.getDate()
    fun price() = price
    fun destination() = destination
    fun type() = type
    fun typeName() = typeName
    fun typeDescription() = typeDescription

    private fun isDateToken(token: String): Boolean = token.matches(Payment.dateRegex)
    private fun isTimeToken(token: String): Boolean = token.matches(Payment.timeRegex)

    private fun parseDateToken(token: String) = Payment.textDateFormat.parse(token)
        .let { tokenDate ->
            textDate.setDate(tokenDate)
            tokens.remove(token)
        }

    private fun parseTimeToken(token: String) = Payment.textTimeFormat.parse(token)
        .let { tokenDate ->
            textDate.setTime(tokenDate)
            tokens.remove(token)
        }

}