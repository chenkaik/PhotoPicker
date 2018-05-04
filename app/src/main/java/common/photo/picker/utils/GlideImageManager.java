package common.photo.picker.utils;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.io.File;

/**
 * Created by CK on 2016/11/15 11:46.
 *
 * @author CK
 * @version 1.0.0
 * @class GlideImageManager
 * @describe Glide图片加载管理类
 * 可能遇到的问题:
 * 1、placeholder()导致的图片变形问题。
 * 问题描述：使用.placeholder()方法在某些情况下会导致图片显示的时候出现图片变形的情况。这是因为Glide默认开启的crossFade动画导致的TransitionDrawable绘制异常，详细描述和讨论可以看一下这个#363 issue。根本原因就是你的placeholder图片和你要加载显示的图片宽高比不一样，而Android的TransitionDrawable无法很好地处理不同宽高比的过渡问题，这的确是个Bug，是Android的也是Glide的。
 * 解决办法：使用.dontAnimate()方法禁用过渡动画，或者使用animate()方法自己写动画，再或者自己修复TransitionDrawable的问题。
 * 1、ImageView的资源回收问题。
 * 问题描述：默认情况下，Glide会根据with()使用的Activity或Fragment的生命周期自动调整资源请求以及资源回收。但是如果有很占内存的Fragment或Activity不销毁而仅仅是隐藏视图，那么这些图片资源就没办法及时回收，即使是GC的时候。
 * 解决办法：可以考虑使用WeakReference，如：
 * final WeakReference<ImageView> imageViewWeakReference = new WeakReference<>(imageView);
 * ImageView target = imageViewWeakReference.get();
 * if (target != null) {
 * Glide.with(context).load(uri).into(target);
 * }
 * 3、ImageView的setTag问题。
 * 问题描述：如果使用Glide的into(imageView)为ImageView设置图片的同时使用ImageView的setTag(final Object tag)方法，将会导致java.lang.IllegalArgumentException: You must not call setTag() on a view Glide is targeting异常。因为Glide的ViewTarget中通过view.setTag(tag)和view.getTag()标记请求的，由于Android 4.0之前Tag存储在静态map里，如果Glide使用setTag(int key, final Object tag)方法标记请求会导致内存泄露，所以Glide默认使用view.setTag(tag)标记请求，你就不能重复调用了。
 * 解决办法：如果你需要为ImageView设置Tag，必须使用setTag(int key, final Object tag)及getTag(int key)方法，其中key必须是合法的资源ID以确保key的唯一性，典型做法就是在资源文件中声明type="id"的item资源。
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class GlideImageManager {

    public static int default_img;
    public static int default_err_img;
    public static int default_circle_img;
    public static int default_circle_err_img;
    public static final int NOT_THUMBNAIL = -1;//不使用缩略图标记

    /**
     * 加载图片
     *
     * @param context
     * @param url
     * @param iv
     */
//    public static void loadImage(Context context, String url, ImageView iv) {
//        Glide.with(context).load(url)
//                .crossFade()//淡入显示,注意:如果设置了这个,则必须要去掉asBitmap
//                .placeholder(default_img)
//                .error(default_err_img)
//                .dontAnimate()
//                .into(iv);
//    }

    /**
     * 加载设置优先级的图片
     *
     * @param context
     * @param url
     * @param iv
     * @param priority
     */
//    public static void loadImage(Context context, String url, ImageView iv, Priority priority) {
//        Glide.with(context).load(url)
//                .crossFade()//淡入显示,注意:如果设置了这个,则必须要去掉asBitmap
//                .priority(priority)//优先级
//                .placeholder(default_img)
//                .error(default_err_img)
//                .into(iv);
//    }

    /**
     * 加载图片
     *
     * @param context
     * @param url
     * @param defaultImg
     * @param errImg
     * @param iv
     */
