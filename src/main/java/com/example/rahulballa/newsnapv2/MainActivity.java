package com.example.rahulballa.newsnapv2;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    private static final int TAKE_PICTURE_ID = 1;
    private static final int CAMERA_PERMISSION = 2;
    private static final int SELECT_IMAGE_ID = 3;
    private static final int SELECT_IMAGE_PERMISSION = 4;
    private static final int DARKEN_IMAGE_PERMISSION = 5;
    private static final int BRIGHTEN_IMAGE_PERMISSION = 6;
    private static final int INVERSE_IMAGE_PERMISSION = 7;
    private static final int SHARE_IMAGE_PERMISSION = 8;
    private static final int GREYSCALE_IMAGE_PERMISSION = 9;
    private String folder = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "localImage" + File.separator;

    Button takePictureButton;
    Button selectPictureButton;
    ImageView imageView;
    Uri galleryUri;
    Button darkenButton;
    Button brightenButton;
    Button inverseButton;
    Button shareButton;
    Button greyscaleButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        takePictureButton = (Button) findViewById(R.id.takePicture);
        selectPictureButton = (Button) findViewById(R.id.selectImage);
        imageView = (ImageView) findViewById(R.id.imageView);
        darkenButton = (Button) findViewById(R.id.darken);
        brightenButton = (Button) findViewById(R.id.brighten);
        inverseButton = (Button) findViewById(R.id.inverse);
        shareButton = (Button) findViewById(R.id.share);

        takePictureButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v){
                if(checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, TAKE_PICTURE_ID);
                }
                else{
                    if(checkVersion()){
                        requestPermissions(new String[] {Manifest.permission.CAMERA}, CAMERA_PERMISSION);
                    }
                }
            }
        });

        selectPictureButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v){
                if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                    Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(galleryIntent, SELECT_IMAGE_ID);
                }
                else{
                    if(checkVersion()){
                        requestPermissions(new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, SELECT_IMAGE_PERMISSION);
                    }
                }
            }
        });

        darkenButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v){
                if(checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
                    BitmapDrawable bitmapDrawable = (BitmapDrawable) imageView.getDrawable();
                    Bitmap bitmap = bitmapDrawable.getBitmap();
                    imageView.setImageBitmap(darkenImage(bitmap));
                }
                else{
                    if(checkVersion()){
                        requestPermissions(new String[] {Manifest.permission.CAMERA}, DARKEN_IMAGE_PERMISSION);
                    }
                }
            }
        });

        brightenButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v){
                if(checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
                    BitmapDrawable bitmapDrawable = (BitmapDrawable) imageView.getDrawable();
                    Bitmap bitmap = bitmapDrawable.getBitmap();
                    imageView.setImageBitmap(brightenImage(bitmap));
                }
                else{
                    if(checkVersion()){
                        requestPermissions(new String[] {Manifest.permission.CAMERA}, BRIGHTEN_IMAGE_PERMISSION);
                    }
                }
            }
        });

        inverseButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v){
                if(checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
                    BitmapDrawable bitmapDrawable = (BitmapDrawable) imageView.getDrawable();
                    Bitmap bitmap = bitmapDrawable.getBitmap();
                    imageView.setImageBitmap(inverseImage(bitmap));
                }
                else{
                    if(checkVersion()){
                        requestPermissions(new String[] {Manifest.permission.CAMERA}, INVERSE_IMAGE_PERMISSION);
                    }
                }
            }
        });

        shareButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v){
                if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                    BitmapDrawable bitmapDrawable = (BitmapDrawable) imageView.getDrawable();
                    Bitmap bitmap = bitmapDrawable.getBitmap();
                    shareImage(bitmap);
                }
                else{
                    if(checkVersion()){
                        requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, SHARE_IMAGE_PERMISSION);
                    }
                }
            }
        });

    }

    private boolean checkVersion(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            return true;
        }
        return false;
    }

    protected void onRequestPermissionResult(int requestCode, String[] permissions, int[] grantResults){
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch(requestCode){
            case CAMERA_PERMISSION:
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, TAKE_PICTURE_ID);
                }
                else{
                    if(shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)){
                        Toast.makeText(MainActivity.this, "Permission to use camera was denied", Toast.LENGTH_LONG).show();
                    }
                }
                break;
            case SELECT_IMAGE_PERMISSION:
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                    startActivityForResult(galleryIntent, SELECT_IMAGE_ID);
                }
                else{
                    if(shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)){
                        Toast.makeText(MainActivity.this, "Permission to access gallery was denied", Toast.LENGTH_LONG).show();
                    }
                }
                break;
            case DARKEN_IMAGE_PERMISSION:
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    BitmapDrawable bitmapDrawable = (BitmapDrawable) imageView.getDrawable();
                    Bitmap bitmap = bitmapDrawable.getBitmap();
                    imageView.setImageBitmap(darkenImage(bitmap));
                }
                else{
                    if(shouldShowRequestPermissionRationale(Manifest.permission.CHANGE_CONFIGURATION)){
                        Toast.makeText(MainActivity.this, "Permission to darken image was denied", Toast.LENGTH_LONG).show();
                    }
                }
                break;
            case BRIGHTEN_IMAGE_PERMISSION:
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    BitmapDrawable bitmapDrawable = (BitmapDrawable) imageView.getDrawable();
                    Bitmap bitmap = bitmapDrawable.getBitmap();
                    imageView.setImageBitmap(brightenImage(bitmap));
                }
                else{
                    if(shouldShowRequestPermissionRationale(Manifest.permission.CHANGE_CONFIGURATION)){
                        Toast.makeText(MainActivity.this, "Permission to brighten image was denied", Toast.LENGTH_LONG).show();
                    }
                }
                break;
            case INVERSE_IMAGE_PERMISSION:
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    BitmapDrawable bitmapDrawable = (BitmapDrawable) imageView.getDrawable();
                    Bitmap bitmap = bitmapDrawable.getBitmap();
                    imageView.setImageBitmap(inverseImage(bitmap));
                }
                else{
                    if(shouldShowRequestPermissionRationale(Manifest.permission.CHANGE_CONFIGURATION)){
                        Toast.makeText(MainActivity.this, "Permission to inverse image was denied", Toast.LENGTH_LONG).show();
                    }
                }
                break;
            case SHARE_IMAGE_PERMISSION:
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    BitmapDrawable bitmapDrawable = (BitmapDrawable) imageView.getDrawable();
                    Bitmap newBitmap = bitmapDrawable.getBitmap();
                    shareImage(newBitmap);
                    //intent.setType("image/jpeg");
                    //ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    //bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);

                    try {
                        File file = new File(folder);
                        if (!file.exists()) {
                            file.mkdirs();
                        }
                        // creating new image file
                        File newImage = new File(folder + "savedImage.jpeg");
                        if(!newImage.exists()){
                            newImage.createNewFile();
                        }
                        FileOutputStream stream = new FileOutputStream(newImage);
                        newBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                        stream.flush();
                        stream.close();

                    }catch(Exception e){
                        e.printStackTrace();
                    }

                    Intent intent = new Intent();
                    File shareImage = new File(folder, "savedImage.jpeg");
                    intent.setData(Uri.fromFile(shareImage));
                    intent.setType("image/*");
                    intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(shareImage));
                    startActivity(Intent.createChooser(intent, "Share"));*/
                    /*Bitmap b = BitmapFactory.decodeResource(getResources(), R.id.imageView);
                    Intent share = new Intent(Intent.ACTION_SEND);
                    share.setType("image/jpeg");
                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    b.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                    String path = MediaStore.Images.Media.insertImage(getContentResolver(),
                            b, "Title", null);
                    Uri imageUri =  Uri.parse(path);
                    share.putExtra(Intent.EXTRA_STREAM, imageUri);
                    startActivity(Intent.createChooser(share, "Select"));*/
                }
                break;
        }
    }

    public void shareImage(Bitmap bitmap){
        try {
            File file = new File(folder);
            if (!file.exists()) {
                file.mkdirs();
            }
            // creating new image file
            File newImage = new File(folder + "savedImage.jpeg");
            if(!newImage.exists()){
                newImage.createNewFile();
            }
            FileOutputStream stream = new FileOutputStream(newImage);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            stream.flush();
            stream.close();

        }catch(Exception e){
            e.printStackTrace();
        }

        Intent intent = new Intent();
        File shareImage = new File(folder, "savedImage.jpeg");
        intent.setData(Uri.fromFile(shareImage));
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(shareImage));
        startActivity(Intent.createChooser(intent, "Share"));
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        //super.onActivityResult(requestCode, resultCode, data);

        switch(requestCode){
            case TAKE_PICTURE_ID:
                if(resultCode == RESULT_OK){
                    Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                    imageView.setImageBitmap(bitmap);
                }
                break;
            case SELECT_IMAGE_ID:
                if(resultCode == RESULT_OK){
                    galleryUri = data.getData();
                    BitmapFactory.Options bitmapOption = new BitmapFactory.Options();
                    bitmapOption.inSampleSize = 2;

                    /*InputStream inputStream;

                    try{
                        inputStream = getContentResolver().openInputStream(galleryUri);
                        Bitmap image = BitmapFactory.decodeStream(inputStream);
                        imageView.setImageBitmap(Bitmap.createScaledBitmap(image, 4096, 4096, false));
                    }catch(Exception e){
                        e.printStackTrace();
                    }*/

                    Cursor cursor = getContentResolver().query(galleryUri, new String[] {MediaStore.Images.Media.DATA}, null, null, null);
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(new String[] {MediaStore.Images.Media.DATA}[0]);
                    String filePath = cursor.getString(columnIndex);
                    cursor.close();

                    Bitmap afterFormatImage = BitmapFactory.decodeFile(filePath, bitmapOption);
                    //Drawable drawable = new BitmapDrawable(afterFormatImage);

                    imageView.setImageBitmap(afterFormatImage);
                }
        }
    }

    private Bitmap darkenImage(Bitmap originalImage){
        Bitmap newImage = Bitmap.createBitmap(originalImage.getWidth(), originalImage.getHeight(), originalImage.getConfig());
        int width = originalImage.getWidth();
        int height = originalImage.getHeight();

        for(int i = 0; i < width; i++){
            for(int j = 0; j < height; j++){
                int pixel = originalImage.getPixel(i, j);
                int getRed = Color.red(pixel);
                int getGreen = Color.green(pixel);
                int getBlue = Color.blue(pixel);

                if(getRed > 255){
                    getRed = 255;
                }
                if(getRed < 0){
                    getRed = 0;
                }
                if(getGreen > 255){
                    getGreen = 255;
                }
                if(getGreen < 0){
                    getGreen = 0;
                }
                if(getBlue > 255){
                    getBlue = 255;
                }
                if(getBlue < 0){
                    getBlue = 0;
                }

                getBlue -= 10;
                getGreen -= 10;
                getRed -= 10;

                if(getRed > 255){
                    getRed = 255;
                }
                if(getRed < 0){
                    getRed = 0;
                }
                if(getGreen > 255){
                    getGreen = 255;
                }
                if(getGreen < 0){
                    getGreen = 0;
                }
                if(getBlue > 255){
                    getBlue = 255;
                }
                if(getBlue < 0){
                    getBlue = 0;
                }

                newImage.setPixel(i, j, Color.rgb(getRed, getGreen, getBlue));

            }
        }
        return newImage;
    }

    private Bitmap brightenImage(Bitmap originalImage){
        Bitmap newImage = Bitmap.createBitmap(originalImage.getWidth(), originalImage.getHeight(), originalImage.getConfig());
        int width = originalImage.getWidth();
        int height = originalImage.getHeight();

        for(int i = 0; i < width; i++){
            for(int j = 0; j < height; j++){
                int pixel = originalImage.getPixel(i, j);
                int getRed = Color.red(pixel);
                int getGreen = Color.green(pixel);
                int getBlue = Color.blue(pixel);

                if(getRed > 255){
                    getRed = 255;
                }
                if(getRed < 0){
                    getRed = 0;
                }
                if(getGreen > 255){
                    getGreen = 255;
                }
                if(getGreen < 0){
                    getGreen = 0;
                }
                if(getBlue > 255){
                    getBlue = 255;
                }
                if(getBlue < 0){
                    getBlue = 0;
                }

                getBlue += 10;
                getGreen += 10;
                getRed += 10;

                if(getRed > 255){
                    getRed = 255;
                }
                if(getRed < 0){
                    getRed = 0;
                }
                if(getGreen > 255){
                    getGreen = 255;
                }
                if(getGreen < 0){
                    getGreen = 0;
                }
                if(getBlue > 255){
                    getBlue = 255;
                }
                if(getBlue < 0){
                    getBlue = 0;
                }

                newImage.setPixel(i, j, Color.rgb(getRed, getGreen, getBlue));

            }
        }
        return newImage;
    }

    private Bitmap inverseImage(Bitmap originalImage){
        Bitmap newImage = Bitmap.createBitmap(originalImage.getWidth(), originalImage.getHeight(), originalImage.getConfig());
        int width = originalImage.getWidth();
        int height = originalImage.getHeight();

        for(int i = 0; i < width; i++){
            for(int j = 0; j < height; j++){
                int pixel = originalImage.getPixel(i, j);
                int getRed = Color.red(pixel);
                int getGreen = Color.green(pixel);
                int getBlue = Color.blue(pixel);

                if(getRed > 255){
                    getRed = 255;
                }
                if(getRed < 0){
                    getRed = 0;
                }
                if(getGreen > 255){
                    getGreen = 255;
                }
                if(getGreen < 0){
                    getGreen = 0;
                }
                if(getBlue > 255){
                    getBlue = 255;
                }
                if(getBlue < 0){
                    getBlue = 0;
                }

                getBlue = 255 - getBlue;
                getGreen = 255 - getGreen;
                getRed = 255 - getRed;

                if(getRed > 255){
                    getRed = 255;
                }
                if(getRed < 0){
                    getRed = 0;
                }
                if(getGreen > 255){
                    getGreen = 255;
                }
                if(getGreen < 0){
                    getGreen = 0;
                }
                if(getBlue > 255){
                    getBlue = 255;
                }
                if(getBlue < 0){
                    getBlue = 0;
                }

                newImage.setPixel(i, j, Color.rgb(getRed, getGreen, getBlue));

            }
        }
        return newImage;
    }

    private Bitmap greyscaleImage(Bitmap originalImage){
        Bitmap newImage = Bitmap.createBitmap(originalImage.getWidth(), originalImage.getHeight(), originalImage.getConfig());
        int width = originalImage.getWidth();
        int height = originalImage.getHeight();

        for(int i = 0; i < width; i++){
            for(int j = 0; j < height; j++){
                int pixel = originalImage.getPixel(i, j);
                int getRed = Color.red(pixel);
                int getGreen = Color.green(pixel);
                int getBlue = Color.blue(pixel);

                if(getRed > 255){
                    getRed = 255;
                }
                if(getRed < 0){
                    getRed = 0;
                }
                if(getGreen > 255){
                    getGreen = 255;
                }
                if(getGreen < 0){
                    getGreen = 0;
                }
                if(getBlue > 255){
                    getBlue = 255;
                }
                if(getBlue < 0){
                    getBlue = 0;
                }

                int grayRed = (int)((double)getRed * 0.2989D + (double)getGreen * 0.587D + (double)getBlue * 0.114D);
                int grayBlue = (int)((double)getRed * 0.2989D + (double)getGreen * 0.587D + (double)getBlue * 0.114D);
                int grayGreen = (int)((double)getRed * 0.2989D + (double)getGreen * 0.587D + (double)getBlue * 0.114D);

                if(grayRed > 255){
                    grayRed = 255;
                }
                if(grayRed < 0){
                    grayRed = 0;
                }
                if(grayGreen > 255){
                    grayGreen = 255;
                }
                if(grayGreen < 0){
                    grayGreen = 0;
                }
                if(grayBlue > 255){
                    grayBlue = 255;
                }
                if(grayBlue < 0){
                    grayBlue = 0;
                }

                newImage.setPixel(i, j, Color.rgb(grayRed, grayGreen, grayBlue));

            }
        }
        return newImage;
    }
}
