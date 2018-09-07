package com.sand_corporation.www.uthaopartner.BitmapResize;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.google.firebase.crash.FirebaseCrash;

import java.io.ByteArrayOutputStream;

/**
 * Created by HP on 1/15/2018.
 */

public class BitmapResize {

    private final String TAG = "CheckWelcome";

    public BitmapResize() {
    }

    // convert from bitmap to byte array
    private byte[] getBytes(Bitmap bitmap) {
        FirebaseCrash.log("Home:getBytes.called");
        Crashlytics.log("Home:getBytes.called");
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 20, stream);
        return stream.toByteArray();
    }

    // convert from byte array to bitmap
    private Bitmap getImage(byte[] image) {
        FirebaseCrash.log("Home:getImage.called");
        Crashlytics.log("Home:getImage.called");
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }

    public Bitmap getResizedBitmap1(Bitmap bm, int newWidth, int newHeight) {
        FirebaseCrash.log("Home:getResizedBitmap.called");
        Crashlytics.log("Home:getResizedBitmap.called");
        int width = bm.getWidth();
        int height = bm.getHeight();
        Log.i(TAG,"Original Bitmap Width: " + width + " Height: " + height);
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false);
        int resizedWidth = resizedBitmap.getWidth();
        int resizedHeight = resizedBitmap.getHeight();
        Log.i(TAG,"Resized Bitmap Width: " + resizedWidth + " Height: " + resizedHeight);
        bm.recycle();
        return resizedBitmap;
    }


    //Resize Bitmap maintaining aspect ratio
    public Bitmap getResizedBitmapMaintainingAspectRatio2(Bitmap image,
                                                          int maxWidth,
                                                          int maxHeight) {
        if (maxHeight > 0 && maxWidth > 0) {
            int width = image.getWidth();
            int height = image.getHeight();
            Log.i(TAG,"Original Bitmap Width: " + width + " Height: " + height);
            float ratioBitmap = (float) width / (float) height;
            float ratioMax = (float) maxWidth / (float) maxHeight;

            int finalWidth = maxWidth;
            int finalHeight = maxHeight;
            if (ratioMax > ratioBitmap) {
                finalWidth = (int) ((float)maxHeight * ratioBitmap);
            } else {
                finalHeight = (int) ((float)maxWidth / ratioBitmap);
            }
            image = Bitmap.createScaledBitmap(image, finalWidth, finalHeight, true);
            int resizedWidth = image.getWidth();
            int resizedHeight = image.getHeight();
            Log.i(TAG,"Resized Bitmap Width: " + resizedWidth + " Height: " + resizedHeight);
            return image;
        } else {
            return image;
        }
    }


    //This below method require API 17 and above
    public Bitmap scaleBitmapAndKeepRatio3(Bitmap TargetBmp,
                                                 int reqHeightInPixels,
                                                 int reqWidthInPixels)
    {
        int width = TargetBmp.getWidth();
        int height = TargetBmp.getHeight();
        Log.i(TAG,"Original Bitmap Width: " + width + " Height: " + height);
        Matrix m = new Matrix();
        m.setRectToRect(new RectF(0, 0, TargetBmp.getWidth(), TargetBmp.getHeight()),
                new RectF(0, 0, reqWidthInPixels, reqHeightInPixels),
                Matrix.ScaleToFit.CENTER);
        Bitmap scaledBitmap = Bitmap.createBitmap(TargetBmp, 0, 0,
                TargetBmp.getWidth(), TargetBmp.getHeight(), m, true);
        int resizedWidth = scaledBitmap.getWidth();
        int resizedHeight = scaledBitmap.getHeight();
        Log.i(TAG,"Resized Bitmap Width: " + resizedWidth + " Height: " + resizedHeight);
        return scaledBitmap;
    }

    public Bitmap scaleBitmap4(Bitmap bm, int maxWidth,int maxHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();

        Log.i(TAG,"Original Bitmap Width: " + width + " Height: " + height);

        if (width > height) {
            // landscape
            int ratio = width / maxWidth;
            width = maxWidth;
            height = height / ratio;
        } else if (height > width) {
            // portrait
            int ratio = height / maxHeight;
            height = maxHeight;
            width = width / ratio;
        } else {
            // square
            height = maxHeight;
            width = maxWidth;
        }

        bm = Bitmap.createScaledBitmap(bm, width, height, true);
        int resizedWidth = bm.getWidth();
        int resizedHeight = bm.getHeight();
        Log.i(TAG,"Resized Bitmap Width: " + resizedWidth + " Height: " + resizedHeight);
        return bm;
    }

    public Bitmap scaleBitmap5(Bitmap bitmap, int wantedWidth, int wantedHeight) {
        float originalWidth = bitmap.getWidth();
        float originalHeight = bitmap.getHeight();
        Log.i(TAG,"Original Bitmap Width: " + originalWidth + " Height: " + originalHeight);
        Bitmap output = Bitmap.createBitmap(wantedWidth, wantedHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        Matrix m = new Matrix();

        float scalex = wantedWidth/originalWidth;
        float scaley = wantedHeight/originalHeight;
        float xTranslation = 0.0f, yTranslation = (wantedHeight - originalHeight * scaley)/2.0f;

        m.postTranslate(xTranslation, yTranslation);
        m.preScale(scalex, scaley);
        // m.setScale((float) wantedWidth / bitmap.getWidth(), (float) wantedHeight / bitmap.getHeight());
        Paint paint = new Paint();
        paint.setFilterBitmap(true);
        canvas.drawBitmap(bitmap, m, paint);

        int resizedWidth = output.getWidth();
        int resizedHeight = output.getHeight();
        Log.i(TAG,"Resized Bitmap Width: " + resizedWidth + " Height: " + resizedHeight);

        return output;
    }



    //For Below Example:
//    Let's say you have an image as 100 x 100 but the desired size is 300x50, then this method
//    will convert your image to 50 x 50 and paint it into a new image which has dimensions as
//    300 x 50 (and empty fields will be black).
//
//    Another example: let's say you have an image as 600 x 1000 and the desired sizes are
//    300 x 50 again, then your image will be converted into 30 x 50 and painted into
//    a newly created image which has sizes as 300 x 50.

    /**
     * Scale the image preserving the ratio
     * @param imageToScale Image to be scaled
     * @param destinationWidth Destination width after scaling
     * @param destinationHeight Destination height after scaling
     * @return New scaled bitmap preserving the ratio
     */
    public Bitmap scalePreserveRatio6(Bitmap imageToScale, int destinationWidth,
                                            int destinationHeight) {
        if (destinationHeight > 0 && destinationWidth > 0 && imageToScale != null) {
            int width = imageToScale.getWidth();
            int height = imageToScale.getHeight();
            Log.i(TAG,"Original Bitmap Width: " + width + " Height: " + height);
            //Calculate the max changing amount and decide which dimension to use
            float widthRatio = (float) destinationWidth / (float) width;
            float heightRatio = (float) destinationHeight / (float) height;

            //Use the ratio that will fit the image into the desired sizes
            int finalWidth = (int)Math.floor(width * widthRatio);
            int finalHeight = (int)Math.floor(height * widthRatio);
            if (finalWidth > destinationWidth || finalHeight > destinationHeight) {
                finalWidth = (int)Math.floor(width * heightRatio);
                finalHeight = (int)Math.floor(height * heightRatio);
            }

            //Scale given bitmap to fit into the desired area
            imageToScale = Bitmap.createScaledBitmap(imageToScale, finalWidth, finalHeight, true);

            //Created a bitmap with desired sizes
            Bitmap scaledImage = Bitmap.createBitmap(destinationWidth, destinationHeight, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(scaledImage);

            //Draw background color
            Paint paint = new Paint();
            paint.setColor(Color.BLACK);
            paint.setStyle(Paint.Style.FILL);
            canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(), paint);

            //Calculate the ratios and decide which part will have empty areas (width or height)
            float ratioBitmap = (float)finalWidth / (float)finalHeight;
            float destinationRatio = (float) destinationWidth / (float) destinationHeight;
            float left = ratioBitmap >= destinationRatio ? 0 : (float)(destinationWidth - finalWidth) / 2;
            float top = ratioBitmap < destinationRatio ? 0: (float)(destinationHeight - finalHeight) / 2;
            canvas.drawBitmap(imageToScale, left, top, null);

            int resizedWidth = scaledImage.getWidth();
            int resizedHeight = scaledImage.getHeight();
            Log.i(TAG,"Resized Bitmap Width: " + resizedWidth + " Height: " + resizedHeight);
            return scaledImage;
        } else {
            return imageToScale;
        }
    }

    public Bitmap resizeBitmapByScale7(
            Bitmap bitmap, float scale, boolean recycle) {
        float originalWidth = bitmap.getWidth();
        float originalHeight = bitmap.getHeight();
        Log.i(TAG,"Original Bitmap Width: " + originalWidth + " Height: " + originalHeight);
        int width = Math.round(bitmap.getWidth() * scale);
        int height = Math.round(bitmap.getHeight() * scale);
        if (width == bitmap.getWidth()
                && height == bitmap.getHeight()) return bitmap;
        Bitmap target = Bitmap.createBitmap(width, height, getConfig(bitmap));
        Canvas canvas = new Canvas(target);
        canvas.scale(scale, scale);
        Paint paint = new Paint(Paint.FILTER_BITMAP_FLAG | Paint.DITHER_FLAG);
        canvas.drawBitmap(bitmap, 0, 0, paint);
        if (recycle) bitmap.recycle();
        int resizedWidth = target.getWidth();
        int resizedHeight = target.getHeight();
        Log.i(TAG,"Resized Bitmap Width: " + resizedWidth + " Height: " + resizedHeight);
        return target;
    }

    private static Bitmap.Config getConfig(Bitmap bitmap) {
        Bitmap.Config config = bitmap.getConfig();
        if (config == null) {
            config = Bitmap.Config.ARGB_8888;
        }
        return config;
    }



    private int getScreenWidth() {
        FirebaseCrash.log("Home:getScreenWidth.called");
        Crashlytics.log("Home:getScreenWidth.called");
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    private int getScreenHeight() {
        FirebaseCrash.log("Home:getScreenHeight.called");
        Crashlytics.log("Home:getScreenHeight.called");
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }
}