//    public static void loadImage(Context context, String url, @DrawableRes int defaultImg, @DrawableRes int errImg, ImageView iv) {
//        loadImage(context, url, errImg, defaultImg, iv, Priority.NORMAL, DiskCacheStrategy.RESULT);
//    }

    /**
     * 加载设置优先级的图片
     *
     * @param context
     * @param url
     * @param errImg
     * @param defaultImg
     * @param iv
     * @param priority          优先级：Priority.IMMEDIATE，Priority.HIGH，Priority.NORMAL(默认)，Priority.LOW
     * @param diskCacheStrategy 缓存策略：DiskCacheStrategy.SOURCE：缓存原始数据，DiskCacheStrategy.RESULT：缓存变换(如缩放、裁剪等)后的资源数据(默认)，DiskCacheStrategy.NONE：什么都不缓存，DiskCacheStrategy.ALL：缓存SOURC和RESULT
     */
//    public static void loadImage(Context context, String url, @DrawableRes int errImg, @DrawableRes int defaultImg, ImageView iv, Priority priority, DiskCacheStrategy diskCacheStrategy) {
//        Glide.with(context).load(url).diskCacheStrategy(diskCacheStrategy).priority(priority).placeholder(defaultImg).error(errImg).into(iv);
//    }

    /**
     * 加载gif图片(缓存原始数据)
     *
     * @param context
     * @param url
     * @param iv      diskCacheStrategy设置缓存策略:
     */
//    public static void loadGifImage(Context context, String url, ImageView iv) {
//        Glide.with(context).load(url).asGif().diskCacheStrategy(DiskCacheStrategy.SOURCE).placeholder(default_img).into(iv);
//    }

    /**
     * 加载圆形图片
     *
     * @param context
     * @param url
     * @param iv
     */
//    public static void loadCircleImage(Context context, String url, ImageView iv) {
//        Glide.with(context)
//                .load(url)
//                .placeholder(default_circle_img)
//                .error(default_circle_err_img)
//                .transform(new GlideCircleTransform(context)).into(iv);
//    }

    /**
     * 加载圆形资源图片
     *
     * @param context
     * @param resourceId
     * @param iv
     */
//    public static void loadCircleImage(Context context, @DrawableRes int resourceId, ImageView iv) {
//        Glide.with(context)
//                .load(resourceId)
//                .dontAnimate()
//                .placeholder(default_circle_img)
//                .error(default_circle_err_img)
//                .transform(new GlideCircleTransform(context)).into(iv);
////        Glide.with(context).load(resourceId).bitmapTransform(new CropCircleTransformation(context)).into(iv);
//    }

    /**
     * 加载圆角图片
     *
     * @param context
     * @param url
     * @param iv
     * @param radius
     */
//    public static void loadRoundCornerImage(Context context, String url, ImageView iv, int radius) {
//        Glide.with(context).load(url).placeholder(default_img).error(default_err_img).transform(new GlideRoundTramsform(context, radius)).into(iv);
//    }

    /**
     * 加载圆角资源图片
     *
     * @param context
     * @param resourceId
     * @param iv
     * @param radius
     */
    @Deprecated
    public static void loadRoundCornerImageDp(Context context, @DrawableRes int resourceId, ImageView iv, int radius) {
//        Glide.with(context).load(resourceId).dontAnimate().placeholder(default_img).error(default_err_img).transform(new GlideRoundTramsform(context, radius)).into(iv);
//        Glide.with(context).load(resourceId).placeholder(default_img).error(default_err_img).bitmapTransform(new RoundedCornersTransformation(context,radius,0, RoundedCornersTransformation.CornerType.ALL)).into(iv);
        // TODO 修改的
        //Glide.with(context).load(resourceId).bitmapTransform(new RoundedCornersTransformation(context, radius, 0, RoundedCornersTransformation.CornerType.ALL)).into(iv);
    }

    /**
     * 加载圆角资源图片
     *
     * @param context
     * @param resourceId
     * @param iv
     * @param radiusPixel
     */
    public static void loadRoundCornerImage(Context context, @DrawableRes int resourceId, ImageView iv, int radiusPixel) {
        // TODO 修改的
        //Glide.with(context).load(resourceId).bitmapTransform(new RoundedCornersTransformation(context, radiusPixel, 0, RoundedCornersTransformation.CornerType.ALL)).into(iv);
    }

    /**
     * 加载File类型图片
     *
     * @param context
     * @param file
     * @param iv
     */
    public static void loadImage(Context context, File file, ImageView iv) {
        Glide.with(context).load(file).into(iv);
    }

    /**
     * 加载资源图片
     *
     * @param context
     * @param resourceId
     * @param iv
     */
    public static void loadImage(Context context, @DrawableRes int resourceId, ImageView iv) {
        Glide.with(context).load(resourceId).into(iv);
    }

    /**
     * 清除内存缓存
     *
     * @param context
     */
    public static void clearMemory(Context context) {
        Glide.get(context).clearMemory();
    }

    /**
     * 清除磁盘缓存
     *
     * @param applicationContext
     */
    public static void clearDiskCache(Context applicationContext) {
        // 必须在后台线程中调用，建议同时clearMemory()
        Glide.get(applicationContext).clearDiskCache();
        clearMemory(applicationContext);
    }

    /**
     * 获取圆形Bitmap
     */
