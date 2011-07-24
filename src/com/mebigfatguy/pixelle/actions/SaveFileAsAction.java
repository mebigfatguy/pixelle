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
import java.text.MessageFormat;
import java.util.Locale;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.filechooser.FileFilter;

import com.mebigfatguy.pixelle.PixelleBundle;
import com.mebigfatguy.pixelle.PixelleFrame;

public class SaveFileAsAction extends AbstractAction {
	
	private static final long serialVersionUID = 548030636198842553L;
	 
	private final PixelleFrame frame;

	public SaveFileAsAction(PixelleFrame pf) {
		super(PixelleBundle.getString(PixelleBundle.SAVEAS_ITEM));
		putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke('A', ActionEvent.CTRL_MASK));
		frame = pf;
	}
	
	public void actionPerformed(ActionEvent e) {
		JFileChooser fc = new JFileChooser();
		fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fc.setFileFilter(new FileFilter() {
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
				return "Graphic Files";
			}
		});

		if (JFileChooser.APPROVE_OPTION == fc.showSaveDialog(frame)) {
			try {
				File f = fc.getSelectedFile();
				if (!f.exists() || askSaveFile(f)) {
					String ext = f.getPath();
					ext = ext.substring(ext.lastIndexOf('.')+1);
					ImageIO.write(frame.getImage().getSaveImage(), ext, f);
				}
			} catch (IOException ioe) {
				JOptionPane.showMessageDialog(frame, ioe.getMessage());
				ioe.printStackTrace();
			}
		}
	}
	
	private boolean askSaveFile(File f) {
		String title = MessageFormat.format(PixelleBundle.getString(PixelleBundle.TITLE), f.getName());
		String message = MessageFormat.format(PixelleBundle.getString(PixelleBundle.SAVE_OVERWRITE), f.getPath());
		int choice = JOptionPane.showConfirmDialog(frame, message, title, JOptionPane.YES_NO_OPTION);
		return choice == JOptionPane.OK_OPTION;
	}
}
