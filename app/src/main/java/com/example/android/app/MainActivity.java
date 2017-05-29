package com.example.android.app;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.opencv.android.OpenCVLoader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private static int K_LSH=5;    //numero de planos
    private static int K_LBP=10;
    private static int numero_elementos_lbp=256;    //numero de elementos del vector para lbp
    private static int numero_elementos_lsh=256*256;    //numero de elementos del vector para lsh
    public static String lsh_space_name="lsh_space.dat";
    public static String lbp_space_name="lbp_space.dat";
    public static String lsh_name="lsh.dat";
    public static String lbp_name="lbp.dat";
    public static Hashes LSH;
    public static Hashes LBP;
    public static Espacio ELSH;
    public static Espacio ELBP;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final String TAG = MainActivity.class.getSimpleName();
    String mCurrentPhotoPath;
    public static final String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
    static final int REQUEST_TAKE_PHOTO = 1;
    static final int GALLERY_CODE = 2;
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
    private void addImageFromGallery() {

        try{
            Intent i = new Intent(Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(i, GALLERY_CODE);
        }catch(Exception exp){
            Log.i("Error",exp.toString());
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (!OpenCVLoader.initDebug()) {
            Log.e(this.getClass().getSimpleName(), "  OpenCVLoader.initDebug(), not working.");
        } else {
            Log.d(this.getClass().getSimpleName(), "  OpenCVLoader.initDebug(), working.");
        }
        //se deben crear la primera vez que se corren
        SharedPreferences sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String first = sharedpreferences.getString(First, null);
        if(first==null){
            //si es la primera vez que se corre la aplicacion
            Hashes lb=new Hashes(false);
            Hashes ls=new Hashes(true);
            ELBP=new Espacio(K_LBP,numero_elementos_lbp);
            ELSH=new Espacio(K_LSH,numero_elementos_lsh);
            LSH=ls;
            LBP=lb;
            //lb.addImage(1,"abc.def");
            //lb.addImage(2,"agf.if");
            //ls.addImage(12,"ahd.fyf");
            //ls.addImage(13,"hgu.ftr");
            saveHashToFile(true,ls);
            saveHashToFile(false,lb);
            saveSpacetoFile(true,ELSH);
            saveSpacetoFile(false,ELBP);
            System.out.println("PRIMERA CORRIDA");
        }
        else{
            LSH=readHashFromFile(true);
            LBP=readHashFromFile(false);
            ELBP=readSpaceFromFile(false);
            ELSH=readSpaceFromFile(true);
        }

        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(First, "false");



        //



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setImageResource(R.drawable.camera);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this);
                builder1.setMessage("Seleccionar Fuente");
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "Camara",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                dispatchTakePictureIntent();
                            }
                        });

                builder1.setNegativeButton(
                        "Galeria",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                addImageFromGallery();
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();

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
        LSH=readHashFromFile(true);
        LBP=readHashFromFile(false);
        if(requestCode==GALLERY_CODE){
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            final String picturePath = cursor.getString(columnIndex);
            cursor.close();
            System.out.println("PATH "+picturePath);
            new Thread(new Runnable() {
                public void run() {
                    HashResult hr=getHash(picturePath);
                    LBP.addImage(hr.getLbp(),picturePath);
                    LSH.addImage(hr.getLsh(),picturePath);
                    saveHashToFile(true,LSH);
                    saveHashToFile(false,LBP);
                }
            }).start();




        }
        else if (requestCode==REQUEST_TAKE_PHOTO){
            new Thread(new Runnable() {
                public void run() {
                    HashResult hr=getHash(mCurrentPhotoPath);
                    System.out.println(LBP.toString());
                    LBP.addImage(hr.getLbp(),mCurrentPhotoPath);
                    LSH.addImage(hr.getLsh(),mCurrentPhotoPath);
                    saveHashToFile(true,LSH);
                    saveHashToFile(false,LBP);
                }
            }).start();
        }

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

    public  Hashes readHashFromFile(boolean lsh){
        String fileName;
        Hashes hash=null;
        if(lsh){
            fileName=lsh_name;
        }
        else {
            fileName=lbp_name;
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
    public  Espacio readSpaceFromFile(boolean lsh){
        String fileName;
        Espacio hash=null;
        if(lsh){
            fileName=lsh_space_name;
        }
        else {
            fileName=lbp_space_name;
        }
        System.out.println(getApplicationContext().getFilesDir().toString());
        File file = new File(getApplicationContext().getFilesDir(), fileName);
        try{

            FileInputStream fis = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(fis);
            hash = (Espacio) ois.readObject();
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
    public void saveSpacetoFile(boolean lsh,Espacio espacio){
        String fileName;
        if(lsh){
            fileName=lsh_space_name;
        }
        else {
            fileName=lbp_space_name;
        }
        File file = new File(getApplicationContext().getFilesDir(), fileName);
        try{
            FileOutputStream fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(espacio);
            oos.close();
        }
        catch (Exception e){

        }
    }
    public HashResult getHash(String path){
        Random rand=new Random();
        int lsh;
        int lbp;
        Bitmap bmp;
        HashResult hr =new HashResult();
        //calcular el resultado de ambos hashes
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize=4;
        bmp = BitmapFactory.decodeFile(path,options);
        LBP lb=new LBP(bmp);
        int[]hashlbp=lb.LBP();
        lbp=ELBP.getHash(hashlbp);
        System.out.println("LBP: "+lbp);
        bmp=Bitmap.createScaledBitmap(bmp, 256, 256, true);
        lsh = ELSH.getHash(pp(bmp));

        hr.setLbp(lbp);
        hr.setLsh(lsh);
        return hr;
    }
    public String getRealPathFromURI(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        @SuppressWarnings("deprecation")
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    public static int[] pp(Bitmap bmp){
        double r=0.21;
        double g=0.72;
        double b=0.07;
        int red,green,blue;
        int fuente;
        int[]retorno=new int[256*256];
        int cont=0;
        for(int i=0;i<256;i++){
            for(int j=0;i<256;i++){
                fuente=bmp.getPixel(j,i);
                red= Color.red(fuente);
                green=Color.green(fuente);
                blue=Color.blue(fuente);
                retorno[cont]=(int)((red*r)+(green*g)+(blue*b));
                cont++;
            }
        }
        return retorno;
    }


    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();
}
