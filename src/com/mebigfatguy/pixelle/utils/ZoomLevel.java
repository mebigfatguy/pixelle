/*
 * pixelle - Graphics algorithmic editor
 * Copyright (C) 2008-2010 Dave Brosius
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

public enum ZoomLevel {
	OneEighth("menu.view.one_eighth", 1.0/8.0),
	OneFourth("menu.view.one_fourth", 1.0/4.0),
	OneHalf("menu.view.one_half", 1.0/2.0),
	FullSize("menu.view.full_size", 1.0),
	Double("menu.view.double", 2.0),
	FourTimes("menu.view.four_times", 4.0),
	EightTimes("menu.view.eight_times", 8.0),
	FitToWindow("menu.view.fit_to_window", 0.0);
	
	private String zoomKey;
	double zoomFrac;
	
	ZoomLevel(String key, double zoom) {
		zoomKey = key;
		zoomFrac = zoom;
	}
	
	public double getZoom() {
		return zoomFrac;
	}
	
	public String getKey() {
		return zoomKey;
	}
}
