package com.example.android.app;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Juan on 24/05/2017.
 */

public class HashImages implements Serializable{
    private int hash;
    private String bucketName;
    private ArrayList<String> paths;
    public HashImages(int hash){
        paths = new ArrayList<String>();
        this.hash=hash;
        bucketName=Integer.toString(hash);
    }
    public int getHash(){
        return hash;
    }
    public void addImage(String path){
        paths.add(path);
    }
    public ArrayList<String> getPaths(){
        return paths;
    }
    public String getBucketName(){
        return bucketName;
    }
    public void setBucketName(String bucket_name){
        bucketName=bucket_name;
    }
}
