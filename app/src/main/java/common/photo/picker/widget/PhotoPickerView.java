package common.photo.picker.widget;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.support.annotation.AttrRes;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

import common.photo.picker.Constants;
import common.photo.picker.R;
import common.photo.picker.adapter.PhotoAdapter;
import common.photo.picker.listener.OnSelectorPhotoClickListener;
import common.photo.picker.listener.PhotoPickerStatus;
import common.photo.picker.utils.ImageLoader;
import common.photo.picker.utils.PhotoPickerManager;

/**
 * Created by chenKai on 2018/4/22.
 * 多功能图片选择器
 */
public class PhotoPickerView extends FrameLayout{

    @IntDef({ACTION_SELECT, ACTION_ONLY_SHOW, ACTION_CROP})
    //Tell the compiler not to store annotation data in the .class file
    @Retention(RetentionPolicy.SOURCE)
    //Declare the NavigationMode annotation
    public @interface MultPhotoPickerAction {

    }

    public static final int ACTION_SELECT = 1; // 该组件用于图片选择
    public static final int ACTION_ONLY_SHOW = 2; // 该组件仅用于图片显示
    public static final int ACTION_CROP = 3; // 该组件用于选择一张图片并裁剪

    private int mMultPhotoPickerAction; // ACTION_SELECT  or  ACTION_ONLY_SHOW    or  ACTION_CROP. default:ACTION_SELECT
    public int mMaxPickerPhotoCount; // 选择照片时最多选择的数量.支持裁剪时此值重置为1.
    public static int mGridColumnCount; // 每行显示的列数
    public static boolean isShowGif; // 是否加载Gif格式的图片
    public static boolean isShowCamera; // 是否显示照相按钮
    public static boolean isPreviewEnable; // 是否启用预览

    private RecyclerView mRecyclerView;
    private PhotoAdapter mPhotoAdapter;
    private ArrayList<String> mSelectedPhotos; // 选择的照片的路径

    private ImageView mImageView;
    private Context mContext;
    private OnSelectorPhotoClickListener mOnSelectorPhotoClickListener = new OnSelectorPhotoClickListener() {
        @Override
        public void selectorPhotoClickListener() {
            //先判断是否有拍照权限和读写权限，再去选择照片
            checkPermission();
        }
    };

    public PhotoPickerView(@NonNull Context context) {
        this(context, null);
    }

