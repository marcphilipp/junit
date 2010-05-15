package org.junit.experimental.cpsuite.tests.p2;

import org.junit.*;

public abstract class AbstractP2Test {
	@Test
	public void testSuccess() {
	}
	@Test
	public void testFailure() {
		Assert.fail();
	}

}
