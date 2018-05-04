package common.photo.picker.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

import java.util.ArrayList;

import common.photo.picker.Constants;
import common.photo.picker.R;
import common.photo.picker.adapter.PreviewPhotosAdapter;
import common.photo.picker.widget.PhotoPickerView;
import common.photo.picker.widget.TitleBar;

/**
 * 预览图片页面
 */
public class PhotoPreviewActivity extends BaseActivity {
    private TitleBar mTitleBar;
    private int currentIndex;
    private int action;
    private boolean deleteEnable;

    private ViewPager mViewPager;
    private ArrayList<String> mPhotosPathList = new ArrayList<>(); // 预览照片路径集合
    private int mCurrentIndex; // 当前预览照片角标索引
    private boolean isExecuteAnimation; // 是否执行动画，true执行，false不执行.
    private PreviewPhotosAdapter mPreviewPhotosAdapter; // 照片预览ViewPager适配器

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_preview);
        Intent intent = getIntent();
        currentIndex = intent.getIntExtra(Constants.PREVIEW_CURRENT_INDEX, 0);
        final ArrayList<String> allPreviewPhotoPath = intent.getStringArrayListExtra(Constants.PREVIEW_PHOTOS_PATH);
        action = intent.getIntExtra(Constants.PREVIEW_EXTRA_ACTION, PhotoPickerView.ACTION_ONLY_SHOW);
        deleteEnable = intent.getBooleanExtra(Constants.PREVIEW_DELETE_ENABLE, false);

        mTitleBar = (TitleBar) findViewById(R.id.title_bar_photo_preview);
        mViewPager = (ViewPager) findViewById(R.id.vp_photo_preview);
        mPhotosPathList.clear();
        mPhotosPathList.addAll(allPreviewPhotoPath);
        mPreviewPhotosAdapter = new PreviewPhotosAdapter(mPhotosPathList);
        mViewPager.setAdapter(mPreviewPhotosAdapter);
        mViewPager.setCurrentItem(mCurrentIndex);

        if (null != allPreviewPhotoPath && allPreviewPhotoPath.size() > 0) {
            mTitleBar.setCenterTextViewText(getString(R.string.title_photo_preview_count, currentIndex + 1, allPreviewPhotoPath.size()));
        }
//        mViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
//            @Override
//            public void onPageSelected(int position) {
//                //super.onPageSelected(position);
//                mTitleBar.setCenterTextViewText(getString(R.string.title_photo_preview_count, position + 1, allPreviewPhotoPath.size()));
//            }
//        });
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mTitleBar.setCenterTextViewText(getString(R.string.title_photo_preview_count, position + 1, allPreviewPhotoPath.size()));
                isExecuteAnimation = mCurrentIndex == position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent();
        intent.putExtra(Constants.EXTRA_SELECTED_PHOTOS, mPhotosPathList);
        setResult(RESULT_OK, intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPhotosPathList.clear();
        mPhotosPathList = null;
        if (null != mViewPager) {
            mViewPager.setAdapter(null);
        }
    }

}
