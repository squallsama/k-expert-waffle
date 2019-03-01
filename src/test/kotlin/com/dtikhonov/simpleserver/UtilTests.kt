package com.dtikhonov.simpleserver

import com.dtikhonov.simpleserver.utils.timeConversion
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test


class UtilTests {

	@Test
	fun testConverter(){
		assertEquals("21:40:30", timeConversion("09:40:30PM"))
		assertEquals("06:40:30", timeConversion("6:40:30AM"))
        assertEquals("00:40:22", timeConversion("12:40:22AM"))
	}
}
