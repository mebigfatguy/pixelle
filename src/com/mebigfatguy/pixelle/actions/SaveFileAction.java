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
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

import com.mebigfatguy.pixelle.PixelleBundle;
import com.mebigfatguy.pixelle.PixelleFrame;

public class SaveFileAction extends AbstractAction {
	
	private static final long serialVersionUID = -8339987202623985741L;
	
	private final PixelleFrame frame;
	private final SaveFileAsAction saveAsAction;

	public SaveFileAction(PixelleFrame pf, SaveFileAsAction action) {
		super(PixelleBundle.getString(PixelleBundle.SAVE_ITEM));
		putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke('S', ActionEvent.CTRL_MASK));
		frame = pf;
		saveAsAction = action;
	}
	
	public void actionPerformed(ActionEvent e) {
		try {
			File f = frame.getImageFile();
			if (f == null)
				saveAsAction.actionPerformed(e);
			else {
				String ext = f.getName();
				ext = ext.substring(ext.lastIndexOf('.') + 1);
				ImageIO.write(frame.getImage().getSaveImage(), ext, f);
			}
		} catch (IOException ioe) {
			ioe.printStackTrace();
			JOptionPane.showMessageDialog(frame, ioe.getMessage());
		}
	}
}
