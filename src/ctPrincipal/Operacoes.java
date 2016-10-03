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
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

/**
 *
 * @author Cid
 */
public final class Operacoes {

    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    String imgS1, imgS2;
    File imgF1, imgF2;

    Mat image1;
    Mat image2;

    Mat image1bin;
    Mat image2bin;

    Mat output;

    public Operacoes(File imgF1, File imgF2) {
        this.imgF1 = imgF1;
        this.imgF2 = imgF2;
        this.imgS1 = imgF1.getAbsolutePath();
        this.imgS2 = imgF2.getAbsolutePath();
        IniciaOperacoes();
    }

    private void IniciaOperacoes() {
        this.output = new Mat(256, 256, CvType.CV_8UC3);
        LoadImagA();
        LoadImagB();
    }

    private void LoadImagB() {
        try {
            BufferedImage imageB = ImageIO.read(imgF2);
            byte[] data = ((DataBufferByte) imageB.getRaster().getDataBuffer()).getData();
            image2 = new Mat(imageB.getHeight(), imageB.getWidth(), CvType.CV_8UC3);
            image2.put(0, 0, data);
            this.image2bin = new Mat(imageB.getHeight(), imageB.getWidth(), CvType.CV_8UC1);
            Imgproc.cvtColor(image2, image2bin, Imgproc.COLOR_RGB2GRAY);
        } catch (IOException ex) {
            Logger.getLogger(Operacoes.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void LoadImagA() {
        try {
            BufferedImage imageA = ImageIO.read(imgF1);
            byte[] data = ((DataBufferByte) imageA.getRaster().getDataBuffer()).getData();
            image1 = new Mat(imageA.getHeight(), imageA.getWidth(), CvType.CV_8UC3);
            image1.put(0, 0, data);
            this.image1bin = new Mat(imageA.getHeight(), imageA.getWidth(), CvType.CV_8UC1);
            Imgproc.cvtColor(image1, image1bin, Imgproc.COLOR_RGB2GRAY);
        } catch (IOException ex) {
            Logger.getLogger(Operacoes.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    String realizarOperacoes(int op) {
        String resultImgOutput = "";
        switch (op) {
            case 1://and
                imagemBinaria();
                Core.bitwise_and(image1bin, image2bin, output);
                normalizarBinario();
                Imgcodecs.imwrite("OutputImg/and.jpg", output);
                resultImgOutput = "OutputImg/and.jpg";
                break;
            case 2://or
                imagemBinaria();
                Core.bitwise_or(image1bin, image2bin, output);
                normalizarBinario();
                Imgcodecs.imwrite("OutputImg/or.jpg", output);
                resultImgOutput = "OutputImg/or.jpg";
                break;
            case 3://xor
                imagemBinaria();
                Core.bitwise_xor(image1bin, image2bin, output);
                normalizarBinario();
                Imgcodecs.imwrite("OutputImg/xor.jpg", output);
                resultImgOutput = "OutputImg/xor.jpg";
                break;
            case 4://not
                imagemBinaria();
                Core.bitwise_not(image2bin, output);
                normalizarBinario();
                Imgcodecs.imwrite("OutputImg/not.jpg", output);
                resultImgOutput = "OutputImg/not.jpg";
                break;
            case 5://soma
                Core.add(image1, image2, output);
                Imgcodecs.imwrite("OutputImg/soma.jpg", output);
                resultImgOutput = "OutputImg/soma.jpg";
                break;
            case 6://subtracao
                Core.subtract(image1, image2, output);
                Imgcodecs.imwrite("OutputImg/subtracao.jpg", output);
                resultImgOutput = "OutputImg/subtracao.jpg";
                break;
            case 7:// multiplicacao
                Core.multiply(image1, image2, output);
                Imgcodecs.imwrite("OutputImg/multiplicacao.jpg", output);
                resultImgOutput = "OutputImg/multiplicacao.jpg";
                break;
            case 8://divisao
                Core.divide(image1, image2, output);
                Imgcodecs.imwrite("OutputImg/divisao.jpg", output);
                resultImgOutput = "OutputImg/divisao.jpg";
                break;
        }

        return resultImgOutput;
    }

    void imagemBinaria() {
        for (int i = 0; i < image1bin.rows(); i++) {
            for (int j = 0; j < image1bin.cols(); j++) {
                if (image1bin.get(i, j)[0] > 128) {
                    image1bin.put(i, j, 0);
                } else {
                    image1bin.put(i, j, 1);
                }
                if (image2bin.get(i, j)[0] > 128) {
                    image2bin.put(i, j, 0);
                } else {
                    image2bin.put(i, j, 1);
                }
            }
        }
    }

    void normalizarBinario() {
        for (int i = 0; i < output.rows(); i++) {
            for (int j = 0; j < output.cols(); j++) {
                if (output.get(i, j)[0] > 0) {
                    output.put(i, j, 0);
                } else {
                    output.put(i, j, 255);
                }
            }
        }
    }
}
