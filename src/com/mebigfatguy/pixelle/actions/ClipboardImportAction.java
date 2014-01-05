/*
 * pixelle - Graphics algorithmic editor
 * Copyright (C) 2008-2014 Dave Brosius
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

import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

import com.mebigfatguy.pixelle.FrameMgr;
import com.mebigfatguy.pixelle.PixelleBundle;
import com.mebigfatguy.pixelle.PixelleFrame;
import com.mebigfatguy.pixelle.PixelleImage;
import com.mebigfatguy.pixelle.utils.GuiUtils;

public class ClipboardImportAction extends AbstractAction {

    private static final long serialVersionUID = -357508741723961505L;
    PixelleFrame frame;
    
    public ClipboardImportAction(PixelleFrame pf) {
        super(PixelleBundle.getString(PixelleBundle.CLIPBOARD_IMPORT_ITEM));
        putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke('I', ActionEvent.CTRL_MASK));
        frame = pf;
    }
    
    public void actionPerformed(ActionEvent e) {
        try {
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            Image imageContents = (Image)clipboard.getData(DataFlavor.imageFlavor);
            PixelleFrame pf = new PixelleFrame(new PixelleImage((BufferedImage) imageContents));
            Rectangle bounds = GuiUtils.getScreenBounds();
            bounds.grow(-200, -200);
            pf.setBounds(bounds);
            pf.setVisible(true);
            FrameMgr.getInstance().add(pf);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frame, ex.getClass().getSimpleName() + " " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}
