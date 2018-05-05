package common.photo.picker.adapter;

import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import common.photo.picker.entity.Photo;
import common.photo.picker.entity.PhotoDirectory;
import common.photo.picker.listener.SelectManager;

/**
 * Created by CK on 2017/3/13  10:10.
 *
 * @author CK
 * @version 1.0.0
 * @class SelectManagerAdapter
 * @describe 照片选择状态管理适配器
 */
public abstract class SelectManagerAdapter<HV extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<HV> implements SelectManager {

    protected List<PhotoDirectory> mPhotoDirectoryList;//所有照片.
    protected List<String> mOriginalPhotoList;//源照片集合.

    protected List<Photo> mSelectedPhotoList;//已选择照片集合.

    protected int mCurrentDirectoryIndex;//当前目录索引.

    public SelectManagerAdapter() {
        mPhotoDirectoryList = new ArrayList<>();
        mSelectedPhotoList = new ArrayList<>();
    }

    /**
     * 是否选中该照片
     *
     * @param photo 需要判断的照片
     * @return true已选择，false未选择。
     */
    @Override
    public boolean isSelected(Photo photo) { // 判断进入页面之前已选择的照片
        if (null != mOriginalPhotoList && mOriginalPhotoList.size() > 0 &&
                mOriginalPhotoList.contains(photo.getPath()) &&
                !mSelectedPhotoList.contains(photo)) {
            return mSelectedPhotoList.add(photo);
        }
        return mSelectedPhotoList.contains(photo);
    }

    /**
     * 切换选择状态
     *
     * @param photo 需要切换选择状态的Photo对象。
     */
    @Override
    public void toggleSelection(Photo photo) {
        if (mSelectedPhotoList.contains(photo)) {
            mSelectedPhotoList.remove(photo);
            if (null != mOriginalPhotoList &&
                    mOriginalPhotoList.size() > 0 &&
                    mOriginalPhotoList.contains(photo.getPath())) {
                mOriginalPhotoList.remove(photo.getPath());
            }
        } else {
            mSelectedPhotoList.add(photo);
        }
    }

    @Override
    public void clearAllSelection() {
        mSelectedPhotoList.clear();
    }

    @Override
    public int getSelectedItemCount() {
        return mSelectedPhotoList.size();
    }

    /**
     * 设置当前目录索引.
     *
     * @param currentDirectoryIndex 当前目录索引.
     */
    public void setCurrentDirectoryIndex(int currentDirectoryIndex) {
        this.mCurrentDirectoryIndex = currentDirectoryIndex;
    }

    /**
     * 获取当前目录下所有照片.
     *
     * @return 当前目录下所有照片.
     */
    public List<Photo> getCurrentDirectoryOfPhotos() {
        return mPhotoDirectoryList.get(mCurrentDirectoryIndex).getPhotoList();
    }

    /**
     * 获取当前目录下所有照片的路径.
     *
     * @return 当前目录下所有照片的路径.
     */
    public ArrayList<String> getCurrentDirectoryPhotosPath() {
        List<Photo> photos = getCurrentDirectoryOfPhotos();
        ArrayList<String> currentDirectoryPhotosPath = new ArrayList<>(photos.size());
        for (Photo photo : photos) {
            currentDirectoryPhotosPath.add(photo.getPath());
        }
        return currentDirectoryPhotosPath;
    }

    /**
     * 获取所有已选择的照片.
     *
     * @return 所有已选择的照片.
     */
    public List<Photo> getSelectedPhotoList() {
        return mSelectedPhotoList;
    }

    /**
     * 获取所有已选择的照片路径
     *
     * @return 所有已选择的照片路径
     */
    public ArrayList<String> getSelectedPhotoPathList() {
        if (null != mSelectedPhotoList && mSelectedPhotoList.size() > 0) {
            ArrayList<String> selectedPhotoPathList = new ArrayList<>(mSelectedPhotoList.size());
            for (Photo photo : mSelectedPhotoList) {
                selectedPhotoPathList.add(photo.getPath());
            }
            return selectedPhotoPathList;
        }
        return null;
    }

}
