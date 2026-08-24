package com.zettaonline.task.foreigncurrencyexchangetask;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.zettaonline.task.foreigncurrencyexchangetask.controllers.ForeignCurrencyReqsController;

public class ForeignCurrencyReqsControllerTests {

	
	@Test
	public void testForInvalidCurrencyParameterInFromField() {
		ForeignCurrencyReqsController fcqc = new ForeignCurrencyReqsController();
		
		assertEquals("Error, invalid parameter in \"from\" field!", fcqc.getCurrentRates("YUR", "BGN"));
	}
	
	@Test
	public void testForInvalidCurrencyParameterInToCurrencyField() {
		ForeignCurrencyReqsController fcqc = new ForeignCurrencyReqsController();
		
		assertEquals("Error, invalid parameter in \"to\" field!", fcqc.getCurrentRates("USD", "YUR"));		
	}
	
}
