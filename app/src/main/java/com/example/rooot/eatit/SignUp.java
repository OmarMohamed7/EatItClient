package com.example.rooot.eatit;

import android.app.ProgressDialog;
import android.provider.BaseColumns;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.rooot.eatit.Model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.Random;

public class SignUp extends AppCompatActivity implements BaseColumns {

    MaterialEditText edtPhone , edtName , edtPassword;
    Button btnSignUp;
    User user;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference table_users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        Init();

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final ProgressDialog mDialog = new ProgressDialog(SignUp.this);
                mDialog.setMessage("Please wait..");
                mDialog.show();

                table_users.addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if(edtName.getText().toString().isEmpty() || edtPassword.getText().toString().isEmpty()){
                            Toast.makeText(SignUp.this, "You must enter name and password", Toast.LENGTH_SHORT).show();
                            mDialog.dismiss();
                        }else {

                            if (dataSnapshot.child(edtName.getText().toString()).exists()) {

                                mDialog.dismiss();
                                Toast.makeText(SignUp.this, "The user is already exist. ", Toast.LENGTH_SHORT).show();

                            } else {

                                mDialog.dismiss();
                                user = new User(edtName.getText().toString() ,edtPassword.getText().toString(), edtPhone.getText().toString());
                                table_users.child(edtName.getText().toString()).setValue(user);
                                Toast.makeText(SignUp.this, "Success", Toast.LENGTH_SHORT).show();
                                finish();
                                table_users.removeEventListener(this);
                            }


                            finish();
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
        edtName = findViewById(R.id.edtName2);
        edtPassword = findViewById(R.id.edtPassword2);
        edtPhone = findViewById(R.id.edtPhone2);

        btnSignUp = findViewById(R.id.btnSignUp);

        firebaseDatabase = FirebaseDatabase.getInstance();
        table_users = firebaseDatabase.getReference("Users");

    }
}
