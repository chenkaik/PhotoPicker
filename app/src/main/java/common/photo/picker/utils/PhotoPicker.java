package common.photo.picker.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

import java.util.ArrayList;

import common.photo.picker.Constants;
import common.photo.picker.activity.PhotoPickerActivity;

/**
 * Created by CK on 2017/3/10  13:26.
 *
 * @author CK
 * @version 1.0.0
 * @class PhotoPicker
 * @describe 照片选择构建的辅助类
 */
public final class PhotoPicker {
    public static final int RESULT_CODE = 201;

    public static PhotoPickerBuilder builder() {
        return new PhotoPickerBuilder();
    }

    public final static class PhotoPickerBuilder {
        private Bundle mPhotoPickerBundle;
        private Intent mPhotoPickerIntent;

        public PhotoPickerBuilder() {
            mPhotoPickerBundle = new Bundle();
            mPhotoPickerIntent = new Intent();
        }

        public void start(@NonNull Activity activity, int requestCode) {
            activity.startActivityForResult(getIntent(activity), requestCode);
        }

        /**
         * 设置选择照片的最大数量
         *
         * @param maxPickerPhotoCount 选择照片的最大数量
         * @return PhotoPickerBuilder对象
         */
        public PhotoPickerBuilder setMaxPickerPhotoCount(int maxPickerPhotoCount) {
            mPhotoPickerBundle.putInt(Constants.EXTRA_MAX_PICKER_PHOTO_COUNT, maxPickerPhotoCount);
            return this;
        }

        /**
         * 设置每行显示的列数
         *
         * @param columnCount 每行显示的列数
         * @return PhotoPickerBuilder对象
         */
        public PhotoPickerBuilder setGridColumnCount(int columnCount) {
            mPhotoPickerBundle.putInt(Constants.EXTRA_GRID_COLUMN, columnCount);
            return this;
        }

        /**
         * 设置是否显示gif格式图片
         *
         * @param isShowGif 是否显示gif格式图片.true显示，false不显示
         * @return PhotoPickerBuilder对象
         */
        public PhotoPickerBuilder setShowGif(boolean isShowGif) {
            mPhotoPickerBundle.putBoolean(Constants.EXTRA_IS_SHOW_GIF, isShowGif);
            return this;
        }
        /**
         * 设置是否支持裁剪
         *
         * @param isCrop 是否支持裁剪，true支持，false不支持。支持裁剪时最多选择一张图片
         * @return PhotoPickerBuilder对象
         */
        public PhotoPickerBuilder setIsCrop(boolean isCrop) {
            mPhotoPickerBundle.putBoolean(Constants.EXTRA_IS_CROP, isCrop);
            return this;
        }

        /**
         * 设置是否显示拍照按钮
         *
         * @param isShowCamera 是否显示拍照按钮.true显示，false不显示
         * @return PhotoPickerBuilder对象
         */
        public PhotoPickerBuilder setShowCamera(boolean isShowCamera) {
            mPhotoPickerBundle.putBoolean(Constants.EXTRA_IS_SHOW_CAMERA, isShowCamera);
            return this;
        }

        /**
         * 设置原始的选择的照片数据
         *
         * @param originalSelectedDatas 原始的选择的照片数据
         * @return PhotoPickerBuilder对象
         */
        public PhotoPickerBuilder setOriginalSelectedDatas(ArrayList<String> originalSelectedDatas) {
            mPhotoPickerBundle.putStringArrayList(Constants.EXTRA_ORIGINAL_SELECTED_PHOTO_DATAS, originalSelectedDatas);
            return this;
        }

        /**
         * 设置是否支持预览
         *
         * @param previewEnabled 是否支持预览.true支持预览，false不支持预览.
         * @return PhotoPickerBuilder对象
         */
        public PhotoPickerBuilder setPreviewEnabled(boolean previewEnabled) {
            mPhotoPickerBundle.putBoolean(Constants.EXTRA_PREVIEW_ENABLED, previewEnabled);
            return this;
        }


        public Intent getIntent(@NonNull Context context) {
            mPhotoPickerIntent.setClass(context, PhotoPickerActivity.class);
            mPhotoPickerIntent.putExtras(mPhotoPickerBundle);
            return mPhotoPickerIntent;
        }
    }

}