//    private static class GlideCircleTransform extends BitmapTransformation {
//
//        public GlideCircleTransform(Context context) {
//            super(context);
//        }
//
//        public GlideCircleTransform(BitmapPool bitmapPool) {
//            super(bitmapPool);
//        }
//
//        @Override
//        protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
//            //return null;
//            return circleCrop(pool, toTransform);
//        }
//
//        private Bitmap circleCrop(BitmapPool pool, Bitmap source) {
//            if (source == null) {
//                return null;
//            }
//            int size = Math.min(source.getWidth(), source.getHeight());
//            int x = (source.getWidth() - size) / 2;
//            int y = (source.getHeight() - size) / 2;
//            Bitmap squared = Bitmap.createBitmap(source, x, y, size, size);
//            // 如果BitmapPool中找不到符合该条件的Bitmap，get()方法会返回null，就需要我们自己创建Bitmap
//            Bitmap result = pool.get(size, size, Bitmap.Config.ARGB_8888);
//            if (result == null) {
//                // 如果想让Bitmap支持透明度，就需要使用ARGB_8888
//                result = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
//            }
//            //创建最终Bitmap的Canvas
//            Canvas canvas = new Canvas(result);
//            Paint paint = new Paint();
//            paint.setShader(new BitmapShader(squared, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
//            paint.setAntiAlias(true);
//            float r = size / 2f;
//            // 将原始Bitmap处理后画到最终Bitmap中
//            canvas.drawCircle(r, r, r, paint);
//            // return我们新的Bitmap就行,Glide会自动帮我们回收原始Bitmap。
//            return result;
//        }
//
//        @Override
//        public String getId() {
//            //return null;
//            return getClass().getName();
//        }
//    }

    /**
     * 获取圆角的Bitmap
     */
//    private static class GlideRoundTramsform extends BitmapTransformation {
//        private static float radius = 0f;
//
//        public GlideRoundTramsform(Context context) {
//            super(context);
//        }
//
//        public GlideRoundTramsform(Context context, int dp) {
//            super(context);
//            this.radius = Resources.getSystem().getDisplayMetrics().density * dp;
//        }
//
//        public GlideRoundTramsform(BitmapPool bitmapPool) {
//            super(bitmapPool);
//        }
//
//        @Override
//        protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
//            //return null;
//            return roundCrop(pool, toTransform);
//        }
//
//        private static Bitmap roundCrop(BitmapPool pool, Bitmap source) {
//            if (source == null) {
//                return null;
//            }
//            Bitmap result = pool.get(source.getWidth(), source.getHeight(), Bitmap.Config.ARGB_8888);
//            if (result == null) {
//                result = Bitmap.createBitmap(source.getWidth(), source.getHeight(), Bitmap.Config.ARGB_8888);
//            }
//            Canvas canvas = new Canvas(result);
//            Paint paint = new Paint();
//            paint.setShader(new BitmapShader(source, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
//            paint.setAntiAlias(true);
//            RectF rectF = new RectF(0f, 0f, source.getWidth(), source.getHeight());
//            canvas.drawRoundRect(rectF, radius, radius, paint);
//            return result;
//        }
//
//        @Override
//        public String getId() {
//            //return null;
//            return getClass().getName() + Math.round(radius);
//        }
//    }


}
