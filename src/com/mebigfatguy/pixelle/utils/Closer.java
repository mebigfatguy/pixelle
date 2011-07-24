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

import java.io.Closeable;
import java.io.IOException;

public final class Closer {
	private Closer() {
	}
	
	public static void close(Closeable c) {
		try {
			if (c != null) {
				c.close();
			}
		} catch (IOException ioe) {
		}
	}
}
