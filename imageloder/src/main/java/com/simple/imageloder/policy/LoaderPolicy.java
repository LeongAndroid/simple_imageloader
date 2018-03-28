package com.simple.imageloder.policy;

import com.simple.imageloder.request.BitmapRequest;

public interface LoaderPolicy {
    public int compareTo(BitmapRequest a, BitmapRequest b);
}
