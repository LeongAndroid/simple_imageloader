package com.simple.imageloder.cache;


import android.graphics.Bitmap;

import com.simple.imageloder.request.BitmapRequest;

/**
 * 没有缓存
 *
 * @author mrsimple
 */

public class NoCache implements BitmapCache{
    @Override
    public void put(BitmapRequest key, Bitmap value) {

    }

    @Override
    public Bitmap get(BitmapRequest key) {
        return null;
    }

    @Override
    public void remove(BitmapRequest key) {

    }
}
