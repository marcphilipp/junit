/*
 * @author Johannes Link (business@johanneslink.net)
 * 
 * Published under GNU General Public License 2.0 (http://www.gnu.org/licenses/gpl.html)
 */
package org.junit.experimental.cpsuite.internal;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import junit.framework.TestCase;
import org.junit.Test;
import org.junit.experimental.cpsuite.SuiteType;
import org.junit.runner.RunWith;

/**
 * ClassTester implementation to retrieve JUnit38 & 4.x test classes in the
 * classpath. You can specify if you want to include jar files in the search and
 * you can give a set of regex expression to specify the class names to include.
 */
public class ClasspathSuiteTester implements ClassTester {

	private final boolean fSearchInJars;

	private final SuiteType[] fSuiteTypes;

	private List<String> fPositiveFilters;

	private List<String> fNegationFilters;

	private final Class<?>[] fBaseTypes;

	private final Class<?>[] fExcludedBaseTypes;

	/**
	 * @param searchInJars
	 *            Specify if you want to include jar files in the search
	 * @param filterPatterns
	 *            A set of regex expression to specify the class names to
	 *            include (included if any pattern matches); use null to include
	 *            all test classes in all packages.
	 * @param baseTypes
	 *            TODO
	 * @param types
	 */
	public ClasspathSuiteTester(boolean searchInJars, String[] filterPatterns,
			SuiteType[] suiteTypes, Class<?>[] baseTypes,
			Class<?>[] excludedBaseTypes) {
		this.fSearchInJars= searchInJars;
		this.fPositiveFilters= findPositiveFilters(filterPatterns);
		this.fNegationFilters= findNegationFilters(filterPatterns);
		this.fSuiteTypes= suiteTypes;
		this.fBaseTypes= baseTypes;
		this.fExcludedBaseTypes= excludedBaseTypes;
	}

	public boolean acceptClass(Class<?> clazz) {
		if (isInSuiteTypes(SuiteType.TEST_CLASSES)) {
			if (acceptTestClass(clazz)) {
				return true;
			}

		}
		if (isInSuiteTypes(SuiteType.JUNIT38_TEST_CLASSES)) {
			if (acceptJUnit38Test(clazz)) {
				return true;
			}
		}
		if (isInSuiteTypes(SuiteType.RUN_WITH_CLASSES)) {
			return acceptRunWithClass(clazz);
		}

		return false;
	}

	private boolean acceptJUnit38Test(Class<?> clazz) {
		if (isAbstractClass(clazz)) {
			return false;
		}
		if (hasExcludedBaseType(clazz)) {
			return false;
		}
		if (!hasCorrectBaseType(clazz)) {
			return false;
		}
		return TestCase.class.isAssignableFrom(clazz);
	}

	private boolean acceptRunWithClass(Class<?> clazz) {
		return clazz.isAnnotationPresent(RunWith.class);
	}

	private boolean isInSuiteTypes(SuiteType suiteType) {
		return Arrays.asList(fSuiteTypes).contains(suiteType);
	}

	private boolean acceptTestClass(Class<?> clazz) {
		if (isAbstractClass(clazz)) {
			return false;
		}
		if (hasExcludedBaseType(clazz)) {
			return false;
		}
		if (!hasCorrectBaseType(clazz)) {
			return false;
		}
		try {
			for (Method method : clazz.getMethods()) {
				if (method.getAnnotation(Test.class) != null) {
					return true;
				}
			}
		} catch (NoClassDefFoundError ignore) {
		}
		return false;
	}

	private boolean hasExcludedBaseType(Class<?> clazz) {
		for (Class<?> excludedBaseType : fExcludedBaseTypes) {
			if (excludedBaseType.isAssignableFrom(clazz)) {
				return true;
			}
		}
		return false;
	}

	private boolean hasCorrectBaseType(Class<?> clazz) {
		for (Class<?> baseType : fBaseTypes) {
			if (baseType.isAssignableFrom(clazz)) {
				return true;
			}
		}
		return false;
	}

	private boolean isAbstractClass(Class<?> clazz) {
		return (clazz.getModifiers() & Modifier.ABSTRACT) != 0;
	}

	public boolean acceptClassName(String className) {
		if (!acceptInPositiveFilers(className)) {
			return false;
		}
		return acceptInNegationFilters(className);
	}

	private boolean acceptInNegationFilters(String className) {
		for (String pattern : fNegationFilters) {
			if (className.matches(pattern)) {
				return false;
			}
		}
		return true;
	}

	private boolean acceptInPositiveFilers(String className) {
		boolean isPositiveAccepted= fPositiveFilters.isEmpty();
		for (String pattern : fPositiveFilters) {
			if (className.matches(pattern)) {
				isPositiveAccepted= true;
				break;
			} else {
				isPositiveAccepted= false;
			}
		}
		return isPositiveAccepted;
	}

	private List<String> findPositiveFilters(String[] filterPatterns) {
		List<String> filters= new ArrayList<String>();
		if (filterPatterns != null) {
			for (String pattern : filterPatterns) {
				if (!pattern.startsWith("!")) {
					filters.add(pattern);
				}
			}
		}
		return filters;
	}

	private List<String> findNegationFilters(String[] filterPatterns) {
		List<String> filters= new ArrayList<String>();
		for (String pattern : filterPatterns) {
			if (pattern.startsWith("!")) {
				filters.add(pattern.substring(1));
			}
		}
		return filters;
	}

	public boolean acceptInnerClass() {
		return true;
	}

	public boolean searchInJars() {
		return fSearchInJars;
	}

	protected List<String> getPositiveClassnameFilters() {
		return fPositiveFilters;
	}

	protected List<String> getNegationClassnameFilters() {
		return fNegationFilters;
	}

	protected SuiteType[] getSuiteTypes() {
		return fSuiteTypes;
	}

	protected Class<?>[] getBaseTypes() {
		return fBaseTypes;
	}

	protected Class<?>[] getExcludedBaseTypes() {
		return fExcludedBaseTypes;
	}
}
