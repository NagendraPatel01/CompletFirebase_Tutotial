package com.hackerkernel.firebaseconnect.Adapter;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.hackerkernel.firebaseconnect.Model.PostModel;
import com.hackerkernel.firebaseconnect.R;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import org.jetbrains.annotations.NotNull;

import java.text.BreakIterator;
import java.util.HashMap;
import java.util.HashSet;

public class PostAdapter extends FirebaseRecyclerAdapter<PostModel, PostAdapter.ViewHolder> {

    Context context;

    public PostAdapter(@NonNull @NotNull FirebaseRecyclerOptions<PostModel> options,Context context) {
        super(options);
        this.context=context;
    }


    @Override
    protected void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position, @NonNull @NotNull PostModel model) {

        holder.text1.setText(model.getNam());
        holder.text2.setText(model.getClas());
        holder.text3.setText(model.getRollno());

        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DialogPlus dialog = DialogPlus.newDialog(context)
                        .setGravity(Gravity.CENTER)
                        .setMargin(50,0,50,0)
                        .setContentHolder(new com.orhanobut.dialogplus.ViewHolder(R.layout.editbox))

                        .setExpanded(false)  // This will enable the expand feature, (similar to android L share dialog)
                        .create();

                View holderView=(LinearLayout)dialog.getHolderView();

                 EditText name=holderView.findViewById(R.id.edit1);
             EditText clsss=holderView.findViewById(R.id.edit2);
                 EditText rollnoo=holderView.findViewById(R.id.edit3);


                name.setText(model.getNam());
                clsss.setText(model.getClas());
                rollnoo.setText(model.getRollno());

                TextView update=holderView.findViewById(R.id.update);

                update.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                      
                        HashMap<String,Object> map=new HashMap<>();
                        map.put("nam",name.getText().toString());
                        map.put("clas",clsss.getText().toString());
                        map.put("rollno",rollnoo.getText().toString());


                        FirebaseDatabase.getInstance().getReference().child("post")
                                
                              
                                .child(getRef(position).getKey())
                                .updateChildren(map)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull @NotNull Task<Void> task) {

                                        dialog.dismiss();
                                    }
                                });

                    }
                });

                dialog.show();

            }
        });

        holder.delite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              FirebaseDatabase.getInstance().getReference().child("post")
                      .child(getRef(position).getKey())
                      .removeValue();
            }
        });
    }


    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.message, parent, false);

        return new ViewHolder(view);
    }

    class  ViewHolder extends RecyclerView.ViewHolder {

        TextView text1,text2,text3;
        ImageView edit,delite;
        public ViewHolder( View itemView) {
            super(itemView);

            text1=itemView.findViewById(R.id.text1);
            text2=itemView.findViewById(R.id.text2);
            text3=itemView.findViewById(R.id.text3);
            delite=itemView.findViewById(R.id.delite);
            edit=itemView.findViewById(R.id.edit);
        }
    }
}
