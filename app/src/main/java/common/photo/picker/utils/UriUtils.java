package common.photo.picker.utils;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;

import java.io.File;

import common.photo.picker.Constants;

/**
 * Created by CK on 2017/4/27  14:17.
 *
 * @author CK
 * @version 1.0.0
 * @class UriUtils
 * @describe 7.0适配uri
 */
public class UriUtils {

    public static Uri getUriFromFile(Context context, File file) {
        Uri uri = null;
        if (null != file) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                //第二个参数为在Manifest定义的provider的authorities属性
                uri = FileProvider.getUriForFile(context, Constants.AUTHORITY, file);
            } else {
                uri = Uri.fromFile(file);
            }
        }
        return uri;
    }

    public static Uri getUriFromFilePath(Context context, String filePath) {
        return getUriFromFile(context, new File(filePath));
    }

}
