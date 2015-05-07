/*
 * pixelle - Graphics algorithmic editor
 * Copyright (C) 2008 Dave Brosius
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
package com.mebigfatguy.pixelle.actions;

import java.awt.event.ActionEvent;
import java.awt.print.PrinterException;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

import com.mebigfatguy.pixelle.PixelleBundle;
import com.mebigfatguy.pixelle.PixelleFrame;

public class PrintAction extends AbstractAction {
	
	private static final long serialVersionUID = 5826553084556182790L;

	private PixelleFrame frame;
	
	public PrintAction(PixelleFrame pf) {
		super(PixelleBundle.getString(PixelleBundle.PRINT_ITEM));
		putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke('P', ActionEvent.CTRL_MASK));
		frame = pf;
	}
	
	@Override
	public void actionPerformed(ActionEvent ae) {
		try {
			frame.getPrinterJob().print();
		} catch (PrinterException e) {
			JOptionPane.showMessageDialog(frame, e.getMessage());
		}
	}
}
