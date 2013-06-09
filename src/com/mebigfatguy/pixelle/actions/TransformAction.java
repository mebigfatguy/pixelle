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

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.text.MessageFormat;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

import com.mebigfatguy.pixelle.ImageType;
import com.mebigfatguy.pixelle.PixelleBundle;
import com.mebigfatguy.pixelle.PixelleFrame;
import com.mebigfatguy.pixelle.PixelleImage;
import com.mebigfatguy.pixelle.PixelleTransformException;
import com.mebigfatguy.pixelle.PixelleTransformer;
import com.mebigfatguy.pixelle.dialogs.PixelleExpressionDialog;
import com.mebigfatguy.pixelle.dialogs.TransformExceptionDialog;

public class TransformAction extends AbstractAction {
	
	private static final long serialVersionUID = 3609590839274864129L;
	PixelleFrame frame;
	
	public TransformAction(PixelleFrame pf) {
		super(PixelleBundle.getString(PixelleBundle.TRANSFORM_ITEM));
		putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke('T', ActionEvent.CTRL_MASK));
		frame = pf;
	}
	
	public void actionPerformed(ActionEvent e) {
		try {
			PixelleExpressionDialog d = new PixelleExpressionDialog(frame);
			d.setLocationRelativeTo(frame);
			d.setModal(true);
			d.setVisible(true);
			if (d.isOK()) {
				ImageType imageType = d.getImageType();
				PixelleImage[] srcImages = d.getSourceImages();
				PixelleTransformer transformer = new PixelleTransformer(srcImages, d.getAlgorithms(imageType), imageType, d.getOutputSize());
				PixelleImage dstImage = transformer.transform();
				if (dstImage != null) {
					if (frame.createNewWindow()) {
						PixelleFrame f = new PixelleFrame(dstImage);
						f.setSize(frame.getSize());
						Point location = frame.getLocation();
						location.x += 20;
						location.y += 20;
						f.setLocation(location);
						f.setZoom(((PixelleFrame) frame).getZoom());
						String title = MessageFormat.format(PixelleBundle.getString(PixelleBundle.TITLE), PixelleBundle.getString(PixelleBundle.UNTITLED));
						f.setTitle(title);
						f.setVisible(true);
					} else
						frame.setImage(dstImage);
				}
			}
		} catch (PixelleTransformException pe) {
			pe.printStackTrace();
			TransformExceptionDialog d = new TransformExceptionDialog(pe);
			d.setModal(true);
			d.setLocationRelativeTo(frame);
			d.setVisible(true);
		}
	}
}