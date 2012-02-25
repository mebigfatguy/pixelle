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

import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.Locale;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.filechooser.FileFilter;

import com.mebigfatguy.pixelle.FrameMgr;
import com.mebigfatguy.pixelle.PixelleBundle;
import com.mebigfatguy.pixelle.PixelleFrame;
import com.mebigfatguy.pixelle.PixelleTransformException;
import com.mebigfatguy.pixelle.utils.GuiUtils;

public class OpenFileAction extends AbstractAction {
	
	private static final long serialVersionUID = -143429629947905056L;
	PixelleFrame frame;
	File lastDir = null;
	
	public OpenFileAction(PixelleFrame pf) {
		super(PixelleBundle.getString(PixelleBundle.OPEN_ITEM));
		putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke('O', ActionEvent.CTRL_MASK));
		frame = pf;
	}
	
	public void actionPerformed(ActionEvent e) {
		JFileChooser jf = new JFileChooser(lastDir);
		jf.setFileFilter(new FileFilter() {

			@Override
			public boolean accept(File f) {
				if (f.isDirectory())
					return true;
				
				String path = f.getPath().toLowerCase(Locale.getDefault());
				String[] formats = ImageIO.getReaderFormatNames();
				for (String format : formats) {
					if (path.endsWith(format.toLowerCase(Locale.getDefault())))
						return true;
				}
				return false;
			}

			@Override
			public String getDescription() {
				return PixelleBundle.getString(PixelleBundle.GRAPHIC_FILES);
			}
		});
		
		jf.setFileSelectionMode(JFileChooser.FILES_ONLY);
		jf.showOpenDialog(frame);
		File f = jf.getSelectedFile();
		if (f != null) {
			try {
				lastDir = f.getParentFile();
				PixelleFrame pf = new PixelleFrame();
				pf.openFile(f);
				Rectangle bounds = GuiUtils.getScreenBounds();
	            bounds.grow(-200, -200);
	            pf.setBounds(bounds);
				pf.setVisible(true);
				FrameMgr.getInstance().add(pf);
			} catch (PixelleTransformException pte) {
				JOptionPane.showMessageDialog(frame, pte.getMessage());
				pte.printStackTrace();
			}			
		}
		
	}
}
