package com.example.logintest;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private static final int RC_SIGN_IN = 9001;
    com.google.android.gms.auth.api.signin.GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //google
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        SignInButton signInButton = findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_ICON_ONLY);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("@@@@ ", "signIn() 실행");
                        signIn();

            }
        });

        // //이전에 로그인 한적있는 유저의 정보 받아옴
       //GoogleSignInAccount account = GoogleSignInAccount.getLastSignedInAccount(this);
        //updateUI(account);
    }

    private void handleSignInResult(Task<GoogleSignInAccount> task) {
        Log.d("@@@@ ", "정보 받아오는 중1");
        try{
            // 에러 발생
            GoogleSignInAccount account = task.getResult(ApiException.class);

            Log.d("@@@@ ", "정보 받아오는 중2");
            String email = account.getEmail();
            String name1 = account.getFamilyName();
            String name2 = account.getGivenName();
            String name3 = account.getDisplayName();

            Log.d("@@@@", email);
            Log.d("@@@@", name1);
            Log.d("@@@@", name2);
            Log.d("@@@@", name3);
        }
        catch (ApiException e) {
            Log.d("@@@@ ", "ApiException 발생");
            Toast.makeText(this, "ApiException 발생" + e.getStatusCode(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode ==RC_SIGN_IN){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            Log.d("@@@@ ", "handleSignInResult() 실행");
            handleSignInResult(task);
        }
    }

    private void signIn(){
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        //RC_SIGN_IN : 임의의 수로 이루어진 요청코드로 활동완료시 리턴
        Log.d("@@@@ ", "startActivityForResult() 실행");
        startActivityForResult(signInIntent, RC_SIGN_IN);   //로그인 창열기

    }

}
