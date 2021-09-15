package com.hackerkernel;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.auth.FirebaseAuth;
import com.hackerkernel.firebaseconnect.ListdataActivity;
import com.hackerkernel.firebaseconnect.MainActivity;
import com.hackerkernel.firebaseconnect.R;
public class SplashActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        Handler handler=new Handler();
        handler.postDelayed(
                new Runnable() {
                    public void run() {

                        if (FirebaseAuth.getInstance().getCurrentUser()==null){

                            startActivity(new Intent(SplashActivity.this, MainActivity.class));
                        }
                        else {

                            startActivity(new Intent(SplashActivity.this, ListdataActivity.class));

                        }
                    }
                }, 3000);
    }
}