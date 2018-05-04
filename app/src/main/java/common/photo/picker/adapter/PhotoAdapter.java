package common.photo.picker.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import common.photo.picker.R;
import common.photo.picker.listener.OnSelectorPhotoClickListener;
import common.photo.picker.utils.ImageLoader;
import common.photo.picker.utils.PhotoPreview;
import common.photo.picker.widget.PhotoPickerView;

/**
 * Created by chenKai on 2018/4/22.
 * 多功能照片选择器照片列表适配器
 */
public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder> {

    private int mMaxPickerPhotoCount;
    private Context mContext;
    private ArrayList<String> mSelectedPhotos;
    private int mMultPhotoPickerAction;
    private LayoutInflater mInflater;
    private int mPadding;
    private OnSelectorPhotoClickListener mOnSelectorPhotoClickListener;//选择照片按钮点击监听接口

    public PhotoAdapter(Context context, ArrayList<String> mSelectedPhotos) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(mContext);
        this.mSelectedPhotos = mSelectedPhotos;
        this.mPadding = dp2px(context, 10);
    }

    /**
     * dp转pixel
     *
     * @param context 上下文对象
     * @param dp      dp
     * @return pixel
     */
    public static int dp2px(Context context, float dp) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return (int) (dp * (metrics.densityDpi / 160f));
    }

    @NonNull
    @Override
    public PhotoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.item_photo_picker, parent, false);
        return new PhotoViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final PhotoViewHolder holder, final int position) {
        if (PhotoPickerView.ACTION_SELECT == mMultPhotoPickerAction) { // 选择图片
            holder.ivPhoto.setPadding(mPadding, mPadding, mPadding, mPadding);
            holder.rlPhotoItem.setVisibility(View.VISIBLE);
            if (position == getItemCount() - 1) { // 最后一张图片位置(显示照片或者+)
                if (null != mSelectedPhotos && mSelectedPhotos.size() == mMaxPickerPhotoCount) { // 选择照片数量已达上限
                    holder.rlPhotoItem.setVisibility(View.GONE);
                } else { // 选择照片+
                    holder.ivDelete.setVisibility(View.GONE); // 删除照片隐藏
                    ImageLoader.load(R.drawable.icon_add_picture, holder.ivPhoto);
                    holder.ivPhoto.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (null != mOnSelectorPhotoClickListener) { // 回调选择图片
                                mOnSelectorPhotoClickListener.selectorPhotoClickListener();
                            }
                        }
                    });
                }
            } else { // 显示已选择照片
                // 加载图片
                ImageLoader.load(mSelectedPhotos.get(position), holder.ivPhoto);

                holder.ivDelete.setVisibility(View.VISIBLE);
                // 点击移除
                holder.ivDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mSelectedPhotos.remove(holder.getAdapterPosition());
                        if (mSelectedPhotos.size() < mMaxPickerPhotoCount - 1) { // TODO: 2018/5/4 这个地方需要打断点好好看下
                            notifyItemRemoved(holder.getAdapterPosition());
                        } else {
                            notifyDataSetChanged();
                        }
                    }
                });
                // 点击预览
                holder.ivPhoto.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        previewPhotos(holder.getAdapterPosition());
                    }
                });
            }
        } else if (PhotoPickerView.ACTION_ONLY_SHOW == mMultPhotoPickerAction) { // 显示照片
            // 加载图片
            ImageLoader.load(mSelectedPhotos.get(position), holder.ivPhoto);
            // 点击预览
            holder.ivPhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    previewPhotos(holder.getAdapterPosition());
                }
            });
        }
    }

    /**
     * 预览照片
     *
     * @param currentIndex 当前角标索引
     */
    private void previewPhotos(int currentIndex) {
        PhotoPreview.builder()
                .setAction(PhotoPickerView.ACTION_ONLY_SHOW)
                .setCurrentIndex(currentIndex)
                .setDeleteEnable(false)
                .setPreviewPhotoPathList(mSelectedPhotos)
                .start((Activity) mContext);
    }

    @Override
    public int getItemCount() { // mMultPhotoPickerAction值为1
        return PhotoPickerView.ACTION_SELECT == mMultPhotoPickerAction ? mSelectedPhotos.size() + 1 : mSelectedPhotos.size();
    }

    /**
     * 设置操作模式
     *
     * @param multPhotoPickerAction {@link PhotoPickerView.MultPhotoPickerAction}
     * @return PhotoAdapter
     */
    public PhotoAdapter setMultPhotoPickerAction(@PhotoPickerView.MultPhotoPickerAction int multPhotoPickerAction) {
        this.mMultPhotoPickerAction = multPhotoPickerAction;
        return this;
    }

    /**
     * 设置选择照片的最大数量
     *
     * @param maxPickerPhotoCount 选择照片的最大数量
     * @return PhotoAdapter
     */
    public PhotoAdapter setMaxPickerPhotoCount(int maxPickerPhotoCount) {
        this.mMaxPickerPhotoCount = maxPickerPhotoCount;
        return this;
    }

    /**
     * 设置选择照片按钮点击事件
     *
     * @param selectedPhotoClickListener 选择照片按钮点击监听接口
     * @return PhotoAdapter
     */
    public PhotoAdapter setOnSelectorPhotoClickListener(OnSelectorPhotoClickListener selectedPhotoClickListener) {
        this.mOnSelectorPhotoClickListener = selectedPhotoClickListener;
        return this;
    }

    /**
     * 刷新数据
     *
     * @param photoPaths 照片路径
     */
    public void refresh(List<String> photoPaths) {
        mSelectedPhotos.clear();
        if (null != photoPaths && photoPaths.size() > 0) {
            mSelectedPhotos.addAll(photoPaths);
        }
        notifyDataSetChanged();
    }

    protected static class PhotoViewHolder extends RecyclerView.ViewHolder {

        private ImageView ivPhoto; // 照片控件
        private ImageView ivSelectorStatus; // 照片选择状态显示控件
        private View vItemCover; // 选中时仿微信蒙版效果控件
        private ImageView ivDelete; // 删除按钮
        private RelativeLayout rlPhotoItem; // item布局

        public PhotoViewHolder(View itemView) {
            super(itemView);
            ivPhoto = (ImageView) itemView.findViewById(R.id.iv_photo_picker_list_item_photo);
            ivSelectorStatus = (ImageView) itemView.findViewById(R.id.iv_photo_picker_list_item_selected_status);
            vItemCover = itemView.findViewById(R.id.v_photo_picker_list_item_cover);
            ivDelete = (ImageView) itemView.findViewById(R.id.iv_photo_picker_list_item_delete);
            rlPhotoItem = (RelativeLayout) itemView.findViewById(R.id.rl_iv_photo_picker_list_item);
        }
    }

}
