/*
 * @author Johannes Link (business@johanneslink.net)
 * 
 * Published under GNU General Public License 2.0 (http://www.gnu.org/licenses/gpl.html)
 */
package org.junit.experimental.cpsuite.internal;

import org.junit.experimental.cpsuite.SuiteType;

public class ClassesFinderFactory {

	/**
	 * Creates a new configured ClassesFinder.
	 * 
	 * @param searchInJars
	 * @param filterPatterns
	 * @param suiteTypes
	 * @param baseTypes
	 * @param excludedBaseTypes
	 * @param classpathProperty
	 * @return
	 */
	public ClassesFinder create(boolean searchInJars,
			String[] filterPatterns, SuiteType[] suiteTypes,
			Class<?>[] baseTypes, Class<?>[] excludedBaseTypes,
			String classpathProperty) {
		ClassTester tester= new ClasspathSuiteTester(searchInJars,
				filterPatterns, suiteTypes, baseTypes, excludedBaseTypes);
		return new ClassesFinder(tester, classpathProperty);
	}

}
