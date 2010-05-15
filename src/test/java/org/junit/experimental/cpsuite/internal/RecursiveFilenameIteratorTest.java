package org.junit.experimental.cpsuite.internal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.Iterator;
import java.util.NoSuchElementException;

import org.junit.Test;
import org.junit.experimental.cpsuite.ClasspathSuite;

public class RecursiveFilenameIteratorTest {

	protected Iterator<String> createFileIterator(String path) {
		return new RecursiveFilenameIterator(new File("./bin/"
				+ ClasspathSuite.class.getPackage().getName().replace('.', '/')
				+ "/tests/" + path));
	}

	private void assertNextFilename(Iterator<String> i, String expectedName) {
		assertTrue(i.hasNext());
		String filename= i.next();
		assertEquals(expectedName, filename);
	}

	@Test(expected= NoSuchElementException.class)
	public void emptyRoot() {
		Iterator<String> i= createFileIterator("p/emptysubdir");
		assertFalse(i.hasNext());
		i.next();
	}

	@Test(expected= NoSuchElementException.class)
	public void rootWithEmptySubDirectory() {
		Iterator<String> i= createFileIterator("p");
		assertFalse(i.hasNext());
		i.next();
	}

	@Test(expected= NoSuchElementException.class)
	public void threeFilesInRoot() {
		Iterator<String> i= createFileIterator("p1");
		assertNextFilename(i, "P1NoTest$InnerTest.class");
		assertNextFilename(i, "P1NoTest.class");
		assertNextFilename(i, "P1Test.class");
		assertFalse(i.hasNext());
		i.next();
	}

	@Test
	public void recursiveRoot() {
		Iterator<String> i= createFileIterator("");
		assertTrue(i.hasNext());
		assertNextFilename(i, "ju38/JU38AbstractTest.class");
		assertNextFilename(i, "ju38/JU38ConcreteTest.class");
		assertNextFilename(i, "ju38/JU38TestWithoutBase.class");
		assertNextFilename(i, "p1/P1NoTest$InnerTest.class");
		assertNextFilename(i, "p1/P1NoTest.class");
		assertNextFilename(i, "p1/P1Test.class");
		assertNextFilename(i, "p2/AbstractP2Test.class");
		assertNextFilename(i, "p2/ConcreteP2Test.class");
		assertFalse(i.hasNext());
	}

}
