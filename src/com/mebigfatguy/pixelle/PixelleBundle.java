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
package com.mebigfatguy.pixelle;

import java.util.ResourceBundle;

/**
 * manages the resource bundle using in the program
 */
public class PixelleBundle {

	public static final String OK = "ok";
	public static final String CANCEL = "cancel";
	public static final String RESET = "reset";
	public static final String TITLE = "pixelle.title";
	public static final String UNTITLED = "pixelle.untitled";
	public static final String FILE_MENU = "menu.file.title";
	public static final String NEW_ITEM = "menu.file.new";
	public static final String OPEN_ITEM = "menu.file.open";
	public static final String CLOSE_ITEM = "menu.file.close";
	public static final String SAVE_ITEM = "menu.file.save";
	public static final String SAVEAS_ITEM = "menu.file.saveas";
	public static final String PAGESETUP_ITEM = "menu.file.pagesetup";
	public static final String PRINT_ITEM = "menu.file.print";
	public static final String QUIT_ITEM = "menu.file.quit";
	public static final String VIEW_MENU = "menu.view.title";
	public static final String ONEEIGHT_ITEM = "menu.view.one_eighth";
	public static final String ONEFOURTH_ITEM = "menu.view.one_fourth";
	public static final String ONEHALF_ITEM = "menu.view.one_half";
	public static final String FULLSIZE_ITEM = "menu.view.full_size";
	public static final String DOUBLE_ITEM = "menu.view.double";
	public static final String FOURTIMES_ITEM = "menu.view.four_times";
	public static final String EIGHTTIMES_ITEM = "menu.view.eight_times";
	public static final String FITTOWINDOW_ITEM = "menu.view.fit_to_window";
	public static final String TRANSFORM_MENU = "menu.transform.title";
	public static final String OPTIONS_ITEM = "menu.transform.options";
	public static final String TRANSFORM_NEW_ITEM = "menu.transform.newwindow";
	public static final String TRANSFORM_ITEM = "menu.transform.transform";
	public static final String GOODIES_MENU = "menu.goodies.title";
	public static final String INSPECTOR_ITEM = "menu.goodies.inspector";
	public static final String GRAPHIC_FILES = "label.graphic_files";
	public static final String SAVE_ALGORITHM = "save.algorithm";
	public static final String DELETE_ALGORITHM = "delete.algorithm";
	public static final String RED_FORMULA = "formula.red";
	public static final String GREEN_FORMULA = "formula.green";
	public static final String BLUE_FORMULA = "formula.blue";
	public static final String BLACK_FORMULA = "formula.black";
	public static final String TRANSPARENCY_FORMULA = "formula.transparency";
	public static final String SELECTION_FORMULA = "formula.selection";
	public static final String PIXEL_ALGORITHMS = "title.algorithms";
	public static final String PIXEL_ALGORITHM = "label.algorithm";
	public static final String PIXEL_GROUP = "label.group";
	public static final String RED_LABEL = "label.red";
	public static final String GREEN_LABEL = "label.green";
	public static final String BLUE_LABEL = "label.blue";
	public static final String BLACK_LABEL = "label.black";
	public static final String TRANSPARENCY_LABEL = "label.transparency";
	public static final String SELECTION_LABEL = "label.selection";
	public static final String RED_ALGORITHM = "title.red";
	public static final String GREEN_ALGORITHM = "title.green";
	public static final String BLUE_ALGORITHM = "title.blue";
	public static final String BLACK_ALGORITHM = "title.black";
	public static final String TRANSPARENCY_ALGORITHM = "title.transparency";
	public static final String SELECTION_ALGORITHM = "title.selection";	public static final String PIXEL_OPTIONS = "title.pixel_options";
	public static final String INDEX_OUT_OF_BOUNDS_PIXELS = "title.index_out_of_bounds_pixels";
	public static final String STATIC_COLOR = "label.color";
	public static final String BORDER_COLOR = "label.border_color";
	public static final String WRAPPED_COLOR = "label.wrapped_color";
	public static final String PICK_COLOR = "title.pick_color";
	public static final String COLOR_OUT_OF_BOUNDS_PIXELS = "title.color_out_of_bounds_pixels";
	public static final String CLIP_COLOR = "label.clip_color";
	public static final String ROLL_COLOR = "label.roll_color";
	public static final String WAVE_COLOR = "label.wave_color";
	public static final String SAVE_OVERWRITE = "label.save_overwrite";
	public static final String X = "label.x";
	public static final String Y = "label.y";
	public static final String COLOR = "label.color";
	public static final String INSPECTOR_TOOLTIP = "tooltip.inspector";
	public static final String RGB = "label.rgb";
	public static final String GRAYSCALE = "label.grayscale";
	public static final String WIDTH ="label.width";
	public static final String HEIGHT = "label.height";
	public static final String SOURCES_LABEL = "label.sources";
	public static final String SOURCES_NUMBER = "label.inputnumber";
	public static final String SOURCES_NAME = "label.inputname";
	public static final String OUTPUT_PROPERTIES = "label.output_properties";
	public static final String PICK_SOURCE_LABEL = "label.pick_source";
	public static final String SHORTCUTS = "label.shortcuts";
	public static final String PIXEL_SPECIFICATION = "label.pixelspecs";
	public static final String POSITIONS = "label.positions";
	public static final String LEFT = "label.left";
	public static final String TOP = "label.top";
	public static final String RIGHT = "label.right";
	public static final String BOTTOM = "label.bottom";
	public static final String CENTERX = "label.centerx";
	public static final String CENTERY = "label.centery";
	public static final String OPERATORS = "label.operators";
	public static final String AND = "label.and";
	public static final String OR = "label.or";
	public static final String TRINARY = "label.trinary";
	public static final String EQUALS = "label.equals";
	public static final String NOTEQUALS = "label.notequals";
	public static final String LESS_THAN_OR_EQUALS= "label.lessorequals";
	public static final String GREATER_THAN_OR_EQUALS= "label.greaterthanorequals";
	public static final String LESS_THAN = "label.lessthan";
	public static final String GREATER_THAN = "label.greaterthan";
	public static final String MULTIPLY = "label.multiply";
	public static final String DIVIDE = "label.divide";
	public static final String ADD = "label.add";
	public static final String SUBTRACT = "label.subtract";
	public static final String NOT = "label.not";
	public static final String FUNCTIONS = "label.functions";
	public static final String ABS = "label.abs";
	public static final String MAX = "label.max";
	public static final String MIN = "label.min";
	public static final String POW = "label.pow";
	public static final String SQRT = "label.sqrt";
	public static final String SIN = "label.sin";
	public static final String COS = "label.cos";
	public static final String TAN = "label.tan";
	public static final String ASIN = "label.asin";
	public static final String ACOS = "label.acos";
	public static final String ATAN = "label.atan";
	public static final String LOG = "label.log";
	public static final String EXP = "label.exp";
	public static final String E = "label.e";
	public static final String PI = "label.pi";
	public static final String RANDOM = "label.random";
	public static final String SPECIAL = "label.special";
	public static final String PIXEL_IN_RECT = "label.pixelInRect";
	public static final String PIXEL_IN_CIRCLE = "label.pixelInCircle";
	public static final String PIXEL_ON_EDGE = "label.pixelOnEdge";
	public static final String PIXEL_AVERAGE = "label.pixelAverage";


	private static ResourceBundle rb = ResourceBundle.getBundle("com/mebigfatguy/pixelle/resources/pixelle");

	private PixelleBundle()
	{
	}

	/**
	 * returns a localized string from a given key
	 * 
	 * @param key the key of the string to return
	 * @return the localized string
	 */
	public static String getString(String key) {
		return rb.getString(key);
	}
}