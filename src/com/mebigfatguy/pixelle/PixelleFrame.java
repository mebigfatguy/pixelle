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
package com.mebigfatguy.pixelle;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;

import javax.imageio.ImageIO;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.mebigfatguy.pixelle.actions.ClipboardImportAction;
import com.mebigfatguy.pixelle.actions.CloseFileAction;
import com.mebigfatguy.pixelle.actions.InspectorAction;
import com.mebigfatguy.pixelle.actions.NewFileAction;
import com.mebigfatguy.pixelle.actions.OpenFileAction;
import com.mebigfatguy.pixelle.actions.OptionsAction;
import com.mebigfatguy.pixelle.actions.PageSetupAction;
import com.mebigfatguy.pixelle.actions.PrintAction;
import com.mebigfatguy.pixelle.actions.QuitAction;
import com.mebigfatguy.pixelle.actions.SaveFileAction;
import com.mebigfatguy.pixelle.actions.SaveFileAsAction;
import com.mebigfatguy.pixelle.actions.TransformAction;
import com.mebigfatguy.pixelle.actions.TransformNewWindowAction;
import com.mebigfatguy.pixelle.actions.ZoomAction;
import com.mebigfatguy.pixelle.dialogs.PixelInspector;
import com.mebigfatguy.pixelle.utils.GuiUtils;
import com.mebigfatguy.pixelle.utils.ZoomLevel;

public class PixelleFrame extends JFrame {

	private static final long serialVersionUID = -2993532609001467136L;
	private static int pixelleFrameCount = 1;
	
	JMenu fileMenu;
	JMenuItem newItem;
	JMenuItem openItem;
	JMenuItem clipboardItem;
	JMenuItem closeItem;
	JMenuItem saveItem;
	JMenuItem saveAsItem;
	JMenuItem pageSetupItem;
	JMenuItem printItem;
	JMenuItem quitItem;
	
	JMenu viewMenu;
	JCheckBoxMenuItem oneEighthItem;
	JCheckBoxMenuItem oneFourthItem;
	JCheckBoxMenuItem oneHalfItem;
	JCheckBoxMenuItem fullSizeItem;
	JCheckBoxMenuItem twoTimesItem;
	JCheckBoxMenuItem fourTimesItem;
	JCheckBoxMenuItem eightTimesItem;
	JCheckBoxMenuItem fitToWindowItem;
	
	JMenu transformMenu;
	JCheckBoxMenuItem transformNewWindowItem;
	JMenuItem optionsItem;
	JMenuItem transformItem;
	
	JMenu goodiesMenu;
	JCheckBoxMenuItem inspectorItem;
	
	PrinterJob printerJob;
	
	File imageFile;
	JScrollPane scroll;
	ImagePanel panel;
	transient PixelleImage image;
	PixelInspector inspector;
	boolean doNewWindow;
	int pixelleFrameId;
	
	public PixelleFrame() throws PixelleTransformException {
		this(new PixelleTransformer(new PixelleImage[] {new PixelleImage()}, PixelleTransformer.getSampleTransform(), ImageType.RGB, new Point(400, 400)).transform(), false);
	}
	
	public PixelleFrame(PixelleImage srcImage) {
		this(srcImage, true);
	}
	
	private PixelleFrame(PixelleImage srcImage, boolean transformInNewWindow) {
		pixelleFrameId = pixelleFrameCount++;
		imageFile = null;
		image = srcImage;
		doNewWindow = transformInNewWindow;
		initComponents();
		initListeners();
		
		printerJob = PrinterJob.getPrinterJob();
		printerJob.setPrintable(image);
		
		FrameMgr.getInstance().add(this);
	}
	
