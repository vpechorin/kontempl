package net.pechorina.kontempl.utils;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileUtils {
	private static final Logger logger = LoggerFactory
			.getLogger(FileUtils.class);

	public static boolean deleteFileByName(String fileName) {
		logger.debug("Delete file: " + fileName);
		if (fileName == null) {
			logger.warn("missed filename");
			return false;
		}

		boolean success = false;
		try {
			File file = new File(fileName);

			if (file.delete()) {
				logger.debug(file.getName() + " is deleted!");
				success = true;

			} else {
				logger.error(file.getName()
						+ " - file delete operation failed.");
			}

		} catch (Exception e) {
			logger.error(fileName + " - file delete operation failed: " + e);
		}

		return success;
	}

	/**
	 * Force deletion of directory
	 * 
	 * @param path
	 * @return
	 */
	public static boolean deleteDirectory(File path) {
		if (path.exists()) {
			File[] files = path.listFiles();
			for (int i = 0; i < files.length; i++) {
				if (files[i].isDirectory()) {
					deleteDirectory(files[i]);
				} else {
					files[i].delete();
				}
			}
		}
		return (path.delete());
	}
}
