/*
 * @author Johannes Link (business@johanneslink.net)
 * 
 * Published under GNU General Public License 2.0 (http://www.gnu.org/licenses/gpl.html)
 */
package org.junit.experimental.cpsuite.internal;

import java.util.NoSuchElementException;

public class NullFilenameIterator extends FilenameIterator {

	public boolean hasNext() {
		return false;
	}

	public String next() {
		throw new NoSuchElementException();
	}

}
