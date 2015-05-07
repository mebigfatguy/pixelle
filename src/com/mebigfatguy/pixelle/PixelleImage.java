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

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.IndexColorModel;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;

/**
 * a representation of an image to manipulate. Encapsulates two BufferedImages, one
 * to represent the real image, and the second to represent selection.
 */
public class PixelleImage implements Printable {

	private final BufferedImage image;
	private final BufferedImage selection;
	private final BufferedImage selectionOutline;
	private final Composite composite;

	public PixelleImage() {
		this(new BufferedImage(400, 400, BufferedImage.TYPE_3BYTE_BGR));
	}

	public PixelleImage(BufferedImage img) {
		image = img;

		byte[] wb = new byte[] {-1, 0};
		byte[] alpha = new byte[] {0, -1};
		IndexColorModel model = new IndexColorModel(1, 2, wb, wb, wb, alpha);
		selection = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_BYTE_BINARY, model);
		composite = AlphaComposite.getInstance(AlphaComposite.XOR, 1.0f);
		Graphics g = selection.getGraphics();
		try {
    		g.setColor(Color.WHITE);
    		g.fillRect(0, 0, getWidth(), getHeight());
    		selectionOutline = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_BYTE_BINARY, model);
    		g = selectionOutline.getGraphics();
    		g.setColor(Color.WHITE);
    		g.fillRect(0, 0, getWidth(), getHeight());	
    	} finally {
    	    g.dispose();
    	}
	}
	
	@Override
	public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
		graphics.drawImage(image, 0, 0, 0, 0, image.getWidth(), image.getHeight(), image.getWidth(), image.getHeight(), null);
		return PAGE_EXISTS;
	}

	public BufferedImage getSaveImage() {
		return image;
	}

	public int getType() {
		return image.getType();
	}

	public final int getWidth() {
		return image.getWidth();
	}

	public final int getHeight() {
		return image.getHeight();
	}

	public DataBuffer getBuffer() {
		return image.getRaster().getDataBuffer();
	}

	public void draw(Graphics g, int left, int top, int width, int height) {
		g.drawImage(image, left, top, width, height, null);
		if (g instanceof Graphics2D) {
			Graphics2D g2d = (Graphics2D)g;
			Composite saveComposite = g2d.getComposite();
			try {
				g2d.setComposite(composite);
				g.drawImage(selectionOutline, left, top, width, height, null);
			} finally {
				g2d.setComposite(saveComposite);
			}
		}
	}

	public int getSelectionByte(int xByteOffset, int y) {
		DataBuffer buffer = selection.getRaster().getDataBuffer();
		int byteWidth = (getWidth() + 7) >> 3;
		return buffer.getElem(y * byteWidth + xByteOffset);
	}

	public void setSelectionByte(int xByteOffset, int y, int byteValue) {
		DataBuffer buffer = selection.getRaster().getDataBuffer();
		int byteWidth = (getWidth() + 7) >> 3;
		buffer.setElem(y * byteWidth + xByteOffset, byteValue);
	}

	public void finishImage() {
		SelectionOutliner so = new SelectionOutliner();
		so.createOutline(selection, selectionOutline);
	}
}
