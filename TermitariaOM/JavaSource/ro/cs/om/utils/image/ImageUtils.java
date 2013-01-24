/*******************************************************************************
 * This file is part of Termitaria, a project management tool 
 *  Copyright (C) 2008-2013 CodeSphere S.R.L., www.codesphere.ro
 *   
 *  Termitaria is free software; you can redistribute it and/or 
 *  modify it under the terms of the GNU Affero General Public License 
 *  as published by the Free Software Foundation; either version 3 of 
 *  the License, or (at your option) any later version.
 *  
 *  This program is distributed in the hope that it will be useful, 
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of 
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the 
 *  GNU Affero General Public License for more details.
 *  
 *  You should have received a copy of the GNU Affero General Public License 
 *  along with Termitaria. If not, see  <http://www.gnu.org/licenses/> .
 ******************************************************************************/
package ro.cs.om.utils.image;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.FileImageOutputStream;

import ro.cs.om.common.ApplicationObjectSupport;
import sun.awt.image.ImageFormatException;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

/**
 * @author dan.damian
 *
 */
public class ImageUtils extends ApplicationObjectSupport {

	private static ImageUtils theInstance = null;
	
	static{
		theInstance = new ImageUtils();
	}
	private ImageUtils() {}
	
	public static ImageUtils getInstance() {
		return theInstance;
	}

	public void writeImageToFile(BufferedImage input, String fileName) throws IOException {
		logger.debug("writeImageToFile " + fileName);
		Iterator iter = ImageIO.getImageWritersByFormatName("JPG");
		if (iter.hasNext()) {
			ImageWriter writer = (ImageWriter) iter.next();
			ImageWriteParam iwp = writer.getDefaultWriteParam();
			iwp.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
			iwp.setCompressionQuality(0.95f);
			File outFile = new File(fileName);
			FileImageOutputStream output = new FileImageOutputStream(outFile);
			writer.setOutput(output);
			IIOImage image =
				new IIOImage(input, null, null);
			writer.write(null, image, iwp);
			output.close();
		}
	}



	public byte[] bufferedImageToByteArray(BufferedImage img) throws ImageFormatException, IOException{	
		logger.debug("bufferedImageToByteArray");
		ByteArrayOutputStream os = new ByteArrayOutputStream();		
		JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(os);		
		encoder.encode(img);		
		return os.toByteArray();		
	}

	public byte[] createResizedCopy(byte[] originalImageBytes, int scaledWidth, int scaledHeight) throws IOException, ImageFormatException {
		logger.debug("createResizedCopy");
		BufferedInputStream bio = new BufferedInputStream(new ByteArrayInputStream(originalImageBytes));
		Image originalImage = ImageIO.read(bio);
		if (originalImage.getWidth(null) > scaledWidth || originalImage.getHeight(null) > scaledHeight) {
			BufferedImage scaledBI = new BufferedImage(scaledWidth, scaledHeight, BufferedImage.TYPE_INT_RGB);
			Graphics2D g = scaledBI.createGraphics();
			g.setComposite(AlphaComposite.Src);
			g.drawImage(originalImage, 0, 0, scaledWidth, scaledHeight, null);
			g.dispose();
			return bufferedImageToByteArray(scaledBI);
		} else {
			return originalImageBytes;
		}
	}
	
	public int[] getImageDimensions(byte[] imageBytes) throws IOException {
		logger.debug("getImageDimensions - start");
		BufferedInputStream bio = new BufferedInputStream(new ByteArrayInputStream(imageBytes));
		Image img = ImageIO.read(bio);
		
		int[] dimension = new int[2];
		dimension[0] = img.getWidth(null);
		dimension[1] = img.getHeight(null);
		
		logger.debug("Width: " + dimension[0] + " Height: " + dimension[1]);
		
		logger.debug("getImageDimensions - end");
		
		return dimension;		
	}
}
