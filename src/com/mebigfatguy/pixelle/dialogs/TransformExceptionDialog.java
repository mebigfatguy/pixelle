package com.mebigfatguy.pixelle.dialogs;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JTextPane;
import javax.swing.border.Border;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import com.mebigfatguy.pixelle.PixelleBundle;
import com.mebigfatguy.pixelle.PixelleTransformException;

public class TransformExceptionDialog extends JDialog {

    private static final Border BORDER = BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10), 
                                            BorderFactory.createCompoundBorder(BorderFactory.createEtchedBorder(), BorderFactory.createEmptyBorder(10, 10, 10, 10)));
            

    public TransformExceptionDialog(PixelleTransformException ex) {
        try {
            setTitle(PixelleBundle.getString(PixelleBundle.FAILED_TRANFORMATION));
            setLayout(new BorderLayout());
            
            JTextPane pane = new JTextPane();
            pane.setMinimumSize(new Dimension(500, 80));
            pane.setPreferredSize(new Dimension(500, 80));
            StyledDocument doc = pane.getStyledDocument();

            doc.insertString(0, ex.getComponent(), null);
            doc.insertString(doc.getLength(), "\n", null);

            int pos = ex.getPosition();
            if (pos < 0) {
                doc.insertString(doc.getLength(), ex.getAlgorithm(), null);
            } else {
                Style style = pane.addStyle("ERROR", null);
                StyleConstants.setBackground(style, Color.red);
                
                doc.insertString(doc.getLength(), ex.getAlgorithm().substring(0, pos), null);
                doc.insertString(doc.getLength(), ex.getAlgorithm().substring(pos, pos+1), style);
                doc.insertString(doc.getLength(), ex.getAlgorithm().substring(pos+1), null);
                doc.insertString(doc.getLength(), "\n", null);  
            }

            pane.setEditable(false);
            pane.setBorder(BORDER);
            add(pane, BorderLayout.CENTER);
            pack();
        } catch (BadLocationException e) { 
        }
    }
}
