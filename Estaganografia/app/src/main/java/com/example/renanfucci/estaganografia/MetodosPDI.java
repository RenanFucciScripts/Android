package com.example.renanfucci.estaganografia;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.logging.Level;


/**
 * Created by Renan Fucci on 27/11/2015.
 */
public class MetodosPDI {

    public Bitmap esconderMSG(Bitmap image, String texto){
        try {
            int[][] red = bitmapToMatriz(image, "Red");
            int[][] green = bitmapToMatriz(image, "Green");
            int[][] blue = bitmapToMatriz(image, "Blue");
            red = codificarMsgBinária(red,texto);
            Bitmap imgOut = MatrizToBimap(red,green,blue);
            return imgOut;
        }catch (Exception e){
            Log.e("Exception ", e.getLocalizedMessage());
        }
        return null;
    }

    public String mostrarMSG(Bitmap image){
        String texto = "";
        try {
            int[][] red = bitmapToMatriz(image, "Red");
            int[][] green = bitmapToMatriz(image, "Green");
            int[][] blue = bitmapToMatriz(image, "Blue");
            texto = decodificarMsgBin(red);
            return texto;
        }catch (Exception e){
            if(e.getLocalizedMessage()!=null)
                Log.e("Exception ",e.getLocalizedMessage());
            Log.e("mostrarMSG ","Exception");
        }
        texto="Mensagem não encontrada.";
        return texto;
    }

    private static Bitmap MatrizToBimap(int[][] red,int[][] green,int[][] blue){
        Bitmap imgOut = Bitmap.createBitmap(red[0].length, red.length, Bitmap.Config.ARGB_8888);
        for (int i = 0; i < red.length; i++) {
            for (int j = 0; j < red[i].length; j++) {
                imgOut.setPixel(j,i, Color.rgb(red[i][j],green[i][j],blue[i][j]));
            }
        }
        return imgOut;
    }

    private static int[][] bitmapToMatriz(Bitmap image, String bandaRGB) throws Exception {
        int[][] imgOut = new int[image.getHeight()][image.getWidth()];
        for (int i = 0; i < image.getHeight(); i++) {
            for (int j = 0; j < image.getWidth(); j++) {
                int pixel = image.getPixel(j,i);
                if(bandaRGB.contentEquals("Red")){
                    imgOut[i][j] = Color.red(pixel);
                }
                else if(bandaRGB.contentEquals("Green")){
                    imgOut[i][j] =  Color.green(pixel);
                }
                else if(bandaRGB.contentEquals("Blue")){
                    imgOut[i][j] = Color.blue(pixel);
                }
            }
        }
        return imgOut;
    }

    /*
     * Método que utiliza técnica de esteganografia para esconder um texto dentro de uma img
     */
    public static int[][] codificarMsgBinária(int[][] matrizOriginal, String msg){
        try {
            String letra;
            int[][] matrizResult = new int[matrizOriginal.length][matrizOriginal[0].length];
            int[][] matrizMsg = new int[matrizOriginal.length][matrizOriginal[0].length];
            int[][] matrizresult0;
            int[][] matrizresult1;
            int[][] matrizresult2;
            int[][] matrizresult3;
            int[][] matrizresult4;
            int[][] matrizresult5;
            int[][] matrizresult6;
            int[][] matrizresult7;
            //menos signficativo 7 e o mais segnificativo 0
            matrizresult0 = separaMatrizBinária(matrizOriginal, 0);
            matrizresult1 = separaMatrizBinária(matrizOriginal, 1);
            matrizresult2 = separaMatrizBinária(matrizOriginal, 2);
            matrizresult3 = separaMatrizBinária(matrizOriginal, 3);
            matrizresult4 = separaMatrizBinária(matrizOriginal, 4);
            matrizresult5 = separaMatrizBinária(matrizOriginal, 5);
            matrizresult6 = separaMatrizBinária(matrizOriginal, 6);
            matrizresult7 = separaMatrizBinária(matrizOriginal, 7);
            ArrayList<String> vet = new ArrayList<String>();
            for (char c : msg.toCharArray()) {
                letra = Integer.toBinaryString(c);
                letra = StringUtils.leftPad(letra, 8, '0');
                for (char d : letra.toCharArray()) {
                    vet.add(Character.toString(d));
                }
            }
            int cont = 0;
            for (int i = 0; i < matrizresult7.length; i++) {
                for (int j = 0; j < matrizresult7[i].length; j++) {
                    if (vet.size() == cont) {
                        break;
                    } else {
                        matrizMsg[i][j] = Integer.parseInt(vet.get(cont));
                        cont += 1;
                    }
                }
                if (vet.size() == cont) {
                    break;
                }
            }
            matrizResult = juntarMatrizesBináParaUmaMatrizInteira(matrizresult0, matrizresult1, matrizresult2, matrizresult3, matrizresult4, matrizresult5, matrizresult6, matrizMsg);
            return matrizResult;
        }catch (Exception e){
            Log.e("Mensagem Secreta", e.getLocalizedMessage());
        }
        return null;
    }

