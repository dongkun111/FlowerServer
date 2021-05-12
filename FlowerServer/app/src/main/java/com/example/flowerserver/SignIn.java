package com.example.flowerserver;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.drm.ProcessedData;
import android.icu.util.Measure;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.flowerserver.Common.Common;
import com.example.flowerserver.Model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

public class SignIn extends AppCompatActivity {

    EditText edtUsername,edtPassword;
    Button btnDn;

    FirebaseDatabase db;
    DatabaseReference users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        edtUsername = (MaterialEditText)findViewById(R.id.edtTaikhoan);
        edtPassword = (MaterialEditText)findViewById(R.id.edtMatkhau);
        btnDn = (Button)findViewById(R.id.btnSignIn);

        db = FirebaseDatabase.getInstance();
        users = db.getReference("Account");

        btnDn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                signInUser(edtUsername.getText().toString(),edtPassword.getText().toString());
            }
        });
    }

    private void signInUser(String username, String password) {
        ProgressDialog mDialog = new ProgressDialog(SignIn.this);
        mDialog.setMessage("Vui lòng chờ một chút...");
        mDialog.show();

        final String localUsername = username;
        final String localPassword = password;


        users.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(localUsername).exists())
                {
                    mDialog.dismiss();
                    User user = dataSnapshot.child(localUsername).getValue(User.class);
                    user.setUsername(localUsername);
                    if(Integer.parseInt(user.getType()) != 1)
                    {
                        if(user.getPassword().equals(localPassword))
                        {
                            Intent home = new Intent(SignIn.this,Home.class);
                            Common.currentUser = user;
                            startActivity(home);
                            finish();
                        }
                        else
                        {
                            Toast.makeText(SignIn.this,"Sai mật khẩu!",Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                    {
                        Toast.makeText(SignIn.this,"Vui lòng đăng nhập với tài khoản nhân viên!",Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    mDialog.dismiss();
                    Toast.makeText(SignIn.this,"Tài khoản không tồn tại",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}