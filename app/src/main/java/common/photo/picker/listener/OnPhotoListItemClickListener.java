package common.photo.picker.listener;

import android.view.View;

/**
 * Created by CK on 2017/3/21  17:55.
 *
 * @author CK
 * @version 1.0.0
 * @class OnPhotoListItemClickeListener
 * @describe PhotoPicker照片列表item点击事件监听器
 */
public interface OnPhotoListItemClickListener {
    /**
     * PhotoPicker照片列表item点击事件回调
     *
     * @param view         View
     * @param position     index
     * @param isShowCamera 是否显示了拍照按钮，true显示，false不显示
     */
    void onPhotoListItemClick(View view, int position, boolean isShowCamera);
}
