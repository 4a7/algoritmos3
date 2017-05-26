package com.example.android.app;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Juan on 25/05/2017.
 */

public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<String> imagenes;
    private Bitmap bitMapObj;
    final int THUMBSIZE = 100;
    public ImageAdapter(Context c) {
        mContext = c;
    }

    public void setImagenes(ArrayList<String> imagenes) {
        this.imagenes = imagenes;
    }
    public int getCount() {
        return imagenes.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageButton imageView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageButton(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(200,200));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(1, 1, 1, 1);
        } else {
            imageView = (ImageButton) convertView;
        }
        LoadImages li=new LoadImages();
        li.setImageButton(imageView);
       li.execute(imagenes.get(position));




        return imageView;
    }

}

class LoadImages extends AsyncTask<String, Integer,Long> {
    ImageButton btn;
    Bitmap bmp=null;
    int THUMBSIZE=64;
    protected Long doInBackground(String... urls) {
        long j=0;
        int count = urls.length;
        for (int i = 0; i < count; i++) {
            File fileObj = new  File(urls[i]);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize=32;
        /*final Bitmap bitMapObj= BitmapFactory.decodeFile(fileObj.getAbsolutePath(),options);*/
           bmp = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(fileObj.getAbsolutePath()),
                    THUMBSIZE, THUMBSIZE);

        }
        return j;
    }

    protected void onProgressUpdate(Integer... progress) {
        //setProgressPercent(progress[0]);
    }

    protected void onPostExecute(Long result) {
        btn.setImageBitmap(bmp);
    }
    public void setImageButton(ImageButton i){
        btn=i;
    }
}
