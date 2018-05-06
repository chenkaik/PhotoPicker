package common.photo.picker.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import java.util.List;

import common.photo.picker.R;
import common.photo.picker.utils.ImageLoader;
import common.photo.picker.widget.PhotoPickerView;

public class MainActivity extends BaseActivity {

    private static final String TAG = "MainActivity";

    private PhotoPickerView mPhotoPicker;
    private List<String> mSelectedAllPhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { // 申请相机的权限
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
            }
        }
        mPhotoPicker = (PhotoPickerView) findViewById(R.id.photo_picker);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ImageLoader.clearMemory(MainActivity.this);
        mPhotoPicker.onActivityResult(requestCode, resultCode, data);
        mSelectedAllPhotoPath = mPhotoPicker.getSelectedPhotos();
        if (null != mSelectedAllPhotoPath && mSelectedAllPhotoPath.size() > 0) {
            Toast.makeText(this, "选择了" + mSelectedAllPhotoPath.size() + "张", Toast.LENGTH_SHORT).show();
            Toast.makeText(this, mSelectedAllPhotoPath.get(0), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "未选择图片", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 100:
                if (grantResults.length > 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "拒绝了权限，程序无法正常运行!", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

}
