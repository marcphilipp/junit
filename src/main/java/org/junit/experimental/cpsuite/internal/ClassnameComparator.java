package org.junit.experimental.cpsuite.internal;

import java.util.Comparator;

public class ClassnameComparator implements Comparator<Class<?>> {
	
	public int compare(Class<?> o1, Class<?> o2) {
		return o1.getName().compareTo(o2.getName());
	}
	
}