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
package com.mebigfatguy.pixelle.dialogs;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.EnumMap;
import java.util.Map;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import com.mebigfatguy.pixelle.AlgorithmArchiver;
import com.mebigfatguy.pixelle.FrameMgr;
import com.mebigfatguy.pixelle.ImageType;
import com.mebigfatguy.pixelle.PixelleBundle;
import com.mebigfatguy.pixelle.PixelleComponent;
import com.mebigfatguy.pixelle.PixelleFrame;
import com.mebigfatguy.pixelle.PixelleImage;
import com.mebigfatguy.pixelle.utils.GuiUtils;
import com.mebigfatguy.pixelle.utils.IntegerDocument;
import com.mebigfatguy.pixelle.utils.GuiUtils.Sizing;

public class PixelleExpressionDialog extends JDialog {

	private static final long serialVersionUID = -4273352119079307059L;

	private final Map<PixelleComponent, JLabel> rgbLHS = new EnumMap<PixelleComponent, JLabel>(PixelleComponent.class);
	{
		for (PixelleComponent comp : PixelleComponent.rgbValues()) {
			rgbLHS.put(comp, new JLabel(PixelleBundle.getFormattedString(PixelleBundle.PIXEL_EQUALS, PixelleBundle.getString("formula." + comp.name().toLowerCase()))));
		}
	}

	private final Map<PixelleComponent, JTextField> rgbEditor = new EnumMap<PixelleComponent, JTextField>(PixelleComponent.class);
	{
		for (PixelleComponent comp : PixelleComponent.rgbValues()) {
			JTextField tf = new JTextField(PixelleBundle.getString("formula." + comp.name().toLowerCase()), 50);
			tf.setFont(EditorFontFactory.getEditorFont());
			rgbEditor.put(comp, tf);
		}
	}

	private final Map<PixelleComponent, JLabel> rgbLabels = new EnumMap<PixelleComponent, JLabel>(PixelleComponent.class);
	{
		for (PixelleComponent comp : PixelleComponent.rgbValues()) {
			rgbLabels.put(comp, new JLabel(PixelleBundle.getString("label." + comp.name().toLowerCase())));
		}
	}

	private final Map<PixelleComponent, JLabel> gsLHS = new EnumMap<PixelleComponent, JLabel>(PixelleComponent.class);
	{
		for (PixelleComponent comp : PixelleComponent.gsValues()) {
			gsLHS.put(comp, new JLabel(PixelleBundle.getFormattedString(PixelleBundle.PIXEL_EQUALS, PixelleBundle.getString("formula." + comp.name().toLowerCase()))));
		}
	}

	private final Map<PixelleComponent, JTextField> gsEditor = new EnumMap<PixelleComponent, JTextField>(PixelleComponent.class);
	{
		for (PixelleComponent comp : PixelleComponent.gsValues()) {
			JTextField tf = new JTextField(PixelleBundle.getString("formula." + comp.name().toLowerCase()), 50);
			tf.setFont(EditorFontFactory.getEditorFont());
			gsEditor.put(comp, tf);
		}
	}

	private final Map<PixelleComponent, JLabel> gsLabels = new EnumMap<PixelleComponent, JLabel>(PixelleComponent.class);
	{
		for (PixelleComponent comp : PixelleComponent.gsValues()) {
			gsLabels.put(comp, new JLabel(PixelleBundle.getString("label." + comp.name().toLowerCase())));
		}
	}

	PixelleFrame frame;
	JButton ok;
	JButton cancel;
	JButton reset;
	SourcePanel sourcePanel;
	JTextField selectedRGBAlgorithm;
	JPopupMenu savedRGBAlgorithms;
	JTextField selectedGSAlgorithm;
	JPopupMenu savedGSAlgorithms;
	JTabbedPane tabbedPane;
	JTextField widthField;
	JTextField heightField;

	JButton save;
	JButton delete;
	boolean clickedOK = false;