    /*
     * Método que decodifica codificarMsgBinária
     */
    public static String decodificarMsgBin(int[][] matrizOriginal){
        int[][] matrizAux= new int[matrizOriginal.length][matrizOriginal[0].length];
        int cont=0;
        String palavra="";
        String texto = "";
        int charCode;
        matrizAux=separaMatrizBinária(matrizOriginal, 7);
        for (int i = 0; i <matrizAux.length; i++) {
            for (int j = 0; j < matrizAux[i].length; j++) {
                if(cont<8){
                    cont+=1;
                    palavra += Integer.toString( matrizAux[i][j]);
                }
                else{
                    if(! palavra.contentEquals("00000000")){
                        charCode = Integer.parseInt(palavra, 2);
                        String x= (new Character((char)charCode).toString());
                        texto+= x;
                        palavra=""+matrizAux[i][j];
                        cont=1;
                    }
                }
            }
        }
        return texto;
    }

    /*
     * Método que separa uma imagem normal para uma imagem binária de pos(n)
     */
    public static int[][] separaMatrizBinária(int[][] matriz, int pos){
        int[][] matrizBin = new int[matriz.length][matriz[0].length];
        String x;
        for (int i = 0; i < matriz.length; i++) {
            for (int j = 0; j < matriz[i].length; j++) {
                x=Integer.toBinaryString(matriz[i][j]);
                x=StringUtils.leftPad(x, 8,"0");
                x=x.substring(pos, pos+1);
                matrizBin[i][j]=(Integer.parseInt(x));
            }
        }
        return matrizBin;
    }

	/*
	 * Método que junta 8 imagens binária em uma imagem normal
	 */
    public static int[][] juntarMatrizesBináParaUmaMatrizInteira(int[][] matriz, int[][] matriz1,int[][] matriz2,int[][] matriz3,int[][] matriz4,int[][] matriz5,int[][] matriz6,int[][] matriz7){
        int[][] matrizInt = new int[matriz.length][matriz[0].length];
        String x;
        for (int i = 0; i < matrizInt.length; i++) {
            for (int j = 0; j < matrizInt[i].length; j++) {
                x="";
                x+=Integer.toString(matriz[i][j]);
                x+=Integer.toString(matriz1[i][j]);
                x+=Integer.toString(matriz2[i][j]);
                x+=Integer.toString(matriz3[i][j]);
                x+=Integer.toString(matriz4[i][j]);
                x+=Integer.toString(matriz5[i][j]);
                x+=Integer.toString(matriz6[i][j]);
                x+=Integer.toString(matriz7[i][j]);
                matrizInt[i][j]=Integer.parseInt(x,2);
            }
        }
        return matrizInt;
    }
}