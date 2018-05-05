package common.photo.picker.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.List;

import common.photo.picker.Constants;
import common.photo.picker.R;
import common.photo.picker.entity.Photo;
import common.photo.picker.entity.PhotoDirectory;
import common.photo.picker.listener.OnItemSelectStatusChangeListener;
import common.photo.picker.listener.OnPhotoListItemClickListener;
import common.photo.picker.utils.ImageLoader;
import common.photo.picker.utils.MediaStoreHelper;
import common.photo.picker.utils.PhotoCaptureManager;
import common.photo.picker.utils.PhotoPickerManager;
import common.photo.picker.utils.UriUtils;

/**
 * Created by CK on 2017/3/10  16:34.
 *
 * @author CK
 * @version 1.0.0
 * @class PhotoPickerFragmentRvAdapter
 * @describe 选择照片 {@link PhotoPickerFragment}中照片列表适配器
 */
public class PhotoPickerFragmentRvAdapter extends SelectManagerAdapter<PhotoPickerFragmentRvAdapter.PhotoPickerViewHolder> {
    private static final String TAG = "PhotoPickerFragmentRvAd";
    private Context mContext;
    private LayoutInflater mLayoutInflater;

    private int mPhotoViewSize;//显示照片的View大小.

    private OnPhotoListItemClickListener mOnPhotoListItemClickListener;//PhotoPicker照片列表item点击事件监听器
    private OnItemSelectStatusChangeListener mOnItemSelectStatusChangeListener;//选择照片item状态改变监听器
    private View.OnClickListener mOnCameraClickListener;//拍照按钮点击事件

    private static final int TYPE_CAMERA = 101;//拍照按钮
    private static final int TYPE_PHOTO = 102;//照片

    /**
     *
     * @param context
     * @param photoDirectoryList 所有图片
     * @param originalPhotoList 源图片
     */
    public PhotoPickerFragmentRvAdapter(Context context, List<PhotoDirectory> photoDirectoryList, List<String> originalPhotoList) {
        this.mContext = context;
        this.mPhotoDirectoryList = photoDirectoryList;
        this.mOriginalPhotoList = originalPhotoList;

        mLayoutInflater = LayoutInflater.from(context);

        setPhotoViewSize(context);
    }

    /**
     * 设置显示照片的View大小
     *
     * @param context     Context
     */
    private void setPhotoViewSize(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(displayMetrics);
        mPhotoViewSize = displayMetrics.widthPixels / Constants.GRID_COLUMN_COUNT;
    }

    @Override
    public PhotoPickerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.e(TAG, "onCreateViewHolder: " + viewType);
        View itemView = mLayoutInflater.inflate(R.layout.item_photo_picker, parent, false);

