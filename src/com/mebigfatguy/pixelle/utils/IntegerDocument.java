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
package com.mebigfatguy.pixelle.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;


public class IntegerDocument extends PlainDocument
{
	private static final long serialVersionUID = -4300414460486882975L;
	
	private static final Pattern INTEGERPATTERN = Pattern.compile("-?[0-9]*");
    
    @Override
    public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
        String start = getText(0, offs);
        String end = getText(offs, this.getLength() - offs);
        String newIntegerString = (start + str + end).trim();
        
        Matcher m = INTEGERPATTERN.matcher(newIntegerString);
        if (m.matches()) {
            super.insertString(offs, str, a);
        }
    }
}