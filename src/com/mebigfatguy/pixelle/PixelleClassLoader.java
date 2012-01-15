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
package com.mebigfatguy.pixelle;

/**
 * a dynamic class loader for holding generated pixel evaluation classes that
 * are derived from PixelleEval. An instance of this class loader only exists
 * as long as it takes to transform the bitmap.
 */
public class PixelleClassLoader extends ClassLoader {
	/**
	 * constructs a new dynamic class loader derived from a parent loader as per
	 * the classloader delegation paradigm.
	 * 
	 * @param parent the parent class loader
	 */
	public PixelleClassLoader(ClassLoader parent) {
        super(parent);
    }
    
	/** 
	 * adds a new dynamic class to the class loader
	 * 
	 * @param name the name of the class to add
	 * @param byteCode the data that represents the bytecode of the class
	 */
    public void addClass(String name, byte[] byteCode) {
        defineClass(name, byteCode, 0, byteCode.length);
    }
}
