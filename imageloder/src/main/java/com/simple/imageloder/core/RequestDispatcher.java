package com.simple.imageloder.core;

import android.util.Log;

import com.simple.imageloder.loader.Loader;
import com.simple.imageloder.loader.LoaderManager;
import com.simple.imageloder.request.BitmapRequest;

import java.util.concurrent.BlockingQueue;

public class RequestDispatcher extends Thread {
    /**
     * 网络请求队列
     */
    private BlockingQueue<BitmapRequest> mRequestQueue;

    public RequestDispatcher(BlockingQueue<BitmapRequest> requestQueue) {
        mRequestQueue = requestQueue;
    }


    @Override
    public void run() {
        while (!isInterrupted()) {
            try {
                final BitmapRequest request = mRequestQueue.take();
                if (request.isCancel) {
                    continue;
                }
                final String schema = parseSchema(request.imageUri);
                Loader imageLoader = LoaderManager.getInstance().getLoader(schema);
                imageLoader.loadImage(request);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    private String parseSchema(String uri) {
        if (uri.contains("://")) {
            return uri.split("://")[0];
        } else {
            Log.e(getName(), "### wrong scheme, image uri is : " + uri);
        }

        return "";
    }
}