	private void initComponents() {
		JMenuBar mb = new JMenuBar();
		
		fileMenu = new JMenu(PixelleBundle.getString(PixelleBundle.FILE_MENU));
		newItem = new JMenuItem(new NewFileAction(this));
		fileMenu.add(newItem);
		openItem = new JMenuItem(new OpenFileAction(this));
		fileMenu.add(openItem);
		clipboardItem = new JMenuItem(new ClipboardImportAction(this));
		fileMenu.add(clipboardItem);
		fileMenu.addSeparator();
		closeItem = new JMenuItem(new CloseFileAction(this));
		fileMenu.add(closeItem);
		fileMenu.addSeparator();
		saveAsItem = new JMenuItem(new SaveFileAsAction(this));
		saveItem = new JMenuItem(new SaveFileAction(this, (SaveFileAsAction)saveAsItem.getAction()));
		fileMenu.add(saveItem);
		fileMenu.add(saveAsItem);
		fileMenu.addSeparator();
		pageSetupItem = new JMenuItem(new PageSetupAction(this));
		fileMenu.add(pageSetupItem);
		printItem = new JMenuItem(new PrintAction(this));
		fileMenu.add(printItem);
		fileMenu.addSeparator();
		quitItem = new JMenuItem(new QuitAction());
		fileMenu.add(quitItem);
		
		mb.add(fileMenu);
		
		viewMenu = new JMenu(PixelleBundle.getString(PixelleBundle.VIEW_MENU));
		oneEighthItem = new JCheckBoxMenuItem(new ZoomAction(this, ZoomLevel.OneEighth));
		viewMenu.add(oneEighthItem);
		oneFourthItem = new JCheckBoxMenuItem(new ZoomAction(this, ZoomLevel.OneFourth));
		viewMenu.add(oneFourthItem);
		oneHalfItem = new JCheckBoxMenuItem(new ZoomAction(this, ZoomLevel.OneHalf));
		viewMenu.add(oneHalfItem);
		fullSizeItem = new JCheckBoxMenuItem(new ZoomAction(this, ZoomLevel.FullSize));
		viewMenu.add(fullSizeItem);
		twoTimesItem = new JCheckBoxMenuItem(new ZoomAction(this, ZoomLevel.Double));
		viewMenu.add(twoTimesItem);
		fourTimesItem = new JCheckBoxMenuItem(new ZoomAction(this, ZoomLevel.FourTimes));
		viewMenu.add(fourTimesItem);
		eightTimesItem = new JCheckBoxMenuItem(new ZoomAction(this, ZoomLevel.EightTimes));
		viewMenu.add(eightTimesItem);
		viewMenu.addSeparator();
		fitToWindowItem = new JCheckBoxMenuItem(new ZoomAction(this, ZoomLevel.FitToWindow));
		viewMenu.add(fitToWindowItem);
		
		ButtonGroup bg = new ButtonGroup();
		bg.add(oneEighthItem);
		bg.add(oneFourthItem);
		bg.add(oneHalfItem);
		bg.add(fullSizeItem);
		bg.add(twoTimesItem);
		bg.add(fourTimesItem);
		bg.add(eightTimesItem);
		bg.add(fitToWindowItem);
		
		fitToWindowItem.setSelected(true);
		
		mb.add(viewMenu);
		
		transformMenu = new JMenu(PixelleBundle.getString(PixelleBundle.TRANSFORM_MENU));
		transformNewWindowItem = new JCheckBoxMenuItem(new TransformNewWindowAction(this));
		transformNewWindowItem.setSelected(doNewWindow);
		transformMenu.add(transformNewWindowItem);
		optionsItem = new JMenuItem(new OptionsAction(this));
		transformMenu.add(optionsItem);
		transformMenu.addSeparator();
		transformItem = new JMenuItem(new TransformAction(this));
		transformMenu.add(transformItem);
		
		mb.add(transformMenu);
		
		goodiesMenu = new JMenu(PixelleBundle.getString(PixelleBundle.GOODIES_MENU));
		inspectorItem = new JCheckBoxMenuItem(new InspectorAction(this));
		goodiesMenu.add(inspectorItem);
		
		mb.add(goodiesMenu);
		
		setJMenuBar(mb);
		
		Container cp = getContentPane();
		cp.setLayout(new BorderLayout());

		panel = new ImagePanel();
		scroll = new JScrollPane(panel);
		cp.add(scroll, BorderLayout.CENTER);
		setBounds(GuiUtils.getScreenBounds());
		
		setZoom(ZoomLevel.FitToWindow);
		FrameMgr.getInstance().add(this);
		
	}
	
	public boolean createNewWindow() {
		return doNewWindow;
	}
	
	public void toggleNewWindowOption() {
		doNewWindow = !doNewWindow;
		transformNewWindowItem.setSelected(doNewWindow);
	}
	
