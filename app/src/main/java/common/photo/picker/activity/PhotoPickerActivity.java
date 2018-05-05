package common.photo.picker.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import common.photo.picker.Constants;
import common.photo.picker.R;
import common.photo.picker.adapter.PhotoPickerFragmentRvAdapter;
import common.photo.picker.entity.Photo;
import common.photo.picker.entity.PhotoDirectory;
import common.photo.picker.listener.OnItemSelectStatusChangeListener;
import common.photo.picker.listener.OnPhotoListItemClickListener;
import common.photo.picker.utils.MediaStoreHelper;
import common.photo.picker.utils.PhotoCaptureManager;
import common.photo.picker.widget.TitleBar;

/**
 * 图片选择页面
 */
public class PhotoPickerActivity extends BaseActivity implements View.OnClickListener{

    private TitleBar mTitleBar;
    private ArrayList<String> mOriginalSelectedDatas; // 原始数据

    private PhotoCaptureManager mPhotoCaptureManager; // 照片捕获管理器
    private PhotoPickerFragmentRvAdapter mPhotoAdapter; // 照片列表适配器
    private List<PhotoDirectory> mPhotoDirectoryList = new ArrayList<>(); // 所有的照片
    //private List<String> mOriginalPhotos; // 原始的照片
    private Button mBtnDirectory; // 目录选择按钮
    private Button mBtnPreview;//预览
    //private Context mContext;
//    private DirectorySelectorDialog mDirectorySelectorDialog; // 目录选择Dialog

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_picker);
        mTitleBar = (TitleBar) findViewById(R.id.title_bar_photo_picker);
        initExtras();
        mPhotoAdapter = new PhotoPickerFragmentRvAdapter(PhotoPickerActivity.this, mPhotoDirectoryList, mOriginalSelectedDatas);
        mPhotoCaptureManager = new PhotoCaptureManager(PhotoPickerActivity.this); // 照片捕获管理器
        getAllPhotos();
        RecyclerView rvPhoto = (RecyclerView) findViewById(R.id.rv_photo_picker_fragment);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(Constants.GRID_COLUMN_COUNT, StaggeredGridLayoutManager.VERTICAL);
        rvPhoto.setLayoutManager(staggeredGridLayoutManager);
        rvPhoto.setAdapter(mPhotoAdapter);

        mBtnDirectory = (Button) findViewById(R.id.btn_fragment_photo_picker_directory);
        mBtnPreview = (Button) findViewById(R.id.btn_fragment_photo_picker_preview);

        mBtnDirectory.setOnClickListener(this);
        mBtnPreview.setOnClickListener(this);
        initEvent();
        setListener();
    }

    private void setListener() { //照片选择框点击事件
        mPhotoAdapter.setOnItemSelectStatusChangeListener(new OnItemSelectStatusChangeListener() {
            @Override
            public boolean onItemSelectStatusChange(int position, Photo photo, boolean isSelected, int selectedCount) {
                int total = selectedCount + (isSelected ? -1 : 1);
                if (Constants.MAX_PICKER_PHOTO_COUNT <= 1) {
                    List<Photo> photos = mPhotoAdapter.getSelectedPhotoList();

                    if (!photos.contains(photo)) {
                        photos.clear();
                        mPhotoAdapter.notifyDataSetChanged();
                        return true;
                    }
                }
                if (total > Constants.MAX_PICKER_PHOTO_COUNT) {
                    Toast.makeText(PhotoPickerActivity.this, getString(R.string.hint_over_the_max_limit, Constants.MAX_PICKER_PHOTO_COUNT), Toast.LENGTH_LONG).show();
                    return false;
                }
                //设置标题栏右边已选择数量
                mTitleBar.getRightTextView().setText(getString(R.string.title_right_complete_with_count, total, Constants.MAX_PICKER_PHOTO_COUNT));
                return true;
            }
        });

        //设置TitleBar右侧点击事件
        mTitleBar.getRightTextView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> photoPaths;
                if (Constants.IS_CROP) {//裁剪
                    photoPaths = new ArrayList<String>();
                    photoPaths.add(Constants.CUT_PHOTO);
                } else {
                    photoPaths = mPhotoAdapter.getSelectedPhotoPathList();
                }
                if (null != photoPaths && photoPaths.size() > 0) {
                    Intent intent = new Intent();
                    intent.putStringArrayListExtra(Constants.EXTRA_SELECTED_PHOTOS, photoPaths);
                    setResult(RESULT_OK, intent);
                    finish();
                } else {
                    Toast.makeText(PhotoPickerActivity.this, getString(R.string.no_selected_photo), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void initEvent() {
        // 图片预览点击事件回调
        mPhotoAdapter.setOnPhotoListItemClickeListener(new OnPhotoListItemClickListener() {
            @Override
            public void onPhotoListItemClick(View view, int position, boolean isShowCamera) {//点击预览
                int currentIndex = isShowCamera ? position - 1 : position;
                ArrayList<String> allPreviewPhotoPaths = mPhotoAdapter.getCurrentDirectoryPhotosPath();
                int[] screenLocation = new int[2];
                view.getLocationOnScreen(screenLocation);

//                PhotoPreviewFragment photoPreviewFragment = PhotoPreviewFragment.newInstance(allPreviewPhotoPaths, currentIndex, screenLocation, view.getWidth(), view.getHeight());
//                ((PhotoPickerActivity) getActivity()).addPreviewFragment(photoPreviewFragment);
            }
        });
        mPhotoAdapter.setOnCameraClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { // 拍照的点击事件
                if (null == mPhotoCaptureManager) {
                    //创建照片捕获去器
                    mPhotoCaptureManager = new PhotoCaptureManager(PhotoPickerActivity.this);
                }
                try {
                    //调用系统相机拍照
                    mPhotoCaptureManager.startTakePicture(PhotoPickerActivity.this);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 获取所有照片
     */
    private void getAllPhotos() {
        Bundle bundle = new Bundle();
        MediaStoreHelper.getPhotoDirs(PhotoPickerActivity.this, bundle, new MediaStoreHelper.CursorFinishCallback() {
            @Override
            public void onCursorFinishCallback(List<PhotoDirectory> photoDirs) {
                // 查询到手机上的照片回调
                mPhotoDirectoryList.clear();
                mPhotoDirectoryList.addAll(photoDirs);

                mPhotoAdapter.notifyDataSetChanged();
//                if (null != mDirectorySelectorDialog) {//刷新目录列表数据
//                    mDirectorySelectorDialog.refreshDirectoryListData(mPhotoDirectoryList);
//                }
            }
        });
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
        if (Constants.IS_CROP) { // 支持裁剪时不支持预览,并且选择照片数量重置为1
            Constants.IS_PREVIEW_ENABLE = false;
            Constants.MAX_PICKER_PHOTO_COUNT = 1;
        }
        mOriginalSelectedDatas = intent.getStringArrayListExtra(Constants.EXTRA_ORIGINAL_SELECTED_PHOTO_DATAS);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        if (Activity.RESULT_OK == resultCode && PhotoCaptureManager.REQUEST_CODE_CAMERA == requestCode) {
            //拍照后将照片添加到选择照片界面
            mPhotoCaptureManager.addPhotoToPhotoPicker();
            if (null != mPhotoDirectoryList && mPhotoDirectoryList.size() > 0) {
                String photoPath = mPhotoCaptureManager.getCurrentPhotoPath();
                PhotoDirectory photoDirectory = mPhotoDirectoryList.get(MediaStoreHelper.INDEX_ALL_PHOTOS);
                photoDirectory.getPhotoList().add(MediaStoreHelper.INDEX_ALL_PHOTOS, new Photo(photoPath.hashCode(), photoPath));
                photoDirectory.setCoverPath(photoPath);
                mPhotoAdapter.notifyDataSetChanged();
            }
        }
    }

}
