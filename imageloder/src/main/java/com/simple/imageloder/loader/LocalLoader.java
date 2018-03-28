package com.simple.imageloder.loader;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import com.simple.imageloder.request.BitmapRequest;
import com.simple.imageloder.utils.BitmapDecoder;

import java.io.File;

public class LocalLoader extends AbsLoader{
    @Override
    protected Bitmap onLoadImage(BitmapRequest request) {
        final String imagePath = Uri.parse(request.imageUri).getPath();
        final File imgFile = new File(imagePath);
        if (!imgFile.exists()) {
            return null;
        }
        // 从sd卡中加载的图片仅缓存到内存中,不做本地缓存
        request.justCacheInMem = true;

        // 加载图片
        BitmapDecoder decoder = new BitmapDecoder() {

            @Override
            public Bitmap decodeBitmapWithOption(BitmapFactory.Options options) {
                return BitmapFactory.decodeFile(imagePath, options);
            }
        };

        return decoder.decodeBitmap(request.getImageViewWidth(),
                request.getImageViewHeight());
    }

}
