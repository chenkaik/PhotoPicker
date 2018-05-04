package common.photo.picker.widget;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.AttrRes;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import common.photo.picker.R;


/**
 * Created by CK on 2017/3/10  17:01.
 *
 * @author CK
 * @version 1.0.0
 * @class TitleBar
 * @describe PhotoPicker标题栏
 */
public class TitleBar extends FrameLayout {
    private RelativeLayout mRootView;

    protected Toolbar mToolBar; // 标题栏
    protected ImageView mIvTitleLeft; // 标题左图片
    protected TextView mTvTitleLeft; // 标题左文字
    protected TextView mTvTitle; // 标题
    protected TextView mTvTitleRight; // 标题右边文字
    protected ImageView mIvTitleRight; // 标题右图片

    private OnClickListener mLeftOnClickListener;
    private OnClickListener mRightOnClickListener;

    protected FrameLayout mFmTitleCenter; // 标题中间区域

    public TitleBar(@NonNull Context context) {
        this(context, null);
    }

    public TitleBar(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TitleBar(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
        initData(context, attrs);
        initEvent(context);
    }

    /**
     * 初始化控件
     * @param context
     */
    private void initView(Context context) {
        mRootView = (RelativeLayout) View.inflate(context, R.layout.picker_view_title_bar, null);

        mToolBar = getViewById(R.id.toolbar_photo_picker);

        mIvTitleLeft = getViewById(R.id.iv_title_left);
        mTvTitleLeft = getViewById(R.id.tv_title_left);

        mTvTitle = getViewById(R.id.tv_title);

        mTvTitleRight = getViewById(R.id.tv_title_right);
        mIvTitleRight = getViewById(R.id.iv_title_right);

        mFmTitleCenter = getViewById(R.id.fm_title_center);

        this.addView(mRootView);
    }

    /**
     * 初始化数据
     *
     * @param context context
     * @param attrs   AttributeSet
     */
    private void initData(Context context, @Nullable AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TitleBar);

        String titleName = typedArray.getString(R.styleable.TitleBar_title_name);
        String leftText = typedArray.getString(R.styleable.TitleBar_left_text);
        String rightText = typedArray.getString(R.styleable.TitleBar_right_text);

        Drawable leftDrawable = typedArray.getDrawable(R.styleable.TitleBar_left_icon);
        Drawable rightDrawable = typedArray.getDrawable(R.styleable.TitleBar_right_icon);

        typedArray.recycle();

        setTitleBarLeft(leftText, leftDrawable, null);
        setTitleBarCenter(titleName);
        setTitleBarRight(rightText, rightDrawable, null);
    }

    /**
     * 初始化监听器
     *
     * @param context context
     */
    private void initEvent(final Context context) {
        if (context instanceof Activity) {
            mLeftOnClickListener = new OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((Activity) context).onBackPressed();
                }
            };
            mIvTitleLeft.setOnClickListener(mLeftOnClickListener);
        }
    }

    /**
     * 设置TitleBar左侧区域点击事件
     *
     * @param leftOnClickListener TitleBar左侧区域点击事件
     */
    public void setLeftOnClickListener(OnClickListener leftOnClickListener) {
        this.mLeftOnClickListener = leftOnClickListener;
        mIvTitleLeft.setOnClickListener(mLeftOnClickListener);
        mTvTitleLeft.setOnClickListener(mLeftOnClickListener);
    }

    /**
     * 设置TitleBar右侧区域点击事件
     *
     * @param rightOnClickListener TitleBar右侧区域点击事件
     */
    public void setRightOnClickListener(OnClickListener rightOnClickListener) {
        this.mRightOnClickListener = rightOnClickListener;
        mTvTitleRight.setOnClickListener(mRightOnClickListener);
        mIvTitleRight.setOnClickListener(mRightOnClickListener);
    }

    /**
     * 设置TitleBar左侧文字和图片
     *
     * @param leftText     左侧文字
     * @param leftDrawable 左侧图片
     */
    private void setTitleBarLeft(String leftText, Drawable leftDrawable, OnClickListener leftListener) {
        if (!TextUtils.isEmpty(leftText)) {
            mTvTitleLeft.setVisibility(VISIBLE);
            mTvTitleLeft.setText(leftText);
        }
        if (null != leftDrawable) {
            mIvTitleLeft.setVisibility(VISIBLE);
            mIvTitleLeft.setImageDrawable(leftDrawable);
        }
        if (null != leftListener) {
            mLeftOnClickListener = leftListener;
        }
    }

    /**
     * 设置TitleBar右侧文字和图片
     *
     * @param rightText     右侧文字
     * @param rightDrawable 右侧图片
     */
    private void setTitleBarRight(String rightText, Drawable rightDrawable, OnClickListener rightListener) {
        if (!TextUtils.isEmpty(rightText)) {
            mTvTitleRight.setVisibility(VISIBLE);
            mTvTitleRight.setText(rightText);
        }
        if (null != rightDrawable) {
            mIvTitleRight.setVisibility(VISIBLE);
            mIvTitleRight.setImageDrawable(rightDrawable);
        }
        if (null != rightListener) {
            mRightOnClickListener = rightListener;
        }
    }

    /**
     * 设置TitleBar标题
     *
     * @param titleName TitleBar标题
     */
    private void setTitleBarCenter(String titleName) {
        if (!TextUtils.isEmpty(titleName)) {
            mTvTitle.setVisibility(VISIBLE);
            mTvTitle.setText(titleName);
        }
    }

    /**
     * 获取右侧TextView控件
     *
     * @return 右侧TextView控件
     */
    public TextView getRightTextView() {
        return mTvTitleRight;
    }

    /**
     * 设置TitleBar中间TextView标题控件标题
     *
     * @return 标题
     */
    public void setCenterTextViewText(String text) {
        mTvTitle.setText(text);
    }

    /**
     * 查找View,不用强制转型
     *
     * @param id   控件的id
     * @param <VT> View类型
     * @return 对应的View
     */
    @SuppressWarnings("unchecked")
    protected <VT extends View> VT getViewById(@IdRes int id) {
        return (VT) mRootView.findViewById(id);
    }

    @Override
    public RelativeLayout getRootView() {
        return mRootView;
    }

}
