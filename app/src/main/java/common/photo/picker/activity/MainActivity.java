package common.photo.picker.activity;

import android.content.Intent;
import android.os.Bundle;
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

}
