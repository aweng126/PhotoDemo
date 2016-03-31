package com.example.kingwen.photodemo;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    //拍照
    public static  final int TAKE_PHOTO=1;

    //剪裁
    public static  final int CROP_PHOTO=2;

    //选择图片
    public static final int CHOOSE_PHOTO=3;


    //相册中选择
    private Button btn_photoAlbum;

    //相机拍摄
    private Button btn_Photograph;

    //用于展示所得的图片
    private ImageView iv_show;

    //用于保存我们的路径
    private Uri imgUri;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();

        initListeners();
    }

    private void initListeners() {
        iv_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this,"iv_show",Toast.LENGTH_SHORT).show();
            }
        });
        /**
         * 从相册中进行选择
         */
        btn_photoAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, CHOOSE_PHOTO);

            }
        });
        /**
         * 用相机进行拍摄
         */
        btn_Photograph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            File outputImage=new File(Environment.getExternalStorageDirectory(),"output_image.jpg");
                try {
                    if(outputImage.exists()){
                        outputImage.delete();
                    }
                    outputImage.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                imgUri=Uri.fromFile(outputImage);

                Intent intent=new Intent("android.media.action.IMAGE_CAPTURE");
                intent.putExtra(MediaStore.EXTRA_OUTPUT,imgUri);
                startActivityForResult(intent,TAKE_PHOTO);



            }
        });

    }

    private void initViews() {
        btn_photoAlbum= (Button) findViewById(R.id.btn_photoAlbum_mainActivity);
        btn_Photograph= (Button) findViewById(R.id.btn_Photograph_mainActivity);
        iv_show= (ImageView) findViewById(R.id.iv_show_mainActivity);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case CHOOSE_PHOTO:
                if(resultCode==RESULT_OK){
                    imgUri=data.getData();

                    Log.e("MainActivity", imgUri + "");

                    Glide .with(MainActivity.this)
                            .load(imgUri)
                            .override(250, 350)

                            //跳过内存缓存
                            .skipMemoryCache(true)

                            //跳过硬盘缓存
                            .diskCacheStrategy(DiskCacheStrategy.ALL)

                            .centerCrop()

                            .into(iv_show);


                }
                break;
            case TAKE_PHOTO:
                if(resultCode==RESULT_OK){
                    Log.e("MainActivity Takephoto ", imgUri + "");
                    Intent intent =new Intent("com.android.camera.action.CROP");
                    intent.setDataAndType(imgUri,"image/*");
                    intent.putExtra("scale", true);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT,imgUri);
                    startActivityForResult(intent,CROP_PHOTO);//启动裁剪部分
                }
                break;
            case CROP_PHOTO:
                if(resultCode==RESULT_OK){
                    Glide .with(MainActivity.this)
                            .load(imgUri)
                            .override(250, 350)

                                    //跳过内存缓存
                            .skipMemoryCache(true)

                                    //跳过硬盘缓存
                            .diskCacheStrategy(DiskCacheStrategy.ALL)

                            .centerCrop()

                            .into(iv_show);
                }
                break;
            default:
                break;
        }
    }
}
