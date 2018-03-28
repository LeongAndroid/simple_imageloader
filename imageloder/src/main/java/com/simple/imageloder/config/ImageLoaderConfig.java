package com.simple.imageloder.config;

import com.simple.imageloder.cache.BitmapCache;
import com.simple.imageloder.cache.MemoryCache;
import com.simple.imageloder.policy.LoaderPolicy;
import com.simple.imageloder.policy.SerialPolicy;

public class ImageLoaderConfig {
    /**
     * 图片缓存配置对象
     */
    public BitmapCache mBitmapCache = new MemoryCache();

    public DisplayConfig displayConfig = new DisplayConfig();

    public LoaderPolicy mPolicy = new SerialPolicy();
    /**
     * 线程默认个数
     */
    public int threadCount = Runtime.getRuntime().availableProcessors() + 1;

    ///此处可以方便使用者自定义 cache，返回this方便链式调用
    public ImageLoaderConfig setBitmapCache(BitmapCache bitmapCache) {
        mBitmapCache = bitmapCache;
        return this;
    }

    public ImageLoaderConfig setThreadCount(int count) {
        threadCount = count;
        return this;

    }


    public ImageLoaderConfig setLoadingPlaceholder(int resId) {
        displayConfig.loadingResId = resId;
        return this;
    }

    public ImageLoaderConfig setNotFoundPlaceholder(int resId) {
        displayConfig.failedResId = resId;
        return this;
    }

    ///此处可以方便使用者自定义policy
    public ImageLoaderConfig setLoadPolicy(LoaderPolicy loadPolicy) {
        if (loadPolicy != null) {
            mPolicy = loadPolicy;
        }
        return this;
    }

    public LoaderPolicy getLoaderPolicy() {
        return mPolicy;
    }
}