	private void initListeners() {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent we) {
				dispose();
				FrameMgr.getInstance().remove(PixelleFrame.this);
			}
		});
	}
	
	public PixelleImage getImage() {
		return image;
	}
	
	public void setImage(PixelleImage img) {
		image = img;
		panel.invalidate();
		panel.revalidate();
		panel.repaint();
	}
	
	public File getImageFile() {
		return imageFile;
	}
	
	public void openFile(File f) {
		String title = MessageFormat.format(PixelleBundle.getString(PixelleBundle.TITLE), f.getName());
		setTitle(title);
		try {
		    image = new PixelleImage(ImageIO.read(f));
		    imageFile = f;
		    EventQueue.invokeLater(new Runnable() {
		    	@Override
				public void run() {
		    		Container cp = getContentPane();
		    		cp.removeAll();
		    		setZoom(ZoomLevel.FitToWindow);
		    		scroll = new JScrollPane(panel);
		    		cp.add(scroll, BorderLayout.CENTER);
		    		doNewWindow = true;
		    		transformNewWindowItem.setSelected(true);
				    invalidate();
				    validate();
				    repaint();
		    	}
		    });
		    
		} catch (IOException e) {
			JOptionPane.showMessageDialog(this, e.getMessage());
		}
	}
	
	public ZoomLevel getZoom() {
	    return panel.getZoom();
	}
	
	public final void setZoom(final ZoomLevel zoom) {
		panel.setZoom(zoom);
		panel.invalidate();
		panel.validate();
		panel.repaint();				
	}
	
	public final void toggleInspector() {
		if (inspector == null) {
			inspector = new PixelInspector(this);
			inspector.setAlwaysOnTop(true);
			Rectangle bounds = getBounds();
			Point p = new Point(bounds.x + bounds.width - inspector.getWidth() - 20, bounds.y + 50);
			inspector.setLocation(p);
			inspector.setVisible(true);
			inspectorItem.setSelected(true);
		} else {
			inspector.dispose();
			inspector = null;
			inspectorItem.setSelected(false);
		}
	}
	
	public PrinterJob getPrinterJob() {
		return printerJob;
	}
	
	@Override
	public String getTitle() {
		String title;
		if (imageFile == null)
			title = PixelleBundle.getString(PixelleBundle.UNTITLED);
		else
			title = imageFile.getName();
		
		return title + " [" + pixelleFrameId + "]";
	}
	
	@Override
	public String toString() {
		return getTitle();
	}
	
	public class ImagePanel extends JPanel {
		
		private static final long serialVersionUID = 1004811680136095184L;
		
		private ZoomLevel zoom = ZoomLevel.FitToWindow;
		private final int[] color = new int[4];
		private Point lastPoint = new Point();
		
		public ImagePanel() {
			
			initListeners();
		}
		
		private void initListeners() {
			addMouseMotionListener(new MouseMotionAdapter() {

				@Override
				public void mouseMoved(MouseEvent e) {
					if ((inspector != null) && !inspector.isFrozen()) {
						Point p = e.getPoint();
						if (!lastPoint.equals(p)) {
							lastPoint = p;
							Dimension d = getDimension();
							int iWidth = image.getWidth();
							int iHeight = image.getHeight();
							double x = (p.x * (double)iWidth / d.width) + 0.5;
							double y = (p.y * (double)iHeight / d.height) + 0.5;
							p = new Point((int) x, (int) y);
							if ((p.x >= 0) && (p.y >= 0) && (p.x < iWidth) && (p.y < iHeight)) {
								inspector.setInspectorPosition(p);
								image.getSaveImage().getData().getPixel(p.x, p.y, color);
								inspector.setInspectorColor(new Color(color[0], color[1], color[2]), color[3]);
							}
						}
					}
				}
			});
			
			addMouseListener(new MouseAdapter() {
				
				@Override
				public void mouseClicked(MouseEvent e) {
				    if (inspector != null) {
				        inspector.toggleFrozen();
				    }
				}
			});
		}
		
		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			
			if (image != null) {
				Dimension dim = getDimension();
				image.draw(g, 0, 0, dim.width, dim.height);
			}
		}
		
		public ZoomLevel getZoom() {
		    return zoom;
		}
		
		public void setZoom(ZoomLevel zoomLevel) {
			zoom = zoomLevel;
		}
		
		private Dimension getDimension() {
			if (zoom == ZoomLevel.FitToWindow) {
				Container cp = PixelleFrame.this.getContentPane();
				int cHeight = cp.getHeight();
				int cWidth = cp.getWidth();
				int iHeight = image.getHeight();
				int iWidth = image.getWidth();
				double divH = (double)cHeight / (double)iHeight;
				double divW = (double)cWidth / (double)iWidth;
				double div = Math.min(divH, divW);
				return new Dimension((int)(div * iWidth), (int)(div * iHeight));
			}
			else {
				double zoomFactor = zoom.getZoom();
				return new Dimension((int)(image.getWidth() * zoomFactor), (int)(image.getHeight() * zoomFactor));
			}
		}
		
		@Override
		public Dimension getPreferredSize() {
			return getSize();
		}
	}
}
