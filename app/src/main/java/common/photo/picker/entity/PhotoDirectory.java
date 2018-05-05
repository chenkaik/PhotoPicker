package common.photo.picker.entity;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CK on 2017/3/10  9:58.
 *
 * @author CK
 * @version 1.0.0
 * @class PhotoDirectory
 * @describe 目录实体
 */
public class PhotoDirectory {
    private String id;
    private String name;
    private String coverPath;
    private long dateAdded;
    private List<Photo> photoList = new ArrayList<>();
    private boolean isSelected;//当前目录是否选中

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public PhotoDirectory() {

    }

    public PhotoDirectory(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCoverPath() {
        return coverPath;
    }

    public void setCoverPath(String coverPath) {
        this.coverPath = coverPath;
    }

    public long getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(long dateAdded) {
        this.dateAdded = dateAdded;
    }

    public List<Photo> getPhotoList() {
        return photoList;
    }

    public void setPhotoList(List<Photo> photoList) {
        this.photoList = photoList;
    }

    public List<String> getPhotoPaths() {
        List<String> photoPaths = new ArrayList<>(photoList.size());
        for (Photo photo : photoList) {
            photoPaths.add(photo.getPath());
        }
        return photoPaths;
    }

    public void addPhoto(int id, String path) {
        photoList.add(new Photo(id, path));
    }

    public void addPhoto(Photo photo) {
        photoList.add(photo);
    }

    public void addPhotos(List<Photo> photos) {
        photoList.addAll(photos);
    }

    @Override
    public int hashCode() {
        if (TextUtils.isEmpty(id)) {
            if (TextUtils.isEmpty(name)) {
                return 0;
            }
            return name.hashCode();
        }
        int result = id.hashCode();
        if (TextUtils.isEmpty(name)) {
            return result;
        }
        result = 31 * result + name.hashCode();
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof PhotoDirectory)) {
            return false;
        }
        PhotoDirectory photoDirectory = (PhotoDirectory) obj;
        if (!TextUtils.isEmpty(id) && !TextUtils.isEmpty(photoDirectory.id)) {
            if (!TextUtils.equals(id, photoDirectory.id)) {
                return false;
            }
            return TextUtils.equals(name, photoDirectory.name);
        }
        return false;
    }

}
