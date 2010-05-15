/*
 * @author Johannes Link (business@johanneslink.net)
 * 
 * Published under GNU General Public License 2.0 (http://www.gnu.org/licenses/gpl.html)
 */
package org.junit.experimental.cpsuite.internal;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * This class provides an iterator over all file names in a directory and its
 * subdirectories. The filenames are given relative to the root. Directories are
 * not considered to be files.
 */
public class RecursiveFilenameIterator extends FilenameIterator {

	private List<RecursiveFilenameIterator> fInnerIterators;

	private int fPrefixLength;

	private File fRoot;

	private boolean fAlreadyUsed= false;

	private int fIndex= 0;

	public RecursiveFilenameIterator(File root) {
		this(root, root.getAbsolutePath().length() + 1);
	}

	private RecursiveFilenameIterator(File root, int prefixLength) {
		this.fRoot= root;
		this.fPrefixLength= prefixLength;
		if (!isRootFile()) {
			fInnerIterators= getInnerIterators(root);
		}
	}

	private boolean isRootFile() {
		return this.fRoot.isFile();
	}

	private List<RecursiveFilenameIterator> getInnerIterators(File root) {
		List<RecursiveFilenameIterator> iterators= new ArrayList<RecursiveFilenameIterator>();
		for (File each : root.listFiles()) {
			iterators.add(new RecursiveFilenameIterator(each, fPrefixLength));
		}
		return iterators;
	}

	public boolean hasNext() {
		if (isRootFile()) {
			return !fAlreadyUsed;
		}
		if (fIndex >= fInnerIterators.size()) {
			return false;
		}
		if (currentIterator().hasNext()) {
			return true;
		}
		fIndex++;
		return hasNext();
	}

	private FilenameIterator currentIterator() {
		return fInnerIterators.get(fIndex);
	}

	public String next() {
		if (isRootFile()) {
			if (fAlreadyUsed) {
				throw new NoSuchElementException();
			}
			fAlreadyUsed= true;
			return fRoot.getAbsolutePath().substring(fPrefixLength);
		}
		if (hasNext()) {
			return currentIterator().next();
		}
		throw new NoSuchElementException();
	}

}
