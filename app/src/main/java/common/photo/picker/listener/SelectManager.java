package common.photo.picker.listener;


import common.photo.picker.entity.Photo;

/**
 * Created by CK on 2017/3/13  10:01.
 *
 * @author CK
 * @version 1.0.0
 * @class SelectManager
 * @describe 照片选择管理类基类.
 */
public interface SelectManager {
    /**
     * 是否选中该照片
     *
     * @param photo 需要判断的照片
     * @return true已选择，false未选择。
     */
    boolean isSelected(Photo photo);

    /**
     * 切换选择状态
     *
     * @param photo 需要切换选择状态的Photo对象。
     */
    void toggleSelection(Photo photo);

    /**
     * 清空选择状态.
     */
    void clearAllSelection();

    /**
     * 获取已选择的照片数量.
     *
     * @return
     */
    int getSelectedItemCount();

}
