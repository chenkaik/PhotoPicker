package common.photo.picker.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import java.util.ArrayList;

import common.photo.picker.Constants;
import common.photo.picker.R;
import common.photo.picker.adapter.PreviewPhotosAdapter;

/**
 * Created by CK on 2017/3/10  14:16.
 *
 * @author CK
 * @version 1.0.0
 * @class PhotoPreviewFragment
 * @describe 照片预览的Fragment
 */
public class PhotoPreviewFragment extends Fragment {
    private ViewPager mViewPager;
    private ArrayList<String> mPhotosPathList = new ArrayList<>();//预览照片路径集合
    private int mCurrentIndex;//当前预览照片角标索引
    private int mThumbnailLeft;//
    private int mThumbnailTop;//
    private int mThumbnailWidth;//
    private int mThumbnailHeight;//
    private boolean isExecuteAnimation;//是否执行动画，true执行，false不执行.

    private PreviewPhotosAdapter mPreviewPhotosAdapter;//照片预览ViewPager适配器

    public static PhotoPreviewFragment newInstance(ArrayList<String> photosPath, int currentIndex) {

        Bundle args = new Bundle();
        args.putStringArrayList(Constants.PREVIEW_PHOTOS_PATH, photosPath);
        args.putInt(Constants.PREVIEW_CURRENT_INDEX, currentIndex);
        args.putBoolean(Constants.PREVIEW_IS_EXECUTE_ANIMATION, false);

        PhotoPreviewFragment fragment = new PhotoPreviewFragment();
        fragment.setArguments(args);

        return fragment;
    }

    public static PhotoPreviewFragment newInstance(ArrayList<String> photosPath, int currentIndex, int[] screenLocation, int thumbnailWidth, int thumbnailHeight) {

        PhotoPreviewFragment fragment = newInstance(photosPath, currentIndex);

        Bundle args = fragment.getArguments();
        args.putInt(Constants.PREVIEW_THUMBNAIL_LEFT, screenLocation[0]);
        args.putInt(Constants.PREVIEW_THUMBNAIL_TOP, screenLocation[1]);
        args.putInt(Constants.PREVIEW_THUMBNAIL_WIDTH, thumbnailWidth);
        args.putInt(Constants.PREVIEW_THUMBNAIL_HEIGHT, thumbnailHeight);
        args.putBoolean(Constants.PREVIEW_IS_EXECUTE_ANIMATION, true);

        return fragment;
    }

    /**
     * 刷新预览图片数据
     *
     * @param allPreviewPhotoPath 所有预览图片路径集合
     * @param currentIndex        当前预览图片角标索引
     */
    public final void refreshData(ArrayList<String> allPreviewPhotoPath, int currentIndex) {
        this.mPhotosPathList.clear();
        this.mPhotosPathList.addAll(allPreviewPhotoPath);
        this.mCurrentIndex = currentIndex;

        mPreviewPhotosAdapter.notifyDataSetChanged();
        mViewPager.setCurrentItem(mCurrentIndex);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        initAgrs();
        mPreviewPhotosAdapter = new PreviewPhotosAdapter(mPhotosPathList);

        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_photo_preview, container, false);

        initView(rootView);

        if (null == savedInstanceState && isExecuteAnimation) {
            ViewTreeObserver viewTreeObserver = mViewPager.getViewTreeObserver();
            viewTreeObserver.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    mViewPager.getViewTreeObserver().removeOnPreDrawListener(this);
                    int[] screenLocation = new int[2];
                    mViewPager.getLocationOnScreen(screenLocation);
                    mThumbnailLeft -= screenLocation[0];
                    mThumbnailTop -= screenLocation[1];

                    runEnterAnimation();
                    return true;
                }
            });
        }

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                isExecuteAnimation = mCurrentIndex == position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        return rootView;
    }

    /**
     * 获取显示照片的ViewPager控件
     *
     * @return 显示照片的ViewPager控件
     */
    public final ViewPager getViewPager() {
        return mViewPager;
    }

    /**
     * 获取预览照片路径集合
     *
     * @return 预览照片路径集合
     */
    public final ArrayList<String> getPhotosPathList() {
        return mPhotosPathList;
    }

    /**
     * 获取当前预览照片角标索引
     *
     * @return 当前预览照片角标索引
     */
    public final int getCurrentIndex() {
        return mViewPager.getCurrentItem();
    }

    /**
     * 初始化配置参数
     */
    private void initAgrs() {
        Bundle bundle = getArguments();
        if (null != bundle) {
            mPhotosPathList = bundle.getStringArrayList(Constants.PREVIEW_PHOTOS_PATH);
            mCurrentIndex = bundle.getInt(Constants.PREVIEW_CURRENT_INDEX);
            mThumbnailLeft = bundle.getInt(Constants.PREVIEW_THUMBNAIL_LEFT);
            mThumbnailTop = bundle.getInt(Constants.PREVIEW_THUMBNAIL_TOP);
            mThumbnailWidth = bundle.getInt(Constants.PREVIEW_THUMBNAIL_WIDTH);
            mThumbnailHeight = bundle.getInt(Constants.PREVIEW_THUMBNAIL_HEIGHT);
            isExecuteAnimation = bundle.getBoolean(Constants.PREVIEW_IS_EXECUTE_ANIMATION);
        }
    }

    /**
     * 初始化View
     *
     * @param rootView 根View
     */
    private void initView(View rootView) {
        mViewPager = (ViewPager) rootView.findViewById(R.id.vp_photo_preview);
        mViewPager.setAdapter(mPreviewPhotosAdapter);
        mViewPager.setCurrentItem(mCurrentIndex);
    }

    /**
     * 执行进入动画
     */
    private void runEnterAnimation() {
        //TODO
    }

    /**
     * 执行退出动画
     */
    public void runExitAnimation(Runnable ExitAction) {
        //TODO
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPhotosPathList.clear();
        mPhotosPathList = null;
        if (null != mViewPager) {
            mViewPager.setAdapter(null);
        }
    }

}
