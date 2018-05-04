package common.photo.picker.utils;

import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

/**
 * Created by chenKai on 2018/4/22.
 * 图片加载器
 */
public class ImageLoader {

    /**
     * @param path      图片路径
     * @param imageView ImageView
     */
    public static void load(String path, @NonNull ImageView imageView) {
        RequestOptions requestOptions = new RequestOptions()
                .circleCrop()
                .dontAnimate(); // centerCrop、fitCenter、circleCrop
        Glide.with(imageView.getContext())
                .load(path)
                .thumbnail(0.1f)
                //.centerCrop()
                //.dontAnimate()
                .apply(requestOptions)
                .into(imageView);
    }

    /**
     * 加载图片
     *
     * @param path             图片路径
     * @param imageView        ImageView
     * @param placeholderResId 占位图
     * @param errorResId       加载失败显示图片
     */
    public static void load(String path, @NonNull ImageView imageView, @DrawableRes int placeholderResId, @DrawableRes int errorResId) {
        RequestOptions requestOptions = new RequestOptions()
                .dontAnimate()
                .placeholder(placeholderResId)
                .error(errorResId)
                .dontAnimate();
        Glide.with(imageView.getContext())
                .load(path)
                //.dontAnimate()
                //.placeholder(placeholderResId)
                //.error(errorResId)
                //.dontAnimate()
                .apply(requestOptions)
                .into(imageView);
    }

    /**
     * 加载资源图片
     *
     * @param resourceId 资源图片id
     * @param imageView  ImageView
     */
    public static void load(@DrawableRes int resourceId, @NonNull ImageView imageView) {
        RequestOptions requestOptions = new RequestOptions()
                .circleCrop()
                .dontAnimate();
        Glide.with(imageView.getContext())
                .load(resourceId)
                //.centerCrop()
                //.dontAnimate()
                .apply(requestOptions)
                .into(imageView);
    }

    /**
     * 设置拍照按钮图片
     *
     * @param imageView  ImageView
     * @param resourceId 拍照按钮图片
     */
    public static void loadCamera(@NonNull ImageView imageView, @DrawableRes int resourceId) {
        imageView.setImageResource(resourceId);
    }

    /**
     * 清除View加载的图片
     *
     * @param view 加载图片的View
     */
    public static void clear(@NonNull View view) {
        //Glide.clear(view);
        Glide.with(view).clear(view);
    }

}
