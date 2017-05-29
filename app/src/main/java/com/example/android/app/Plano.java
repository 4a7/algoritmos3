package com.example.android.app;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Juan on 28/05/2017.
 */

public class Plano {
    int Size=256;
    int MAX=2000;
    int []plano;
    public Plano(int size){
        this.Size=size;
        int numero;

        plano=new int[Size];
        int MIN=-1*MAX;
        for(int i=0;i<Size;i++){
            Random rand=new Random();
            //numero=r.nextInt(MAX - -1*MAX) + MAX;
            plano[i] = rand.nextInt((MAX - MIN) + 1)+MIN;
        }
    }
    public int pp(int[]vector){
        int suma=0;
        int numero;
        for(int i=0;i<Size;i++){
            numero=(vector[i]*plano[i]);

            suma+=numero;
        }
        return suma;
    }

}
