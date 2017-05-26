package com.example.android.app;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class Gallery extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private ListView mainListView ;
    private ArrayAdapter<String> listAdapter ;
    private Hashes hash;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String nombre;
        int codigo;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        Intent intent = getIntent();
        // intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
        hash= (Hashes)intent.getSerializableExtra("elhash");

        mainListView=(ListView)findViewById(R.id.lista);
        mainListView.setOnItemClickListener(this);
        String[] planets = new String[] { "Mercury", "Venus", "Earth", "Mars",
                "Jupiter", "Saturn", "Uranus", "Neptune"};
        ArrayList<String>nombres=new ArrayList<String>();
        ArrayList<Integer>codigos=new ArrayList<Integer>();
        for(HashImages h:hash.getHashes()){
            nombre=h.getBucketName();
            codigo=h.getHash();
            nombres.add(nombre);
            codigos.add(codigo);
        }

        listAdapter = new ArrayAdapter<String>(this, R.layout.simplerow, nombres);
        mainListView.setAdapter( listAdapter );





    }
    public void onItemClick(AdapterView<?> l, View v, int position, long id) {
        Log.i("HelloListView", "You clicked Item: " + id + " at position:" + position);
        // Then you start a new Activity via Intent
        /*
        Intent intent = new Intent();
        intent.setClass(this, ListItemDetail.class);
        intent.putExtra("position", position);
        // Or / And
        intent.putExtra("id", id);

        */
        HashImages hi=hash.getHashes().get(position);

        Intent intent = new Intent(this,ImageGallery.class);
        intent.putExtra("lasimagenes", hi);
        startActivity(intent);
    }
}
