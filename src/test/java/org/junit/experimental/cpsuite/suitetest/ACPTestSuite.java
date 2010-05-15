package org.junit.experimental.cpsuite.suitetest;

import org.junit.experimental.cpsuite.ClasspathSuite;
import org.junit.experimental.cpsuite.ClasspathSuite.ClassnameFilters;
import org.junit.runner.RunWith;

@RunWith(ClasspathSuite.class)
@ClassnameFilters("oops")
public class ACPTestSuite {
}
