package com.simple.imageloder.core;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.simple.imageloder.cache.BitmapCache;
import com.simple.imageloder.cache.MemoryCache;
import com.simple.imageloder.config.DisplayConfig;
import com.simple.imageloder.config.ImageLoaderConfig;
import com.simple.imageloder.policy.SerialPolicy;
import com.simple.imageloder.request.BitmapRequest;

public class SimpleImageLoader {
    private static SimpleImageLoader mInstance;

    /** 网络请求队列  */
    private RequestQueue mImageQueue;
    /** 图片加载配置对象 */
    private ImageLoaderConfig mConfig;

    /** 缓存 */
    private volatile BitmapCache mCache = new MemoryCache();

    private SimpleImageLoader() {

    }

    /// DCL 双重校验锁 单例模式
    public static SimpleImageLoader getInstance() {
        if (null == mInstance) {
            synchronized (SimpleImageLoader.class) {
                if (null == mInstance) {
                    mInstance = new SimpleImageLoader();
                }
            }
        }
        return mInstance;
    }

    public void init(ImageLoaderConfig config) {
        mConfig = config;
        mCache = config.mBitmapCache;
        checkConfig();
        mImageQueue = new RequestQueue(config.threadCount);
        mImageQueue.start();
    }

    private void checkConfig() {
        if (mConfig == null) {
            throw new RuntimeException(
                    "The config of SimpleImageLoader is Null, please call the init(ImageLoaderConfig config) method to initialize");
        }

        if (mConfig.mPolicy == null) {
            mConfig.mPolicy = new SerialPolicy();
        }
        if (mCache == null) {
            mCache = new MemoryCache();
        }

    }

    public void displayImage(ImageView imageView, String url) {
        displayImage(imageView, url, null, null);
    }

    public void displayImage(ImageView imageView, String url, DisplayConfig config) {
        displayImage(imageView, url, config, null);
    }

    public void displayImage(final ImageView imageView, final String url,
                             final DisplayConfig displayConfig, final ImageListener listener) {
        BitmapRequest request = new BitmapRequest(url,imageView, displayConfig, listener);
        // 加载的配置对象,如果没有设置则使用ImageLoader的配置
        request.displayConfig = request.displayConfig != null ? request.displayConfig
                : mConfig.displayConfig;
        mImageQueue.addRequest(request);
    }

    public ImageLoaderConfig getConfig() {
        return mConfig;
    }

    public void stop() {
        mImageQueue.stop();
    }

    /**
     * 图片加载Listener
     *
     * @author mrsimple
     */
    public static interface ImageListener {
        public void onComplete(ImageView imageView, Bitmap bitmap, String uri);
    }

}
