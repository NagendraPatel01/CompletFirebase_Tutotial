package com.hackerkernel.firebaseconnect;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.core.Context;
import com.hackerkernel.firebaseconnect.Adapter.PostAdapter;
import com.hackerkernel.firebaseconnect.Model.PostModel;

public class ListdataActivity extends AppCompatActivity {

    RecyclerView recycle;
    PostAdapter postAdapter;
    FloatingActionButton floatbtn;
    Button logoutbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listdata);



        recycle=findViewById(R.id.recycle);
        floatbtn=findViewById(R.id.floatbtn);
        logoutbtn=findViewById(R.id.logoutbtn);

        logoutbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseAuth.getInstance().signOut();

                startActivity(new Intent(ListdataActivity.this, MainActivity.class));
                finish();

            }
        });

        floatbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ListdataActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(ListdataActivity.this,RecyclerView.VERTICAL,false);
        recycle.setLayoutManager(linearLayoutManager);

        FirebaseRecyclerOptions<PostModel> options =
                new FirebaseRecyclerOptions.Builder<PostModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("post"), PostModel.class)
                        .build();

        postAdapter=new PostAdapter(options,ListdataActivity.this);
        recycle.setAdapter(postAdapter);

    }

    @Override
    protected void onStart() {
        super.onStart();
        postAdapter.startListening();
    }
}