/*
 * pixelle - Graphics algorithmic editor
 * Copyright (C) 2008-2019 Dave Brosius
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

import java.awt.Color;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.EmptyBorder;

import com.mebigfatguy.pixelle.ColorOutOfBoundsOption;
import com.mebigfatguy.pixelle.IndexOutOfBoundsOption;
import com.mebigfatguy.pixelle.PixelleBundle;
import com.mebigfatguy.pixelle.utils.GuiUtils;

public class PixelleOptionsDialog extends JDialog {

	private static final long serialVersionUID = -2593224982080676492L;
	
	private JRadioButton colorRadio = new JRadioButton(PixelleBundle.getString(PixelleBundle.STATIC_COLOR));
	private JRadioButton borderColorRadio = new JRadioButton(PixelleBundle.getString(PixelleBundle.BORDER_COLOR));
	private JRadioButton wrappedColorRadio = new JRadioButton(PixelleBundle.getString(PixelleBundle.WRAPPED_COLOR));
	private ColorButton colorButton = new ColorButton(Color.WHITE);
	
	private JRadioButton clipRadio = new JRadioButton(PixelleBundle.getString(PixelleBundle.CLIP_COLOR));
	private JRadioButton rollRadio = new JRadioButton(PixelleBundle.getString(PixelleBundle.ROLL_COLOR));
	private JRadioButton waveRadio = new JRadioButton(PixelleBundle.getString(PixelleBundle.WAVE_COLOR));
	
	private JButton ok = new JButton(PixelleBundle.getString(PixelleBundle.OK));
	private JButton cancel = new JButton(PixelleBundle.getString(PixelleBundle.CANCEL));
	private boolean okClicked = false;
	private Color color;
	private IndexOutOfBoundsOption ioobOption;
	private ColorOutOfBoundsOption coobOption;
	
	public PixelleOptionsDialog(JFrame owner, IndexOutOfBoundsOption ioption, ColorOutOfBoundsOption coption) {
		super(owner, PixelleBundle.getString(PixelleBundle.PIXEL_OPTIONS));
		ioobOption = ioption;
		color = ioobOption.getColor();
		coobOption = coption;
		initComponents();
		initListeners();
		pack();
	}
	
	public boolean isOK() {
		if (clipRadio.isSelected())
			coobOption = ColorOutOfBoundsOption.Clip;
		else if (rollRadio.isSelected())
			coobOption = ColorOutOfBoundsOption.Roll;
		else
			coobOption = ColorOutOfBoundsOption.Wave;
		
		if (colorRadio.isSelected()) {
			ioobOption = IndexOutOfBoundsOption.SpecifiedColor;
			ioobOption.setColor(color);
		}
		else if (borderColorRadio.isSelected())
			ioobOption = IndexOutOfBoundsOption.BorderColor;
		else
			ioobOption = IndexOutOfBoundsOption.WrapColor;
		return okClicked;
	}
	
	public IndexOutOfBoundsOption getIndexOutOfBoundsOption() {
		return ioobOption;
	}
	
	public ColorOutOfBoundsOption getColorOutOfBoundsOption() {
		return coobOption;
	}
	
	private void initComponents() {
		Container cp = getContentPane();
		
		JPanel mainPanel = new JPanel();
		mainPanel.setBorder(new EmptyBorder(10, 10, 5, 10));
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		
		mainPanel.add(Box.createVerticalStrut(5));

		{
			JPanel p = new JPanel();
			p.setLayout(new GridLayout(3, 1));
			p.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), PixelleBundle.getString(PixelleBundle.COLOR_OUT_OF_BOUNDS_PIXELS)));

			GuiUtils.sizeUniformly(GuiUtils.Sizing.Height, clipRadio, rollRadio, waveRadio);
			
			p.add(clipRadio);
			p.add(rollRadio);
			p.add(waveRadio);
			
			ButtonGroup g = new ButtonGroup();
			g.add(clipRadio);
			g.add(rollRadio);
			g.add(waveRadio);
			
			switch (coobOption) {
				case Clip:
					clipRadio.setSelected(true);
				break;
				
				case Roll:
					rollRadio.setSelected(true);
				break;
				
				case Wave:
					waveRadio.setSelected(true);
				break;
			}
			
			mainPanel.add(p);
		}
	
		mainPanel.add(Box.createVerticalStrut(5));
		mainPanel.add(Box.createVerticalGlue());
		
		{
			JPanel p = new JPanel();
			p.setLayout(new GridLayout(3, 1));
			p.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), PixelleBundle.getString(PixelleBundle.INDEX_OUT_OF_BOUNDS_PIXELS)));
			
			GuiUtils.sizeUniformly(GuiUtils.Sizing.Height, colorRadio, colorButton, borderColorRadio, wrappedColorRadio);
			
			colorButton.setColor(color);
			
			JPanel colorP = new JPanel();
			colorP.setLayout(new BoxLayout(colorP, BoxLayout.X_AXIS));
			colorP.add(colorRadio);
			colorP.add(Box.createHorizontalStrut(5));
			colorP.add(colorButton);
			colorP.add(Box.createHorizontalGlue());
			
			p.add(colorP);
			p.add(borderColorRadio);
			p.add(wrappedColorRadio);
			
			ButtonGroup g = new ButtonGroup();
			g.add(colorRadio);
			g.add(borderColorRadio);
			g.add(wrappedColorRadio);
			
			switch (ioobOption) {
				case SpecifiedColor:
					colorRadio.setSelected(true);
				break;
				
				case BorderColor:
					borderColorRadio.setSelected(true);
				break;
				
				case WrapColor:
					wrappedColorRadio.setSelected(true);
				break;
			}
			
			mainPanel.add(p);
		}
		
		mainPanel.add(Box.createVerticalStrut(5));
		mainPanel.add(Box.createVerticalGlue());
		
		{
			JPanel p = new JPanel();
			p.setLayout(new BoxLayout(p, BoxLayout.X_AXIS));
			p.setBorder(BorderFactory.createEmptyBorder(4, 40, 4, 40));
			p.add(Box.createHorizontalGlue());
			
			GuiUtils.sizeUniformly(GuiUtils.Sizing.Both, ok, cancel);
			p.add(ok);
			p.add(Box.createHorizontalStrut(10));
			p.add(cancel);
			p.add(Box.createHorizontalGlue());
			mainPanel.add(p);
		}
		
		mainPanel.add(Box.createVerticalStrut(5));
		
		cp.add(mainPanel);
	}
	
	private void initListeners() {
		getRootPane().setDefaultButton(ok);

		ok.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				okClicked = true;
				dispose();
			}
		});
		
		cancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				dispose();
			}
		});
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent we) {
				dispose();
			}
		});
		
		colorButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				colorRadio.setSelected(true);
				Color chosenColor = JColorChooser.showDialog(PixelleOptionsDialog.this, PixelleBundle.getString(PixelleBundle.PICK_COLOR), color);
				if (chosenColor != null) {
					color = chosenColor;
					colorButton.setColor(chosenColor);
					colorButton.repaint();
				}
			}
		});
	}
}
