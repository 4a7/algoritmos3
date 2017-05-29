package com.example.android.app;

import android.graphics.Bitmap;
import android.graphics.Color;

/**
 * Created by Juan on 27/05/2017.
 */

public class LBP {
    Bitmap bmp;
    private int intArray[];
    private int width;
    private int height;
    int hash[] = new int[256];
    public LBP(Bitmap imagen){
        this.bmp=imagen;
        width=bmp.getWidth();
        height=bmp.getHeight();
        intArray = new int[width*height];
        System.out.println("WH "+width+" "+height);
        bmp.getPixels(intArray, 0, width, 0, 0, width, height);

    }
    public int[] LBP(){
        //intarray queda con el vector con el histograma
        //buscar como adaptarlo
        double r=0.21;
        double g=0.72;
        double b=0.07;
        int red,green,blue;

        int gris;
        int suma;
        int x,y;
        int fuente;
        int valor;
        int indice;
        int hash_pixel[]=new int[8];
        int potencias[]={128,64,32,16,8,4,2,1};
        for(int i=0;i<height;i++){
            for(int j=0;j<width;j++){
                fuente=bmp.getPixel(j,i);
                red= Color.red(fuente);
                green=Color.green(fuente);
                blue=Color.blue(fuente);
                int cont=0;
                fuente=(int)((red*r)+(green*g)+(blue*b));
                for(int k=-1;k<2;k++){
                    for(int l=-1;l<2;l++){
                        x=i+k;
                        y=j+l;

                        indice=(3*(k+1))+(l+1);
                        if(indice>=5){
                            indice--;
                        }
                        hash_pixel[indice]=0;
                        //System.out.println("* "+k+" "+" "+l+" "+x+" "+y+" "+(k!=0 &&l!=0)+" "+(x>=0&&y>=0&&x<height&&y<width));

                        if((!(k==0 &&l==0))&&(x>=0&&y>=0&&x<height&&y<width)){
                            cont++;
                            valor=bmp.getPixel(y,x);
                            red= Color.red(valor);
                            green=Color.green(valor);
                            blue=Color.blue(valor);
                            valor=(int)((red*r)+(green*g)+(blue*b));
                            //System.out.println(fuente+" "+valor+" "+x+" "+y);
                            if(valor>=fuente){
                                hash_pixel[indice]=1;
                            }
                        }

                    }
                }
                //System.out.println(cont);
                suma=0;
                String hashs="";
                for(int u=0;u<8;u++){
                    hashs+=Integer.toString(hash_pixel[u]);
                    suma+=(potencias[u]*hash_pixel[u]);
                }
                hash[suma]+=1;
                //System.out.println(hashs+" "+Integer.toString(suma)+" "+j+" "+i);

            }
        }
        String res="";
        for(int i=0;i<256;i++){
            res+=Integer.toString(hash[i])+"*";
        }
        //System.out.println(res);
        return hash;

    }

}
