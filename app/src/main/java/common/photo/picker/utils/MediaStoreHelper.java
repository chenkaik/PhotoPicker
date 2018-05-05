package common.photo.picker.utils;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import common.photo.picker.R;
import common.photo.picker.entity.PhotoDirectory;

import static android.provider.MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME;
import static android.provider.MediaStore.Images.ImageColumns.BUCKET_ID;
import static android.provider.MediaStore.MediaColumns.DATA;
import static android.provider.MediaStore.MediaColumns.DATE_ADDED;

/**
 * Created by CK on 2017/3/10  15:19.
 *
 * @author CK
 * @version 1.0.0
 * @class MediaStoreHelper
 * @describe 获取设备所有照片帮助类
 */
public class MediaStoreHelper {
    public final static int INDEX_ALL_PHOTOS = 0;

    public static void getPhotoDirs(FragmentActivity fragmentActivity, Bundle args, CursorFinishCallback cursorFinishCallback) {
        fragmentActivity.getSupportLoaderManager().initLoader(0, args, new PhotoDirLoaderCallbacks(fragmentActivity, cursorFinishCallback));
    }

    private static class PhotoDirLoaderCallbacks implements LoaderManager.LoaderCallbacks<Cursor> {
        private WeakReference<Context> contexts;
        private CursorFinishCallback cursorFinishCallback;

        public PhotoDirLoaderCallbacks(Context context, CursorFinishCallback cursorFinishCallback) {
            this.contexts = new WeakReference<Context>(context);
            this.cursorFinishCallback = cursorFinishCallback;
        }

        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            return new PhotoDirectoryLoader(contexts.get());
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            if (null == data) {
                return;
            }
//            if(!data.moveToFirst()){ // 此处注意是v7包27版本 LoaderManager引起的问题
//                return;
//            }
            List<PhotoDirectory> photoDirectorys = new ArrayList<>();
            PhotoDirectory photoDirecyoryAll = new PhotoDirectory(contexts.get().getString(R.string.photo_directory_all_id), contexts.get().getString(R.string.photo_directory_all_name));
            photoDirecyoryAll.setSelected(true);//设置默认选中所有照片目录

            while (data.moveToNext()) {
                int photoId = data.getInt(data.getColumnIndexOrThrow(BaseColumns._ID));
                String bucketId = data.getString(data.getColumnIndexOrThrow(BUCKET_ID));//dir id 目录
                String name = data.getString(data.getColumnIndexOrThrow(BUCKET_DISPLAY_NAME));//dir id 名称
                String path = data.getString(data.getColumnIndexOrThrow(DATA));//图片路径

                PhotoDirectory photoDirectory = new PhotoDirectory(bucketId, name);
                if (!photoDirectorys.contains(photoDirectory)) {
                    photoDirectory.setCoverPath(path);
                    photoDirectory.addPhoto(photoId, path);
                    photoDirectory.setDateAdded(data.getLong(data.getColumnIndexOrThrow(DATE_ADDED)));

                    photoDirectorys.add(photoDirectory);
                } else {
                    photoDirectorys.get(photoDirectorys.indexOf(photoDirectory)).addPhoto(photoId, path);
                }

                photoDirecyoryAll.addPhoto(photoId, path);
            }

            if (photoDirecyoryAll.getPhotoPaths().size() > 0) {
                photoDirecyoryAll.setCoverPath(photoDirecyoryAll.getPhotoPaths().get(0));
            }

            photoDirectorys.add(INDEX_ALL_PHOTOS, photoDirecyoryAll);

            if (null != cursorFinishCallback) {
                cursorFinishCallback.onCursorFinishCallback(photoDirectorys);
            }
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {

        }
    }

    /**
     * 照片查询完成回调接口
     */
    public interface CursorFinishCallback {
        /**
         * 照片查询完成
         *
         * @param photoDirs 查询结果
         */
        void onCursorFinishCallback(List<PhotoDirectory> photoDirs);
    }

}
