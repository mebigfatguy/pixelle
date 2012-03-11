/*
 * pixelle - Graphics algorithmic editor
 * Copyright (C) 2008-2012 Dave Brosius
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
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.WindowConstants;

import com.mebigfatguy.pixelle.PixelleBundle;
import com.mebigfatguy.pixelle.PixelleComponent;
import com.mebigfatguy.pixelle.utils.GuiUtils;
import com.mebigfatguy.pixelle.utils.GuiUtils.Sizing;

/**
 * an expanded popup dialog for editing algorithms
 */
public class AlgorithmEditor extends JDialog {
	private static final long serialVersionUID = 7704059483753204128L;

	private JEditorPane algo;
	private JButton ok;
	private JButton cancel;
	private final String origValue;
	private JLabel shortcutTrigger;
	private JPopupMenu shortcuts;
	private boolean isOK = false;

	public AlgorithmEditor(PixelleComponent component, Point pt, Dimension dim, String value) {
		origValue = value;
		initComponents();
		initListeners();

		setTitle(component.toString());
		pt.y -= dim.height + 5; // 5 is clearly a fudge
		setLocation(pt);
		dim.height *= 8;
		setSize(dim);
		algo.setText(value);
	}

	public String getValue() {
		if (isOK) {
			return algo.getText();
		}

		return origValue;
	}

	private void initComponents() {
		Container cp = getContentPane();
		cp.setLayout(new BorderLayout(4, 4));

		algo = new JEditorPane();
		cp.add(new JScrollPane(algo), BorderLayout.CENTER);

		ok = new JButton(PixelleBundle.getString(PixelleBundle.OK));
		cancel = new JButton(PixelleBundle.getString(PixelleBundle.CANCEL));
		GuiUtils.sizeUniformly(Sizing.Both, ok, cancel);

		shortcutTrigger = new JLabel(PixelleBundle.getString(PixelleBundle.SHORTCUTS));
		shortcutTrigger.setBorder(BorderFactory.createEtchedBorder());
		shortcutTrigger.setEnabled(true);
		shortcutTrigger.setOpaque(true);

		buildShortCutsMenu();

		JPanel p = new JPanel();
		p.setLayout(new BoxLayout(p, BoxLayout.X_AXIS));
		p.add(Box.createHorizontalStrut(20));
		p.add(shortcutTrigger);
		p.add(Box.createHorizontalStrut(10));
		p.add(Box.createHorizontalGlue());
		p.add(ok);
		p.add(Box.createHorizontalStrut(10));
		p.add(cancel);
		p.add(Box.createHorizontalStrut(20));

		cp.add(p, BorderLayout.SOUTH);
	}

