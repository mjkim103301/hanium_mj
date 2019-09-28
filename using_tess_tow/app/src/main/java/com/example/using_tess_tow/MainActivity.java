package com.example.using_tess_tow;

import android.Manifest;
import android.content.ContentUris;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.DocumentsContract;
import android.support.v4.content.CursorLoader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.googlecode.tesseract.android.TessBaseAPI;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

//갤러리에서 이미지 가져오기
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Logger;

import android.widget.Toast;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.provider.MediaStore;
import android.net.Uri;
import android.widget.ImageView;

//base64 인코딩하기
import android.util.Base64;
//import org.apache.commons.codec.binary.Base64;

//서버로 이미지 전송
import org.json.JSONObject;

//google cloud vision api


public class MainActivity extends AppCompatActivity {

    Bitmap image; //사용되는 이미지
    private TessBaseAPI mTess; //Tess API reference
    String datapath = "" ; //언어데이터가 있는 경로
    public Uri uri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //이미지 디코딩을 위한 초기화
        image = BitmapFactory.decodeResource(getResources(), R.drawable.text_sample_kor); //샘플이미지파일
        //base64
        base64(image, Bitmap.CompressFormat.JPEG, 100);
        //image = BitmapFactory.decodeResource(getResources(), R.drawable.text_sample4); //샘플이미지파일
        //언어파일 경로
        datapath = getFilesDir()+ "/tesseract/";

        //트레이닝데이터가 카피되어 있는지 체크
        checkFile(new File(datapath + "tessdata/"));

        //Tesseract API
        //String lang = "eng";
        String lang = "kor";

        mTess = new TessBaseAPI();
        mTess.init(datapath, lang);


        //권한요청
        tedPermission();
    }

    //ProcessImage버튼 클릭시 실행
    public void processImage(View view) {
        String OCRresult = null;
        mTess.setImage(image);
        OCRresult = mTess.getUTF8Text();
        TextView OCRTextView = (TextView) findViewById(R.id.OCRTextView);
        OCRTextView.setText(OCRresult);
    }


    //copy file to device
    private void copyFiles() {
        try{
            //String filepath = datapath + "/tessdata/eng.traineddata";
            String filepath = datapath + "/tessdata/kor.traineddata";
            AssetManager assetManager = getAssets();
            //InputStream instream = assetManager.open("tessdata/eng.traineddata");
            InputStream instream = assetManager.open("tessdata/kor.traineddata");
            OutputStream outstream = new FileOutputStream(filepath);
            byte[] buffer = new byte[1024];
            int read;
            while ((read = instream.read(buffer)) != -1) {
                outstream.write(buffer, 0, read);
            }
            outstream.flush();
            outstream.close();
            instream.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //check file on the device
    private void checkFile(File dir) {
        //디렉토리가 없으면 디렉토리를 만들고 그후에 파일을 카피
        if(!dir.exists()&& dir.mkdirs()) {
            copyFiles();
        }
        //디렉토리가 있지만 파일이 없으면 파일카피 진행
        if(dir.exists()) {
           // String datafilepath = datapath+ "/tessdata/eng.traineddata";
            String datafilepath = datapath+ "/tessdata/kor.traineddata";
            File datafile = new File(datafilepath);
            if(!datafile.exists()) {
                copyFiles();
            }
        }
    }
    //
    //갤러리에서 사진 고르기
    //

    private static int PICK_IMAGE_REQUEST = 1;

    static final String TAG = "MainActivity";


    //권한요청
    private void tedPermission() {

        PermissionListener permissionListener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                // 권한 요청 성공
            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                // 권한 요청 실패
            }
        };

        TedPermission.with(this)
                .setPermissionListener(permissionListener)
                .setRationaleMessage(getResources().getString(R.string.permission_2))
                .setDeniedMessage(getResources().getString(R.string.permission_1))
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                .check();

    }

    //로드버튼 클릭시 실행
    public void loadImagefromGallery(View view) {
        //Intent 생성
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT); //ACTION_PIC과 차이점?
        intent.setType("image/*"); //이미지만 보이게
        //Intent 시작 - 갤러리앱을 열어서 원하는 이미지를 선택할 수 있다.
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
        Toast.makeText(this, "원하는 이미지 선택", Toast.LENGTH_LONG).show();
    }



    //이미지 선택작업을 후의 결과 처리
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            //이미지를 하나 골랐을때
            if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && null != data) {
                //data에서 절대경로로 이미지를 가져옴

                uri = data.getData();

                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
                //이미지가 한계이상(?) 크면 불러 오지 못하므로 사이즈를 줄여 준다.

                int nh = (int) (bitmap.getHeight() * (1024.0 / bitmap.getWidth()));
                image= Bitmap.createScaledBitmap(bitmap, 1024, nh, true);

                //base64
                base64(image, Bitmap.CompressFormat.JPEG, 100);

                ImageView imgView = (ImageView) findViewById(R.id.imageView3);
                imgView.setImageBitmap(image);





            } else {
                Toast.makeText(this, "취소 되었습니다.", Toast.LENGTH_LONG).show();
            }

        } catch (Exception e) {
            Toast.makeText(this, "Oops! 로딩에 오류가 있습니다.", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
        String real_path=getRealImagePath(uri);
        TextView txt_Image_path=(TextView) findViewById(R.id.txt_Image_path);
        txt_Image_path.setText("이미지경로: "+real_path);
    }

    //실제파일 경로 가져오기
    public String getRealImagePath(Uri uri)
    {

        String[] proj = { MediaStore.Images.Media.DATA };

        Cursor cursor = getContentResolver().query(uri, proj, null, null, null);
       int index=cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
       cursor.moveToFirst();
       String path=cursor.getString(index);
      /*  try {
            base64(path);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }*/
        return path;

    }
    String encodedImage;


    //이미지를 base64로 바꿔줌
 /*   public void base64(String path){

        Bitmap bm = BitmapFactory.decodeFile(path);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
        byte[] b = baos.toByteArray();
        encodedImage = Base64.encodeToString(b, Base64.DEFAULT);



    }*/

    //경로를 base64로 바꾸는 거였음...
/*    public void base64(String path)throws UnsupportedEncodingException{
        byte[] data = path.getBytes("UTF-8");
        encodedImage= Base64.encodeToString(data, Base64.DEFAULT);
        TextView txt_base64=(TextView) findViewById(R.id.txt_base64);
        txt_base64.setText("base64: "+encodedImage);
    }*/

    public void base64(Bitmap scaled, Bitmap.CompressFormat compressFormat, int quality)
    {
        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
        image.compress(compressFormat, quality, byteArrayOS);
        encodedImage=Base64.encodeToString(byteArrayOS.toByteArray(), Base64.DEFAULT);
        TextView txt_base64=(TextView) findViewById(R.id.txt_base64);
        txt_base64.setText("base64: "+encodedImage);
    }

    //이미지 서버처리
    HTTPConnecter connecter;
    String des="";
//     //Send버튼 클릭시 이미지를 서버로 보냄.
    public void SendImageToServer(View view){
        connecter=new HTTPConnecter("18.219.204.210", 55252);
        des=encodedImage;
        try{
            Map<String, String> data=new HashMap<>();

            data.put("image", des);


            connecter.sendImage("/apptest/image", data, null, new HTTPConnecter.Callback() {
                @Override
                public Object DataReceived(String ReceiveString) {
                    return ReceiveString;
                }

                @Override
                public void HandlerMethod(Object obj) {
                    Toast.makeText(getApplicationContext(), (String) obj, Toast.LENGTH_LONG).show();

                }
            });
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }


}


