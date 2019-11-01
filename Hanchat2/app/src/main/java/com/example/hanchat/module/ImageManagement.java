package com.example.hanchat.module;

import android.Manifest;

import android.content.Intent;
import android.graphics.Bitmap;

import android.widget.Toast;

import java.util.List;

import android.database.Cursor;

import android.provider.MediaStore;
import android.net.Uri;

import androidx.fragment.app.Fragment;

import com.example.hanchat.R;
import com.example.hanchat.data.chatting.OtherChatting;
import com.example.hanchat.module.adapter.RecyclerAdapter;
import com.example.hanchat.module.connecter.HttpConnecter;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import org.json.JSONObject;

/*완료*/
//깃허브 테스트
public class ImageManagement {//} extends AppCompatActivity {

    Bitmap image; //사용되는 이미지
    public Uri uri;

    static int PICK_IMAGE_REQUEST = 1;

    static final String TAG = "MainActivity";
    //AppCompatActivity MainActivity;
    Fragment fragment;
    HttpConnecter connecter;
    RecyclerAdapter chatAdapter;

    public ImageManagement(Fragment fragment, RecyclerAdapter chatAdapter){
        this.fragment = fragment;
        this.connecter = HttpConnecter.getinstance(R.string.server_ip, R.string.server_port);
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

        TedPermission.with(fragment.getContext())
                .setPermissionListener(permissionListener)
                .setRationaleMessage(fragment.getString(R.string.permission_2))
                .setDeniedMessage(fragment.getString(R.string.permission_1))
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                .check();


    }
    public void loadImage(){
        //Intent 생성
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT); //ACTION_PIC과 차이점?
        intent.setType("image/*"); //이미지만 보이게
        //Intent 시작 - 갤러리앱을 열어서 원하는 이미지를 선택할 수 있다.
        fragment.startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
        Toast.makeText(fragment.getContext(), "원하는 이미지 선택", Toast.LENGTH_LONG).show();

    }

    //이미지 선택작업 후의 결과 처리
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        try {
            //이미지를 하나 골랐을때
            if (requestCode == PICK_IMAGE_REQUEST && resultCode == fragment.getActivity().RESULT_OK && null != data) {
                //data에서 절대경로로 이미지를 가져옴

                uri = data.getData();


                Bitmap bitmap = MediaStore.Images.Media.getBitmap(fragment.getContext().getContentResolver(),uri);
                //이미지가 한계이상(?) 크면 불러 오지 못하므로 사이즈를 줄여 준다.


                int nh = (int) (bitmap.getHeight() * (1024.0 / bitmap.getWidth()));
                image= Bitmap.createScaledBitmap(bitmap, 1024, nh, true);


                //base64
                //base64(image, Bitmap.CompressFormat.JPEG, 100);

            } else {
                Toast.makeText(fragment.getContext(), "취소 되었습니다.", Toast.LENGTH_LONG).show();
                return;
            }

        } catch (Exception e) {
            Toast.makeText(fragment.getContext(), "Oops! 로딩에 오류가 있습니다.", Toast.LENGTH_LONG).show();
            e.printStackTrace();
            return;
        }
        //String real_path=getRealImagePath(uri);
        //SendImage(real_path);
        SendImage(image);

    }
    //실제파일 경로 가져오기
    public String getRealImagePath(Uri uri)
    {
        String[] proj = { MediaStore.Images.Media.DATA };

        Cursor cursor = fragment.getContext().getContentResolver().query(uri, proj, null, null, null);
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

    public void SendImage(Bitmap bitmap){
        try{
            connecter.sendImage(R.string.route_image, null, bitmap, new HttpConnecter.ResponseRecivedCallback() {
                @Override
                public void DataReceived(JSONObject data) {

                }

                @Override
                public void DataInvoked(JSONObject data) {
                    try {
                        if(data.getBoolean("result")){
                            chatAdapter.addItemwithNotify(new OtherChatting (data.getString("description")));
                        }


                    }
                    catch (Exception e){

                    }
                }

                @Override
                public void ConnectionFailed(Exception e) {
                    Toast.makeText(fragment.getContext(), "서버에 연결할 수 없습니다", Toast.LENGTH_LONG).show();
                }

            });
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

}
