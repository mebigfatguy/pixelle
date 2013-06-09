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
package com.mebigfatguy.pixelle;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;

public class SelectionOutliner {

	public void createOutline(BufferedImage selectionImage, BufferedImage selectionOutline) {
		int width = selectionImage.getWidth();
		int height = selectionImage.getHeight();

		WritableRaster outRaster = selectionOutline.getRaster();
		WritableRaster inRaster = selectionImage.getRaster();

		double[] pixel = new double[9];

		for (int row = 0; row < height; row++) {
			boolean yedge = (row == 0) || (row == (height - 1));

			for (int col = 0; col < width; col++) {
				pixel = inRaster.getPixel(col, row, pixel);

				// If the current pixel is selected, look to see if it's got a non selected pixel next to it
				if (pixel[0] != 0) {
					boolean xedge = false;
					if (!yedge) {
						xedge = (col == 0) || (col == (width - 1));
					}

					if (!xedge && !yedge) {
						pixel = inRaster.getPixels(col - 1, row - 1, 3, 3, pixel);
						boolean foundUnselected = false;
						for (int p = 0; p < 9; p++) {
							if (pixel[p] == 0) {
								foundUnselected = true;
								break;
							}
						}
						pixel[0] = foundUnselected ? 1.0 : 0.0;
					}
					outRaster.setPixel(col, row, pixel);
				}
			}
		}
	}
}
