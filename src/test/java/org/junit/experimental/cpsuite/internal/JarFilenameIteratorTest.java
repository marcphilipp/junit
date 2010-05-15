package org.junit.experimental.cpsuite.internal;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.Iterator;

import org.junit.Test;

public class JarFilenameIteratorTest {

	@Test
	public void recursiveRoot() throws Exception {
		Iterator<String> i= new JarFilenameIterator(new File("lib/mytests.jar"));
		assertTrue(i.hasNext());
		assertNotNull(i.next()); // Manifest-File
		assertNotNull(i.next());
		assertNotNull(i.next());
		assertNotNull(i.next());
		assertNotNull(i.next());
		assertFalse(i.hasNext());
	}
}
