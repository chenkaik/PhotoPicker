package common.photo.picker.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

import java.util.ArrayList;

import common.photo.picker.Constants;
import common.photo.picker.activity.PhotoPreviewActivity;
import common.photo.picker.widget.PhotoPickerView;

/**
 * Created by CK on 2017/3/22  16:34.
 *
 * @author CK
 * @version 1.0.0
 * @class PhotoPreview
 * @describe 照片预览Intent构建类
 */
public class PhotoPreview {
    public static final int RESULT_CODE = 202;

    public static PhotoPreviewBuilder builder() {
        return new PhotoPreviewBuilder();
    }

    public static class PhotoPreviewBuilder {
        private Bundle mPhotoPreviewOptionsBundle;
        private Intent mPreviewIntent;

        private PhotoPreviewBuilder() {
            mPhotoPreviewOptionsBundle = new Bundle();
            mPreviewIntent = new Intent();
        }

        /**
         * 设置动作
         * @param action
         * @return
         */
        public PhotoPreviewBuilder setAction(@PhotoPickerView.MultPhotoPickerAction int action) {
            mPhotoPreviewOptionsBundle.getInt(Constants.PREVIEW_EXTRA_ACTION, action);
            return this;
        }

        /**
         * 预览照片的集合
         * @param previewPhotoPathLsit
         * @return
         */
        public PhotoPreviewBuilder setPreviewPhotoPathList(ArrayList<String> previewPhotoPathLsit) {
            mPhotoPreviewOptionsBundle.putStringArrayList(Constants.PREVIEW_PHOTOS_PATH, previewPhotoPathLsit);
            return this;
        }

        /**
         * 当前的索引
         * @param currentIndex
         * @return
         */
        public PhotoPreviewBuilder setCurrentIndex(int currentIndex) {
            mPhotoPreviewOptionsBundle.putInt(Constants.PREVIEW_CURRENT_INDEX, currentIndex);
            return this;
        }

        /**
         * 是否启用删除
         * @param deleteEnable
         * @return
         */
        public PhotoPreviewBuilder setDeleteEnable(boolean deleteEnable) {
            mPhotoPreviewOptionsBundle.putBoolean(Constants.PREVIEW_DELETE_ENABLE, deleteEnable);
            return this;
        }

        private Intent getIntent(@NonNull Context context) {
            mPreviewIntent.setClass(context, PhotoPreviewActivity.class);
            mPreviewIntent.putExtras(mPhotoPreviewOptionsBundle);
            return mPreviewIntent;
        }

        public void start(Activity activity) {
            start(activity, RESULT_CODE);
        }

        private void start(Activity activity, int requestCode) {
            activity.startActivityForResult(getIntent(activity), requestCode);
        }
    }

}
