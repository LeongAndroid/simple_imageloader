package com.simple.imageloder.loader;

import android.graphics.Bitmap;
import android.util.Log;
import android.widget.ImageView;

import com.simple.imageloder.cache.BitmapCache;
import com.simple.imageloder.config.DisplayConfig;
import com.simple.imageloder.core.SimpleImageLoader;
import com.simple.imageloder.request.BitmapRequest;

public abstract class AbsLoader implements Loader {

    /**
     * 图片缓存
     */
    private static BitmapCache mCache = SimpleImageLoader.getInstance().getConfig().mBitmapCache;

    @Override
    public void loadImage(BitmapRequest request) {
        ///1.先从缓存中获取
        Bitmap resultBitmap = mCache.get(request);
        if (resultBitmap == null) {
            ///显示loading时候的图片
            showLoading(request);
            ///2.加载图片
            resultBitmap = onLoadImage(request);
            /// 3、缓存图片
            cacheBitmap(request, resultBitmap);
        }else {
            request.justCacheInMem = true;
        }
        /// 4、将结果投递到UI线程
        deliveryToUIThread(request, resultBitmap);

    }

    /**
     * 将结果投递到UI,更新ImageView
     *
     * @param request
     * @param bitmap
     */
    protected void deliveryToUIThread(final BitmapRequest request,
                                      final Bitmap bitmap) {
        final ImageView imageView = request.getImageView();
        if (imageView == null) {
            return;
        }
        imageView.post(new Runnable() {
            @Override
            public void run() {
                updateImageView(request, bitmap);
            }
        });
    }

    /**
     * 更新ImageView
     *
     * @param request
     * @param result
     */
    private void updateImageView(BitmapRequest request, Bitmap result) {
        final ImageView imageView = request.getImageView();
        final String uri = request.imageUri;
        if (result != null && imageView.getTag().equals(uri)) {
            imageView.setImageBitmap(result);
        }

        // 加载失败
        if (result == null && hasFaildPlaceholder(request.displayConfig)) {
            imageView.setImageResource(request.displayConfig.failedResId);
        }

        // 回调接口
        if (request.mImageListener != null) {
            request.mImageListener.onComplete(imageView, result, uri);
        }
    }

    private boolean hasLoadingPlaceholder(DisplayConfig displayConfig) {
        return displayConfig != null && displayConfig.loadingResId > 0;
    }

    private boolean hasFaildPlaceholder(DisplayConfig displayConfig) {
        return displayConfig != null && displayConfig.failedResId > 0;
    }

    /**
     * @param request
     * @param bitmap
     */
    private void cacheBitmap(BitmapRequest request, Bitmap bitmap) {
        // 缓存新的图片
        if (bitmap != null && mCache != null) {
            synchronized (mCache) {
                mCache.put(request, bitmap);
            }
        }
    }
    /**
     * 显示加载中的视图,注意这里也要判断imageview的tag与image uri的相等性,否则逆序加载时出现问题
     *
     * @param request
     */
    private void showLoading(final BitmapRequest request) {
        final ImageView imageView = request.getImageView();
        if (request.isImageViewTagValid()
                && hasLoadingPlaceholder(request.displayConfig)) {
            imageView.post(new Runnable() {

                @Override
                public void run() {
                    imageView.setImageResource(request.displayConfig.loadingResId);
                }
            });
        }
    }

    /** 加载图片的hook方法，留给子类处理
     * @param request
     * @return
     */
    protected abstract Bitmap onLoadImage(BitmapRequest request);

}
