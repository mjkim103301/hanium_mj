package com.example.logintest;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

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
                switch (view.getId()) {
                    case R.id.sign_in_button:
                        signIn();
                        break;
                }
            }
        });

        // //이전에 로그인 한적있는 유저의 정보 받아옴
       //GoogleSignInAccount account = GoogleSignInAccount.getLastSignedInAccount(this);
        //updateUI(account);
    }

    private void signIn(){
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        //RC_SIGN_IN : 임의의 수로 이루어진 요청코드로 활동완료시 리턴
        startActivityForResult(signInIntent, RC_SIGN_IN);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode ==RC_SIGN_IN){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> task) {
        try{
            GoogleSignInAccount account = task.getResult(ApiException.class);

            String email = Objects.requireNonNull(account).getEmail();
            String name1 = account.getFamilyName();
            String name2 = account.getGivenName();
            String name3 = account.getDisplayName();

            Log.d("@@@@ email : ", Objects.requireNonNull(email));
            Log.d("@@@@ name1 : ", Objects.requireNonNull(name1));
            Log.d("@@@@ name2 : ", Objects.requireNonNull(name2));
            Log.d("@@@@ name3 : ", Objects.requireNonNull(name3));
        } catch (ApiException e) {
            e.printStackTrace();
        }
    }
}
