package common.photo.picker.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import common.photo.picker.R;
import common.photo.picker.utils.ImageLoader;
import common.photo.picker.widget.PhotoPickerView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class MainActivity extends BaseActivity {

    private static final String TAG = "MainActivity";

    private PhotoPickerView mPhotoPicker;
    private List<String> mSelectedAllPhotoPath;
    private Retrofit retrofit;
    private Test test;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mPhotoPicker = (PhotoPickerView) findViewById(R.id.photo_picker);
        retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.31.190:8080/") //设置baseUrl,注意baseUrl必须后缀"/"
                .addConverterFactory(ScalarsConverterFactory.create()) // 支持返回值为String
                .addConverterFactory(GsonConverterFactory.create()) // 支持Gson转换器
                .client(buildOkHttpClient())
                .build();
        test = retrofit.create(Test.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { // 申请相机的权限
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
            }
        }
        findViewById(R.id.btn_upload).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSelectedAllPhotoPath != null){
                    List<MultipartBody.Part> list = new ArrayList<>();
                    for (int i = 0; i < mSelectedAllPhotoPath.size(); i++) {
                        File file = new File(mSelectedAllPhotoPath.get(i));
                        RequestBody body = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                        list.add(MultipartBody.Part.createFormData("file", file.getName(), body));
                    }
                    MultipartBody.Part[] params = list.toArray(new MultipartBody.Part[list.size()]);
                    test.uploadFile(params).enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            Toast.makeText(MainActivity.this, "成功", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            Toast.makeText(MainActivity.this, "失败", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ImageLoader.clearMemory(MainActivity.this);
        mPhotoPicker.onActivityResult(requestCode, resultCode, data);
        mSelectedAllPhotoPath = mPhotoPicker.getSelectedPhotos();
        if (null != mSelectedAllPhotoPath && mSelectedAllPhotoPath.size() > 0) {
            Toast.makeText(this, "选择了" + mSelectedAllPhotoPath.size() + "张", Toast.LENGTH_SHORT).show();
            Toast.makeText(this, mSelectedAllPhotoPath.get(0), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "未选择图片", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 100:
                if (grantResults.length > 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "拒绝了权限，程序无法正常运行!", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    private static OkHttpClient client;

    public static OkHttpClient buildOkHttpClient() {

        if (client == null) {
            client = new OkHttpClient.Builder()
                    // 日志拦截器
                    .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                    //失败重连
                    //.retryOnConnectionFailure(true)
                    .build();
        }

        return client;
    }

}
