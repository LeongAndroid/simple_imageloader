package com.simple.imageloder.policy;

import com.simple.imageloder.request.BitmapRequest;

public class SerialPolicy implements LoaderPolicy{
    @Override
    public int compareTo(BitmapRequest a, BitmapRequest b) {
        // 那么按照添加到队列的序列号顺序来执行
        return a.serialNum - b.serialNum;
    }
}
