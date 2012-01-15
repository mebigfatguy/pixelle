/*
 * pixelle - Graphics algorithmic editor
 * Copyright (C) 2008-2012 Dave Brosius
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
package com.mebigfatguy.pixelle.utils;

import java.awt.Dimension;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;

import javax.swing.JComponent;

public class GuiUtils {
	public enum Sizing {Width, Height, Both}
	
	private GuiUtils() {}
	
	public static Rectangle getScreenBounds() {
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice gd = ge.getDefaultScreenDevice();
		GraphicsConfiguration gc = gd.getDefaultConfiguration();
		return gc.getBounds();
	}
	
	public static void sizeUniformly(Sizing sizing, JComponent... components) {
		int width = 0;
		int height = 0;
		
		for (JComponent comp : components) {
			Dimension d = comp.getPreferredSize();
			if (d.width > width)
				width = d.width;
			if (d.height > height)
				height = d.height;
		}
		
		for (JComponent comp : components) {
			Dimension minD = comp.getMinimumSize();
			if (sizing != Sizing.Height)
				minD.width = width;
			if (sizing != Sizing.Width)
				minD.height = height;
			comp.setMinimumSize(minD);
			
			Dimension maxD = comp.getMaximumSize();
			if (sizing != Sizing.Height)
				maxD.width = width;
			if (sizing != Sizing.Width)
				maxD.height = height;
			comp.setMaximumSize(maxD);
			
			Dimension prefD = comp.getPreferredSize();
			if (sizing != Sizing.Height)
				prefD.width = width;
			if (sizing != Sizing.Width)
				prefD.height = height;
			comp.setPreferredSize(prefD);
		}
	}
}
