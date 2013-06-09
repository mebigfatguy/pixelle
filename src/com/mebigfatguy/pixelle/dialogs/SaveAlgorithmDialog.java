/*
 * pixelle - Graphics algorithmic editor
 * Copyright (C) 2008-2013 Dave Brosius
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
package com.mebigfatguy.pixelle.dialogs;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import com.mebigfatguy.pixelle.AlgorithmArchiver;
import com.mebigfatguy.pixelle.ImageType;
import com.mebigfatguy.pixelle.PixelleBundle;
import com.mebigfatguy.pixelle.PixelleFrame;
import com.mebigfatguy.pixelle.utils.GuiUtils;

public class SaveAlgorithmDialog extends JDialog {	
	private static final long serialVersionUID = -6991592080722431427L;
	
	private final JLabel[] labels = new JLabel[] {
		new JLabel(PixelleBundle.getString(PixelleBundle.PIXEL_GROUP)),
		new JLabel(PixelleBundle.getString(PixelleBundle.PIXEL_ALGORITHM))
	};
	
	private final ImageType imageType;
	private JComboBox groupBox;
	private JTextField nameField;
	private JButton ok;
	private JButton cancel;
	private boolean okClicked = false;
	
	public SaveAlgorithmDialog(PixelleFrame owner, ImageType imageOutputType) {
		super(owner, PixelleBundle.getString(PixelleBundle.SAVE_ALGORITHM));
		imageType = imageOutputType;
		initComponents();
		initListeners();
	}
	
	public boolean isOK() {
		return okClicked;
	}
	
	public String getGroup() {
		return (String)groupBox.getSelectedItem();
	}
	
	@Override
	public String getName() {
		return nameField.getText();
	}
	
	private void initComponents() {
		Container cp = getContentPane();
		cp.setLayout(new BoxLayout(cp, BoxLayout.Y_AXIS));
		
		String[] groups = AlgorithmArchiver.getArchiver().getUserGroups(imageType);
		groupBox = new JComboBox(new DefaultComboBoxModel(groups));
		groupBox.setEditable(true);
		nameField = new JTextField(10);
		GuiUtils.sizeUniformly(GuiUtils.Sizing.Both, labels);
		GuiUtils.sizeUniformly(GuiUtils.Sizing.Both, groupBox, nameField);
		
		JComponent[] fields = new JComponent[] {groupBox, nameField};
		cp.add(Box.createVerticalStrut(20));
		for (int i = 0; i < labels.length; i++) {
			JPanel p = new JPanel();
			p.setBorder(BorderFactory.createEmptyBorder(2, 30, 2, 30));
			p.setLayout(new BoxLayout(p, BoxLayout.X_AXIS));
			p.add(Box.createHorizontalGlue());
			p.add(labels[i]);
			p.add(Box.createHorizontalStrut(10));
			p.add(fields[i]);
			labels[i].setLabelFor(fields[i]);
			p.add(Box.createHorizontalGlue());
			cp.add(p);
			cp.add(Box.createVerticalStrut(10));
		}
		
		JPanel p = new JPanel();
		p.setBorder(BorderFactory.createEmptyBorder(2, 30, 20, 30));
		p.setLayout(new BoxLayout(p, BoxLayout.X_AXIS));
		ok = new JButton(PixelleBundle.getString(PixelleBundle.OK));
		cancel = new JButton(PixelleBundle.getString(PixelleBundle.CANCEL));
		GuiUtils.sizeUniformly(GuiUtils.Sizing.Both, ok, cancel);
		p.add(Box.createHorizontalGlue());
		p.add(ok);
		p.add(Box.createHorizontalStrut(10));
		p.add(cancel);
		p.add(Box.createHorizontalGlue());

		cp.add(p);
		pack();
	}
	
	private void initListeners() {
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent we) {
				dispose();
			}
		});
		
		ok.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				okClicked = true;
				dispose();
			}
		});
		
		cancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				dispose();
			}
		});
	}
}
