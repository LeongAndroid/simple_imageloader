package com.simple.imageloder.request;

import android.support.annotation.NonNull;
import android.widget.ImageView;

import com.simple.imageloder.config.DisplayConfig;
import com.simple.imageloder.core.SimpleImageLoader;
import com.simple.imageloder.policy.LoaderPolicy;
import com.simple.imageloder.utils.ImageViewHelper;
import com.simple.imageloder.utils.Md5Helper;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;


/**
 * 将请求封装成bitmaprequest
 * 网络请求类. 注意GET和DELETE不能传递参数,因为其请求的性质所致,用户可以将参数构建到url后传递进来到Request中.
 *
 * @author mrsimple
 * @param <T> T为请求返回的数据类型
 */

public class BitmapRequest implements Comparable<BitmapRequest> {

    Reference<ImageView> mImageViewRef;

    public String imageUri = "";
    public String imageUriMd5 = "";
    public DisplayConfig displayConfig = null;
    public SimpleImageLoader.ImageListener mImageListener;
    /**
     * 请求序列号
     */
    public int serialNum = 0;
    /**
     * 是否取消该请求
     */
    public boolean isCancel = false;

    /**
     * 仅仅支持内存缓存
     */
    public boolean justCacheInMem = false;

    /**
     * 加载策略
     */
    LoaderPolicy mLoadPolicy = SimpleImageLoader.getInstance().getConfig().getLoaderPolicy();

    public BitmapRequest(String url, ImageView mImageView, DisplayConfig displayConfig, SimpleImageLoader.ImageListener listener) {
        this.imageUri = url;
        mImageViewRef = new WeakReference<ImageView>(mImageView);
        this.displayConfig = displayConfig;
        this.mImageListener = listener;
        ///设置ImageView的tag为图片的uri  防止图片错位显示
        mImageView.setTag(url);
        imageUriMd5 = Md5Helper.toMD5(url);
    }


    /**
     * ????
     * ImageLoaderConfig中已经有setLoadPolicy，这里还需要吗???
     */
    /**
     * @param policy
     */
    public void setLoadPolicy(LoaderPolicy policy) {
        if (policy != null) {
            mLoadPolicy = policy;
        }
    }

    @Override
    public int compareTo(@NonNull BitmapRequest o) {
        return mLoadPolicy.compareTo(this, o);
    }

    /**
     * 判断imageview的tag与uri是否相等
     *
     * @return
     */
    public boolean isImageViewTagValid() {
        return mImageViewRef.get() != null ? mImageViewRef.get().getTag().equals(imageUri) : false;
    }

    public ImageView getImageView() {
        return mImageViewRef.get();
    }

    public int getImageViewWidth() {
        return ImageViewHelper.getImageViewWidth(mImageViewRef.get());
    }

    public int getImageViewHeight() {
        return ImageViewHelper.getImageViewHeight(mImageViewRef.get());
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((imageUri == null) ? 0 : imageUri.hashCode());
        result = prime * result + ((mImageViewRef == null) ? 0 : mImageViewRef.get().hashCode());
        result = prime * result + serialNum;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        BitmapRequest other = (BitmapRequest) obj;
        if (imageUri == null) {
            if (other.imageUri != null) {
                return false;
            }
        } else if (!imageUri.equals(other.imageUri)) {
            return false;
        }
        if (mImageViewRef == null) {
            if (other.mImageViewRef != null) {
                return false;
            }
        } else if (!mImageViewRef.get().equals(other.mImageViewRef.get())) {
            return false;
        }
        if (serialNum != other.serialNum) {
            return false;
        }
        return true;
    }

}
