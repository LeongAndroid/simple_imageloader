package com.simple.imageloder.cache;


import android.graphics.Bitmap;
import android.util.LruCache;

import com.simple.imageloder.request.BitmapRequest;

public class MemoryCache implements BitmapCache{

    private LruCache<String, Bitmap> mMemeryCache;

    public MemoryCache() {
        int maxMemory = (int)(Runtime.getRuntime().maxMemory() / 1024);
        final int cacheSize = maxMemory / 4;
        mMemeryCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getRowBytes() * bitmap.getHeight() / 1024;
            }
        };
    }

    @Override
    public void put(BitmapRequest key, Bitmap value) {
        mMemeryCache.put(key.imageUri, value);
    }

    @Override
    public Bitmap get(BitmapRequest key) {
        return mMemeryCache.get(key.imageUri);
    }

    @Override
    public void remove(BitmapRequest key) {
        mMemeryCache.remove(key.imageUri);
    }
}
