/*
 * pixelle - Graphics algorithmic editor
 * Copyright (C) 2008-2015 Dave Brosius
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package com.mebigfatguy.pixelle;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.mebigfatguy.pixelle.eval.PixelleEval3ByteBGR;
import com.mebigfatguy.pixelle.eval.PixelleEval4ByteABGR;
import com.mebigfatguy.pixelle.eval.PixelleEvalByteGray;
import com.mebigfatguy.pixelle.eval.PixelleEvalCustom;
import com.mebigfatguy.pixelle.eval.PixelleEvalIndexed;
import com.mebigfatguy.pixelle.eval.PixelleEvalIntARGB;
import com.mebigfatguy.pixelle.eval.PixelleEvalIntBGR;
import com.mebigfatguy.pixelle.eval.PixelleEvalIntRGB;

/**
 * factory for creating pixel evaluation instances based on the type of image that is
 * input.
 */
public class PixelleEvalFactory {

    public static final String PIXELLE = ".mebigfatguy/pixelle";
	private static IndexOutOfBoundsOption ioobOption = IndexOutOfBoundsOption.BorderColor;
	private static ColorOutOfBoundsOption coobOption = ColorOutOfBoundsOption.Clip;
	
	private PixelleEvalFactory() {
	}
	
	public static void loadSettings() {
        File f = new File(new File(System.getProperty("user.home"), PIXELLE), "pef.ser");
	    try (ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream(f)))) {
	        ioobOption = (IndexOutOfBoundsOption) ois.readObject();
	        ioobOption.setColor((Color) ois.readObject());
	        coobOption = (ColorOutOfBoundsOption) ois.readObject();
	    } catch (Exception e) {  
	    }
	}
	
	public static void saveSettings() {
	    File f = new File(System.getProperty("user.home"), PIXELLE);
	    f.mkdir();
	    f = new File(f, "pef.ser");
	    try (ObjectOutputStream oos = new ObjectOutputStream( new BufferedOutputStream(new FileOutputStream(f)))){

    	    oos.writeObject(ioobOption);
    	    oos.writeObject(ioobOption.getColor());
    	    oos.writeObject(coobOption);
	    } catch (Exception e) {   
	    }
	}
	
	public static IndexOutOfBoundsOption getIndexOutOfBoundsOption() {
		return ioobOption;
	}

	public static void setIndexOutOfBoundsOption(IndexOutOfBoundsOption option) {
		ioobOption = option;
	}
	
	public static ColorOutOfBoundsOption getColorOutOfBoundsOption() {
		return coobOption;
	}

	public static void setColorOutOfBoundsOption(ColorOutOfBoundsOption option) {
		coobOption = option;
	}
	
	/**
	 * factory method for creating a pixel evaluator for the type of image that is
	 * passed in.
	 * 
	 * @param image the image that will need to be evaluated
	 * @return the pixel evaluator
	 */
	public static PixelleEval create(PixelleImage image) {
		
		switch (image.getType()) {
			case BufferedImage.TYPE_3BYTE_BGR:
				return new PixelleEval3ByteBGR(image, ioobOption, coobOption);
			
			case BufferedImage.TYPE_4BYTE_ABGR:
				return new PixelleEval4ByteABGR(image, ioobOption, coobOption);
			
			case BufferedImage.TYPE_BYTE_BINARY:
				throw new IllegalArgumentException("Image type: " + image.getType() + " (Byte Binary) is not supported yet.");
			
			case BufferedImage.TYPE_BYTE_INDEXED:
				return new PixelleEvalIndexed(image, ioobOption, coobOption);
			
			case BufferedImage.TYPE_BYTE_GRAY:
				return new PixelleEvalByteGray(image, ioobOption, coobOption);
			
			case BufferedImage.TYPE_INT_ARGB:
				return new PixelleEvalIntARGB(image, ioobOption, coobOption);
			
			case BufferedImage.TYPE_INT_BGR:
				return new PixelleEvalIntBGR(image, ioobOption, coobOption);
			
			case BufferedImage.TYPE_INT_RGB:
				return new PixelleEvalIntRGB(image, ioobOption, coobOption);
				
			case BufferedImage.TYPE_CUSTOM:
				return new PixelleEvalCustom(image, ioobOption, coobOption);
				
			default:
			    return new PixelleEval4ByteABGR(image, ioobOption, coobOption);
		}
	}
}
