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
package com.mebigfatguy.pixelle.eval;

import com.mebigfatguy.pixelle.ColorOutOfBoundsOption;
import com.mebigfatguy.pixelle.IndexOutOfBoundsOption;
import com.mebigfatguy.pixelle.PixelleEval;
import com.mebigfatguy.pixelle.PixelleImage;

public class PixelleEval3ByteBGR extends PixelleEval {

	public PixelleEval3ByteBGR(PixelleImage srcImage, IndexOutOfBoundsOption iOption, ColorOutOfBoundsOption cOption) {
		super(srcImage, iOption, cOption);
	}
	
	@Override
	public double getBlueValue(int x, int y) {
		return buffer.getElem(y * width * 3 + x * 3);
	}

	@Override
	public double getGreenValue(int x, int y) {
		return buffer.getElem(y * width * 3 + x * 3 + 1);
	}

	@Override
	public double getRedValue(int x, int y) {
		return buffer.getElem(y * width * 3 + x * 3 + 2);
	}

	@Override
	public double getTransparencyValue(int x, int y) {
		return 255.0;
	}

}
