/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ctFourierTransform;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;

/**
 *
 * @author Cid
 */
public class GrayScaleBiniriaImage {

    public static BufferedImage criaImagemCinza(BufferedImage imgJPEG) {
        // Cria um novo Buffer para BYTE GRAY
        BufferedImage img = new BufferedImage(imgJPEG.getWidth(), imgJPEG.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
        WritableRaster raster = img.getRaster();
        WritableRaster rasterJPEG = imgJPEG.getRaster();
        // Para cada Pixel realiza a transformação para tons de cinza e joga na nova imagem.
        for (int h = 0; h < 256; h++) {
            for (int w = 0; w < 256; w++) {
                int[] p = new int[4];
                rasterJPEG.getPixel(w, h, p);
                p[0] = (int) (0.3 * p[0]);
                p[1] = (int) (0.59 * p[1]);
                p[2] = (int) (0.11 * p[2]);
                int y = p[0] + p[1] + p[2];
                raster.setSample(w, h, 0, y);
            }
        }
        return img;
    }

    public static BufferedImage criaImagemBinaria(BufferedImage imgJPEG) {
        // Cria um novo buffer para binário
        BufferedImage img = new BufferedImage(imgJPEG.getWidth(), imgJPEG.getHeight(), BufferedImage.TYPE_BYTE_BINARY);
        WritableRaster raster = img.getRaster();
        WritableRaster rasterPB = criaImagemCinza(imgJPEG).getRaster();
        // Para cada pixel realiza a verificação de se é mais cinza ou mais branco decidindo se o pixel será branco ou preto.
        for (int h = 0; h < 256; h++) {
            for (int w = 0; w < 256; w++) {
                int[] p = new int[1];
                rasterPB.getPixel(w, h, p);
                if (p[0] > 127) {
                    raster.setSample(w, h, 0, 1);
                } else {
                    raster.setSample(w, h, 0, 0);
                }
            }
        }
        return img;
    }

    public static BufferedImage criaImagemRGB(BufferedImage imgJPEG, int tipo) {
        // Cria um novo Buffer para RGB
        BufferedImage img = new BufferedImage(imgJPEG.getWidth(), imgJPEG.getHeight(), BufferedImage.TYPE_INT_RGB);

        WritableRaster rasterT = img.getRaster();
        WritableRaster raster = imgJPEG.getRaster();
        // Para cada pixel pega a componente desejada e utiliza somente a mesma na nova imagem         
        for (int h = 0; h < 256; h++) {
            for (int w = 0; w < 256; w++) {
                int[] p = new int[4];
                raster.getPixel(w, h, p);
                rasterT.setSample(w, h, tipo - 1, p[tipo - 1]);
            }
        }
        return img;
    }

}
