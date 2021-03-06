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

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFrame;
import javax.swing.KeyStroke;

import com.mebigfatguy.pixelle.FrameMgr;
import com.mebigfatguy.pixelle.PixelleBundle;

public class CloseFileAction extends AbstractAction {

	private static final long serialVersionUID = -6860345801345095089L;
	JFrame frame;
	
	public CloseFileAction(JFrame pf) {
		super(PixelleBundle.getString(PixelleBundle.CLOSE_ITEM));
		putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke('W', ActionEvent.CTRL_MASK));
		frame = pf;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		frame.dispose();
		FrameMgr.getInstance().remove(frame);
	}
}