	public PixelleExpressionDialog(PixelleFrame owner) {
		super(owner, PixelleBundle.getString(PixelleBundle.PIXEL_ALGORITHMS));
		frame = owner;
		initComponents();
		initListeners();
	}

	public boolean isOK() {
		return clickedOK;
	}

	public ImageType getImageType() {
		return (tabbedPane.getSelectedIndex() == 0) ? ImageType.RGB : ImageType.Grayscale;
	}

	public Map<PixelleComponent, String> getAlgorithms(ImageType imageType) {
		Map<PixelleComponent, String> algorithms = new EnumMap<PixelleComponent, String>(PixelleComponent.class);
		for (PixelleComponent comp : (imageType == ImageType.RGB) ? PixelleComponent.rgbValues() : PixelleComponent.gsValues()) {
			algorithms.put(comp, (imageType == ImageType.RGB) ? rgbEditor.get(comp).getText() : gsEditor.get(comp).getText());
		}
		return algorithms;
	}

	public Point getOutputSize() {
		return new Point(Integer.parseInt(widthField.getText()), Integer.parseInt(heightField.getText()));
	}

	public PixelleImage[] getSourceImages() {
		PixelleImage[] images = sourcePanel.getSourceImages();
		if (images.length == 0) {
			images = new PixelleImage[] { frame.getImage() };
		}

		return images;
	}

	private void initComponents() {
		Container cp = getContentPane();

		ok = new JButton(PixelleBundle.getString(PixelleBundle.OK));
		cancel = new JButton(PixelleBundle.getString(PixelleBundle.CANCEL));
		reset = new JButton(PixelleBundle.getString(PixelleBundle.RESET));
		save = new JButton(PixelleBundle.getString(PixelleBundle.SAVE_ALGORITHM));
		delete = new JButton(PixelleBundle.getString(PixelleBundle.DELETE_ALGORITHM));
		GuiUtils.sizeUniformly(GuiUtils.Sizing.Both, new JComponent[] {ok, cancel, reset, save, delete});

		cp.setLayout(new BorderLayout(4, 4));

		sourcePanel = new SourcePanel();
		cp.add(sourcePanel, BorderLayout.NORTH);

		JPanel centerPanel = new JPanel();
		centerPanel.setLayout(new BorderLayout(4, 4));
		cp.add(centerPanel, BorderLayout.CENTER);

		centerPanel.setBorder(BorderFactory.createTitledBorder(PixelleBundle.getString(PixelleBundle.PIXEL_ALGORITHMS)));

		tabbedPane = new JTabbedPane();
		centerPanel.add(tabbedPane, BorderLayout.CENTER);

		tabbedPane.add(PixelleBundle.getString(PixelleBundle.RGB), buildRGBPane());
		tabbedPane.add(PixelleBundle.getString(PixelleBundle.GRAYSCALE), buildGrayscalePanel());

		centerPanel.add(buildSizingPanel(), BorderLayout.SOUTH);

		cp.add(buildCtrlPanel(), BorderLayout.SOUTH);

		pack();
	}

	private JPanel buildSizingPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		panel.setBorder(BorderFactory.createTitledBorder(PixelleBundle.getString(PixelleBundle.OUTPUT_PROPERTIES)));

		JLabel wl = new JLabel(PixelleBundle.getString(PixelleBundle.WIDTH));
		JLabel hl = new JLabel(PixelleBundle.getString(PixelleBundle.HEIGHT));
		GuiUtils.sizeUniformly(Sizing.Both, wl, hl);

		widthField = new JTextField(new IntegerDocument(), "", 6);
		heightField = new JTextField(new IntegerDocument(), "", 6);
		GuiUtils.sizeUniformly(Sizing.Both, widthField, heightField);

		wl.setLabelFor(widthField);
		hl.setLabelFor(heightField);

		widthField.setText(String.valueOf(frame.getImage().getWidth()));
		heightField.setText(String.valueOf(frame.getImage().getHeight()));

