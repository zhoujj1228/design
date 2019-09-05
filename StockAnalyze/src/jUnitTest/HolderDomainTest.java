package jUnitTest;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import domain.HolderDomain;
import junit.framework.TestCase;

public class HolderDomainTest {
	HolderDomain hd;
	@Before
	public void setUp() throws Exception {
		hd = new HolderDomain();
		hd.setAmount("493.64Íò");
	}

	@Test
	public void testGetNumberOfJjAmount() {
		double numberOfJjAmount = hd.getNumberOfJjAmount();
		System.out.println(numberOfJjAmount);
		TestCase.assertEquals(4936400.0, numberOfJjAmount);
	}

}
