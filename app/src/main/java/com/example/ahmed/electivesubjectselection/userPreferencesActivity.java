package com.example.ahmed.electivesubjectselection;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class userPreferencesActivity extends AppCompatActivity {
    private TextView pref;
    String branch,rollNumber,tablename,options;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_preferences);
        progressDialog =new ProgressDialog(this);
        progressDialog.setMessage("Connecting to Database..");
        progressDialog.setCancelable(false);
        pref = (TextView)findViewById(R.id.preferences_textview);

        branch = getIntent().getStringExtra("branch");
        rollNumber = getIntent().getStringExtra("rollNumber");
        options= getIntent().getStringExtra("options");
        checkStatus();
        try{
            Class.forName(MyConstants.classes);
        }catch (ClassNotFoundException e){
            e.printStackTrace();
            Log.i("ForName",e.toString());
        }
        Myparams myparams = new Myparams(branch,rollNumber,options);
    }

    private void checkStatus() {
        if(options.equals("False")) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(userPreferencesActivity.this);
            builder.setMessage("No Records found..!! ");
            builder.setCancelable(false);
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    pref.setText("No Records mathching Roll Number "+rollNumber+"...\n Kindly Select your open electives");
                    Toast.makeText(getApplicationContext(),"Please select your options",Toast.LENGTH_LONG);
                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();

        }
        else{
           // progressDialog.show();
            getPrefs(branch,rollNumber);
        }

    }
    private void getPrefs(String br,String rollno) {
        Connection connection=null;
        ResultSet resultSet=null;
        ResultSetMetaData rsmd = null;
        Statement statement= null;
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        try {
            connection = DriverManager.getConnection(MyConstants.url, MyConstants.user, MyConstants.password);
            Log.i("Connection :", connection.toString());
            String result = "\n\nREQUESTED DETAILS ARE\n\n";
            String str = br;
            if(str.equals("CIVIL")){
                tablename = "useroptions_civil";
            }
            else  if(str.equals("EEE")){
                tablename = "useroptions_eee";
            }
            else  if(str.equals("MECH")){
                tablename = "useroptions_mechanical";
            }
            else  if(str.equals("ECE")){
                tablename = "useroptions_ece";
            }
            else  if(str.equals("CSE")){
                tablename = "useroptions_cse";
            }
            else  if(str.equals("IT")){
                tablename = "useroptions_it";
            }
            else  if(str.equals("EIE")){
                tablename = "useroptions_eie";
            }
            String sql ="SELECT * FROM "+tablename+" WHERE RollNo like " +rollno+ ";";
            statement =connection.createStatement();
            resultSet= statement.executeQuery(sql);
            rsmd = resultSet.getMetaData();
            Log.i("Metadata",rsmd.toString());
            while(resultSet.next()){
                result+=rsmd.getColumnName(1)+ ": " +resultSet.getString(1)+"\n";
                result+=rsmd.getColumnName(2)+ ": " +resultSet.getString(2)+"\n";
                result+=rsmd.getColumnName(3)+ ": " +resultSet.getString(3)+"\n";
                result+=rsmd.getColumnName(4)+ ": " +resultSet.getString(4)+"\n";
                result+=rsmd.getColumnName(5)+ ": " +resultSet.getString(5)+"\n\n\n";
            }
            pref.setText(result);

        }  catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            try {
                resultSet.close();
            } catch (Exception e) {}
            try {
                statement.close();
            } catch (Exception e) {}
            try {
                connection.close();
            } catch (Exception e) {}
        }
    }
}
class Myparams{
    String branch = "";
    String rollNumber = "";
    String options="";

    public Myparams(String branch, String rollNumber, String options) {
        this.branch = branch;
        this.rollNumber = rollNumber;
        this.options = options;
    }
}

