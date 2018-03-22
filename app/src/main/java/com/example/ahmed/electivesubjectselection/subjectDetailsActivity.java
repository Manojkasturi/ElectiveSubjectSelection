package com.example.ahmed.electivesubjectselection;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import static com.example.ahmed.electivesubjectselection.MyConstants.classes;

public class subjectDetailsActivity extends AppCompatActivity {

    private TextView subjectDetails;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Getting Subjects");
        progressDialog.show();

        setContentView(R.layout.activity_subject_details);
        subjectDetails = (TextView)findViewById(R.id.subjectsdesc);

        testDB();

    }
    private void testDB() {
        Connection connection=null;
        Statement statement=null;
        ResultSet resultSet=null;
        ResultSetMetaData rsmd = null;
        try {
            String result ="Subjects : \n";
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            Class.forName(MyConstants.classes);
            connection = DriverManager.getConnection(MyConstants.url, MyConstants.user, MyConstants.password);
            Log.i("Connection :", connection.toString());
            String sql ="SELECT * FROM Subject_details";
            statement =connection.createStatement();
            resultSet= statement.executeQuery(sql);
            rsmd = resultSet.getMetaData();
            Log.i("Metadata",rsmd.toString());
            while(resultSet.next()){
                result+=rsmd.getColumnName(1)+ ": " +resultSet.getString(1)+"\n\n";
                result+=rsmd.getColumnName(2)+ ": " +resultSet.getString(2)+"\n\n\n\n";
            }
            Toast.makeText(this, "Subjects list displayed ", Toast.LENGTH_SHORT).show();
            subjectDetails.setText(result);
            progressDialog.dismiss();
        }catch (SQLException sqle){
            sqle.printStackTrace();
        } catch (ClassNotFoundException e) {
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
