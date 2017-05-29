package com.example.android.app;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;

public class Gallery extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private ListView mainListView ;
    private ArrayAdapter<String> listAdapter ;
    private Hashes hash;
    private String m_Text = "";
    ArrayList<String>nombres;
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
        mainListView.setLongClickable(true);
        mainListView.setClickable(true);
        mainListView.setOnItemClickListener(this);
        mainListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final int position2=position;

                AlertDialog.Builder builder = new AlertDialog.Builder(Gallery.this);
                builder.setTitle("Seleccionar nuevo nombre");

// Set up the input
                final EditText input = new EditText(Gallery.this);
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);

// Set up the buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        m_Text = input.getText().toString();
                        HashImages hi=hash.getHashes().get(position2);
                        hi.setBucketName(m_Text);
                        System.out.println(hi.getBucketName());
                        View v = mainListView.getChildAt(position2);

                        if(v == null) {
                            System.out.println("NULL");
                            return;
                        }

                        TextView someText = (TextView) v.findViewById(R.id.rowTextView);

                        someText.setText(m_Text);
                        System.out.println("000 "+someText.getText());
                        saveHashToFile(hash.isLsh(),hash);
                        nombres.set(position2,m_Text);
                        listAdapter.notifyDataSetChanged();


                    }
                });
                builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
                return false;
            }
        });

        String[] planets = new String[] { "Mercury", "Venus", "Earth", "Mars",
                "Jupiter", "Saturn", "Uranus", "Neptune"};
        nombres=new ArrayList<String>();
        ArrayList<Integer>codigos=new ArrayList<Integer>();
        for(HashImages h:hash.getHashes()){
            nombre=h.getBucketName();
            codigo=h.getHash();
            System.out.println(nombre+" "+codigo);
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
    public void saveHashToFile(boolean lsh,Hashes hash){
        String fileName;
        if(lsh){
            fileName=MainActivity.lsh_name;
        }
        else {
            fileName=MainActivity.lbp_name;
        }
        System.out.println(getApplicationContext().getFilesDir().toString());
        File file = new File(getApplicationContext().getFilesDir(), fileName);
        try{
            FileOutputStream fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(hash);
            oos.close();
            MainActivity.LBP=readHashFromFile(false);
            MainActivity.LSH=readHashFromFile(true);
        }
        catch (Exception e){
                System.out.println("**************************");
        }

    }

    public  Hashes readHashFromFile(boolean lsh){
        String fileName;
        Hashes hash=null;
        if(lsh){
            fileName=MainActivity.lsh_name;
        }
        else {
            fileName=MainActivity.lbp_name;
        }
        System.out.println(getApplicationContext().getFilesDir().toString());
        File file = new File(getApplicationContext().getFilesDir(), fileName);
        try{

            FileInputStream fis = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(fis);
            hash = (Hashes) ois.readObject();
            ois.close();
        }
        catch(Exception e){
            System.out.println("ERROR EN LECTURA DE ARCHIVO HASH");
        }
        return hash;
    }

}
