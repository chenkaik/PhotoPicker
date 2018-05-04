package common.photo.picker.activity;

import android.content.Intent;
import android.os.Bundle;

import java.util.ArrayList;

import common.photo.picker.Constants;
import common.photo.picker.R;
import common.photo.picker.widget.TitleBar;

/**
 * 图片选择页面
 */
public class PhotoPickerActivity extends BaseActivity {

    private TitleBar mTitleBar;
    private ArrayList<String> mOriginalSelectedDatas;//原始数据

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_picker);
        mTitleBar = (TitleBar) findViewById(R.id.title_bar_photo_picker);
        initExtras();
    }

    /**
     * 初始化参数值
     */
    private void initExtras() {
        Intent intent = getIntent();
        Constants.MAX_PICKER_PHOTO_COUNT = intent.getIntExtra(Constants.EXTRA_MAX_PICKER_PHOTO_COUNT, Constants.MAX_PICKER_PHOTO_COUNT);
        Constants.GRID_COLUMN_COUNT = intent.getIntExtra(Constants.EXTRA_GRID_COLUMN_COUNT, Constants.GRID_COLUMN_COUNT);
        Constants.IS_CROP = intent.getBooleanExtra(Constants.EXTRA_IS_CROP, Constants.IS_CROP);
        Constants.IS_PREVIEW_ENABLE = intent.getBooleanExtra(Constants.EXTRA_PREVIEW_ENABLED, Constants.IS_PREVIEW_ENABLE);
        Constants.IS_SHOW_GIF = intent.getBooleanExtra(Constants.EXTRA_IS_SHOW_GIF, Constants.IS_SHOW_GIF);
        Constants.IS_SHOW_CAMERA = intent.getBooleanExtra(Constants.EXTRA_IS_SHOW_CAMERA, Constants.IS_SHOW_CAMERA);
        if (Constants.IS_CROP) {//支持裁剪时不支持预览,并且选择照片数量重置为1
            Constants.IS_PREVIEW_ENABLE = false;
            Constants.MAX_PICKER_PHOTO_COUNT = 1;
        }
        mOriginalSelectedDatas = intent.getStringArrayListExtra(Constants.EXTRA_ORIGINAL_SELECTED_PHOTO_DATAS);
    }

}
