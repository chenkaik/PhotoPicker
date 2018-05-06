package common.photo.picker.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import common.photo.picker.R;
import common.photo.picker.entity.PhotoDirectory;
import common.photo.picker.listener.OnPhotoDirectoryItemClickListener;
import common.photo.picker.utils.ImageLoader;

/**
 * Created by CK on 2017/3/24  10:50.
 *
 * @author CK
 * @version 1.0.0
 * @class DirectorySelectorRvAdapter
 * @describe 目录选择列表适配器.
 */
public class DirectorySelectorRvAdapter extends RecyclerView.Adapter<DirectorySelectorRvAdapter.DirectorySelectoryViewHolder> {
    private Button mBtnDirectory;
    private List<PhotoDirectory> mPhotoDirectoryList;//照片目录集合
    private LayoutInflater mLayoutInflater;
    private int mHistorySelectedIndex;//历史选中目录下标索引
    private OnPhotoDirectoryItemClickListener mOnPhotoDirectoryItemClickListener;

    public DirectorySelectorRvAdapter(Context context, List<PhotoDirectory> photoDirectoryList, OnPhotoDirectoryItemClickListener onPhotoDirectoryItemClick) {
        this.mPhotoDirectoryList = photoDirectoryList;
        this.mOnPhotoDirectoryItemClickListener = onPhotoDirectoryItemClick;
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public DirectorySelectoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mLayoutInflater.inflate(R.layout.item_directory_selectory_list, parent, false);
        return new DirectorySelectoryViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final DirectorySelectoryViewHolder holder, int position) {
        final int pos = holder.getAdapterPosition();
        final PhotoDirectory photoDirectory = mPhotoDirectoryList.get(pos);
        //目录照片
        ImageLoader.load(photoDirectory.getCoverPath(), holder.ivDirectoryPhoto);
        //目录名字
        holder.tvDirectoryName.setText(photoDirectory.getName());
        //目录下照片数量
        holder.tvDirectoryCount.setText(holder.tvDirectoryCount.getContext().getString(R.string.directory_photo_count, photoDirectory.getPhotoPaths().size()));
        if (photoDirectory.isSelected()) {
            mHistorySelectedIndex = pos;
        }
        //目录选择状态
        holder.ivDirectoryStatus.setSelected(photoDirectory.isSelected());
        //子条目点击事件
        holder.llDirectorySelectorLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pos != mHistorySelectedIndex) {
                    photoDirectory.setSelected(true);
                    mPhotoDirectoryList.get(mHistorySelectedIndex).setSelected(false);
                    notifyItemChanged(pos);
                    notifyItemChanged(mHistorySelectedIndex);
                    if (null != mBtnDirectory) {
                        mBtnDirectory.setText(photoDirectory.getName());
                    }
                    if (null != mOnPhotoDirectoryItemClickListener) {
                        mOnPhotoDirectoryItemClickListener.onPhotoDirectoryItemClick(pos);
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return null == mPhotoDirectoryList ? 0 : mPhotoDirectoryList.size();
    }

    public static class DirectorySelectoryViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout llDirectorySelectorLayout;
        private ImageView ivDirectoryPhoto;//目录照片
        private TextView tvDirectoryName;//目录名字
        private TextView tvDirectoryCount;//目录下照片数量
        private ImageView ivDirectoryStatus;//目录选择状态，true选中，false未选中

        private DirectorySelectoryViewHolder(View itemView) {
            super(itemView);
            llDirectorySelectorLayout = (LinearLayout) itemView.findViewById(R.id.ll_directory_selector_list_item);
            ivDirectoryPhoto = (ImageView) itemView.findViewById(R.id.iv_directory_selector_list_item_photo);
            tvDirectoryName = (TextView) itemView.findViewById(R.id.tv_directory_selector_list_item_name);
            tvDirectoryCount = (TextView) itemView.findViewById(R.id.tv_directory_selector_list_item_count);
            ivDirectoryStatus = (ImageView) itemView.findViewById(R.id.iv_directory_selector_list_item_status);
        }
    }

    public void setDirectoryNameView(Button directoryNameView) {
        this.mBtnDirectory = directoryNameView;
    }

}
