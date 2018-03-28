<img src="http://avatar.csdn.net/blogpic/20150127140257890.jpg">    
# SimpleImageLoader Framework
   A Simple ImageLoader for Android, articles for this framework <a href="http://blog.csdn.net/column/details/android-imageloader.html" target="_blank">How to make a ImageLoader Framework</a>.


## Useage 
1. init The SimpleImageLoader with ImageLoaderConfig
```java
 private void initImageLoader() {
        ImageLoaderConfig config = new ImageLoaderConfig()
                .setLoadingPlaceholder(R.drawable.loading)
                .setNotFoundPlaceholder(R.drawable.not_found)
                .setCache(new DoubleCache(this))
                .setThreadCount(4)
                .setLoadPolicy(new ReversePolicy());
        // 初始化
        SimpleImageLoader.getInstance().init(config);
    }
```     
2. call the displayImage to load bitmap.
```java
SimpleImageLoader.getInstance().displayImage(myImageView, "http://www.xxx/myimage.jpg");
```    


- - -
March 28, 2018 1:43 PM
将工程代码结构修改成Android studio的结构。
### 学习图片加载ImageLoader框架开发思路
- 缓存策略
- 本地缓存DisLruCache基本用法
- 策略模式
- 链式调用

