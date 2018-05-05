package common.photo.picker.utils;

import android.content.Context;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.content.CursorLoader;

import common.photo.picker.Constants;

import static android.provider.MediaStore.MediaColumns.MIME_TYPE;

/**
 * Created by CK on 2017/3/10  15:28.
 *
 * @author CK
 * @version 1.0.0
 * @class PhotoDirectoryLoader
 * @describe 照片目录查询器
 */
public final class PhotoDirectoryLoader extends CursorLoader {

    public PhotoDirectoryLoader(Context context) {
        super(context);
        String[] imageProjection = {
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media.BUCKET_ID,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
                MediaStore.Images.Media.DATE_ADDED};

        setProjection(imageProjection);
        setUri(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        setSortOrder(MediaStore.Images.Media.DATE_ADDED + " DESC");//降序排列
        setSelection(MIME_TYPE + "=? or " + MIME_TYPE + "=? or " + MIME_TYPE + "=? " + (Constants.IS_SHOW_GIF ? ("or " + MIME_TYPE + "=?") : ""));

        String[] selectionArgs;
        if (Constants.IS_SHOW_GIF) {
            selectionArgs = new String[]{"image/jpeg", "image/png", "image/jpg", "image/gif"};
        } else {
            selectionArgs = new String[]{"image/jpeg", "image/png", "image/jpg"};
        }
        setSelectionArgs(selectionArgs);
    }

    public PhotoDirectoryLoader(Context context, Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        super(context, uri, projection, selection, selectionArgs, sortOrder);
    }

}
