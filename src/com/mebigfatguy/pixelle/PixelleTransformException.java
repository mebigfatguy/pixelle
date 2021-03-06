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
package com.mebigfatguy.pixelle;

public class PixelleTransformException extends Exception {
    private static final long serialVersionUID = -7209029044223677078L;

    private String component;
    private String algorithm;
    private int position;

    public PixelleTransformException(String comp, String algo, int pos,
            Throwable orig) {
        super("Failed to transform the image: " + comp + ": [" + algo  + "] at position: " + pos, orig);
        component = comp;
        algorithm = algo;
        position = pos;
    }

    public String getComponent() {
        return component;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public int getPosition() {
        return position;
    }
}
