package com.example.ahmed.electivesubjectselection;

import android.app.ProgressDialog;
import android.os.StrictMode;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.xml.transform.Result;

public class resultsActivity extends AppCompatActivity {

    private Spinner s1,s2,s3;
    private Button getresult;
    private TextView ResultTV,countTV;
    String tablename;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        s3=(Spinner)findViewById(R.id.spinner4);
        getresult=(Button)findViewById(R.id.getResult);
        ResultTV =(TextView)findViewById(R.id.result_textview);
        countTV =(TextView)findViewById(R.id.count);

        List<String> listBranch = new ArrayList<>();
        listBranch.add("--Select Branch--");
        listBranch.add("CIVIL");
        listBranch.add("EEE");
        listBranch.add("MECH");
        listBranch.add("ECE");
        listBranch.add("CSE");
        listBranch.add("IT");
        listBranch.add("EIE");

        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, listBranch);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s3.setAdapter(adapter2);
        s3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selbranch = parent.getItemAtPosition(position).toString();
                Toast.makeText(resultsActivity.this, selbranch + " Selected", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        getresult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog= new ProgressDialog(resultsActivity.this);
                progressDialog.setMessage("Loading Please Wait....");
                progressDialog.show();
                getResult();
            }
        });

    }
    private void getResult(){
        Connection connection=null;
        ResultSet resultSet = null;
        ResultSetMetaData rsmd = null;
        Statement statement= null;
        ResultSet resultSet1=null;
        Statement statement1=null;
        int count =0;

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        try {
            Class.forName(MyConstants.classes);
            connection = DriverManager.getConnection(MyConstants.url, MyConstants.user, MyConstants.password);
            Log.i("Connection :", connection.toString());
            String result = "REQUESTED DETAILS ARE\n\n";
            String str = s3.getSelectedItem().toString();
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
            String sql ="SELECT * FROM "+tablename+";";
            String sql1="SELECT COUNT(*) FROM "+tablename+";";
            statement =connection.createStatement();
            statement1=connection.createStatement();
            resultSet= statement.executeQuery(sql);
            resultSet1=statement1.executeQuery(sql1);
            rsmd = resultSet.getMetaData();
            Log.i("Metadata",rsmd.toString());
            while(resultSet.next()){
                result+=rsmd.getColumnName(1)+ ": " +resultSet.getString(1)+"\n";
                result+=rsmd.getColumnName(2)+ ": " +resultSet.getString(2)+"\n";
                result+=rsmd.getColumnName(3)+ ": " +resultSet.getString(3)+"\n";
                result+=rsmd.getColumnName(4)+ ": " +resultSet.getString(4)+"\n";
                result+=rsmd.getColumnName(5)+ ": " +resultSet.getString(5)+"\n\n\n";
            }
            ResultTV.setText(result);
            while ((resultSet1.next())){
                count = resultSet1.getInt(1);
            }
            countTV.setText(countTV.getText()+ " "+count);
            progressDialog.dismiss();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();

        }
        finally {
            try {
                resultSet.close();
            } catch (Exception e) {}
            try {
                resultSet1.close();
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


