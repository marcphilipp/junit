package org.junit.experimental.cpsuite.internal;

import java.util.Iterator;

public abstract class FilenameIterator implements Iterable<String>, Iterator<String> {

	public Iterator<String> iterator() {
		return this;
	}

	public void remove() {
		throw new RuntimeException("Not implemented");
	}
	
}
