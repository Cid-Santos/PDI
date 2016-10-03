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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

/**
 *
 * @author Cid
 */
public class Filtros {

    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    String imgS;
    File imgF;

    Mat image;
    Mat output;

    public Filtros(File imgF) {
        this.imgF = imgF;
        this.imgS = imgF.getAbsolutePath();
        IniciaFiltros();
    }

    private void IniciaFiltros() {
        this.output = new Mat(256, 26, CvType.CV_8UC3);
        LoadImagA();
    }

    private void LoadImagA() {
        try {
            BufferedImage imageA = ImageIO.read(imgF);
            byte[] data = ((DataBufferByte) imageA.getRaster().getDataBuffer()).getData();
            image = new Mat(imageA.getHeight(), imageA.getWidth(), CvType.CV_8UC3);
            image.put(0, 0, data);
        } catch (IOException ex) {
            Logger.getLogger(Operacoes.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String aplicarFiltros(int filtro, int ksize, int mx, int my, int sx, int sy) {
        String OutPutImg = "";
        switch (filtro) {
            case 1:
                OutPutImg = media(mx, my);
                break;
            case 2:
                OutPutImg = mediana(ksize);
                break;
            case 3:
                OutPutImg = gaussiano(mx, my, sx, sy);
                break;
            case 4:
                OutPutImg = maximo(mx, my);
                break;
            case 5:
                OutPutImg = minimo(mx, my);
                break;
        }
        return OutPutImg;
    }

    private String media(int mx, int my) {
        Mat img = Imgcodecs.imread(imgS);
        Imgproc.blur(img, output, new Size(mx, my));
        Imgcodecs.imwrite("OutputImg/media.jpg", output);
        return "OutputImg/media.jpg";
    }

    private String mediana(int ksize) {
        Mat img = Imgcodecs.imread(imgS);
        Imgproc.medianBlur(img, output, ksize);
        Imgcodecs.imwrite("OutputImg/mediana.jpg", output);
        return "OutputImg/mediana.jpg";
    }

    private String gaussiano(int mx, int my, int sx, int sy) {
        Mat img = Imgcodecs.imread(imgS);
        Imgproc.GaussianBlur(img, output, new Size(mx, my), sx, sy);
        Imgcodecs.imwrite("OutputImg/gaussian-blur.jpg", output);
        return "OutputImg/gaussian-blur.jpg";
    }

    private String maximo(int mx, int my) {
        Mat img = Imgcodecs.imread(imgS);
        Mat one = Mat.ones(mx, my, CvType.CV_32F);
        Imgproc.dilate(img, output, one);
        Imgcodecs.imwrite("OutputImg/maximo.jpg", output);
        return "OutputImg/maximo.jpg";
    }

    private String minimo(int mx, int my) {
        Mat img = Imgcodecs.imread(imgS);
        Mat one = Mat.ones(mx, my, CvType.CV_32F);
        Imgproc.erode(img, output, one);
        Imgcodecs.imwrite("OutputImg/minimo.jpg", output);
        return "OutputImg/minimo.jpg";
    }

}
