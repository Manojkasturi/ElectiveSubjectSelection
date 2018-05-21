package com.example.ahmed.electivesubjectselection;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class SelectedSubjectStatusActivity extends AppCompatActivity {
    private TextView appliedsubs;
    private String branch, sem, year;
    MyModelClass1 myModelClass1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_subject_status);
        appliedsubs = (TextView)findViewById(R.id.textView24);
        myModelClass1 = new MyModelClass1(MyConstants.url, MyConstants.user, MyConstants.password,branch,year,sem);
    }
}
class getappliedSubjects extends AsyncTask<MyModelClass1,Integer,ResultSet>{

    @Override
    protected ResultSet doInBackground(MyModelClass1... myModelClass1s) {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try{
            connection = DriverManager.getConnection(myModelClass1s[0].url, myModelClass1s[0].user, myModelClass1s[0].password);
            statement = connection.createStatement();
            String query ="select * from";


        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
class MyModelClass1 {
    String url = "";
    String user = "";
    String branch = "";
    String year = "";
    String sem = "";
    String password = "";

    MyModelClass1(String url, String user, String password, String branch, String year, String sem) {
        this.url = url;
        this.password = password;
        this.user = user;
        this.branch = branch;
        this.year = year;
        this.sem = sem;
    }
}
