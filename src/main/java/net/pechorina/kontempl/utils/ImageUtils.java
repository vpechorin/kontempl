package net.pechorina.kontempl.utils;

import net.pechorina.kontempl.exceptions.BadImageException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.FileImageInputStream;
import javax.imageio.stream.FileImageOutputStream;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import static org.imgscalr.Scalr.Method;
import static org.imgscalr.Scalr.OP_ANTIALIAS;
import static org.imgscalr.Scalr.OP_BRIGHTER;
import static org.imgscalr.Scalr.resize;

public class ImageUtils {

	static final Logger logger = LoggerFactory.getLogger(ImageUtils.class);
	
	public static String getImageExtension(File imgFile) {
		int pos = imgFile.getName().lastIndexOf(".");
		if (pos == -1) return null;
        return imgFile.getName().substring(pos + 1);
	}
	
	/**
	 * Gets image dimensions for given file
	 * 
	 * @param imgFile
	 *            image file
	 * @return dimensions of image
	 * @throws BadImageException if the file is not a known image or has incorrect extension or lack of it
	 */
	public static Dimension getImageDimension(File imgFile) throws BadImageException {
		String suffix = getImageExtension(imgFile);
		if (suffix == null) {
			logger.warn("Image file has no extension: " + imgFile.getName());
			throw new BadImageException("Image file has no extension: " + imgFile.getName());
		}

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
				throw new BadImageException("Error reading image file: " + imgFile.getAbsolutePath() + ", Exception: " + e);
			} finally {
				reader.dispose();
			}
		}

		throw new BadImageException("Not a known image file: "
				+ imgFile.getAbsolutePath());
	}
	
	public static String convertImageToJpeg(File imgFile, String suffix, String path) throws BadImageException {
		int pos = imgFile.getName().lastIndexOf(".");
		String existingName = imgFile.getName();
		String newName = existingName.substring(0, pos) + ".jpg";
		logger.debug("new name: " + newName);
		String newPath = path + File.separator + newName;
		logger.debug("new Path: " + newPath);
		BufferedImage image = null;
		ImageOutputStream outStream = null;
		Iterator<ImageWriter> iter = ImageIO.getImageWritersByFormatName("jpeg");
		ImageWriter writer = null;
		if (iter.hasNext()) writer = iter.next();
		if (writer == null) {
			logger.error("No JPEG image writers - fatal error");
			throw new BadImageException("No JPEG image writers found");
		}
		
		// adjust JPEG output quality
		ImageWriteParam iwp = writer.getDefaultWriteParam();
		iwp.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
		iwp.setCompressionQuality(0.95f);
		iwp.setProgressiveMode(ImageWriteParam.MODE_DEFAULT);
		
		try {
			outStream = new FileImageOutputStream(new File(newPath));
			image = ImageIO.read(imgFile);
			writer.setOutput(outStream);
			IIOImage outImage = new IIOImage(image, null, null);
			writer.write(null, outImage, iwp);
		} catch (IOException e) {
			logger.warn("Error reading -> writing: " + imgFile.getAbsolutePath(), e);
			throw new BadImageException("Error transcoding image file: " + imgFile.getAbsolutePath() + ", Exception: " + e);
        } finally {
        	writer.dispose();
        	try {
                assert outStream != null;
                outStream.close();
			} catch (IOException e) {
				logger.error("Cannot close output stream: " + e);
			}
        }
		return newName;
	}
	
	public static BufferedImage createThumbnail(BufferedImage img, int targetSize) {
		// Create quickly, then smooth and brighten it.
		img = resize(img, Method.ULTRA_QUALITY, targetSize, OP_ANTIALIAS, OP_BRIGHTER);

		// Let's add a little border before we return result.
		// return pad(img, 4);
		return img;
	}
}
