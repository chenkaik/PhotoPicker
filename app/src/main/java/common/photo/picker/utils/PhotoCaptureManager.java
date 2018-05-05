package common.photo.picker.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by CK on 2017/3/10  16:30.
 *
 * @author CK
 * @version 1.0.0
 * @class PhotoCaptureManager
 * @describe 照片捕获管理器。用于拍照后捕获照片。
 */
public class PhotoCaptureManager {
    private String TAG = getClass().getName();
    public static final String KEY_CURRENT_PHOTO_PATH = "KEY_CURRENT_PHOTO_PATH";
    public static final int REQUEST_CODE_CAMERA = 1;//拍照请求码

    private Context mContext;
    private String mCurrentPhotoPath;

    public PhotoCaptureManager(Context mContext) {
        this.mContext = mContext;
    }

    public File createPhotoFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH).format(new Date());
        String photoFileName = timeStamp + ".jpg";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        if (!storageDir.exists()) {
            if (!storageDir.mkdirs()) {
                Log.e(TAG, "获取\\创建照片目录失败");
                throw new IOException();
            }
        }
        File photoFile = new File(storageDir, photoFileName);
        mCurrentPhotoPath = photoFile.getAbsolutePath();
        return photoFile;
    }

    /**
     * 调用系统相机拍照
     *
     * @param activity Activity
     * @throws IOException IOException
     */
    public void startTakePicture(Activity activity) throws IOException {
        activity.startActivityForResult(takePhotoIntent(), REQUEST_CODE_CAMERA);
    }

    /**
     * 调用系统相机拍照
     *
     * @param fragment fragment
     * @throws IOException IOException
     */
    public void startTakePicture(Fragment fragment) throws IOException {
        fragment.startActivityForResult(takePhotoIntent(), REQUEST_CODE_CAMERA);
    }

    /**
     * 获取调用系统相机拍照的Intent.
     *
     * @return 调用系统相机拍照的Intent.
     */
    private Intent takePhotoIntent() throws IOException {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (null != takePictureIntent.resolveActivity(mContext.getPackageManager())) {
            File photoFile = createPhotoFile();
            Uri photoUri = UriUtils.getUriFromFile(mContext, photoFile);
            if (null != photoUri) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
            }
        }
        return takePictureIntent;
    }

    /**
     * 拍照后将照片添加到选择照片界面.
     */
    public void addPhotoToPhotoPicker() {
        if (!TextUtils.isEmpty(mCurrentPhotoPath)) {
            Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            File photoFile = new File(mCurrentPhotoPath);
            Uri contentUri = Uri.fromFile(photoFile);
            mediaScanIntent.setData(contentUri);
            mContext.sendBroadcast(mediaScanIntent);
        }
    }

    /**
     * 获取当前照片路径
     *
     * @return 当前照片路径.
     */
    public String getCurrentPhotoPath() {
        return mCurrentPhotoPath;
    }

    /**
     * 保存现场
     *
     * @param saveInstanceState 需要保存的数据.
     */
    public void onSaveInstanceState(Bundle saveInstanceState) {
        if (null != saveInstanceState && !TextUtils.isEmpty(mCurrentPhotoPath)) {
            saveInstanceState.putString(KEY_CURRENT_PHOTO_PATH, mCurrentPhotoPath);
        }
    }

    /**
     * 恢复现场
     *
     * @param saveInstanceState 需要恢复的数据.
     */
    public void onRestoreInstanceState(Bundle saveInstanceState) {
        if (null != saveInstanceState && saveInstanceState.containsKey(KEY_CURRENT_PHOTO_PATH)) {
            mCurrentPhotoPath = saveInstanceState.getString(KEY_CURRENT_PHOTO_PATH);
        }
    }

}
