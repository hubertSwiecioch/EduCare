package com.hswie.educaremobile.helper;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;

/**
 * Created by hswie on 11/6/2015.
 */
public class ImageHelper {

    public static int AVATAR_SIZE = 100;
    public static int AVATAR_QUALITY = 100;

    public static byte[] readFile(File file) throws IOException {
        // Open file
        RandomAccessFile f = new RandomAccessFile(file, "r");
        try {
            // Get and check length
            long longlength = f.length();
            int length = (int) longlength;
            if (length != longlength)
                throw new IOException("File size >= 2 GB");
            // Read file and return data
            byte[] data = new byte[length];
            f.readFully(data);
            return data;
        } finally {
            f.close();
        }
    }
    public static byte[] scale(String filePath, int reqWidth, int reqHeight, int quality) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);

        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        options.inJustDecodeBounds = false;
        Bitmap bitmap = BitmapFactory.decodeFile(filePath, options);

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        if (bitmap != null) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, stream);
            bitmap.recycle();
        }

        byte[] bytesArray = stream.toByteArray();
        try {
            stream.close();
        } catch (IOException e) {
            Log.e("ImageHelper", "IOException ", e);
        }

        return bytesArray;
    }

    public static byte[] scaleFromHttp(String path, int reqWidth, int reqHeight) throws IOException {
        java.net.URL url = new java.net.URL(path);
        HttpURLConnection connection = (HttpURLConnection) url
                .openConnection();
        connection.setDoInput(true);
        connection.connect();
        InputStream inputStream = connection.getInputStream();

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(connection.getInputStream(), null, options);

        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;

        inputStream.close();
        connection.disconnect();
        connection = (HttpURLConnection) url.openConnection();
        connection.setDoInput(true);
        connection.connect();
        inputStream = connection.getInputStream();

        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);

        inputStream.close();
        connection.disconnect();

        if (bitmap != null) {
            bitmap.recycle();
            bitmap = null;
        }

        byte[] bytesArray = stream.toByteArray();

        try {
            stream.close();
            stream = null;
        } catch (IOException e) {
            Log.e("ImageHelper", "IOException ", e);
        }

        return bytesArray;
    }

    private static int calculateInSampleSize(BitmapFactory.Options options,
                                             int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height
                    / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }

        return inSampleSize;
    }

    public static boolean isGraphicFile(String filenameLowerCase) {
        if (filenameLowerCase.contains("tif")
                || filenameLowerCase.contains("gif")
                || filenameLowerCase.contains("jpeg")
                || filenameLowerCase.contains("jpg")
                || filenameLowerCase.contains("png")
                || filenameLowerCase.contains("bmp"))
            return true;

        return false;
    }

    public static String cacheImageOnDisk(Context context, String filePath, int width, int height, int quality) {
        String cachePath;
        FileOutputStream outStream;

        String filename = filePath.split("/")[filePath.split("/").length - 1];
        cachePath = context.getFilesDir() + filename;

        if (new File(cachePath).exists()) {
            return cachePath;
        }

        byte[] image = ImageHelper.scale(filePath, width, height, quality);

        outStream = null;
        try {
            outStream = new FileOutputStream(cachePath);
            outStream.write(image);

            outStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (outStream != null)
                    outStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return cachePath;
    }

    public static String cacheImageOnDisk(Context context, byte[] image, String filename, int width, int height, int quality) {
        String cachePath;
        FileOutputStream outStream;

        if (!filename.startsWith("/"))
            filename = "/" + filename;

        cachePath = context.getFilesDir() + filename;

//        if(new File(cachePath).exists()) {
//            Log.d("cacheImageOnDisk", "File = " + filename + " exists, deleting = " + new File(cachePath).delete());
//        }

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(image, 0, image.length, options);

        options.inSampleSize = calculateInSampleSize(options, width, height);
        options.inJustDecodeBounds = false;

        Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);

        ByteArrayOutputStream stream = new ByteArrayOutputStream();

        if (bitmap == null)
            return null;

        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, stream);

        if (bitmap != null) {
            bitmap.recycle();
            bitmap = null;
        }

        image = stream.toByteArray();

        outStream = null;
        try {
            outStream = new FileOutputStream(cachePath);
            outStream.write(image);

            outStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (outStream != null)
                    outStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        image = null;

        return cachePath;
    }
}
