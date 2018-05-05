package common.photo.picker.listener;

import java.util.List;

/**
 * Created by CK on 2017/3/10  12:09.
 *
 * @author CK
 * @version 1.0.0
 * @class PhotoPickerStatus
 * @describe 照片选择状态接口
 */
public interface PhotoPickerStatus {
    /**
     * 选择照片成功
     *
     * @param selectedPhotoPaths 选择的照片路径
     */
    void onPhotoPickerSuccess(List<String> selectedPhotoPaths);

    /**
     * 选择照片失败
     *
     * @param error 失败信息
     */
    void onPhotoPickerFaile(String error);

    /**
     * 取消选择照片
     */
    void onPhotoPickerCancel();

    /**
     * 预览返回
     *
     * @param photoPaths 选择的照片
     */
    void onPreviewBack(List<String> photoPaths);

}