	private void initListeners() {
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

		ok.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				isOK = true;
				dispose();
			}
		});

		cancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				dispose();
			}
		});

		shortcutTrigger.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				shortcuts.show(e.getComponent(), e.getX(), e.getY());
			}
		});
	}

	public String getText() {
		return algo.getText();
	}

	private void buildShortCutsMenu()
	{
		if (shortcuts != null) {
			return;
		}

		shortcuts = new JPopupMenu(PixelleBundle.getString(PixelleBundle.SHORTCUTS));

		JMenu specMenu = new JMenu(PixelleBundle.getString(PixelleBundle.PIXEL_SPECIFICATION));
		JMenuItem redSpecItem = new JMenuItem(PixelleBundle.getString(PixelleBundle.RED_ALGORITHM));
		JMenuItem greenSpecItem = new JMenuItem(PixelleBundle.getString(PixelleBundle.GREEN_ALGORITHM));
		JMenuItem blueSpecItem = new JMenuItem(PixelleBundle.getString(PixelleBundle.BLUE_ALGORITHM));
		JMenuItem blackSpecItem = new JMenuItem(PixelleBundle.getString(PixelleBundle.BLACK_ALGORITHM));
		JMenuItem transparentSpecItem = new JMenuItem(PixelleBundle.getString(PixelleBundle.TRANSPARENCY_ALGORITHM));
		JMenuItem selectionSpecItem = new JMenuItem(PixelleBundle.getString(PixelleBundle.SELECTION_ALGORITHM));
		specMenu.add(redSpecItem);
		specMenu.add(greenSpecItem);
		specMenu.add(blueSpecItem);
		specMenu.add(blackSpecItem);
		specMenu.add(transparentSpecItem);
		specMenu.add(selectionSpecItem);
		shortcuts.add(specMenu);

		JMenu posMenu = new JMenu(PixelleBundle.getString(PixelleBundle.POSITIONS));
		JMenuItem leftItem = new JMenuItem(PixelleBundle.getString(PixelleBundle.LEFT));
		JMenuItem topItem = new JMenuItem(PixelleBundle.getString(PixelleBundle.TOP));
		JMenuItem rightItem = new JMenuItem(PixelleBundle.getString(PixelleBundle.RIGHT));
		JMenuItem bottomItem = new JMenuItem(PixelleBundle.getString(PixelleBundle.BOTTOM));
		JMenuItem centerXItem = new JMenuItem(PixelleBundle.getString(PixelleBundle.CENTERX));
		JMenuItem centerYItem = new JMenuItem(PixelleBundle.getString(PixelleBundle.CENTERY));
		posMenu.add(leftItem);
		posMenu.add(topItem);
		posMenu.add(rightItem);
		posMenu.add(bottomItem);
		posMenu.add(centerXItem);
		posMenu.add(centerYItem);
		shortcuts.add(posMenu);

		JMenu operatorMenu = new JMenu(PixelleBundle.getString(PixelleBundle.OPERATORS));
		JMenuItem andItem = new JMenuItem(PixelleBundle.getString(PixelleBundle.AND));
		JMenuItem orItem = new JMenuItem(PixelleBundle.getString(PixelleBundle.OR));
		JMenuItem trinaryItem = new JMenuItem(PixelleBundle.getString(PixelleBundle.TRINARY));
		JMenuItem equalsItem = new JMenuItem(PixelleBundle.getString(PixelleBundle.EQUALS));
		JMenuItem notEqualsItem = new JMenuItem(PixelleBundle.getString(PixelleBundle.NOTEQUALS));
		JMenuItem lessThanOrEqualsItem = new JMenuItem(PixelleBundle.getString(PixelleBundle.LESS_THAN_OR_EQUALS));
		JMenuItem greaterThanOrEqualsItem = new JMenuItem(PixelleBundle.getString(PixelleBundle.GREATER_THAN_OR_EQUALS));
		JMenuItem lessThanItem = new JMenuItem(PixelleBundle.getString(PixelleBundle.LESS_THAN));
		JMenuItem greaterThanItem = new JMenuItem(PixelleBundle.getString(PixelleBundle.GREATER_THAN));
		JMenuItem multiplyItem = new JMenuItem(PixelleBundle.getString(PixelleBundle.MULTIPLY));
		JMenuItem divideItem = new JMenuItem(PixelleBundle.getString(PixelleBundle.DIVIDE));
		JMenuItem addItem = new JMenuItem(PixelleBundle.getString(PixelleBundle.ADD));
		JMenuItem subtractItem = new JMenuItem(PixelleBundle.getString(PixelleBundle.SUBTRACT));
		JMenuItem notItem = new JMenuItem(PixelleBundle.getString(PixelleBundle.NOT));

		operatorMenu.add(andItem);
		operatorMenu.add(orItem);
		operatorMenu.add(trinaryItem);
		operatorMenu.add(equalsItem);
		operatorMenu.add(notEqualsItem);
		operatorMenu.add(lessThanOrEqualsItem);
		operatorMenu.add(greaterThanOrEqualsItem);
		operatorMenu.add(lessThanItem);
		operatorMenu.add(greaterThanItem);
		operatorMenu.add(multiplyItem);
		operatorMenu.add(divideItem);
		operatorMenu.add(addItem);
		operatorMenu.add(subtractItem);
		operatorMenu.add(notItem);

		shortcuts.add(operatorMenu);

		JMenu functionMenu = new JMenu(PixelleBundle.getString(PixelleBundle.FUNCTIONS));
		JMenuItem absItem = new JMenuItem(PixelleBundle.getString(PixelleBundle.ABS));
		JMenuItem maxItem = new JMenuItem(PixelleBundle.getString(PixelleBundle.MAX));
		JMenuItem minItem = new JMenuItem(PixelleBundle.getString(PixelleBundle.MIN));
		JMenuItem powItem = new JMenuItem(PixelleBundle.getString(PixelleBundle.POW));
		JMenuItem sqrtItem = new JMenuItem(PixelleBundle.getString(PixelleBundle.SQRT));
		JMenuItem sinItem = new JMenuItem(PixelleBundle.getString(PixelleBundle.SIN));
		JMenuItem cosItem = new JMenuItem(PixelleBundle.getString(PixelleBundle.COS));
		JMenuItem tanItem = new JMenuItem(PixelleBundle.getString(PixelleBundle.TAN));
		JMenuItem asinItem = new JMenuItem(PixelleBundle.getString(PixelleBundle.ASIN));
		JMenuItem acosItem = new JMenuItem(PixelleBundle.getString(PixelleBundle.ACOS));
		JMenuItem atanItem = new JMenuItem(PixelleBundle.getString(PixelleBundle.ATAN));
		JMenuItem logItem = new JMenuItem(PixelleBundle.getString(PixelleBundle.LOG));
		JMenuItem expItem = new JMenuItem(PixelleBundle.getString(PixelleBundle.EXP));
		JMenuItem eItem = new JMenuItem(PixelleBundle.getString(PixelleBundle.E));
		JMenuItem piItem = new JMenuItem(PixelleBundle.getString(PixelleBundle.PI));
		JMenuItem randomItem = new JMenuItem(PixelleBundle.getString(PixelleBundle.RANDOM));

		functionMenu.add(absItem);
		functionMenu.add(maxItem);
		functionMenu.add(minItem);
		functionMenu.add(powItem);
		functionMenu.add(sqrtItem);
		functionMenu.add(sinItem);
		functionMenu.add(cosItem);
		functionMenu.add(tanItem);
		functionMenu.add(asinItem);
		functionMenu.add(acosItem);
		functionMenu.add(atanItem);
		functionMenu.add(logItem);
		functionMenu.add(expItem);
		functionMenu.add(eItem);
		functionMenu.add(piItem);
		functionMenu.add(randomItem);

		shortcuts.add(functionMenu);
		
		JMenu specialMenu = new JMenu(PixelleBundle.getString(PixelleBundle.SPECIAL));
        JMenuItem pixelInRectItem = new JMenuItem(PixelleBundle.getString(PixelleBundle.PIXEL_IN_RECT));
        JMenuItem pixelInCircleItem = new JMenuItem(PixelleBundle.getString(PixelleBundle.PIXEL_IN_CIRCLE));
        JMenuItem pixelOnEdgeItem = new JMenuItem(PixelleBundle.getString(PixelleBundle.PIXEL_ON_EDGE));
        JMenuItem pixelAverageItem = new JMenuItem(PixelleBundle.getString(PixelleBundle.PIXEL_AVERAGE));
        
        specialMenu.add(pixelInRectItem);
        specialMenu.add(pixelInCircleItem);
        specialMenu.add(pixelOnEdgeItem);
        specialMenu.add(pixelAverageItem);
        
        shortcuts.add(specialMenu);

		addSimpleShortCutListener(redSpecItem, "p(0)[x,y].r");
		addSimpleShortCutListener(greenSpecItem, "p(0)[x,y].g");
		addSimpleShortCutListener(blueSpecItem, "p(0)[x,y].b");
		addSimpleShortCutListener(blackSpecItem, "p(0)[x,y].k");
		addSimpleShortCutListener(transparentSpecItem, "p(0)[x,y].t");
		addSimpleShortCutListener(selectionSpecItem, "p(0)[x,y].s");

		addSimpleShortCutListener(leftItem, "0");
		addSimpleShortCutListener(topItem, "0");
		addSimpleShortCutListener(rightItem, "width");
		addSimpleShortCutListener(bottomItem, "height");
		addSimpleShortCutListener(centerXItem, "(width/2)");
		addSimpleShortCutListener(centerYItem, "(height/2)");

		addSimpleShortCutListener(andItem, " && ");
		addSimpleShortCutListener(orItem, " || ");
		addSimpleShortCutListener(trinaryItem, " ? ");
		addSimpleShortCutListener(equalsItem, " == ");
		addSimpleShortCutListener(notEqualsItem, " != ");
		addSimpleShortCutListener(lessThanOrEqualsItem, " <= ");
		addSimpleShortCutListener(greaterThanOrEqualsItem, " >= ");
		addSimpleShortCutListener(lessThanItem, " < ");
		addSimpleShortCutListener(greaterThanItem, " > ");
		addSimpleShortCutListener(multiplyItem, " * ");
		addSimpleShortCutListener(divideItem, " / ");
		addSimpleShortCutListener(addItem, " + ");
		addSimpleShortCutListener(subtractItem, " - ");
		addSimpleShortCutListener(notItem, " !");

		addSimpleShortCutListener(absItem, "abs(x)");
		addSimpleShortCutListener(maxItem, "max(x,y)");
		addSimpleShortCutListener(minItem, "min(x,y)");
		addSimpleShortCutListener(powItem, "pow(x,y)");
		addSimpleShortCutListener(sqrtItem, "sqrt(x)");
		addSimpleShortCutListener(sinItem, "sin(x)");
		addSimpleShortCutListener(cosItem, "cos(x)");
		addSimpleShortCutListener(tanItem, "tan(x)");
		addSimpleShortCutListener(asinItem, "asin(x)");
		addSimpleShortCutListener(acosItem, "acos(x)");
		addSimpleShortCutListener(atanItem, "atan(x)");
		addSimpleShortCutListener(logItem, "log(x)");
		addSimpleShortCutListener(expItem, "exp(x)");
		addSimpleShortCutListener(eItem, "e()");
		addSimpleShortCutListener(piItem, "pi()");
		addSimpleShortCutListener(randomItem, "random()");
		
		addSimpleShortCutListener(pixelInRectItem, "pixelInRect(t,l,b,r)");
		addSimpleShortCutListener(pixelInCircleItem, "pixelInCircle(x,y,radius)");
		addSimpleShortCutListener(pixelOnEdgeItem, "pixelOnEdge(e)");
		addSimpleShortCutListener(pixelOnEdgeItem, "pixelAverage(x, y, size, source, color)");
	}

	private void addSimpleShortCutListener(JMenuItem mi, final String sc) {
		mi.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				algo.replaceSelection(sc);
			}
		});
	}
}
