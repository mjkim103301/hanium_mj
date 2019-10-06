package com.example.hanchat.module;

import android.Manifest;

import android.content.Intent;
import android.graphics.Bitmap;

import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Map;

import android.database.Cursor;

import java.util.HashMap;
import android.provider.MediaStore;
import android.net.Uri;
import android.util.Base64;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hanchat.R;
import com.example.hanchat.data.OtherChatting;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

/*완료*/
//깃허브 테스트
public class ImageManagement_mj{//} extends AppCompatActivity {

    Bitmap image; //사용되는 이미지
    public Uri uri;

    static int PICK_IMAGE_REQUEST = 1;

    static final String TAG = "MainActivity";
    AppCompatActivity MainActivity;
    HTTPConnecter connecter;
    RecyclerAdapter chatAdapter;

    public ImageManagement_mj(AppCompatActivity Activity, RecyclerAdapter chatAdapter){
        MainActivity = Activity;
        this.connecter = HTTPConnecter.getinstance(R.string.server_ip, R.string.server_port, MainActivity);
        this.chatAdapter = chatAdapter;
        //Activity.onActivityResult += this.onActivityResult;
    }


    //권한요청
    public void tedPermission() {

        PermissionListener permissionListener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                // 권한 요청 성공
            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
                // 권한 요청 실패
            }

        };

        TedPermission.with(MainActivity)
                .setPermissionListener(permissionListener)
                .setRationaleMessage(MainActivity.getResources().getString(R.string.permission_2))
                .setDeniedMessage(MainActivity.getResources().getString(R.string.permission_1))
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                .check();


    }
    public void loadImage(){
        //Intent 생성
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT); //ACTION_PIC과 차이점?
        intent.setType("image/*"); //이미지만 보이게
        //Intent 시작 - 갤러리앱을 열어서 원하는 이미지를 선택할 수 있다.
        MainActivity.startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
        Toast.makeText(MainActivity, "원하는 이미지 선택", Toast.LENGTH_LONG).show();

    }

    //이미지 선택작업 후의 결과 처리
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        try {
            //이미지를 하나 골랐을때
            if (requestCode == PICK_IMAGE_REQUEST && resultCode == MainActivity.RESULT_OK && null != data) {
                //data에서 절대경로로 이미지를 가져옴

                uri = data.getData();


                Bitmap bitmap = MediaStore.Images.Media.getBitmap(MainActivity.getContentResolver(),uri);
                //이미지가 한계이상(?) 크면 불러 오지 못하므로 사이즈를 줄여 준다.


                int nh = (int) (bitmap.getHeight() * (1024.0 / bitmap.getWidth()));
                image= Bitmap.createScaledBitmap(bitmap, 1024, nh, true);


                //base64
                //base64(image, Bitmap.CompressFormat.JPEG, 100);

            } else {
                Toast.makeText(MainActivity, "취소 되었습니다.", Toast.LENGTH_LONG).show();
            }

        } catch (Exception e) {
            Toast.makeText(MainActivity, "Oops! 로딩에 오류가 있습니다.", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
        //String real_path=getRealImagePath(uri);
        //SendImage(real_path);
        SendImage(image);

    }
    //실제파일 경로 가져오기
    public String getRealImagePath(Uri uri)
    {
        String[] proj = { MediaStore.Images.Media.DATA };

        Cursor cursor = MainActivity.getContentResolver().query(uri, proj, null, null, null);
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
    public void base64(Bitmap scaled, Bitmap.CompressFormat compressFormat, int quality)
    {
        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
        image.compress(compressFormat, quality, byteArrayOS);
        encodedImage=Base64.encodeToString(byteArrayOS.toByteArray(), Base64.DEFAULT);

    }

    //이미지 서버처리
    String des="";
    //     //Send버튼 클릭시 이미지를 서버로 보냄.
    public void SendImage(String path){
        //connecter=new HTTPConnecter("18.219.204.210", 55252);
        des=encodedImage;
        try{
            Map<String, String> data=new HashMap<>();

            data.put("image", des);


            connecter.sendImage("/apptest/test", null, path, new HTTPConnecter.Callback() {
                @Override
                public Object DataReceived(String ReceiveString) {
                    return ReceiveString;
                }

                @Override
                public void HandlerMethod(Object obj) {
                    //Toast.makeText(MainActivity.getApplicationContext(), (String) obj, Toast.LENGTH_LONG).show();
                    chatAdapter.addItem(new OtherChatting((String) obj));
                }
            });
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void SendImage(Bitmap bitmap){


        try{
            Map<String, String> data=new HashMap<>();

            //data.put("image", des);


            connecter.sendImage("/apptest/test", null, bitmap, new HTTPConnecter.Callback() {
                @Override
                public Object DataReceived(String ReceiveString) {
                    return ReceiveString;
                }

                @Override
                public void HandlerMethod(Object obj) {
                    //Toast.makeText(MainActivity.getApplicationContext(), (String) obj, Toast.LENGTH_LONG).show();
                    chatAdapter.addItem(new OtherChatting ((String) obj));
                    chatAdapter.notifyDataSetChanged();
                }
            });
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

}
