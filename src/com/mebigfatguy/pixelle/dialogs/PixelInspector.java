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
package com.mebigfatguy.pixelle.dialogs;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Point;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.mebigfatguy.pixelle.PixelleBundle;
import com.mebigfatguy.pixelle.PixelleFrame;
import com.mebigfatguy.pixelle.utils.GuiUtils;
import com.mebigfatguy.pixelle.utils.GuiUtils.Sizing;

public class PixelInspector extends JFrame {
	
	private static final long serialVersionUID = 713537909520529431L;
	
	private final PixelleFrame frame;
	private JPanel swatchPanel;
	private JTextField xField;
	private JTextField yField;
	private JTextField colorField;
	private JTextField transparencyField;
	private final StringBuilder sb = new StringBuilder();
	
	public PixelInspector(PixelleFrame pf) {
		frame = pf;
		setTitle(PixelleBundle.getString(PixelleBundle.INSPECTOR_ITEM));
		initComponents();
		initListeners();
	}
	
	public void setInspectorPosition(final Point p) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				xField.setText(String.valueOf(p.x));
				yField.setText(String.valueOf(p.y));
			}
		});
	}
	
	public void setInspectorColor(final Color c, final int transparency) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				sb.setLength(0);
				sb.append(c.getRed());
				sb.append(",");
				sb.append(c.getGreen());
				sb.append(",");
				sb.append(c.getBlue());
				colorField.setText(sb.toString());
				transparencyField.setText(String.valueOf(transparency));
				swatchPanel.setBackground(c);
				swatchPanel.repaint();
			}
		});
	}
	private void initComponents() {
		Container cp = getContentPane();
		cp.setLayout(new BorderLayout(4, 4));
		
		swatchPanel = new JPanel();
		swatchPanel.setPreferredSize(new Dimension(50, 50));
		swatchPanel.setToolTipText(PixelleBundle.getString(PixelleBundle.INSPECTOR_TOOLTIP));
		cp.add(swatchPanel, BorderLayout.WEST);
		
		JPanel infoPanel = new JPanel();
		infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
		
		JLabel xLabel = new JLabel(PixelleBundle.getString(PixelleBundle.X));
		JLabel yLabel = new JLabel(PixelleBundle.getString(PixelleBundle.Y));
		JLabel colorLabel = new JLabel(PixelleBundle.getString(PixelleBundle.COLOR));
		JLabel transparencyLabel = new JLabel(PixelleBundle.getString(PixelleBundle.TRANSPARENCY));
		GuiUtils.sizeUniformly(Sizing.Both, xLabel, yLabel, colorLabel, transparencyLabel);
		
		xField = new JTextField(7);
		yField = new JTextField(7);
		colorField = new JTextField(7);
		transparencyField = new JTextField(7);
		GuiUtils.sizeUniformly(Sizing.Both, xField, yField, colorField, transparencyField);
		
		xField.setEditable(false);
		yField.setEditable(false);
		colorField.setEditable(false);
		transparencyField.setEditable(false);
		
		xLabel.setLabelFor(xField);
		yLabel.setLabelFor(yField);
		colorLabel.setLabelFor(colorField);
		transparencyLabel.setLabelFor(transparencyField);
		
		JPanel xPanel = new JPanel();
		xPanel.setLayout(new BoxLayout(xPanel, BoxLayout.X_AXIS));
		xPanel.add(xLabel);
		xPanel.add(Box.createHorizontalStrut(10));
		xPanel.add(xField);		
		JPanel yPanel = new JPanel();
		yPanel.setLayout(new BoxLayout(yPanel, BoxLayout.X_AXIS));
		yPanel.add(yLabel);
        yPanel.add(Box.createHorizontalStrut(10));
		yPanel.add(yField);
		JPanel colorPanel = new JPanel();
		colorPanel.setLayout(new BoxLayout(colorPanel, BoxLayout.X_AXIS));
		colorPanel.add(colorLabel);
		colorPanel.add(Box.createHorizontalStrut(10));
		colorPanel.add(colorField);
		JPanel transparencyPanel = new JPanel();
		transparencyPanel.setLayout(new BoxLayout(transparencyPanel, BoxLayout.X_AXIS));
		transparencyPanel.add(transparencyLabel);
		transparencyPanel.add(Box.createHorizontalStrut(10));
		transparencyPanel.add(transparencyField);
		
		infoPanel.add(xPanel);
		infoPanel.add(yPanel);
		infoPanel.add(colorPanel);
		infoPanel.add(transparencyPanel);
		
		cp.add(infoPanel, BorderLayout.CENTER);
		pack();
	}
	
	private void initListeners() {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent we) {
				frame.toggleInspector();
			}
		});
	}
}
