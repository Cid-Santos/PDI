/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ctPrincipal;

import java.awt.image.BufferedImage;
import java.awt.image.Raster;

/**
 *
 * @author Cid
 */
public class Psnr {

    // número de bits usados para armazenar a mantissa na norma IEEE-754 de 64 bits
    public  final int DOUBLE_MANTISSA_BITS = 52;
    // número de bits usados para armazenar a mantissa na norma IEEE-754 de 32 bits
    public  final int FLOAT_MANTISSA_BITS = 23;

    public  final byte POSITIVE = 0;
    public  final byte NEGATIVE = 1;

    public  final int BLACK = 0;
    public  final int WHITE = 1;

    public  final int INSIGNIFICANT = 0;
    public  final int SIGNIFICANT = 1;

    public long unsigned(long l) {
        long x = l;
        x <<= 32;
        x >>>= 32;
        return x;
    }

    /**
     * Calcula e imprime o PSNR de duas imagens
     *
     * @param im1
     * @param im2
     * @return
     */
    public double printPSNR(BufferedImage im1, BufferedImage im2) {
        assert (im1.getType() == im2.getType()
                && im1.getHeight() == im2.getHeight()
                && im1.getWidth() == im2.getWidth());

        double mse = 0;
        int width = im1.getWidth();
        int height = im1.getHeight();
        Raster r1 = im1.getRaster();
        Raster r2 = im2.getRaster();
        for (int j = 0; j < height; j++) {
            for (int i = 0; i < width; i++) {
                mse
                        += Math.pow(r1.getSample(i, j, 0) - r2.getSample(i, j, 0), 2);
            }
        }
        mse /= (double) (width * height);
        System.err.println("MSE = " + mse);
        int maxVal = 255;
        double x = Math.pow(maxVal, 2) / mse;
        double psnr = 10.0 * logbase10(x);
        System.err.println("PSNR = " + psnr);
        return psnr;
    }

    /**
     * Retorna o logaritmo de base 10 de um número
     *
     * @param x
     * @return
     */
    public  double logbase10(double x) {
        return Math.log(x) / Math.log(10);
    }
}
