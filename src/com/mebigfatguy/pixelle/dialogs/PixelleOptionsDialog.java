/*
 * pixelle - Graphics algorithmic editor
 * Copyright (C) 2008-2010 Dave Brosius
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
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JPanel;

import com.mebigfatguy.pixelle.ColorOutOfBoundsOption;
import com.mebigfatguy.pixelle.IndexOutOfBoundsOption;
import com.mebigfatguy.pixelle.PixelleBundle;
import com.mebigfatguy.pixelle.PixelleFrame;
import com.mebigfatguy.pixelle.utils.GuiUtils;

public class PixelleOptionsDialog extends JDialog {

	private static final long serialVersionUID = -2593224982080676492L;
	
	private JCheckBox colorBox = new JCheckBox(PixelleBundle.getString(PixelleBundle.STATIC_COLOR));
	private JCheckBox borderColorBox = new JCheckBox(PixelleBundle.getString(PixelleBundle.BORDER_COLOR));
	private JCheckBox wrappedColorBox = new JCheckBox(PixelleBundle.getString(PixelleBundle.WRAPPED_COLOR));
	private ColorButton colorButton = new ColorButton(Color.WHITE);
	
	private JCheckBox clipBox = new JCheckBox(PixelleBundle.getString(PixelleBundle.CLIP_COLOR));
	private JCheckBox rollBox = new JCheckBox(PixelleBundle.getString(PixelleBundle.ROLL_COLOR));
	private JCheckBox waveBox = new JCheckBox(PixelleBundle.getString(PixelleBundle.WAVE_COLOR));
	
	private JButton ok = new JButton(PixelleBundle.getString(PixelleBundle.OK));
	private JButton cancel = new JButton(PixelleBundle.getString(PixelleBundle.CANCEL));
	private boolean okClicked = false;
	private Color color;
	private IndexOutOfBoundsOption ioobOption;
	private ColorOutOfBoundsOption coobOption;
	
	public PixelleOptionsDialog(PixelleFrame owner, IndexOutOfBoundsOption ioption, ColorOutOfBoundsOption coption) {
		super(owner, PixelleBundle.getString(PixelleBundle.PIXEL_OPTIONS));
		ioobOption = ioption;
		color = ioobOption.getColor();
		coobOption = coption;
		initComponents();
		initListeners();
		pack();
	}
	
	public boolean isOK() {
		if (clipBox.isSelected())
			coobOption = ColorOutOfBoundsOption.Clip;
		else if (rollBox.isSelected())
			coobOption = ColorOutOfBoundsOption.Roll;
		else
			coobOption = ColorOutOfBoundsOption.Wave;
		
		if (colorBox.isSelected()) {
			ioobOption = IndexOutOfBoundsOption.SpecifiedColor;
			ioobOption.setColor(color);
		}
		else if (borderColorBox.isSelected())
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
		cp.setLayout(new BoxLayout(cp, BoxLayout.Y_AXIS));
		
		cp.add(Box.createVerticalStrut(5));

		{
			JPanel p = new JPanel();
			p.setLayout(new GridLayout(3, 1));
			p.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), PixelleBundle.getString(PixelleBundle.COLOR_OUT_OF_BOUNDS_PIXELS)));

			GuiUtils.sizeUniformly(GuiUtils.Sizing.Height, clipBox, rollBox, waveBox);
			
			p.add(clipBox);
			p.add(rollBox);
			p.add(waveBox);
			
			ButtonGroup g = new ButtonGroup();
			g.add(clipBox);
			g.add(rollBox);
			g.add(waveBox);
			
			switch (coobOption) {
				case Clip:
					clipBox.setSelected(true);
				break;
				
				case Roll:
					rollBox.setSelected(true);
				break;
				
				case Wave:
					waveBox.setSelected(true);
				break;
			}
			
			cp.add(p);
		}
	
		cp.add(Box.createVerticalStrut(5));
		cp.add(Box.createVerticalGlue());
		
		{
			JPanel p = new JPanel();
			p.setLayout(new GridLayout(3, 1));
			p.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), PixelleBundle.getString(PixelleBundle.INDEX_OUT_OF_BOUNDS_PIXELS)));
			
			GuiUtils.sizeUniformly(GuiUtils.Sizing.Height, colorBox, colorButton, borderColorBox, wrappedColorBox);
			
			colorButton.setColor(color);
			
			JPanel colorP = new JPanel();
			colorP.setLayout(new BoxLayout(colorP, BoxLayout.X_AXIS));
			colorP.add(colorBox);
			colorP.add(Box.createHorizontalStrut(5));
			colorP.add(colorButton);
			colorP.add(Box.createHorizontalGlue());
			
			p.add(colorP);
			p.add(borderColorBox);
			p.add(wrappedColorBox);
			
			ButtonGroup g = new ButtonGroup();
			g.add(colorBox);
			g.add(borderColorBox);
			g.add(wrappedColorBox);
			
			switch (ioobOption) {
				case SpecifiedColor:
					colorBox.setSelected(true);
				break;
				
				case BorderColor:
					borderColorBox.setSelected(true);
				break;
				
				case WrapColor:
					wrappedColorBox.setSelected(true);
				break;
			}
			
			cp.add(p);
		}
		
		cp.add(Box.createVerticalStrut(5));
		cp.add(Box.createVerticalGlue());
		
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
			cp.add(p);
		}
		
		cp.add(Box.createVerticalStrut(5));
	}
	
	private void initListeners() {
		getRootPane().setDefaultButton(ok);

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
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent we) {
				dispose();
			}
		});
		
		colorButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				colorBox.setSelected(true);
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
