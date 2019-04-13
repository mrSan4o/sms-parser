package com.san4o.just4fun.smsparser.app

import com.san4o.just4fun.smsparser.app.utils.removeIfStart
import org.junit.Assert
import org.junit.Test

class TestExt {

    @Test
    fun testRemoveIfStart(){
        Assert.assertEquals(" 22 44", "11 22 44".removeIfStart("11"))
        Assert.assertEquals("33 22 44", "33 22 44".removeIfStart("11"))
    }
}