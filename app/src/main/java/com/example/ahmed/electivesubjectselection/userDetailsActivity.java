package com.example.ahmed.electivesubjectselection;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class userDetailsActivity extends AppCompatActivity {
    private Button click;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    private DatabaseReference databaseReference;
    String uid;
    private TextView userEmail,userRoll,userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);
        click =(Button)findViewById(R.id.changePassword);
        userEmail=(TextView)findViewById(R.id.tv1);
        userRoll=(TextView)findViewById(R.id.tv2);
        userName=(TextView)findViewById(R.id.tv3);
        user=FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();
        
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        databaseReference=FirebaseDatabase.getInstance().getReference();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String user_email = dataSnapshot.child(uid).child("userEmail").getValue(String.class);
                String user_roll = dataSnapshot.child(uid).child("userRoll").getValue(String.class);
                String user_name = dataSnapshot.child(uid).child("userName").getValue(String.class);
                userEmail.setText(user_email);
                userRoll.setText(user_roll);
                userName.setText(user_name);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(userDetailsActivity.this,update.class));
            }
        });
    }
}
