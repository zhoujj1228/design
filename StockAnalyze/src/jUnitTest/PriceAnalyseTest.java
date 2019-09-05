package jUnitTest;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import analyseConcept.ConceptAnalyse;
import analyseHistory.HistoryAnalyse;
import analysePrice.PriceAnalyse;

public class PriceAnalyseTest {

	@Before
	public void setUp() throws Exception {
		HistoryAnalyse.initHistoryMap("2017-10-08", "2017-11-08");
		ConceptAnalyse.initConcept3Map();
	}

	@Test
	public void testGetConceptDayPriceMap() {
		PriceAnalyse.getConceptDayPriceMap("新能源汽车");
		fail("Not yet implemented");
	}

}
