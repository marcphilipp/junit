/*
 * @author Johannes Link (business@johanneslink.net)
 * 
 * Published under GNU General Public License 2.0 (http://www.gnu.org/licenses/gpl.html)
 */
package org.junit.experimental.cpsuite.internal;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.NoSuchElementException;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * This class provides an iterator over all file names in a jar file.
 * Directories are not considered to be files.
 */
public class JarFilenameIterator extends FilenameIterator {

	private Enumeration<JarEntry> fEntries;

	private JarEntry fNext;

	public JarFilenameIterator(File jarFile) throws IOException {
		JarFile jar= new JarFile(jarFile);
		fEntries= jar.entries();
		retrieveNextElement();
	}

	private void retrieveNextElement() {
		fNext= null;
		while (fEntries.hasMoreElements()) {
			fNext= fEntries.nextElement();
			if (!fNext.isDirectory()) {
				break;
			}
		}
	}

	public boolean hasNext() {
		return fNext != null;
	}

	public String next() {
		if (fNext == null) {
			throw new NoSuchElementException();
		}
		String value= fNext.getName();
		retrieveNextElement();
		return value;
	}

}