    public PhotoPickerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PhotoPickerView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initStyle(attrs);
        initView(context, attrs);
    }

    private void initStyle(@Nullable AttributeSet attrs) {
        TypedArray typedArray = getResources().obtainAttributes(attrs, R.styleable.PhotoPickerView);
        mMultPhotoPickerAction = typedArray.getInt(R.styleable.PhotoPickerView_action, ACTION_SELECT);
        mMaxPickerPhotoCount = typedArray.getInt(R.styleable.PhotoPickerView_max_picker_photo_count, Constants.MAX_PICKER_PHOTO_COUNT);
        mGridColumnCount = typedArray.getInt(R.styleable.PhotoPickerView_column_count, Constants.GRID_COLUMN_COUNT);
        if (ACTION_CROP == mMultPhotoPickerAction) {
            // 如果支持裁剪的话只能选择一张图片
            mMaxPickerPhotoCount = 1;
        }
        isShowGif = typedArray.getBoolean(R.styleable.PhotoPickerView_is_show_gif, Constants.IS_SHOW_GIF);
        isShowCamera = typedArray.getBoolean(R.styleable.PhotoPickerView_is_show_camera, Constants.IS_SHOW_CAMERA);
        isPreviewEnable = typedArray.getBoolean(R.styleable.PhotoPickerView_is_preview_enable, Constants.IS_PREVIEW_ENABLE);
        typedArray.recycle();
    }

    private void initView(@NonNull Context context, @Nullable AttributeSet attrs) {
        mSelectedPhotos = new ArrayList<>(mMaxPickerPhotoCount); // 构造一个具有初始容量的空列表
        createView(context, attrs);
        switch (mMultPhotoPickerAction) {  // 值是1
            case ACTION_CROP:
                initImageView();
                break;
            case ACTION_SELECT: // 选择图片
            case ACTION_ONLY_SHOW:// 显示图片
                initRecyclerView(context);
                break;
            default:
                throw new IllegalArgumentException("Please set PhotoPickerView action.");
        }

    }

    private void createView(@NonNull Context context, @Nullable AttributeSet attrs) {
        if (ACTION_CROP == mMultPhotoPickerAction) {
            if (null == mImageView) {
                mImageView = new ImageView(context);
                mImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
                mImageView.setLayoutParams(layoutParams);
                this.addView(mImageView);
            }
        } else {
            if (null == mRecyclerView) {
                mRecyclerView = new RecyclerView(context, attrs);
                this.addView(mRecyclerView);
            }
        }
    }

    private void initRecyclerView(@NonNull Context context) {
        if (ACTION_SELECT == mMultPhotoPickerAction) {
            mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(4, OrientationHelper.VERTICAL));
        } else {
            mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(3, OrientationHelper.VERTICAL));
        }
        mPhotoAdapter = new PhotoAdapter(context, mSelectedPhotos);
        mPhotoAdapter.setMultPhotoPickerAction(mMultPhotoPickerAction) // 设置操作模式
                .setOnSelectorPhotoClickListener(mOnSelectorPhotoClickListener) // 选择照片按钮点击事件
                .setMaxPickerPhotoCount(mMaxPickerPhotoCount); // 选择照片最大数量
        mPhotoAdapter.setHasStableIds(true);
        mRecyclerView.setAdapter(mPhotoAdapter);
    }

    private void initImageView() {
        mImageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermission();
            }
        });
    }

    /**
     * 获取已选择的照片
     *
     * @return 已选择的照片
     */
    public final List<String> getSelectedPhotos() {
        return mSelectedPhotos;
    }

    public final ImageView getImageView() {
        return mImageView;
    }

    /**
     * 显示照片
     *
     * @param photoPaths 需要显示的照片
     */
    public final void showPhotos(List<String> photoPaths) {
        mPhotoAdapter.refresh(photoPaths);
    }

    /**
     * 打开照片选择器选择照片
     */
    private void openPhotoPicker() {
        if (PhotoPickerView.ACTION_SELECT == mMultPhotoPickerAction) {
            PhotoPickerManager.startPicker((Activity) mContext, // Activity对象
                    mMaxPickerPhotoCount, // 选择照片的最大数量
                    mGridColumnCount, // 每行显示的列数
                    PhotoPickerView.ACTION_CROP == mMultPhotoPickerAction, // 是否支持裁剪，true支持，false不支持。支持裁剪时最多选择一张图片
                    isShowGif, // 是否显示gif格式图片.true显示，false不显示
                    isShowCamera, //  是否显示拍照按钮.true显示，false不显示
                    isPreviewEnable, // 是否支持预览.true支持预览，false不支持预览.
                    mSelectedPhotos); // 原始数据
        } else {
            PhotoPickerManager.startPicker((Activity) mContext);
        }
    }

    /**
     * 检查拍照权限(Manifest.permission.CAMERA)、读写手机存储权限(Manifest.permission.WRITE_EXTERNAL_STORAGE)
     */
    private void checkPermission() {
        openPhotoPicker();
//        if (!PermissionsManager.getInstance().hasAllPermissions(mContext, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA})) {
//            Activity activity = (Activity) mContext;
//            PermissionsManager.getInstance().requestPermissionsIfNecessaryForResult(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, new PermissionsResultAction(activity) {
//                @Override
//                public void onGranted() {//如果有权限
//                    openPhotoPicker();
//                }
//
//                @Override
//                public void onDenied(String permission) {
//                    ToastUtils.showToastOnce(mContext, "无权限无法正常使用");
//                    //Log.d("", "onDenied");
//                }
//            });
//        } else { //如果有权限
//            openPhotoPicker();
//        }
    }

    /**
     * onActivityResult
     *
     * @param requestCode 请求码
     * @param resultCode  结果码
     * @param data        Intent对象
     */
    public final void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (ACTION_SELECT == mMultPhotoPickerAction) {
            PhotoPickerManager.onActivityResult(requestCode, resultCode, data, new PhotoPickerStatus() {
                @Override
                public void onPhotoPickerSuccess(List<String> selectedPhotoPaths) {
                    mPhotoAdapter.refresh(selectedPhotoPaths);
                }

                @Override
                public void onPhotoPickerFaile(String error) {
                    Toast.makeText(getContext(), error, Toast.LENGTH_LONG).show();
                    mPhotoAdapter.refresh(null);
                }

                @Override
                public void onPhotoPickerCancel() {

                }

                @Override
                public void onPreviewBack(List<String> photoPaths) {
                    mPhotoAdapter.refresh(photoPaths);
                }
            });
        } else if (ACTION_CROP == mMultPhotoPickerAction) {
            List<String> mSelectedPhotoPath = getSelectedPhotos();
            if (null != mSelectedPhotoPath && mSelectedPhotoPath.size() > 0) {
                ImageLoader.load(getSelectedPhotos().get(0), mImageView);
            }
        }
    }

}
