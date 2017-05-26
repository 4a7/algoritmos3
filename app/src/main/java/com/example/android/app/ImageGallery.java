package com.example.android.app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

public class ImageGallery extends AppCompatActivity {
    HashImages hi;
    ImageView expanded_image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_gallery);
        final GridView gridview = (GridView) findViewById(R.id.grid);
        expanded_image=(ImageView)findViewById(R.id.expanded_image);
        Intent intent = getIntent();
        hi= (HashImages)intent.getSerializableExtra("lasimagenes");
        ImageAdapter ima=new ImageAdapter(this);
        ima .setImagenes(hi.getPaths());
        final ImageAdapter ima2=ima;
        new Thread(new Runnable() {
            public void run() {
                gridview.setAdapter(ima2);
            }
        }).start();


        /*
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Toast.makeText(ImageGallery.this, "" + position,
                        Toast.LENGTH_SHORT).show();
            }
        });
        */
    }
    public ImageView getExpanded_image(){
        return expanded_image;
    }
}
