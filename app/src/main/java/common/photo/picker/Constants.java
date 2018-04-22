package common.photo.picker;

import android.os.Environment;

/**
 * Created by chenKai on 2018/4/22.
 * 用到的常量
 */
public class Constants {
    public static int MAX_PICKER_PHOTO_COUNT = 10;//选择照片的最大数量
    public static int GRID_COLUMN_COUNT = 3;//每行显示的列数
    public static boolean IS_PREVIEW_ENABLE = false;//是否支持预览.true支持预览，false不支持预览.
    public static boolean IS_SHOW_CAMERA = true;//是否显示拍照按钮.true显示，false不显示
    public static boolean IS_SHOW_GIF = true;//是否显示gif格式图片.true显示，false不显示
    public static boolean IS_CROP = false;//是否支持裁剪，true支持，false不支持。支持裁剪时最多选择一张图片

    public final static String EXTRA_MAX_PICKER_PHOTO_COUNT = "MAX_PICKER_PHOTO_COUNT";
    public final static String EXTRA_GRID_COLUMN = "column";
    public final static String EXTRA_IS_SHOW_GIF = "IS_SHOW_GIF";
    public final static String EXTRA_IS_CROP = "IS_CROP";
    public final static String EXTRA_IS_SHOW_CAMERA = "EXTRA_IS_SHOW_CAMERA";
    public final static String EXTRA_ORIGINAL_SELECTED_PHOTO_DATAS = "ORIGINAL_SELECTED_PHOTO_DATAS";
    public final static String EXTRA_PREVIEW_ENABLED = "PREVIEW_ENABLED";
    public final static String EXTRA_GRID_COLUMN_COUNT = "EXTRA_GRID_COLUMN_COUNT";

    public final static String EXTRA_SELECTED_PHOTOS = "EXTRA_SELECTED_PHOTOS";

    public final static long ANIM_DURATION = 200L;//预览动画执行时长

    //--------------------------------预览相关-----------------------------------------/
    public final static String PREVIEW_PHOTOS_PATH = "PREVIEW_PHOTOS_PATH";
    public final static String PREVIEW_CURRENT_INDEX = "PREVIEW_CURRENT_INDEX";
    public final static String PREVIEW_IS_EXECUTE_ANIMATION = "PREVIEW_IS_EXECUTE_ANIMATION";
    public final static String PREVIEW_DELETE_ENABLE = "PREVIEW_DELETE_ENABLE";
    public final static String PREVIEW_THUMBNAIL_LEFT = "PREVIEW_THUMBNAIL_LEFT";
    public final static String PREVIEW_THUMBNAIL_TOP = "PREVIEW_THUMBNAIL_TOP";
    public final static String PREVIEW_THUMBNAIL_WIDTH = "PREVIEW_THUMBNAIL_WIDTH";
    public final static String PREVIEW_THUMBNAIL_HEIGHT = "PREVIEW_THUMBNAIL_HEIGHT";
    public final static String PREVIEW_EXTRA_ACTION = "PREVIEW_EXTRA_ACTION";
    public final static String AUTHORITY = "common.photo.picker"; // 适配7.0拍照获取Uri

    //--------------------------------裁剪相关-----------------------------------------/
    //裁剪参数
    public static int REQUEST_CROP_CODE = 200;//裁剪请求码
    public static boolean RETURN_DATA = false;
    public static boolean SCALE = true;
    public static boolean FACE_DETECTION = true;
    public static boolean CIRCLE_CROP = false;
    public static String CUT_PHOTO = Environment.getExternalStorageDirectory() + "/FrameWork/IMAGE/" + "qinHeYuanUserCutIcon.jpg";

}
