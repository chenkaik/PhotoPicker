package common.photo.picker.listener;


import common.photo.picker.entity.Photo;

/**
 * Created by CK on 2017/3/21  9:45.
 *
 * @author CK
 * @version 1.0.0
 * @class OnItemSelectStatusChangeListener
 * @describe 选择照片item状态改变监听器
 */
public interface OnItemSelectStatusChangeListener {
    /**
     * @param position      所选图片的索引
     * @param photo         所选的图片
     * @param isSelected    当前状态，true选中，false未选中.
     * @param selectedCount 当前已选择图片数量
     * @return 是否执行默认操作，true执行，false不执行.
     */
    boolean onItemSelectStatusChange(int position, Photo photo, boolean isSelected, int selectedCount);
}
