package com.simple.imageloder.cache;
/**
 * 图片缓存抽象类,具体的子类有不使用缓存{@see NoCache}、内存缓存{@see MemoryCache}、sd卡缓存{@see
 * DiskCache}以及内存和sd卡双缓存{@see DoubleCache}
 *
 * @author mrsimple
 */

import android.graphics.Bitmap;

import com.simple.imageloder.request.BitmapRequest;

/**
 * 请求缓存接口
 *
 * @author mrsimple
 * @param <K> key的类型
 * @param <V> value类型
 */
public interface BitmapCache {
    public void put(BitmapRequest key, Bitmap value);
    public Bitmap get(BitmapRequest key);
    public void remove(BitmapRequest key);
}
