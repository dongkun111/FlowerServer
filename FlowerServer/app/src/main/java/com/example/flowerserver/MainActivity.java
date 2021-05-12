package com.example.flowerserver;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    Button btnDn;
    TextView txtSlogan;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnDn = (Button)findViewById(R.id.btnSignActive);
        txtSlogan = (TextView)findViewById(R.id.txtSlogan);
        Typeface face =  Typeface.createFromAsset(getAssets(),"fonts/SomeWeatz.ttf");
        txtSlogan.setTypeface(face);

        btnDn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent singIn = new Intent(MainActivity.this,SignIn.class);
                startActivity(singIn);
            }
        });
    }
}