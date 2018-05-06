package common.photo.picker.widget;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import java.util.List;

import common.photo.picker.R;
import common.photo.picker.adapter.DirectorySelectorRvAdapter;
import common.photo.picker.entity.PhotoDirectory;
import common.photo.picker.listener.OnPhotoDirectoryItemClickListener;

/**
 * Created by CK on 2017/3/24  10:37.
 *
 * @author CK
 * @version 1.0.0
 * @class DirectorySelectorDialog
 * @describe 目录选择Dialog.
 */
public class DirectorySelectorDialog extends Dialog implements View.OnClickListener {
    private Button mBtnDirectory;
    private RecyclerView mRvDirectoryList;//目录列表
    private List<PhotoDirectory> mPhotoDirectoryList;//照片目录集合
    private DirectorySelectorRvAdapter mDirectorySelectoryRvAdapter;//目录选择列表适配器

    public DirectorySelectorDialog(@NonNull Context context, List<PhotoDirectory> photoDirectoryList, OnPhotoDirectoryItemClickListener photoDirectoryItemClickListener) {
        super(context, R.style.PhotoPickerDirectoryDialogStyle);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService
                (Context.LAYOUT_INFLATER_SERVICE);
        View rootView = inflater.inflate(R.layout.dialog_directory_selector, null);
        this.setContentView(rootView);

        this.mPhotoDirectoryList = photoDirectoryList;
        mDirectorySelectoryRvAdapter = new DirectorySelectorRvAdapter(context, mPhotoDirectoryList, photoDirectoryItemClickListener);

        initView(context, rootView);
    }

    /**
     * 初始化View
     */
    private void initView(@NonNull Context context, @NonNull View rootView) {
        mRvDirectoryList = (RecyclerView) rootView.findViewById(R.id.rv_directory_selectory_list);
        mRvDirectoryList.setLayoutManager(new LinearLayoutManager(context));


        View cancelView = rootView.findViewById(R.id.v_directory_dialog_cancel);
        mBtnDirectory = (Button) rootView.findViewById(R.id.btn_directory_dialog_selector_directory);
        cancelView.setOnClickListener(this);
        mBtnDirectory.setOnClickListener(this);

        mDirectorySelectoryRvAdapter.setDirectoryNameView(mBtnDirectory);
        mRvDirectoryList.setAdapter(mDirectorySelectoryRvAdapter);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (R.id.v_directory_dialog_cancel == id || R.id.btn_directory_dialog_selector_directory == id) {
            dismiss();
        }
    }

    /**
     * 刷新目录列表数据
     *
     * @param photoDirectoryList 目录列表数据.
     */
    public final void refreshDirectoryListData(@NonNull List<PhotoDirectory> photoDirectoryList) {
        this.mPhotoDirectoryList = photoDirectoryList;
        mDirectorySelectoryRvAdapter.notifyDataSetChanged();
    }

}
