/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ctPrincipal;

import java.awt.Color;
import java.awt.image.BufferedImage;
import org.opencv.core.Mat;

/**
 *
 * @author Cid
 */
public class Quantizar {

    private BufferedImage biOriginal; //Imagem original enviada para a classe

    private BufferedImage biResultado;//Imagem resultado da aplicacao da mascara

    private int quantizacao;//Quantidade de tons que ira representar

    private boolean flagEscalaCinza;//flag que indica se a imagem em manipulacao atual em  RGB ou a em escala de cinza
    
    Mat output;

    public Quantizar() {
        this.init();
    }

    public BufferedImage getBiOriginal() {
        return biOriginal;
    }

    
    public void setBiOriginal(BufferedImage biOriginal) {
        this.biOriginal = biOriginal;
    }

    public BufferedImage getBiResultado() {
        return biResultado;
    }

    public void setBiResultado(BufferedImage biResultado) {
        this.biResultado = biResultado;
    }

    public boolean isFlagEscalaCinza() {
        return flagEscalaCinza;
    }

    public void setFlagEscalaCinza(boolean flagEscalaCinza) {
        this.flagEscalaCinza = flagEscalaCinza;
    }

    public int getQuantizacao() {
        return quantizacao;
    }

    /**
     * 
     * @param quantizacao 
     */
    public void setQuantizacao(int quantizacao) {
        if (quantizacao <= 1) {
            this.quantizacao = 2;
        } else if (quantizacao >= 255) {
            this.quantizacao = 254;
        } else {
            this.quantizacao = quantizacao;
        }
    }

    /**
     * 
     * @return 
     */
    public BufferedImage doWork() {
        // criando um novo buffer para saida do resultado
        this.setBiResultado(new BufferedImage(this.getBiOriginal().getWidth(), this.getBiOriginal().getHeight(), this.getBiOriginal().getType()));

        int yIni = 0;
        int yEnd = this.getBiOriginal().getHeight(null) - 1;
        while (true) {
            if (yEnd < yIni) {
                break;
            }
            int xIni = 0;
            int xEnd = this.getBiOriginal().getWidth(null) - 1;
            while (true) {
                if (xEnd < xIni) {
                    break;
                }
                //calcula a nova cor do pixel
                Color colorSE = this.calcula(xIni, yIni);
                //seta a nova cor do pixel para a imagem
                this.getBiResultado().setRGB(xIni, yIni, colorSE.getRGB());
                //calcula a nova cor do pixel
                Color colorSD = this.calcula(xIni, yEnd);
                // seta a nova cor do pixel para a imagem
                this.getBiResultado().setRGB(xIni, yEnd, colorSD.getRGB());
                //calcula a nova cor do pixel
                Color colorIE = this.calcula(xEnd, yIni);
                // seta a nova cor do pixel para a imagem
                this.getBiResultado().setRGB(xEnd, yIni, colorIE.getRGB());
                //calcula a nova cor do pixel
                Color colorID = this.calcula(xEnd, yEnd);
                //seta a nova cor do pixel para a imagem
                this.getBiResultado().setRGB(xEnd, yEnd, colorID.getRGB());
                xIni++;
                xEnd--;
            }
            yIni++;
            yEnd--;
        }

        // retorna a imagem resultado
        return this.getBiResultado();
    }

    /**
     * Metodo de inicializacao dos atributos da classe em uma nova instancia de
     * um objeto
     */
    private void init() {
        this.setFlagEscalaCinza(false);
        this.setBiOriginal(null);
        this.setBiResultado(null);
        // representa a imagem em apenas 2 tons de cor
        this.setQuantizacao(2);
    }

    /**
     * Metodo que realiza o calculo da nova cor do pixel
     *
     * @param int x
     * @param int y
     * @return Color - nova cor do pixel calculado
     */
    private Color calcula(int x, int y) {

        // quantidade total de tons da imagem
        int nt = (int) Math.pow(2, 8);
        // fragmenta??o da quantidade total em c?lulas de determinado valor
        int celula = (nt / this.getQuantizacao()) - 1;

        // recupera a cor do pixel da posi??o x,y da imagem origirnal
        Color pixel = new Color(this.getBiOriginal().getRGB(x, y));

        // verifica se a imagem esta em escala de cinza
        if (this.isFlagEscalaCinza()) {

            int c = new Color(this.getBiOriginal().getRGB(x, y)).getRed();

            boolean bC = true;

            for (int n = (this.getQuantizacao() - 1); n >= 0; n--) {
                if (n == 0) {
                    if (c <= celula) {
                        c = 0;
                    }
                } else if (n == (this.getQuantizacao() - 1)) {
                    if (c > (celula * n)) {
                        c = 255;
                    }
                } else {
                    if ((c > (celula * n)) && (bC)) {
                        c = (celula * (n + 1));
                        bC = false;
                    }
                }
            }
            // setando a nova cor do pixel
            pixel = new Color(c, c, c);

            // caso a imagem nao esteja em escala de cinza    
        } else {

            int r = new Color(this.getBiOriginal().getRGB(x, y)).getRed();
            int g = new Color(this.getBiOriginal().getRGB(x, y)).getGreen();
            int b = new Color(this.getBiOriginal().getRGB(x, y)).getBlue();

            boolean bR, bG, bB;
            bR = bG = bB = true;

            for (int n = (this.getQuantizacao() - 1); n >= 0; n--) {
                if (n == 0) {
                    if (r <= celula) {
                        r = 0;
                    }
                    if (g <= celula) {
                        g = 0;
                    }
                    if (b <= celula) {
                        b = 0;
                    }
                } else if (n == (this.getQuantizacao() - 1)) {
                    if (r > (celula * n)) {
                        r = 255;
                    }
                    if (g > (celula * n)) {
                        g = 255;
                    }
                    if (b > (celula * n)) {
                        b = 255;
                    }
                } else {
                    if ((r > (celula * n)) && (bR)) {
                        r = (celula * (n + 1));
                        bR = false;
                    }
                    if ((g > (celula * n)) && (bG)) {
                        g = (celula * (n + 1));
                        bG = false;
                    }
                    if ((b > (celula * n)) && (bB)) {
                        b = (celula * (n + 1));
                        bB = false;
                    }
                }
            }
            // setando a nova cor do pixel
            pixel = new Color(r, g, b);
        }
        return pixel;
    }

}
