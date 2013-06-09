/*
 * pixelle - Graphics algorithmic editor
 * Copyright (C) 2008-2013 Dave Brosius
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

/**
 * an enum that represents what to do when an index specified is outside the coordinates
 * of the source bitmap.
 */
public enum IndexOutOfBoundsOption {
	/** use the specified color */
	SpecifiedColor,
	/** use the closest border color */
	BorderColor,
	/** calculate a new index using the modulus function of the width or height */
	WrapColor;
	
	private Color color = Color.WHITE;

	/**
	 * retrieves the color that is used for when the enum is 'SpecifiedColor'
	 * 
	 * @return the specified color
	 */
	public Color getColor() {
		return color;
	}

	/**
	 * sets the color that is used for when the enum is 'SpecifiedColor'
	 * 
	 * @param clr the specified color
	 */
	public void setColor(Color clr) {
		color = clr;
	}
}
