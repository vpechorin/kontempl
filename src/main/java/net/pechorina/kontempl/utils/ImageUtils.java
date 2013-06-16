package net.pechorina.kontempl.utils;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.FileImageInputStream;
import javax.imageio.stream.ImageInputStream;

import org.apache.log4j.Logger;
import static org.imgscalr.Scalr.*;

public class ImageUtils {

	private static final Logger logger = Logger.getLogger(ImageUtils.class);

	/**
	 * Gets image dimensions for given file
	 * 
	 * @param imgFile
	 *            image file
	 * @return dimensions of image
	 * @throws IOException
	 *             if the file is not a known image
	 */
	public static Dimension getImageDimension(File imgFile) throws IOException {
		int pos = imgFile.getName().lastIndexOf(".");
		if (pos == -1)
			throw new IOException("No extension for file: "
					+ imgFile.getAbsolutePath());
		String suffix = imgFile.getName().substring(pos + 1);
		Iterator<ImageReader> iter = ImageIO.getImageReadersBySuffix(suffix);
		if (iter.hasNext()) {
			ImageReader reader = iter.next();
			try {
				ImageInputStream stream = new FileImageInputStream(imgFile);
				reader.setInput(stream);
				int width = reader.getWidth(reader.getMinIndex());
				int height = reader.getHeight(reader.getMinIndex());
				return new Dimension(width, height);
			} catch (IOException e) {
				logger.warn("Error reading: " + imgFile.getAbsolutePath(), e);
			} finally {
				reader.dispose();
			}
		}

		throw new IOException("Not a known image file: "
				+ imgFile.getAbsolutePath());
	}
	
	public static BufferedImage createThumbnail(BufferedImage img, int targetSize) {
		// Create quickly, then smooth and brighten it.
		img = resize(img, Method.ULTRA_QUALITY, targetSize, OP_ANTIALIAS, OP_BRIGHTER);

		// Let's add a little border before we return result.
		// return pad(img, 4);
		return img;
	}
}
