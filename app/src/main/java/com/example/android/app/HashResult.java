package com.example.android.app;

/**
 * Created by Juan on 24/05/2017.
 */

public class HashResult {
    private int lsh;
    private int lbp;
    public HashResult(int lsh,int lbp){
        this.lsh=lsh;
        this.lbp=lbp;
    }
    HashResult(){
        lsh=0;
        lbp=0;
    }

    public int getLsh() {
        return lsh;
    }

    public int getLbp() {
        return lbp;
    }

    public void setLsh(int lsh) {
        this.lsh = lsh;
    }

    public void setLbp(int lbp) {
        this.lbp = lbp;
    }
}
