package com.example.android.app;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Juan on 24/05/2017.
 */

public class Hashes implements Serializable{
    private ArrayList<HashImages>hashes;
    private boolean lsh;
    public Hashes(boolean lsh){
        hashes = new ArrayList<HashImages>();
        this.lsh=lsh;
    }
    public void addHash(int hash){
        HashImages h=new HashImages(hash);
        hashes.add(h);
    }
    public void addImage(int hash,String path){
        boolean existe=false;
        for(HashImages h:hashes){
            if(h.getHash()==(hash)){
                h.addImage(path);
                existe=true;
                break;
            }
        }
        if(!existe){
            HashImages h=new HashImages(hash);
            h.addImage(path);
            hashes.add(h);
        }

    }
    public ArrayList<String> getHashes(int hash){
        for(HashImages h:hashes){
            if(h.getHash()==hash){
                return h.getPaths();
            }

        }
        return null;
    }
    @Override
    public String toString(){
        String r="";
        for(HashImages hi:hashes){
            r+="Bucket: "+hi.getBucketName()+" ";
            for(String img:hi.getPaths()){
                r+="-Imagen: "+img+" ";
            }

        }
        return r;
    }

    public boolean isLsh() {
        return lsh;
    }

    public ArrayList<HashImages> getHashes() {
        return hashes;
    }
}
