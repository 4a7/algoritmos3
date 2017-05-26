package com.example.android.app;
/**
 * Created by root on 5/20/17.
 */
/*
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ColorConvertOp;
import java.awt.image.Raster;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Hashtable;





public class PixelHash {

    private ArrayList<int []> planes = new ArrayList<>();
    private Hashtable<String,String> hashTable = new Hashtable<>();
    private String[] fileList;
    private int hashSize;
    private int imageW = 256;
    private int imageH = 256;

    public void setHashSize(int hSize){
        this.hashSize = hSize;
    }

    public void setImageSize(int iW,int iH){
        this.imageH = iW;
        this.imageW = iH;
    }

    public BufferedImage openImage(String path) {
        BufferedImage img;
        BufferedImage rImg = null;
        try {
            img = ImageIO.read(new File(path));
            int type = img.getType() == 0? BufferedImage.TYPE_INT_ARGB : img.getType();
            rImg=resizeImage(img,type);

        } catch (IOException e) {
            System.out.println("No se encontro la imagen");
        }
        return rImg;
    }

    private BufferedImage resizeImage(BufferedImage originalImage, int type){
        BufferedImage resizedImage = new BufferedImage(imageW, imageH, type);
        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(originalImage, 0, 0, imageW,imageH, null);
        g.dispose();

        return resizedImage;
    }
    public BufferedImage grayScale(BufferedImage img) {
        BufferedImageOp op = new ColorConvertOp(ColorSpace.getInstance(ColorSpace.CS_GRAY), null);
        BufferedImage image = op.filter(img, null);
        return image;
    }

    public int[] getImageValues(BufferedImage img) {
        Raster r = img.getData();
        return r.getPixels(0, 0, img.getWidth(), img.getHeight(), (int[]) null);
    }

    public void saveToFile(BufferedImage img,String fileName) throws IOException {

        File outputFile = new File("/home/malt/Downloads/Tests"+fileName);
        ImageIO.write(img, "jpg", outputFile);
    }


    public int dotProduct(int[] img,int[] plane){
        int res=0;
        for(int i=0;i<img.length;i++){
            res+=img[i]*plane[i];
        }

        return res;
    }

    public int[] generatePlane(){
        int[] plane= new int[imageW*imageH];
        Random random=new Random();
        for(int i=0;i<imageW*imageH;i++){
            plane[i]=(random.nextInt(65536)-32768);
        }
        return plane;
    }

    public void generatePlanes(){
        for(int i = 0; i<hashSize; i++){
            planes.add(generatePlane());
        }
    }

    public String generateHash(String path) throws IOException {//path de la imagen y tamano del hash(numero planos)
        String hash = "";
        BufferedImage img = openImage(path);
        BufferedImage gImg = grayScale(img);
        int[] imageValues = getImageValues(gImg);
        for(int i=0;i<hashSize;i++){
            if(dotProduct(imageValues,planes.get(i))>0){
                hash+="1";
            }else {
                hash+="0";
            }
        }
        return hash;
    }

    public void hashFiles(String parentFolder) throws IOException {
        File directory = new File(parentFolder);
        fileList = directory.list();
        for(int i=0;i<fileList.length;i++){
            hashTable.put(fileList[i],generateHash(parentFolder+fileList[i]));
        }
    }
    @Override
    public String toString(){
        for(int i=0;i<fileList.length;i++){
            System.out.println(fileList[i]+" -> "+hashTable.get(fileList[i]));
        }

        return null;
    }

    public static void main(String [] args) throws IOException {
        PixelHash ph = new PixelHash();
        ph.setHashSize(20);
        ph.setImageSize(256,256);
        ph.generatePlanes();
        ph.hashFiles("/home/malt/Downloads/Tests/");
        ph.toString();
    }
}
*/