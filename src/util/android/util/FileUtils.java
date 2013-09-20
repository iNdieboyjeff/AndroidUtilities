/**
 * Copyright © 2013 Jeff Sutton.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package util.android.util;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class FileUtils {
	/**
	 * Open a file at <i>pathname</i> and read it's contents into a String.
	 * 
	 * @param pathname
	 * @param characterEncoding
	 * @return String
	 * @throws IOException
	 */
	public static String readFile(String pathname, String characterEncoding) throws IOException {

		File file = new File(pathname);
		StringBuilder fileContents = new StringBuilder((int) file.length());
		Scanner scanner = new Scanner(file, characterEncoding);
		String lineSeparator = System.getProperty("line.separator");

		try {
			while (scanner.hasNextLine()) {
				fileContents.append(scanner.nextLine() + lineSeparator);
			}
			return fileContents.toString();
		} finally {
			scanner.close();
		}
	}

	/**
	 * Open a file at <i>pathname</i> and read it's contents into a String. This method assumes a UTF-8 character
	 * encoding.
	 * 
	 * @param pathname
	 * @return String
	 * @throws IOException
	 */
	public static String readFile(String pathname) throws IOException {
		return readFile(pathname, "UTF-8");
	}

	public static void touch(File file) throws IOException {
		if (!file.exists()) {
			File parent = file.getParentFile();
			if (parent != null)
				if (!parent.exists())
					if (!parent.mkdirs())
						throw new IOException("Cannot create parent directories for file: " + file);

			file.createNewFile();
		}

		boolean success = file.setLastModified(System.currentTimeMillis());
		if (!success)
			throw new IOException("Unable to set the last modification time for " + file);
	}
}
