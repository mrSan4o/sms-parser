package com.san4o.just4fun.smsparser.app

import java.util.*

/*
        Telephony.Sms.Inbox.BODY,
        Telephony.Sms.Inbox.DATE,
        Telephony.Sms.Inbox.SUBJECT,
        Telephony.Sms.Inbox.ADDRESS,
        Telephony.Sms.Inbox.DEFAULT_SORT_ORDER,
        Telephony.Sms.Inbox.PERSON,
        Telephony.Sms.Inbox.READ,
        Telephony.Sms.Inbox.STATUS,
        Telephony.Sms.Inbox.TYPE
* */

class SmsSourceItem(
    val content: String,
    val date: Date) {

}
