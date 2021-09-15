package com.hackerkernel.firebaseconnect;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hackerkernel.SplashActivity;


import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final int RC_SIGN_IN = 123;
    EditText edit1,edit2,edit3;
    TextView text1,text2,text3;
    Button btn,btn1;
    FloatingActionButton floatbtn;
    FirebaseAuth auth;
    GoogleSignInClient googleSignInClient;
    SignInButton signinbtn;
    //MKLoader loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn=findViewById(R.id.btn);
        edit1=findViewById(R.id.edit1);
        edit2=findViewById(R.id.edit2);
        edit3=findViewById(R.id.edit3);
        text1=findViewById(R.id.text1);
        text2=findViewById(R.id.text2);
        text3=findViewById(R.id.text3);
        btn1=findViewById(R.id.btn1);
        floatbtn=findViewById(R.id.floatbtn);
        signinbtn=findViewById(R.id.signinbtn);
      //  loader=findViewById(R.id.loader);

        auth= FirebaseAuth.getInstance();

        signinbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // loader.setVisibility(View.VISIBLE);
                signIn();

            }
        });


        floatbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(MainActivity.this,ListdataActivity.class));
            }
        });

      btn1.setOnClickListener(new View.OnClickListener() {          //data get in firebase
           @Override
           public void onClick(View v) {

               FirebaseDatabase.getInstance().getReference().child("post").child("-MjbbPE5JCUqhWcohLLl")
                       .addValueEventListener(new ValueEventListener() {
                           @Override
                           public void onDataChange( DataSnapshot snapshot) {

                               String post="name :"+snapshot.child("nam").getValue(String.class);
                               String post1="clas :"+snapshot.child("clas").getValue(String.class);
                               String post2="rollno :"+snapshot.child("rollno").getValue(String.class);

                               text1.setText(post);
                               text2.setText(post1);
                               text3.setText(post2);

                           }

                           @Override
                           public void onCancelled( DatabaseError error) {

                           }
                       });
           }
       });

        btn.setOnClickListener(new View.OnClickListener() {         //data send in firebase
            @Override
            public void onClick(View v) {

                HashMap<String,Object> map=new HashMap<>();

                map.put("nam",edit1.getText().toString());
                map.put("clas",edit2.getText().toString());
                map.put("rollno",edit3.getText().toString());

                if (edit1.getText().toString().length()==0){

                    Toast.makeText(MainActivity.this, "please enter data", Toast.LENGTH_SHORT).show();
                }

                else if (edit2.getText().toString().length()==0){

                    Toast.makeText(MainActivity.this, "please enter data", Toast.LENGTH_SHORT).show();
                }

                else if (edit3.getText().toString().length()==0){

                    Toast.makeText(MainActivity.this, "please enter data", Toast.LENGTH_SHORT).show();
                }

                else {
                    startActivity(new Intent(MainActivity.this,ListdataActivity.class));


                }


                FirebaseDatabase.getInstance().getReference().child("post").push()
                        .setValue(map)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete( Task<Void> task) {


                                Toast.makeText(MainActivity.this, "complet", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure( Exception e) {
                        Toast.makeText(MainActivity.this, "failer", Toast.LENGTH_SHORT).show();

                    }
                });

            }
        });

        // firebase google auth


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    private void signIn() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = auth.getCurrentUser();

                            startActivity(new Intent(MainActivity.this,ListdataActivity.class));
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                        }
                    }
                });
    }
}