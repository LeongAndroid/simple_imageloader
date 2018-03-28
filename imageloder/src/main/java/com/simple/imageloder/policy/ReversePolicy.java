package com.simple.imageloder.policy;

import com.simple.imageloder.request.BitmapRequest;

public class ReversePolicy implements LoaderPolicy{

    @Override
    public int compareTo(BitmapRequest a, BitmapRequest b) {
        // 注意Bitmap请求要先执行最晚加入队列的请求,ImageLoader的策略
        return b.serialNum - a.serialNum;
    }
}
