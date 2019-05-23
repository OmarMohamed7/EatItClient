package com.example.rooot.eatit;

import android.app.ProgressDialog;
import android.content.Intent;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.rooot.eatit.Common.Common;
import com.example.rooot.eatit.Model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

import static com.example.rooot.eatit.Common.Common.current_user;

public class SignIn extends AppCompatActivity {

    EditText edtPhone , edtPassword , edtName;
    Button btnSignin;
    User user;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference table_users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        Init();

        btnSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final ProgressDialog mDialog = new ProgressDialog(SignIn.this);
                mDialog.setMessage("Please wait..");
                mDialog.show();

                table_users.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if(edtName.getText().toString().isEmpty() || edtPassword.getText().toString().isEmpty()){
                            Toast.makeText(SignIn.this, "Username/Password is empty", Toast.LENGTH_SHORT).show();
                            mDialog.dismiss();
                        }else {

                            /*for(DataSnapshot ds : dataSnapshot.getChildren()){
                                user = dataSnapshot.getValue(User.class);
                            }*/
                            //System.out.println(dataSnapshot.getValue());


                            if (dataSnapshot.child(edtName.getText().toString()).exists()) {
                                mDialog.dismiss();

                                //Get User
                                user = dataSnapshot.child(edtName.getText().toString()).getValue(User.class);
                                //user = dataSnapshot.getValue(User.class);

                                if (user.getPassword().equals(edtPassword.getText().toString())) {
                                    Common.current_user = user;
                                    Common.current_user.setName(edtName.getText().toString());

                                    startActivity(new Intent(SignIn.this, Home.class));
                                    table_users.removeEventListener(this);
                                } else {
                                    Toast.makeText(SignIn.this, "Failed", Toast.LENGTH_SHORT).show();
                                    mDialog.dismiss();
                                    table_users.removeEventListener(this);
                                }
                            } else {
                                Toast.makeText(SignIn.this, "User Dosent exist", Toast.LENGTH_SHORT).show();
                                mDialog.dismiss();
                                table_users.removeEventListener(this);

                            }
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        });
    }

    private void Init() {
        edtName = (MaterialEditText) findViewById(R.id.edtName);
        edtPassword = (MaterialEditText) findViewById(R.id.edtPassword);
        //edtPhone = (MaterialEditText) findViewById(R.id.edtPhone);

        btnSignin = findViewById(R.id.btnSignIn);

        user = new User();

        firebaseDatabase = FirebaseDatabase.getInstance();
        table_users = firebaseDatabase.getReference("Users");


    }

}