		panel.add(Box.createHorizontalStrut(20));
		panel.add(wl);
		panel.add(Box.createHorizontalStrut(10));
		panel.add(widthField);
		panel.add(Box.createHorizontalStrut(20));
		panel.add(hl);
		panel.add(Box.createHorizontalStrut(10));
		panel.add(heightField);
		panel.add(Box.createHorizontalGlue());

		return panel;
	}

	private JPanel buildCtrlPanel() {
		JPanel ctlPanel = new JPanel();
		ctlPanel.setLayout(new BoxLayout(ctlPanel, BoxLayout.X_AXIS));
		ctlPanel.add(Box.createHorizontalStrut(10));
		ctlPanel.add(reset);
		ctlPanel.add(Box.createHorizontalStrut(10));
		ctlPanel.add(save);
		ctlPanel.add(Box.createHorizontalStrut(10));
		ctlPanel.add(delete);
		ctlPanel.add(Box.createHorizontalGlue());
		ctlPanel.add(ok);
		ctlPanel.add(Box.createHorizontalStrut(10));
		ctlPanel.add(cancel);
		ctlPanel.add(Box.createHorizontalStrut(10));
		ctlPanel.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));

		return ctlPanel;
	}

	private JPanel buildRGBPane() {

		JPanel rgbPanel = new JPanel();
		rgbPanel.setLayout(new BoxLayout(rgbPanel, BoxLayout.Y_AXIS));

		{
			JPanel optionsPanel = new JPanel();
			optionsPanel.setLayout(new BoxLayout(optionsPanel, BoxLayout.X_AXIS));
			optionsPanel.add(Box.createHorizontalGlue());
			JLabel l = new JLabel(PixelleBundle.getString(PixelleBundle.PIXEL_ALGORITHM));
			optionsPanel.add(l);
			optionsPanel.add(Box.createHorizontalStrut(10));
			selectedRGBAlgorithm = new JTextField(PixelleBundle.getString(PixelleBundle.UNTITLED));
			selectedRGBAlgorithm.setEditable(false);
			l.setLabelFor(selectedRGBAlgorithm);
			optionsPanel.add(selectedRGBAlgorithm);
			optionsPanel.add(Box.createHorizontalStrut(10));
			savedRGBAlgorithms = AlgorithmArchiver.getArchiver().getAlgorithmDisplayPopup(ImageType.RGB, new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent ae) {
					JMenuItem item = (JMenuItem)ae.getSource();
					String algorithmName = item.getText();
					String groupName = (String)item.getClientProperty(AlgorithmArchiver.NAME);
					selectedRGBAlgorithm.setText(algorithmName);
					Map<PixelleComponent, String> algorithms = AlgorithmArchiver.getArchiver().getAlgorithm(ImageType.RGB, groupName, algorithmName);
					for (PixelleComponent comp : PixelleComponent.rgbValues()) {
						rgbEditor.get(comp).setText(algorithms.get(comp));
					}
				}
			});
			try {
				Map<PixelleComponent, String> algorithms = AlgorithmArchiver.getArchiver().getCurrent(ImageType.RGB);
				if (algorithms != null) {
					for (PixelleComponent comp : PixelleComponent.rgbValues()) {
						rgbEditor.get(comp).setText(algorithms.get(comp));
					}
				}
			} catch (IllegalArgumentException iae) {
			}

			savedRGBAlgorithms.setInvoker(selectedRGBAlgorithm);
			optionsPanel.add(savedRGBAlgorithms);
			optionsPanel.add(Box.createHorizontalGlue());
			optionsPanel.setBorder(BorderFactory.createEmptyBorder(10, 4, 5, 4));
			rgbPanel.add(optionsPanel);
		}

		{
			JPanel algoPanel = new JPanel();
			algoPanel.setLayout(new GridLayout(5, 1));

			GuiUtils.sizeUniformly(GuiUtils.Sizing.Both, rgbLHS.values().toArray(new JComponent[rgbLHS.size()]));
			GuiUtils.sizeUniformly(GuiUtils.Sizing.Both, rgbEditor.values().toArray(new JComponent[rgbEditor.size()]));
			GuiUtils.sizeUniformly(GuiUtils.Sizing.Both, rgbLabels.values().toArray(new JComponent[rgbLabels.size()]));

			for (PixelleComponent comp : PixelleComponent.rgbValues()) {
				JPanel p = new JPanel();
				p.setLayout(new BorderLayout(10, 10));
				p.add(rgbLHS.get(comp), BorderLayout.WEST);
				p.add(rgbEditor.get(comp), BorderLayout.CENTER);
				p.add(rgbLabels.get(comp), BorderLayout.EAST);
				rgbLabels.get(comp).setLabelFor(rgbEditor.get(comp));
				p.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
				algoPanel.add(p);
			}

			rgbPanel.add(algoPanel);
		}

		return rgbPanel;
	}

	private JPanel buildGrayscalePanel() {
		JPanel gsPanel = new JPanel();
		gsPanel.setLayout(new BoxLayout(gsPanel, BoxLayout.Y_AXIS));

		{
			JPanel optionsPanel = new JPanel();
			optionsPanel.setLayout(new BoxLayout(optionsPanel, BoxLayout.X_AXIS));
			optionsPanel.add(Box.createHorizontalGlue());
			JLabel l = new JLabel(PixelleBundle.getString(PixelleBundle.PIXEL_ALGORITHM));
			optionsPanel.add(l);
			optionsPanel.add(Box.createHorizontalStrut(10));
			selectedGSAlgorithm = new JTextField(PixelleBundle.getString(PixelleBundle.UNTITLED));
			selectedGSAlgorithm.setEditable(false);
			optionsPanel.add(selectedGSAlgorithm);
			l.setLabelFor(selectedGSAlgorithm);
			optionsPanel.add(Box.createHorizontalStrut(10));
			savedGSAlgorithms = AlgorithmArchiver.getArchiver().getAlgorithmDisplayPopup(ImageType.Grayscale, new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent ae) {
					JMenuItem item = (JMenuItem)ae.getSource();
					String algorithmName = item.getText();
					String groupName = (String)item.getClientProperty(AlgorithmArchiver.NAME);
					selectedGSAlgorithm.setText(algorithmName);
					Map<PixelleComponent, String> algorithms = AlgorithmArchiver.getArchiver().getAlgorithm(ImageType.Grayscale, groupName, algorithmName);
					for (PixelleComponent comp : PixelleComponent.gsValues()) {
						gsEditor.get(comp).setText(algorithms.get(comp));
					}
				}
			});
			try {
				Map<PixelleComponent, String> algorithms = AlgorithmArchiver.getArchiver().getCurrent(ImageType.Grayscale);
				if (algorithms != null) {
					for (PixelleComponent comp : PixelleComponent.gsValues()) {
						gsEditor.get(comp).setText(algorithms.get(comp));
					}
				}
			} catch (IllegalArgumentException iae) {
			}

			savedGSAlgorithms.setInvoker(selectedGSAlgorithm);
			optionsPanel.add(savedGSAlgorithms);
			optionsPanel.add(Box.createHorizontalGlue());
			optionsPanel.setBorder(BorderFactory.createEmptyBorder(10, 4, 5, 4));
			gsPanel.add(optionsPanel);
		}

		{
			JPanel algoPanel = new JPanel();
			algoPanel.setLayout(new GridLayout(5, 1));

			GuiUtils.sizeUniformly(GuiUtils.Sizing.Both, gsLHS.values().toArray(new JComponent[gsLHS.size()]));
			GuiUtils.sizeUniformly(GuiUtils.Sizing.Both, gsEditor.values().toArray(new JComponent[gsEditor.size()]));
			GuiUtils.sizeUniformly(GuiUtils.Sizing.Both, gsLabels.values().toArray(new JComponent[gsLabels.size()]));

			for (PixelleComponent comp : PixelleComponent.gsValues()) {
				JPanel p = new JPanel();
				p.setLayout(new BorderLayout(10, 10));
				p.add(gsLHS.get(comp), BorderLayout.WEST);
				p.add(gsEditor.get(comp), BorderLayout.CENTER);
				p.add(gsLabels.get(comp), BorderLayout.EAST);
				gsLabels.get(comp).setLabelFor(gsEditor.get(comp));
				p.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
				algoPanel.add(p);
			}

			gsPanel.add(algoPanel);
		}

		return gsPanel;
	}

	private void initListeners() {
		getRootPane().setDefaultButton(ok);

		selectedRGBAlgorithm.addMouseListener(new MouseAdapter() {

			@Override
			public void mousePressed(MouseEvent me) {
				savedRGBAlgorithms.setSize(selectedRGBAlgorithm.getSize());
				savedRGBAlgorithms.show(selectedRGBAlgorithm, 0, 0);
			}
		});

		selectedGSAlgorithm.addMouseListener(new MouseAdapter() {

			@Override
			public void mousePressed(MouseEvent me) {
				savedGSAlgorithms.setSize(selectedGSAlgorithm.getSize());
				savedGSAlgorithms.show(selectedGSAlgorithm, 0, 0);
			}
		});

		for (final PixelleComponent comp : PixelleComponent.gsValues()) {
			final JTextField tf = gsEditor.get(comp);
			tf.addFocusListener(new FocusExpansionListener(tf, comp));
		}

		for (final PixelleComponent comp : PixelleComponent.rgbValues()) {
			final JTextField tf = rgbEditor.get(comp);
			tf.addFocusListener(new FocusExpansionListener(tf, comp));
		}

		save.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				ImageType imageType = getImageType();
				SaveAlgorithmDialog dialog = new SaveAlgorithmDialog(frame, imageType);
				dialog.setLocationRelativeTo(frame);
				dialog.setModal(true);
				dialog.setVisible(true);
				if (dialog.isOK()) {
					String group = dialog.getGroup();
					String name = dialog.getName();
					Map<PixelleComponent, String> algorithm = getAlgorithms(imageType);
					AlgorithmArchiver archiver = AlgorithmArchiver.getArchiver();
					archiver.addAlgorithm(imageType, group, name, algorithm);
					archiver.save();
				}
			}
		});

		ok.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				ImageType imageType = getImageType();
				Map<PixelleComponent, String> algorithm = getAlgorithms(imageType);
				AlgorithmArchiver archiver = AlgorithmArchiver.getArchiver();
				archiver.setCurrent(imageType, algorithm);
				archiver.save();

				clickedOK = true;
				dispose();
			}
		});

		cancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				dispose();
			}
		});

		reset.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				Set<PixelleComponent> comps = (getImageType() == ImageType.RGB) ? PixelleComponent.rgbValues() : PixelleComponent.gsValues();
				for (PixelleComponent comp : comps) {
					rgbEditor.get(comp).setText(PixelleBundle.getString("formula." + comp.name().toLowerCase()));
				}
			}
		});
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
	}

	static class FocusExpansionListener extends FocusAdapter
	{
		private final JTextField textField;
		private final PixelleComponent component;

		public FocusExpansionListener(JTextField tf, PixelleComponent comp) {
			textField = tf;
			component = comp;
		}

		@Override
		public void focusGained(FocusEvent e) {
			Point pt = textField.getLocation();
			SwingUtilities.convertPointToScreen(pt, textField.getParent());
			Dimension dim = textField.getSize();
			AlgorithmEditor editor = new AlgorithmEditor(component, pt, dim, textField.getText());
			editor.setModal(true);
			editor.setVisible(true);
			textField.setText(editor.getValue());
			textField.setEnabled(false);
			textField.setEnabled(true);
		}
	}

	class SourcePanel extends JPanel
	{
		private static final long serialVersionUID = -8696503930565383721L;
		private static final String PLUS_ICON = "/com/mebigfatguy/pixelle/resources/plus.png";
		private static final String MINUS_ICON = "/com/mebigfatguy/pixelle/resources/minus.png";
		JTable sourceTable;
		DefaultTableModel sourceModel;
		JButton addButton;
		JButton removeButton;

		public SourcePanel() {
			initComponents();
			initListeners();
		}

		public PixelleImage[] getSourceImages() {
			int imageCount = sourceModel.getRowCount();
			PixelleImage[] selectedImages = new PixelleImage[imageCount];
			for (int i = 0; i < imageCount; i++) {
				selectedImages[i] = ((PixelleFrame)sourceModel.getValueAt(i, 1)).getImage();
			}

			return selectedImages;
		}

		private final void initComponents() {
			setLayout(new BorderLayout(4, 4));

			sourceModel = new DefaultTableModel();
			sourceModel.addColumn(PixelleBundle.getString(PixelleBundle.SOURCES_NUMBER));
			sourceModel.addColumn(PixelleBundle.getString(PixelleBundle.SOURCES_NAME));
			sourceModel.addRow(new Object[] { Integer.valueOf(0), frame });
			sourceTable = new JTable(sourceModel) {
				private static final long serialVersionUID = 2415580946606915612L;

				@Override
				public boolean isCellEditable(int row, int column) {
					return false;
				}
			};
			ListSelectionModel selectionModel = sourceTable.getSelectionModel();
			selectionModel.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

			TableColumnModel columnModel = sourceTable.getColumnModel();
			TableColumn column = columnModel.getColumn(0);
			column.setMaxWidth(50);
			JScrollPane scroller = new JScrollPane(sourceTable);
			Dimension d = scroller.getPreferredSize();
			d.setSize(d.width, sourceTable.getRowHeight() * 5);
			scroller.setMaximumSize(d);
			scroller.setPreferredSize(d);
			add(scroller, BorderLayout.CENTER);

			JPanel ctlPanel = new JPanel();
			ctlPanel.setLayout(new BoxLayout(ctlPanel, BoxLayout.Y_AXIS));
			Icon plus = new ImageIcon(SourcePanel.class.getResource(PLUS_ICON));
			Icon minus = new ImageIcon(SourcePanel.class.getResource(MINUS_ICON));
			addButton = new JButton(plus);
			removeButton = new JButton(minus);
			d = new Dimension(plus.getIconWidth(), plus.getIconHeight());
			addButton.setPreferredSize(d);
			removeButton.setPreferredSize(d);
			ctlPanel.add(Box.createVerticalGlue());
			ctlPanel.add(addButton);
			ctlPanel.add(Box.createVerticalStrut(10));
			ctlPanel.add(removeButton);
			ctlPanel.add(Box.createVerticalGlue());
			add(ctlPanel, BorderLayout.EAST);

			setBorder(BorderFactory.createTitledBorder(PixelleBundle.getString(PixelleBundle.SOURCES_LABEL)));
		}

		private final void initListeners() {
			addButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent ae) {
					Set<JFrame> frames = FrameMgr.getInstance().getFrames();
					PixelleFrame pixFrame = (PixelleFrame)JOptionPane.showInputDialog(SourcePanel.this, PixelleBundle.getString(PixelleBundle.PICK_SOURCE_LABEL), PixelleBundle.getString(PixelleBundle.TITLE), JOptionPane.QUESTION_MESSAGE, null, frames.toArray(new JFrame[frames.size()]), null);
					if (pixFrame != null) {
						int id = sourceTable.getRowCount();
						sourceModel.addRow(new Object[] { Integer.valueOf(id), pixFrame });
					}
				}
			});

			removeButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent ae) {
					int[] selRows = sourceTable.getSelectedRows();
					for (int i = selRows.length - 1; i >= 0; i--) {
						sourceModel.removeRow(selRows[i]);
					}
					int numRows = sourceTable.getRowCount();
					for (int i = 0; i < numRows; i++) {
						sourceModel.setValueAt(String.valueOf(i), i, 0);
					}
				}
			});
		}
	}
}
