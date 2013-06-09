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

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.swing.JFrame;

/** 
 * manages the set of open windows
 */
public class FrameMgr {
	
	private static FrameMgr mgr = new FrameMgr();
	public Set<JFrame> frames = new LinkedHashSet<JFrame>();
	
	private FrameMgr() {}
	
	/**
	 * return the singleton frame manager
	 */
	public static FrameMgr getInstance() {
		return mgr;
	}
	
	/** 
	 * add a window to the frame manager
	 * 
	 * @param frame the new window that is about to be shown
	 */
	public void add(JFrame frame) {
		frames.add(frame);
	}
	
	/**
	 * removes the window from the frame manager. When the last
	 * window is remove, the program exits.
	 * 
	 * @param frame the window to remove
	 */
	public void remove(JFrame frame) {
		frames.remove(frame);
		if (frames.isEmpty())
			System.exit(0);
	}
	
	/**
	 * retrieves all the active windows currently on the desktop
	 * 
	 * @return a list of window available
	 */
	public Set<JFrame> getFrames() {
		return new HashSet<JFrame>(frames);
	}
}
