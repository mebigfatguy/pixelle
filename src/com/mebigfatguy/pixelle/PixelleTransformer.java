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

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.EnumMap;
import java.util.Map;

import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CharStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;

import com.mebigfatguy.pixelle.antlr.PixelleLexer;
import com.mebigfatguy.pixelle.antlr.PixelleParser;
import com.mebigfatguy.pixelle.eval.PixelleEval4ByteABGR;
import com.mebigfatguy.pixelle.eval.PixelleEvalByteGray;

/**
 * transforms one bitmap into another based on the algorithms defined by the user.
 */
public class PixelleTransformer {

    private final PixelleImage[] srcImages;
    private Map<PixelleComponent, String> algorithms = null;
    private final ImageType outputImageType;
    private final Point dimension;

    /**
     * constructions a transformer given a source bitmap and algorithms
     *
     * @param images
     *            the source images
     * @param algos
     *            the algorithms for the color components
     * @param newDimension
     *            the desired dimension of the output image
     */
    public PixelleTransformer(PixelleImage[] images, Map<PixelleComponent, String> algos, ImageType imageType, Point newDimension) {
        srcImages = images;
        algorithms = algos;
        outputImageType = imageType;
        dimension = newDimension;
    }

    /**
     * builds a sample set of algorithms that creates a 'pleasing' image
     *
     * @return a set of transformation algorithms
     */
    public static Map<PixelleComponent, String> getSampleTransform() {
        Map<PixelleComponent, String> algorithms = new EnumMap<>(PixelleComponent.class);
        algorithms.put(PixelleComponent.RED, "x");
        algorithms.put(PixelleComponent.GREEN, "y");
        algorithms.put(PixelleComponent.BLUE, "abs((width/2) - x)");
        algorithms.put(PixelleComponent.TRANSPARENCY, "((x < 10) || (x >= (width - 10)) || (y < 10) || (y >= (height - 10))) ? 180 : 255");
        algorithms.put(PixelleComponent.SELECTION, "0");
        return algorithms;
    }

    /**
     * transforms the image into a destination image based on the algorithms supplied. It does this by generating classes that implement the PixelleExpr
     * interface, and dynamically loads them to create the new pixel values.
     *
     * @return the destination bitmap
     * @throws PixelleTransformException
     *             if a failure occurs reading, writing or transforming a bitmap
     */
    public PixelleImage transform() throws PixelleTransformException {

        String currentComponent = "";
        String currentAlgorithm = "";

        try {
            PixelleImage destImage;
            PixelleEval destPE;

            if (outputImageType == ImageType.RGB) {
                destImage = new PixelleImage(new BufferedImage(dimension.x, dimension.y, BufferedImage.TYPE_4BYTE_ABGR));
                destPE = new PixelleEval4ByteABGR(destImage, PixelleEvalFactory.getIndexOutOfBoundsOption(), PixelleEvalFactory.getColorOutOfBoundsOption());
            } else {
                destImage = new PixelleImage(new BufferedImage(dimension.x, dimension.y, BufferedImage.TYPE_BYTE_GRAY));
                destPE = new PixelleEvalByteGray(destImage, PixelleEvalFactory.getIndexOutOfBoundsOption(), PixelleEvalFactory.getColorOutOfBoundsOption());
            }

            PixelleEval[] sourceEvals = new PixelleEval[srcImages.length];
            for (int i = 0; i < srcImages.length; i++) {
                PixelleEval srcPE = PixelleEvalFactory.create(srcImages[i]);
                sourceEvals[i] = srcPE;
            }

            PixelleClassLoader pcl = AccessController.doPrivileged(new PrivilegedAction<PixelleClassLoader>() {
                @Override
                public PixelleClassLoader run() {
                    return new PixelleClassLoader(Thread.currentThread().getContextClassLoader());
                }
            });

            for (Map.Entry<PixelleComponent, String> entry : algorithms.entrySet()) {
                currentComponent = entry.getKey().name();
                currentAlgorithm = entry.getValue();

                CharStream cs = new ANTLRCaseInsensitiveStringStream(currentAlgorithm);
                PixelleLexer pl = new PixelleLexer(cs);
                CommonTokenStream tokens = new CommonTokenStream();
                tokens.setTokenSource(pl);

                String clsName = "com.mebigfatguy.pixelle.asm.PixelleExpr" + currentComponent;
                PixelleParser pp = new PixelleParser(tokens, clsName);
                pp.pixelle();

                byte[] bytes = pp.getClassBytes();
                // dump(bytes, clsName.substring(clsName.lastIndexOf('.') + 1) +
                // ".class");

                pcl.addClass(clsName, bytes);
                Class<?> cl = pcl.loadClass(clsName);
                PixelleExpr expr = (PixelleExpr) cl.newInstance();

                int width = destImage.getWidth();
                int height = destImage.getHeight();
                expr.setOutputSize(width, height);

                char pixelSpec = entry.getKey().getPixelSpec();
                for (int y = 0; y < height; y++) {
                    for (int x = 0; x < width; x++) {
                        double value = expr.eval(sourceEvals, x, y);
                        destPE.setValue(x, y, pixelSpec, value);
                    }
                }
            }

            destImage.finishImage();

            return destImage;
        } catch (RecognitionException re) {
            throw new PixelleTransformException(currentComponent, currentAlgorithm, re.charPositionInLine, re);
        } catch (Exception e) {
            throw new PixelleTransformException(currentComponent, currentAlgorithm, -1, e);
        }
    }

    /** for debugging */
    private static void dump(byte[] byteCode, String name) {
        Path clsPath = Paths.get(System.getProperty("java.io.tmpdir"), name);
        try (OutputStream fos = Files.newOutputStream(clsPath)) {

            fos.write(byteCode);
            fos.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static class ANTLRCaseInsensitiveStringStream extends ANTLRStringStream {
        public ANTLRCaseInsensitiveStringStream(String text) {
            super(text);
        }

        @Override
        public int LA(int i) {
            if (i == 0) {
                return 0;
            }
            if (i < 0) {
                i++;
            }

            if (((p + i) - 1) >= n) {

                return CharStream.EOF;
            }
            return Character.toUpperCase(data[(p + i) - 1]);
        }
    }

}
