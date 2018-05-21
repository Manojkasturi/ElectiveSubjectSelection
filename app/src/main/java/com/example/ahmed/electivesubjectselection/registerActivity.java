package com.example.ahmed.electivesubjectselection;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.Gravity;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class registerActivity extends AppCompatActivity {

    private EditText userRollNo,userEmail,userPassword,userName;
    private Button regButton;
    private TextView goBack;
    private FirebaseAuth firebaseAuth;
    String rollno,email,password,name;
    private ProgressDialog progressDialog;
    private boolean result = false;
    private int internet =3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setUpUIViews();
        firebaseAuth=FirebaseAuth.getInstance();
        regButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validateFeilds()) {
                    if (!checkInternet()) {
                        Toast.makeText(registerActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                        new AlertDialog.Builder(registerActivity.this)
                                .setTitle("Connectivity Status")
                                .setMessage("\n" + "No Internet Connection")
                                .setCancelable(true)
                                .setNegativeButton("Retry", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        internet--;
                                        if (internet == 0) {
                                            Toast toast=Toast.makeText(registerActivity.this, "Please turn on the internet", Toast.LENGTH_LONG);
                                            toast.setGravity(Gravity.TOP| Gravity.LEFT, 320, 40);
                                            toast.show();
                                            startActivity(new Intent(android.provider.Settings.ACTION_SETTINGS));
                                            internet = 3;
                                        }
                                    }
                                }).show();
                    } else {
                        progressDialog.setMessage("Please wait....");
                        progressDialog.show();
                        String user_email = userEmail.getText().toString().trim();
                        String user_password = userPassword.getText().toString().trim();
                        firebaseAuth.createUserWithEmailAndPassword(user_email, user_password)
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    sendEmailVerification();
                                } else {
                                    Toast toast=Toast.makeText(registerActivity.this, "Registration Failed ", Toast.LENGTH_SHORT);
                                    toast.setGravity(Gravity.TOP| Gravity.LEFT, 320, 40);
                                    toast.show();
                                    Toast toast1=Toast.makeText(registerActivity.this, "or this email Id is already registered ", Toast.LENGTH_SHORT);
                                    toast1.setGravity(Gravity.TOP| Gravity.LEFT, 320, 40);
                                    toast1.show();
                                    progressDialog.dismiss();
                                }
                            }
                        });
                    }
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
    private boolean checkInternet() {
        ConnectivityManager connectivityManager=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo !=null && networkInfo.isConnected();
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
        email = userEmail.getText().toString();
        password = userPassword.getText().toString();
        name = userName.getText().toString();
        if(!checkrollno()){
            result= false;
        }
        else if(email.isEmpty()){
            userEmail.setError("Feild cannot be empty");
            result= false;
        }else if( !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            userEmail.setError("Please Enter Valid Email" + "\n" + "example: username@gmail.com");
            result = false;
        }
        else if((password.isEmpty()) ){
            userPassword.setError("Please set an Password");
            result= false;
        }else if(!(password.length()>=6)){
            userPassword.setError("Password length should be more than 6 characters");
            result= false;
        }else if(name.isEmpty()){
            userName.setError("Please enter your Name");
        }
        else{
            result =true;
        }
        return result;
    }
    private boolean checkrollno(){
        boolean status = false;
        rollno = userRollNo.getText().toString();
        if(rollno.isEmpty()){
            userRollNo.setError("Can't be empty");
            status= false;
        }else {
            String reg = "[1-9]{2}(b|B)[8][1](a|A)[\\w]{4}";
            Pattern p = Pattern.compile(reg);
            Matcher m = p.matcher(rollno);
            Boolean aBoolean = m.matches();
            if (rollno.length() != 10) {
                userRollNo.setError("Length must be more than 10 chars");
                status= false;
            } else if (aBoolean == false) {
                userRollNo.setError("Example : --B81A----");
                status= false;
            }else {
                status = true;
            }
        }

        return status;
    }



  /*(  private boolean validateFeilds(){
        int a,b;
        Date now;
        boolean result = false;
        rollno = userRollNo.getText().toString();
        email = userEmail.getText().toString();
        password = userPassword.getText().toString();
        name = userName.getText().toString();
        char arr[]=rollno.toCharArray();
        a = Character.getNumericValue(arr[0]);
        b = Character.getNumericValue(arr[1]);
        now = new Date();
        int currYear = 1900 + now.getYear();
        currYear = currYear- (2000+(a*10+b));
        String reg="[1-9]{2}(b|B)[8][1](a|A)[\\w]{4}";
        Pattern p= Pattern.compile(reg);
        Matcher m=p.matcher(rollno);
        Boolean aBoolean=m.matches();
        if(rollno.isEmpty() || rollno.length()!=10 || aBoolean==false){
            userRollNo.setError("Please enter correct roll No");
            result= false;
        }
        else if (currYear<3){
            new AlertDialog.Builder(this)
                    .setTitle("Access Denied!!")
                    .setMessage("Your roll number is not allowed to register ")
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    }).setCancelable(false).show();
            return false;
        }
        else if(email.isEmpty()){
            userEmail.setError("Feild cannot be empty");
            result= false;
        }else if( !Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            userEmail.setError("Please Enter Valid Email"+"\n"+"example: username@gmail.com");
            result= false;
        }
        else if((password.isEmpty()) ){
            userPassword.setError("Please set an Password");
            result= false;
        }else if((password.length()>=6)){
            userPassword.setError("Password length should be more than 6 characters");
            result= false;
        }
        else if(name.isEmpty()){
            userName.setError("Please enter your Name");
        } else {
            result = true;
        }
        return result;
    }
    */

    private void sendEmailVerification(){
        FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();
        if(firebaseUser!=null){
            firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        progressDialog.dismiss();
                        sendUserData();
                        Toast toast=Toast.makeText(registerActivity.this,"Successfully Registered...Please Verify your Email...",Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.TOP| Gravity.LEFT, 320, 40);
                        toast.show();
                        firebaseAuth.signOut();
                        finish();
                        startActivity(new Intent(registerActivity.this,loginActivity.class));
                    }
                    else {
                        Toast toast=Toast.makeText(registerActivity.this,"Error occured...Verification mail not sent",Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.TOP| Gravity.LEFT, 0, 0);
                        toast.show();
                    }
                }
            });
        }
    }
    private void sendUserData(){
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference myRef =firebaseDatabase.getReference(firebaseAuth.getUid());
        userDetails userDetails = new userDetails(rollno,email,name,"False");
        myRef.setValue(userDetails);
    }
}
