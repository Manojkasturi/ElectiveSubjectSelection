package com.example.ahmed.electivesubjectselection;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class loginActivity extends AppCompatActivity {


    private EditText Email;
    private EditText Password;
    private Button Login;
    private int counter=5;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    private TextView forgotPassword,newUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //  this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        Email = (EditText)findViewById(R.id.etEmail);
        Password = (EditText)findViewById(R.id.etPassword);
        Login = (Button)findViewById(R.id.buttonLogin);
        newUser = (TextView)findViewById(R.id.tvRegister);
        firebaseAuth=FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        progressDialog= new ProgressDialog(this);
        forgotPassword = (TextView)findViewById(R.id.tvForgotPassword);
    /*    if(user!=null){
            finish();
            startActivity(new Intent(loginActivity.this,loginSuccessActivity.class));
        }*/
        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkFeild()) {
                    validate(Email.getText().toString(), Password.getText().toString());
                }
            }
        });
        newUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(loginActivity.this,registerActivity.class));
            }
        });
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(loginActivity.this,passwordActivity.class));
            }
        });
    }
    private void validate(String userName,String userPassword) {
        progressDialog.setMessage("Please Wait...");
        progressDialog.show();
        firebaseAuth.signInWithEmailAndPassword(userName,userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    progressDialog.dismiss();
                  //  startActivity(new Intent(loginActivity.this,loginSuccessActivity.class));
                  //  Toast.makeText(loginActivity.this,"Login Successful",Toast.LENGTH_SHORT).show();
                    checkEmailVerification();
                }
                else{

                    counter--;

                    Toast.makeText(getBaseContext(),"Login Failed " +counter+ " Attempts remaining... or you may not be registered with us.",Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    if(counter==0){
                        Toast.makeText(loginActivity.this, "5 attempts Exceded", Toast.LENGTH_SHORT).show();
                        Login.setEnabled(false);
                    }
                }
            }
        });
    }
    private void checkEmailVerification(){
        FirebaseUser firebaseUser=firebaseAuth.getInstance().getCurrentUser();
        boolean flag = firebaseUser.isEmailVerified();
        if(flag){
            finish();
            startActivity(new Intent(loginActivity.this,loginSuccessActivity.class));
            
        }
        else{
            Toast.makeText(this, "Verify your Email", Toast.LENGTH_LONG).show();
            firebaseAuth.signOut();
        }
    }
    private boolean checkFeild(){
        boolean res=false;
        String enteredemail = Email.getText().toString();
        String enteredPasswrd = Password.getText().toString();
        if(enteredemail.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(enteredemail).matches()){
            Email.setError("Please Enter the Valid Email");
            res = false;
        }
        else if(enteredPasswrd.isEmpty()){
            Password.setError("Please Enter the Password");
            res=false;
        }
        else res= true;
        return res;
    }
}
