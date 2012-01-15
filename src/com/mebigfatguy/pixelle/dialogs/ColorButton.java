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
package com.mebigfatguy.pixelle.dialogs;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JButton;

public class ColorButton extends JButton {
	
	private static final long serialVersionUID = -8845705944739341059L;
	
	private Color color;
	
	public ColorButton(Color clr) {
		color = clr;
	}
	
	public void setColor(Color clr) {
		color = clr;
	}
	
	@Override
	public void paintComponent(Graphics g) {
		Color saveColor = g.getColor();
		try {
			g.setColor(color);
			g.fillRect(0, 0, getWidth(), getHeight());
			g.setColor(Color.BLACK);
			g.drawRect(0, 0, getWidth(), getHeight());
		}
		finally {
			g.setColor(saveColor);
		}
	}
}
