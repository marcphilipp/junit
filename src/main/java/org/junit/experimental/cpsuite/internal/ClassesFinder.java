/*
 * @author Johannes Link (business@johanneslink.net)
 * 
 * Published under GNU General Public License 2.0 (http://www.gnu.org/licenses/gpl.html)
 */
package org.junit.experimental.cpsuite.internal;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * Utility class to find classes within the class path, both inside and outside
 * of jar files. Inner and anonymous classes are not being considered in the
 * first place.
 * 
 * It's originally evolved out of ClassPathTestCollector in JUnit 3.8.1
 */
public class ClassesFinder {

	private static final int CLASS_SUFFIX_LENGTH= ".class".length();

	private static final String FALLBACK_CLASSPATH_PROPERTY= "java.class.path";

	private final ClassTester fTester;

	private final String fClasspathProperty;

	public ClassesFinder(ClassTester tester, String classpathProperty) {
		this.fTester= tester;
		this.fClasspathProperty= classpathProperty;
	}

	/*
	 * (non-Javadoc)
	 * @see org.junit.experimental.cpsuite.internal.ClassesFinder#find()
	 */
	public List<Class<?>> find() {
		return findClassesInClasspath(getClasspath());
	}

	private String getClasspath() {
		String classPath= System.getProperty(getClasspathProperty());
		if (classPath == null)
			classPath= System.getProperty(FALLBACK_CLASSPATH_PROPERTY);
		return classPath;
	}

	private List<Class<?>> findClassesInClasspath(String classPath) {
		return findClassesInRoots(splitClassPath(classPath));
	}

	private List<Class<?>> findClassesInRoots(List<String> roots) {
		List<Class<?>> classes= new ArrayList<Class<?>>(100);
		for (String root : roots) {
			gatherClassesInRoot(new File(root), classes);
		}
		return classes;
	}

	private void gatherClassesInRoot(File classRoot, List<Class<?>> classes) {
		FilenameIterator relativeFilenames= new NullFilenameIterator();
		if (fTester.searchInJars() && isJarFile(classRoot)) {
			try {
				relativeFilenames= new JarFilenameIterator(classRoot);
			} catch (IOException e) {
				// Don't iterate unavailable jar files
				e.printStackTrace();
			}
		} else if (classRoot.isDirectory()) {
			relativeFilenames= new RecursiveFilenameIterator(classRoot);
		}
		gatherClasses(classes, relativeFilenames);
	}

	private boolean isJarFile(File classRoot) {
		return classRoot.getName().endsWith(".jar")
				|| classRoot.getName().endsWith(".JAR");
	}

	private void gatherClasses(List<Class<?>> classes,
			FilenameIterator filenames) {
		for (String fileName : filenames) {
			if (!isClassFile(fileName)) {
				continue;
			}
			String className= classNameFromFile(fileName);
			if (!fTester.acceptClassName(className)) {
				continue;
			}
			if (!fTester.acceptInnerClass() && isInnerClass(className)) {
				continue;
			}
			try {
				Class<?> clazz= Class.forName(className);
				if (clazz == null || clazz.isLocalClass()
						|| clazz.isAnonymousClass()) {
					continue;
				}
				if (fTester.acceptClass(clazz)) {
					classes.add(clazz);
				}
			} catch (ClassNotFoundException cnfe) {
				// ignore not instantiable classes
			} catch (NoClassDefFoundError ncdfe) {
				// ignore not instantiable classes
			} catch (ExceptionInInitializerError ciie) {
				// ignore not instantiable classes
			} catch (UnsatisfiedLinkError ule) {
				// ignore not instantiable classes
			}
		}
	}

	private boolean isInnerClass(String className) {
		return className.contains("$");
	}

	private boolean isClassFile(String classFileName) {
		return classFileName.endsWith(".class");
	}

	private List<String> splitClassPath(String classPath) {
		final String separator= System.getProperty("path.separator");
		return Arrays.asList(classPath.split(separator));
	}

	private String classNameFromFile(String classFileName) {
		// convert /a/b.class to a.b
		String s= replaceFileSeparators(cutOffExtension(classFileName));
		if (s.startsWith("."))
			return s.substring(1);
		return s;
	}

	private String replaceFileSeparators(String s) {
		String result= s.replace(File.separatorChar, '.');
		if (File.separatorChar != '/') {
			// In Jar-Files it's always '/'
			result= result.replace('/', '.');
		}
		return result;
	}

	private String cutOffExtension(String classFileName) {
		return classFileName.substring(0, classFileName.length()
				- CLASS_SUFFIX_LENGTH);
	}

	public ClassTester getTester() {
		return fTester;
	}

	public String getClasspathProperty() {
		return fClasspathProperty;
	}

}
