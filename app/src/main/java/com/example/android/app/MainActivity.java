package com.example.android.app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private static String lsh_name="lsh.dat";
    private static String lbp_name="lbp.dat";
    private Hashes LSH;
    private Hashes LBP;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final String TAG = MainActivity.class.getSimpleName();
    String mCurrentPhotoPath;
    public static final String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
    static final int REQUEST_TAKE_PHOTO = 1;
    private ImageView mImageView;
    private Button btnLSH;
    private Button btnLBP;
    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String First = "firstKey";
    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }



    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }

            //mImageView = (ImageView) findViewById(R.id.imageView);
            //mImageView.setImageBitmap(BitmapFactory.decodeFile(mCurrentPhotoPath));


        }
    }


    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //se deben crear la primera vez que se corren
        SharedPreferences sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String first = sharedpreferences.getString(First, null);
        if(first==null){
            Hashes lb=new Hashes(false);
            Hashes ls=new Hashes(true);
            //lb.addImage(1,"abc.def");
            //lb.addImage(2,"agf.if");
            //ls.addImage(12,"ahd.fyf");
            //ls.addImage(13,"hgu.ftr");
            saveHashToFile(true,ls);
            saveHashToFile(false,lb);
            System.out.println("PRIMERA CORRIDA");
        }

        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(First, "false");



        //


        LBP=readHashFromFile(false);
        LSH=readHashFromFile(true);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setImageResource(R.drawable.camera);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();


            }
        });
        btnLBP=(Button)findViewById(R.id.btnLBP);
        btnLBP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Gallery.class);
                intent.putExtra("elhash", LBP);
                startActivity(intent);
            }
        });

        btnLSH=(Button)findViewById(R.id.btnLSH);
        btnLSH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Gallery.class);
                intent.putExtra("elhash", LSH);
                startActivity(intent);
            }
        });

        /*
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dispatchTakePictureIntent();
                Intent intent = new Intent(this, Gallery.class);




            }
        });
        */

        // Example of a call to a native method
        TextView tv = (TextView) findViewById(R.id.sample_text);
        tv.setText(stringFromJNI());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        HashResult hr=new HashResult();
        System.out.println(LBP.toString());
        LBP.addImage(hr.getLbp(),mCurrentPhotoPath);
        LSH.addImage(hr.getLsh(),mCurrentPhotoPath);
        saveHashToFile(true,LSH);
        saveHashToFile(false,LBP);
        /*
        Intent intent = new Intent(MainActivity.this, Gallery.class);
        intent.putExtra(EXTRA_MESSAGE,mCurrentPhotoPath);
        startActivity(intent);
        /*
        if (requestCode == REQUEST_TAKE_PHOTO) {
            if (resultCode == REQUEST_TAKE_PHOTO) {

            }
        }
        */
    }

    public Hashes readHashFromFile(boolean lsh){
        String fileName;
        Hashes hash=null;
        if(lsh){
            fileName=lsh_name;
        }
        else {
            fileName=lbp_name;
        }
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
    public void saveHashToFile(boolean lsh,Hashes hash){
        String fileName;
        if(lsh){
            fileName=lsh_name;
        }
        else {
            fileName=lbp_name;
        }
        File file = new File(getApplicationContext().getFilesDir(), fileName);
        try{
            FileOutputStream fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(hash);
            oos.close();
        }
        catch (Exception e){

        }

    }
    public HashResult getHash(String path){
        Random rand=new Random();
        int lsh;
        int lbp;
        HashResult hr =new HashResult();
        //calcular el resultado de ambos hashes
        lsh = rand.nextInt((16 - 1) + 1) + 1;
        lbp = rand.nextInt((16 - 1) + 1) + 1;
        hr.setLbp(lbp);
        hr.setLsh(lsh);
        return hr;
    }


    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();
}
