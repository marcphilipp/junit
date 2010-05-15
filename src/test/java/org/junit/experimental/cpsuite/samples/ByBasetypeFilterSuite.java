package org.junit.experimental.cpsuite.samples;

import static org.junit.experimental.cpsuite.SuiteType.*;

import org.junit.experimental.cpsuite.ClasspathSuite;
import org.junit.experimental.cpsuite.ClasspathSuite.*;
import org.junit.experimental.cpsuite.tests.p2.AbstractP2Test;
import org.junit.runner.RunWith;


@RunWith(ClasspathSuite.class)
@SuiteTypes( { TEST_CLASSES, JUNIT38_TEST_CLASSES })
@BaseTypeFilter( { AbstractP2Test.class })
public class ByBasetypeFilterSuite {

}
