package common.photo.picker.entity;

/**
 * Created by CK on 2017/3/10  9:57.
 *
 * @author CK
 * @version 1.0.0
 * @class Photo
 * @describe 照片实体
 */
public class Photo {
    private int id;
    private String path;

    public Photo() {

    }

    public Photo(int id, String path) {
        this.id = id;
        this.path = path;
    }

    public long getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Photo)) return false;
        Photo photo = (Photo) obj;
        return id == photo.id;
    }

}
