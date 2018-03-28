package com.simple.imageloder.loader;


import android.graphics.Bitmap;
import android.util.Log;

import com.simple.imageloder.request.BitmapRequest;

public class NullLoader extends AbsLoader{
    @Override
    protected Bitmap onLoadImage(BitmapRequest request) {
        Log.e(NullLoader.class.getSimpleName(), "### wrong schema, your image uri is : "
                + request.imageUri);
        return null;
    }
}
