package com.simple.imageloder.cache;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import com.jakewharton.disklrucache.DiskLruCache;
import com.jakewharton.disklrucache.IOUtil;
import com.simple.imageloder.request.BitmapRequest;
import com.simple.imageloder.utils.AppVersionUtils;
import com.simple.imageloder.utils.BitmapDecoder;
import com.simple.imageloder.utils.Md5Helper;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DiskCache implements BitmapCache{

    /**
     * 1MB
     */
    private static final int MB = 1024 * 1024;

    /**
     * cache dir
     */
    private static final String IMAGE_DISK_CACHE = "bitmap";

    /**
     * Disk LRU Cache
     */
    private DiskLruCache mDiskLruCache;

    private static DiskCache mDiskCache;

    private DiskCache(Context context) {
        initDiskCache(context);
    }

    public static DiskCache getDiskCache(Context context) {
        if (mDiskCache == null) {
            synchronized (DiskCache.class) {
                if (mDiskCache == null) {
                    mDiskCache = new DiskCache(context);
                }
            }
        }
        return mDiskCache;
    }

    /**
     * 初始化sdcard缓存
     */
    private void initDiskCache(Context context) {
        try {
            File cacheDir = getDiskCacheDir(context, IMAGE_DISK_CACHE);
            if (!cacheDir.exists()) {
                cacheDir.mkdirs();
            }
            mDiskLruCache = DiskLruCache
                    .open(cacheDir, AppVersionUtils.getAppVersionCode(context), 1, 50 * MB);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private File getDiskCacheDir(Context context, String uniqueName) {
        String cachePath ;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            cachePath = context.getExternalCacheDir().getPath();
        }else {
            cachePath = context.getCacheDir().getPath();
        }
        return new File(cachePath + File.separator + uniqueName);
    }



    private InputStream getInputStream(String md5) {
        DiskLruCache.Snapshot snapshot;
        try {
            snapshot = mDiskLruCache.get(md5);
            if (snapshot != null) {
                return snapshot.getInputStream(0);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private boolean writeBitmapToDisk(Bitmap bitmap, OutputStream outputStream) {
        BufferedOutputStream bos = new BufferedOutputStream(outputStream, 8 * 1024);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
        boolean result = true;
        try {
            bos.flush();
        } catch (IOException e) {
            e.printStackTrace();
            result = false;
        } finally {
            IOUtil.closeQuietly(bos);
        }

        return result;
    }

    @Override
    public void put(BitmapRequest key, Bitmap value) {
        if (key.justCacheInMem) {
            Log.e(IMAGE_DISK_CACHE, "### 仅缓存在内存中");
            return;
        }
        DiskLruCache.Editor editor = null;
        try {
            editor = mDiskLruCache.edit(key.imageUriMd5);
            if (editor != null) {
                OutputStream outputStream = editor.newOutputStream(0);
                if (writeBitmapToDisk(value, outputStream)) {
                    editor.commit();
                }else {
                    editor.abort();
                }
                IOUtil.closeQuietly(outputStream);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public Bitmap get(final BitmapRequest key) {
        // 图片解析器
        BitmapDecoder decoder = new BitmapDecoder() {
            @Override
            public Bitmap decodeBitmapWithOption(BitmapFactory.Options options) {
                final InputStream inputStream = getInputStream(key.imageUriMd5);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream, null,
                        options);
                IOUtil.closeQuietly(inputStream);
                return bitmap;
            }
        };
        return null;
    }

    @Override
    public void remove(BitmapRequest key) {
        try {
            mDiskLruCache.remove(Md5Helper.toMD5(key.imageUriMd5));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
