package common.photo.picker.utils;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;

import java.util.ArrayList;

/**
 * Created by CK on 2017/3/10  12:02.
 *
 * @author CK
 * @version 1.0.0
 * @class PhotoPickerManager
 * @describe 照片选择管理类
 */
public class PhotoPickerManager {

    private static final String TAG = "PhotoPickerManager";

//    public static void onActivityResult(int requestCode, int resultCode, Intent data, PhotoPickerStatus photoPickerStatus) {
//        if (Activity.RESULT_OK == resultCode) {
//            if (PhotoPicker.RESULT_CODE == requestCode) {
//                if (null != data) {
//                    List<String> photoPaths = data.getStringArrayListExtra(Constants.EXTRA_SELECTED_PHOTOS);
//                    photoPickerStatus.onPhotoPickerSuccess(photoPaths);
//                } else {
//                    photoPickerStatus.onPhotoPickerFaile("选择图片失败,请重新选择");
//                }
//            } else if (PhotoPreview.RESULT_CODE == requestCode) {//预览与删除后返回
//                if (null != data) {
//                    List<String> photoPaths = data.getStringArrayListExtra(Constants.EXTRA_SELECTED_PHOTOS);
//                    photoPickerStatus.onPreviewBack(photoPaths);
//                } else {
//                    photoPickerStatus.onPhotoPickerCancel();
//                }
//            } else {
//                photoPickerStatus.onPhotoPickerFaile(TAG + "onActivityResult requestCode type error.");
//            }
//        } else {
//            photoPickerStatus.onPhotoPickerCancel();
//        }
//    }

    /**
     * 跳转到选择照片界面
     *
     * @param activity       Activity对象
     * @param maxPickerCount 选择照片的最大数量
     * @param photoPaths     原始数据
     */
    public static void startPicker(Activity activity, int maxPickerCount, ArrayList<String> photoPaths) {
        startPicker(activity, maxPickerCount, 3, false, true, true, true, photoPaths);
    }

    /**
     * 裁剪——跳转到选择照片界面
     *
     * @param activity Activity对象
     */
    public static void startPicker(Activity activity) {
        startPicker(activity, 1, 3, true, false, true, false, null);
    }

    /**
     * 跳转到选择照片界面
     *
     * @param activity        Activity对象
     * @param maxPickerCount  选择照片的最大数量
     * @param columnCount     每行显示的列数
     * @param isCrop          是否支持裁剪，true支持，false不支持。支持裁剪时最多选择一张图片
     * @param isShowGif       是否显示gif格式图片.true显示，false不显示
     * @param isShowCamera    是否显示拍照按钮.true显示，false不显示
     * @param isPreviewEnable 是否支持预览.true支持预览，false不支持预览.
     * @param photoPaths      原始数据
     */
    public static void startPicker(Activity activity, int maxPickerCount, int columnCount, boolean isCrop,
                                   boolean isShowGif, boolean isShowCamera, boolean isPreviewEnable,
                                   ArrayList<String> photoPaths) {
        PhotoPicker.builder()
                .setMaxPickerPhotoCount(maxPickerCount)
                .setGridColumnCount(columnCount)
                .setOriginalSelectedDatas(photoPaths)
                .setPreviewEnabled(isPreviewEnable)
                .setShowCamera(isShowCamera)
                .setShowGif(isShowGif)
                .setIsCrop(isCrop)
                .start(activity, PhotoPicker.RESULT_CODE);
    }

    public static void crop(Context context, Uri uri, int proportion,int x, int y, Uri outPut) {
//        Intent intent = new Intent("com.android.camera.action.CROP");
//        intent.setDataAndType(uri, "image/*");
//        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//        intent.putExtra("crop", "true");
//        // aspectX aspectY 是宽高的比例
//        intent.putExtra("aspectX", proportion);
//        intent.putExtra("aspectY", 2);
//        // outputX outputY 是裁剪图片宽高
//        intent.putExtra("outputX", x);
//        intent.putExtra("outputY", y);
//
//        intent.putExtra("scale", Constants.SCALE);
//        intent.putExtra("return-data", Constants.RETURN_DATA);
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, outPut);
//        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
//        intent.putExtra("noFaceDetection", !Constants.FACE_DETECTION); // lol, negative
//        // boolean
//        // noFaceDetection
//        if (Constants.CIRCLE_CROP) {
//            intent.putExtra("circleCrop", true);
//        }
//        if (null != intent.resolveActivity(context.getPackageManager())) {
//            ((Activity) context).startActivityForResult(intent,
//                    Constants.REQUEST_CROP_CODE);
//        } else {
//            Toast.makeText(context, "此设备不支持裁剪", Toast.LENGTH_LONG).show();
//        }
    }

}