        PhotoPickerViewHolder holder = new PhotoPickerViewHolder(itemView);
        if (TYPE_CAMERA == viewType) { // 第一项显示拍照的位置
            holder.ivSelectorStatus.setVisibility(View.GONE); // 把单击选择图片的选择框隐藏
            holder.ivPhoto.setScaleType(ImageView.ScaleType.CENTER);
            //拍照按钮点击事件
            if (null != mOnCameraClickListener) {
                holder.ivPhoto.setOnClickListener(mOnCameraClickListener);
            }
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(final PhotoPickerViewHolder holder, int position) {
        Log.e(TAG, "onBindViewHolder: " + position);
        int itemType = getItemViewType(position);
        if (TYPE_CAMERA == itemType) {
            //设置拍照按钮图片
            ImageLoader.loadCamera(holder.ivPhoto, R.drawable.selector_photo_picker_camera);
        } else if (TYPE_PHOTO == itemType) {
            List<Photo> photos = getCurrentDirectoryOfPhotos();
            if (null == photos || photos.size() <= 0) {
                return;
            }
            final Photo photo;
            if (isShowCamera()) {
                photo = photos.get(position - 1);
            } else {
                photo = photos.get(position);
            }
            //加载图片
            ImageLoader.load(photo.getPath(), holder.ivPhoto);
            // 进入页面处理之前已选中的
            final boolean isSelected = isSelected(photo); // 第一次进来未选中是false
            holder.ivSelectorStatus.setSelected(isSelected);
            //蒙版效果
            holder.vItemCover.setSelected(isSelected);
            //照片点击事件
            holder.ivPhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    handlePhotoClickEvent(holder);
                }
            });
            //选择框点击事件
            holder.ivSelectorStatus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    handleSelectedEvent(holder, photo, isSelected);
                }
            });
        }
    }

    /**
     * 处理照片点击事件
     */
    private void handlePhotoClickEvent(PhotoPickerViewHolder holder) {
        if (Constants.IS_PREVIEW_ENABLE && null != mOnPhotoListItemClickListener) {
            mOnPhotoListItemClickListener.onPhotoListItemClick(holder.ivPhoto, holder.getAdapterPosition(), isShowCamera());
        } else {
            holder.ivSelectorStatus.performClick();
        }
    }

    /**
     * 处理选择框点击事件
     */
    private void handleSelectedEvent(PhotoPickerViewHolder holder, Photo photo, boolean isSelected) {
        File photoFile = new File(photo.getPath());
        if (Constants.IS_CROP) {//支持裁剪
            File cut_imageFile = new File(Constants.CUT_PHOTO);
            if (!cut_imageFile.exists()) {
                try {
                    cut_imageFile.createNewFile();
                } catch (IOException e) {
                    //e.printStackTrace();
                    Toast.makeText(mContext, "载入出错，请重新选择图片", Toast.LENGTH_LONG).show();
                }
            }
            try {
                PhotoCaptureManager photoCaptureManager = new PhotoCaptureManager(mContext);
                cut_imageFile = photoCaptureManager.createPhotoFile();
                Constants.CUT_PHOTO = photoCaptureManager.getCurrentPhotoPath();
            } catch (IOException e) {
                e.printStackTrace();
            }
//            PhotoPickerManager.crop(mContext, Uri.parse(photo.getPath()), 2, 200, 200, Uri.fromFile(cut_imageFile));
            PhotoPickerManager.crop(mContext, UriUtils.getUriFromFilePath(mContext, photo.getPath()), 2, 200, 200, UriUtils.getUriFromFile(mContext, cut_imageFile));
        } else {
            int position = holder.getAdapterPosition();
            boolean isHandle = true;

            if (null != mOnItemSelectStatusChangeListener) {
                isHandle = mOnItemSelectStatusChangeListener.onItemSelectStatusChange(position, photo, isSelected, mSelectedPhotoList.size());
            }

            if (isHandle) { // 选择框的单击添加或者取消
                toggleSelection(photo);
                notifyItemChanged(position);
            }
        }
    }

    @Override
    public int getItemCount() {
        //Log.e(TAG, "getItemCount: " );
        int photosCount = mPhotoDirectoryList.size() == 0 ? 0 : getCurrentDirectoryOfPhotos().size();
        if (isShowCamera()) {
            photosCount++;
        }
        return photosCount;
    }

    @Override
    public int getItemViewType(int position) {
        //Log.e(TAG, "getItemViewType: " + position);
        return (isShowCamera() && 0 == position) ? TYPE_CAMERA : TYPE_PHOTO;
    }

    public boolean isShowCamera() {
        return Constants.IS_SHOW_CAMERA && mCurrentDirectoryIndex == MediaStoreHelper.INDEX_ALL_PHOTOS;
    }

    /**
     * 设置拍照按钮点击事件
     *
     * @param onCameraClickListener 拍照按钮点击事件
     */
    public void setOnCameraClickListener(View.OnClickListener onCameraClickListener) {
        this.mOnCameraClickListener = onCameraClickListener;
    }

    /**
     * 设置选择照片item状态改变监听器
     *
     * @param onItemSelectStatusChangeListener 选择照片item状态改变监听器
     */
    public void setOnItemSelectStatusChangeListener(OnItemSelectStatusChangeListener onItemSelectStatusChangeListener) {
        this.mOnItemSelectStatusChangeListener = onItemSelectStatusChangeListener;
    }

    /**
     * 设置PhotoPicker照片列表item点击事件监听器
     *
     * @param onPhotoListItemClickeListener PhotoPicker照片列表item点击事件监听器
     */
    public void setOnPhotoListItemClickeListener(OnPhotoListItemClickListener onPhotoListItemClickeListener) {
        this.mOnPhotoListItemClickListener = onPhotoListItemClickeListener;
    }

    protected static class PhotoPickerViewHolder extends RecyclerView.ViewHolder {

        private ImageView ivPhoto;//照片控件
        private ImageView ivSelectorStatus;//照片选择状态显示控件
        private View vItemCover;//选中时仿微信蒙版效果控件

        public PhotoPickerViewHolder(View itemView) {
            super(itemView);
            ivPhoto = (ImageView) itemView.findViewById(R.id.iv_photo_picker_list_item_photo);

            ivSelectorStatus = (ImageView) itemView.findViewById(R.id.iv_photo_picker_list_item_selected_status);
            ivSelectorStatus.setVisibility(View.VISIBLE);

            vItemCover = itemView.findViewById(R.id.v_photo_picker_list_item_cover);
            vItemCover.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onViewRecycled(PhotoPickerViewHolder holder) {
        ImageLoader.clear(holder.ivPhoto);
        super.onViewRecycled(holder);
    }

}
