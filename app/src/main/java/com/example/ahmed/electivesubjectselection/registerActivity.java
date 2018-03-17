package com.example.ahmed.electivesubjectselection;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class registerActivity extends AppCompatActivity {

    private EditText userRollNo,userEmail,userPassword,userName;
    private Button regButton;
    private TextView goBack;
    private FirebaseAuth firebaseAuth;
    String rollno,email,password,name;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_register);
        setUpUIViews();
        firebaseAuth=FirebaseAuth.getInstance();
        regButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validateFeilds()){
                    //put the user data in database
                    progressDialog.setMessage("Please wait....");
                    progressDialog.show();
                    String user_email=userEmail.getText().toString().trim();
                    String user_password=userPassword.getText().toString().trim();
                    firebaseAuth.createUserWithEmailAndPassword(user_email,user_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()) {
                                sendEmailVerification();
                            }
                            else {
                                Toast.makeText(registerActivity.this, "Registration Failed ", Toast.LENGTH_SHORT).show();
                                Toast.makeText(registerActivity.this, "or this email Id is already registered ", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }
                        }
                    });
                }
            }
        });
        goBack.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(registerActivity.this,loginActivity.class));
            }
        });
    }
    private void setUpUIViews(){
        userRollNo = (EditText)findViewById(R.id.etUserRollNo);
        userEmail = (EditText)findViewById(R.id.etUserEmail);
        userPassword = (EditText)findViewById(R.id.etUserPassword);
        regButton = (Button)findViewById(R.id.registerButton);
        goBack = (TextView)findViewById(R.id.tvGoBack);
        userName=(EditText)findViewById(R.id.etUserName);
        progressDialog= new ProgressDialog(this);
    }
    
    private boolean validateFeilds(){
        boolean result = false;
        rollno = userRollNo.getText().toString();
        email = userEmail.getText().toString();
        password = userPassword.getText().toString();
        name = userName.getText().toString();

         if(rollno.isEmpty() || rollno.length()!=10){
            userRollNo.setError("Please enter correct roll No");
            result= false;
        }


        else if(email.isEmpty()|| !Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            userEmail.setError("Please Enter Valid Email");
            result= false;
        }
        else if(password.isEmpty()){
            userPassword.setError("Please set an Password");
            result= false;
        }
        else if(name.isEmpty()){
            userName.setError("Please Enter your Name");
         }
        else {
            result = true;
        }
        return result;
    }

    private void sendEmailVerification(){
        FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();
        if(firebaseUser!=null){
            firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        progressDialog.dismiss();
                        sendUserData();
                        Toast.makeText(registerActivity.this,"Successfully Registered...Please Verify your Email...",Toast.LENGTH_LONG).show();
                        firebaseAuth.signOut();
                        finish();
                        startActivity(new Intent(registerActivity.this,loginActivity.class));
                    }
                    else {
                        Toast.makeText(registerActivity.this,"Error occured...Verification mail not sent",Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
    private void sendUserData(){
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference myRef =firebaseDatabase.getReference(firebaseAuth.getUid());
        userDetails userDetails = new userDetails(rollno,email,name);
        myRef.setValue(userDetails);
    }
}
