package org.junit.experimental.cpsuite.suitetest;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({org.junit.experimental.cpsuite.tests.p1.P1Test.class, org.junit.experimental.cpsuite.tests.p2.ConcreteP2Test.class})
public class TestSuite {

}
