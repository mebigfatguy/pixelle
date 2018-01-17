/*
 * pixelle - Graphics algorithmic editor
 * Copyright (C) 2008-2018 Dave Brosius
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

import java.util.EnumSet;
import java.util.Set;

/**
 * an enum that represents a part of an rgb bitmap
 */
public enum PixelleComponent {
	/** the red component of a pixel */
	RED('r'),
	/** the green component of a pixel */
	GREEN('g'),
	/** the blue component of a pixel */
	BLUE('b'),
	/** the black component of a pixel */
	BLACK('k'),
	/** the transparency component of a pixel */
	TRANSPARENCY('t'),
	/** the selection status of a pixel */
	SELECTION('s');
	
	private char pixelSpec;
	
	PixelleComponent(char c) {
		pixelSpec = c;
	}
	
	public char getPixelSpec() {
		return pixelSpec;
	}
	
	public static Set<PixelleComponent> rgbValues() {
		Set<PixelleComponent> components = EnumSet.<PixelleComponent>allOf(PixelleComponent.class);
		components.remove(PixelleComponent.BLACK);
		return components;
	}
	
	public static Set<PixelleComponent> gsValues() {
		Set<PixelleComponent> components = EnumSet.<PixelleComponent>noneOf(PixelleComponent.class);
		components.add(PixelleComponent.BLACK);
		components.add(PixelleComponent.SELECTION);
		return components;
	}
	
	@Override
	public String toString() {
		return PixelleBundle.getString("title." + name().toLowerCase());
	}
}
