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
import static org.opencv.core.Core.normalize;
import static org.opencv.core.Core.randn;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

/**
 *
 * @author Cid
 */
public class Ruidos {

    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    String img;
    File imgF;

    Mat image;
    Mat output;

    public Ruidos(File imgF) {
        this.imgF = imgF;
        this.img = imgF.getAbsolutePath();
        IniciaOperacoes();
    }

    private void IniciaOperacoes() {
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

    public String aplicarRuidos(int ruido, int inA, int inB) {
        String OutPutImg = "";
        switch (ruido) {
            case 1:
                OutPutImg = ruidoGaussiano(inA, inB);
                break;
            case 2:
                OutPutImg = ruidoSalPimenta(inA, inB);
                break;
        }
        return OutPutImg;
    }

    private String ruidoGaussiano(int mean, int desv) {
        Mat original_Bgr = image.clone();
        Mat mGaussian_noise = new Mat(original_Bgr.size(), original_Bgr.type());
        randn(mGaussian_noise, mean, desv);
        for (int m = 0; m < original_Bgr.rows(); m++) {
            for (int n = 0; n < original_Bgr.cols(); n++) {
                double[] val = new double[3];
                for (int i = 0; i < original_Bgr.get(m, n).length; i++) {
                    val[i] = original_Bgr.get(m, n)[i] + mGaussian_noise.get(m, n)[i];
                }
                original_Bgr.put(m, n, val);
            }
        }
        normalize(original_Bgr, original_Bgr, 0, 255, Core.NORM_MINMAX, CvType.CV_8UC3);
        Imgcodecs.imwrite("OutputImg/gaussian.jpg", original_Bgr);
        return "OutputImg/gaussian.jpg";
    }

    private String ruidoSalPimenta(int min, int max) {
        Mat saltPepper_img = image.clone();
        Mat mSaltPepper_noise = new Mat(saltPepper_img.size(), saltPepper_img.type());
        randn(mSaltPepper_noise, 0, 255);
        for (int m = 0; m < saltPepper_img.rows(); m++) {
            for (int n = 0; n < saltPepper_img.cols(); n++) {
                double[] val = new double[3];
                if (mSaltPepper_noise.get(m, n)[0]
                        < min && mSaltPepper_noise.get(m, n)[1]
                        < min && mSaltPepper_noise.get(m, n)[2]
                        < min) {
                    for (int i = 0; i < saltPepper_img.get(m, n).length; i++) {
                        val[i] = 0;
                    }
                    saltPepper_img.put(m, n, val);
                }
                if (mSaltPepper_noise.get(m, n)[0]
                        > max && mSaltPepper_noise.get(m, n)[1]
                        > max && mSaltPepper_noise.get(m, n)[2]
                        > max) {
                    for (int i = 0; i < saltPepper_img.get(m, n).length; i++) {
                        val[i] = 255;
                    }
                    saltPepper_img.put(m, n, val);
                }
            }
        }
        normalize(saltPepper_img, saltPepper_img, 0, 255, Core.NORM_MINMAX, CvType.CV_8UC3);
        Imgcodecs.imwrite("OutputImg/saltpepper.jpg", saltPepper_img);
        return "OutputImg/saltpepper.jpg";
    }
}
