package com.example.android.app;

import java.util.ArrayList;

/**
 * Created by Juan on 28/05/2017.
 */

public class Espacio {
    private ArrayList<Plano> planos;
    private int k;
    private int plano_size;
    int[]hash;
    int num_hash;
    public Espacio(int k,int size){
        this.k=k;
        plano_size=size;
        hash=new int[k];
        planos=new ArrayList<Plano>();
        for(int i=0;i<k;i++){
            planos.add(new Plano(plano_size));
        }
    }
    public int getHash(int[]vector){
        num_hash=0;
        int pot,pp;
        for(int i=0;i<k;i++){
            pot=(int)Math.pow(2,i);
            pp=planos.get(i).pp(vector);
            if(pp>=0){
                hash[i]=0;
            }
            else{
                hash[i]=1;
                num_hash+=pot;
            }
        }
        return num_hash;
    }
}
