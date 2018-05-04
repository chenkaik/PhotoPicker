package common.photo.picker.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import common.photo.picker.R;
import common.photo.picker.utils.ImageLoader;
import common.photo.picker.widget.TouchImageView;

/**
 * Created by CK on 2017/3/22  12:33.
 *
 * @author CK
 * @version 1.0.0
 * @class PreviewPhotosAdapter
 * @describe 照片预览ViewPager适配器
 */
public class PreviewPhotosAdapter extends PagerAdapter {

    private ArrayList<String> mAllPreviewPhotoPath; // 所有的预览图片路径集合

    public PreviewPhotosAdapter(ArrayList<String> allPreviewPhotoPath) {
        this.mAllPreviewPhotoPath = allPreviewPhotoPath;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        final Context context = container.getContext();
        View itemView;
        itemView = LayoutInflater.from(context).inflate(R.layout.item_photo_preview, container, false);
        TouchImageView touchImageView = (TouchImageView) itemView.findViewById(R.id.tiv_photo_preview_item_photo);

        ImageLoader.load(mAllPreviewPhotoPath.get(position), touchImageView, R.drawable.icon_preview_photo_loading, R.drawable.icon_preview_photo_error);

        // 取消预览时点击返回的操作
//        touchImageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //预览时点击结束预览
//                if (context instanceof Activity) {
//                    if (!((Activity) context).isFinishing()) {
//                        ((Activity) context).onBackPressed();
//                    }
//                }
//            }
//        });
        container.addView(itemView);
        return itemView;
    }

    @Override
    public int getCount() {
        return null == mAllPreviewPhotoPath ? 0 : mAllPreviewPhotoPath.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public int getItemPosition(Object object) {
        //return super.getItemPosition(object);
        return POSITION_NONE;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        //super.destroyItem(container, position, object);
        container.removeView((View) object);
        ImageLoader.clear((View) object);
    }

}
