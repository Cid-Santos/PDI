/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ctPrincipal;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;

/**
 *
 * @author Cid
 */
public class Graficos {

    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }
    String[] arquivos;
    Mat ruido;
    Mat image2;
    ArrayList<Mat> images = new ArrayList();

    public Graficos(String[] listImg) {
        this.arquivos = listImg;
        this.ruido = new Mat(256, 256, CvType.CV_8UC3);
    }

    private Mat LoadImagB(String imgS) {
        try {
            File imgF2 = new File(imgS);
            BufferedImage imageB = ImageIO.read(imgF2);
            byte[] data = ((DataBufferByte) imageB.getRaster().getDataBuffer()).getData();
            image2 = new Mat(imageB.getHeight(), imageB.getWidth(), CvType.CV_8UC3);
            image2.put(0, 0, data);
        } catch (IOException ex) {
            Logger.getLogger(Operacoes.class.getName()).log(Level.SEVERE, null, ex);
        }

        return image2;
    }

    public String desvioPadrao() {
        String resultImgOutput = "";
        double[] tmp = new double[arquivos.length];

        for (String s : arquivos) {
            images.add(LoadImagB(s));
        }

        double[][] stdDev = new double[images.get(0).rows()][images.get(0).cols()];

        for (int i = 0; i < images.get(0).rows(); i++) {
            for (int j = 0; j < images.get(0).cols(); j++) {
                for (int k = 0; k < arquivos.length; k++) {
                    tmp[k] = images.get(k).get(i, j)[0];
                }
                double tmpDev = Math.sqrt(somatorio(tmp) / arquivos.length);
                stdDev[i][j] = tmpDev;
            }
        }

        ruido = new Mat(new Size((int) images.get(0).cols(), (int) images.get(0).rows()), CvType.CV_8UC1);

        double[][] d = normalizacao(stdDev);

        Imgcodecs.imwrite("OutputImg/ruido.jpg", ruido);
        return resultImgOutput = "OutputImg/ruido.jpg";
    }

    public double[][] normalizacao(double[][] matriz) {
        double max = maxValue(matriz);
        double min = minValue(matriz);
        double dis = 255 / (max - min);
        double[][] result = matriz.clone();
        for (int i = 0; i < result.length; i++) {
            for (int j = 0; j < result[0].length; j++) {
                result[i][j] *= dis;
                ruido.put(i, j, result[i][j]);
            }
        }
        return result;
    }

    public double maxValue(double[][] matriz) {
        double max = Double.MIN_VALUE;
        for (double[] linha : matriz) {
            for (double d : linha) {
                if (d > max) {
                    max = d;
                }
            }
        }
        return max;
    }

    public double minValue(double[][] matriz) {
        double min = Double.MAX_VALUE;
        for (double[] linha : matriz) {
            for (double d : linha) {
                if (d < min) {
                    min = d;
                }
            }
        }
        return min;
    }

    public double somatorio(double[] array) {
        double sum = 0;
        double media = media(array);

        for (double d : array) {
            double x = Math.pow((d - media), 2);
            sum += x;
        }
        return sum;
    }

    public double media(double[] array) {
        double sum = 0;
        for (double d : array) {
            sum += d;
        }
        return sum / array.length;
    }

    public double calculaMedia() {
        Scalar d = Core.mean(ruido);
        return d.val[0];
    }

    CategoryDataset createDataset() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (int i = 0; i < images.get(0).cols(); i++) {
            double med1 = 0;
            double med2 = 0;
            for (int x = 0; x < images.size(); x++) {
                med1 += (images.get(x).get(0, i)[0]);
                med2 += (images.get(x).get(0, i)[0]);
            }
            med1 /= images.size();
            med2 /= images.size();

            dataset.addValue(med1 - ruido.get(0, i)[0], "Media - Ruído", "Pixel" + i);
            dataset.addValue(med2 + ruido.get(0, i)[0], "Media + Ruído", "Pixel" + i);
        }

        return dataset;

    }

    
}
