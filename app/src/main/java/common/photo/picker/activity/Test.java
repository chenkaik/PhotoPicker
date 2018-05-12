package common.photo.picker.activity;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by chenKai on 2018/5/12.
 */
public interface Test {

    @Multipart
    @POST("http://192.168.31.190:8080/FileUploadTest/upload")
    Call<String> uploadFile(@Part() MultipartBody.Part[] file);

}
