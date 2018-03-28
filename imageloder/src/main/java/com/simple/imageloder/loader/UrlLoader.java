package com.simple.imageloder.loader;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.jakewharton.disklrucache.IOUtil;
import com.simple.imageloder.request.BitmapRequest;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class UrlLoader extends AbsLoader{

    @Override
    protected Bitmap onLoadImage(BitmapRequest request) {
        final String imageUrl = request.imageUri;
        FileOutputStream fos = null;
        InputStream is = null;
        Bitmap bitmap = null ;
        HttpURLConnection conn = null ;
        try {
            URL url = new URL(imageUrl);
            conn = (HttpURLConnection) url.openConnection();
            is = new BufferedInputStream(conn.getInputStream());
            bitmap =  BitmapFactory.decodeStream(is, null, null);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IOUtil.closeQuietly(is);
            IOUtil.closeQuietly(fos);
            if ( conn != null ) {
                // 关闭流
                conn.disconnect();
            }
        }
        return bitmap;
    }
}
