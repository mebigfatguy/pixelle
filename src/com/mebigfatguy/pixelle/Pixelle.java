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

import java.awt.Rectangle;
import java.text.MessageFormat;

import javax.swing.JOptionPane;

import com.mebigfatguy.pixelle.utils.GuiUtils;

/**
 * the main class of the program

 */
public class Pixelle {

	/**
	 * the main method of the program
	 * 
	 * @param args command line arguments
	 */
	public static void main(String[] args) {
		try {
		    PixelleEvalFactory.loadSettings();
			PixelleFrame pf = new PixelleFrame();
			String title = MessageFormat.format(PixelleBundle.getString(PixelleBundle.TITLE), PixelleBundle.getString(PixelleBundle.UNTITLED));
			pf.setTitle(title);
			Rectangle bounds = GuiUtils.getScreenBounds();
			bounds.grow(-200, -200);
			pf.setBounds(bounds);
			pf.setVisible(true);
		} catch (PixelleTransformException pte) {
			JOptionPane.showMessageDialog(null, pte.getMessage());
			pte.printStackTrace();
		}
	}
}
