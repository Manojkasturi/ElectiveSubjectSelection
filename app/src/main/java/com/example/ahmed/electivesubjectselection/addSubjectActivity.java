package com.example.ahmed.electivesubjectselection;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class addSubjectActivity extends AppCompatActivity {
    private TextView result1;
    private EditText sname, scode;
    private Spinner syear, ssem, sbranch;
    private Button add;
    private String valname, valcode, valyear, valsem, valbranch;
    String classes = "com.mysql.jdbc.Driver";
    String tablename;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_subject);
        sname = (EditText) findViewById(R.id.etsname);
        scode = (EditText) findViewById(R.id.etscode);
        syear = (Spinner) findViewById(R.id.spyear);
        ssem = (Spinner) findViewById(R.id.spsem);
        sbranch = (Spinner) findViewById(R.id.spbranch);
        add = (Button) findViewById(R.id.buttonadd);
        progressDialog = new ProgressDialog(this);

        List<String> listYear = new ArrayList<>();
        listYear.add("Select year");
        listYear.add("3");
        listYear.add("4");

        List<String> listsem = new ArrayList<>();
        listsem.add("Select Semester");        listsem.add("1");
        listsem.add("2");

        List<String> listBranch = new ArrayList<>();
        listBranch.add("Select Branch");
        listBranch.add("CIVIL");
        listBranch.add("EEE");
        listBranch.add("MECH");
        listBranch.add("ECE");
        listBranch.add("CSE");
        listBranch.add("IT");
        listBranch.add("EIE");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, listYear);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        syear.setAdapter(adapter);
        syear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selyear = parent.getItemAtPosition(position).toString();
                Toast.makeText(addSubjectActivity.this, selyear + " Selected", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, listsem);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ssem.setAdapter(adapter1);
        ssem.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selsem = parent.getItemAtPosition(position).toString();
                Toast.makeText(addSubjectActivity.this, selsem + " Selected", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, listBranch);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sbranch.setAdapter(adapter2);
        sbranch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selbranch = parent.getItemAtPosition(position).toString();
                Toast.makeText(addSubjectActivity.this, selbranch + " Selected", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {

                    testDB();
                    Toast.makeText(addSubjectActivity.this, "Subject Added to the Database", Toast.LENGTH_SHORT).show();

                }
            }
        });

    }
    private boolean validate() {
        boolean result = false;
        valname = sname.getText().toString();
        valcode = scode.getText().toString();

        if (valname.isEmpty()) {
            sname.setError("Please Enter the name");
            result = false;
        } else if (valcode.isEmpty()) {
            scode.setError("Please Enter the Code ");

        } else {
            result = true;
        }
        return result;
    }

    private void testDB() {

        try {
            progressDialog.setTitle("Adding Subject");
            progressDialog.setMessage("Adding "+sname+" in the database");
            progressDialog.show();
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            Class.forName(classes);
            Connection con = DriverManager.getConnection(MyConstants.url, MyConstants.user, MyConstants.password);
            Log.i("Connection :", con.toString());
            String result = "Database connection success\n";
           // Statement st = con.prepareStatement();
            String str = sbranch.getSelectedItem().toString();
            if(str.equals("CIVIL")){
                tablename = "electives_civil";
            }
            else  if(str.equals("EEE")){
                tablename = "electives_eee";
            }
            else  if(str.equals("MECH")){
                tablename = "electives_mech";
            }
            else  if(str.equals("ECE")){
                tablename = "electives_ece";
            }
            else  if(str.equals("CSE")||str.equals("IT")){
                tablename = "electives_cseit";
            }
            else  if(str.equals("EIE")){
                tablename = "electives_eie";
            }
            String c = scode.getText().toString();
            int courseid=Integer.parseInt(c);
            String coursename =sname.getText().toString();
           // int  coursename =Integer.parseInt(cn);
            String y =syear.getSelectedItem().toString();
            int year = Integer.parseInt(y);
            String s =ssem.getSelectedItem().toString();
            int semester = Integer.parseInt(s);
            PreparedStatement st = con.prepareStatement("INSERT INTO " +tablename+ " VALUES ("+courseid+",('"+coursename+"'),"+year+","+semester+");");
            Log.i("Statement", st.toString());
            st.executeUpdate();
            startActivity(new Intent(addSubjectActivity.this,adminActivity.class));
            progressDialog.dismiss();
        }catch (SQLException sqle){
            sqle.printStackTrace();
            result1.setText(sqle.toString());
            Toast.makeText(this,sqle.toString(),Toast.LENGTH_LONG).show();
            } catch (ClassNotFoundException e) {
            e.printStackTrace();
            result1.setText(e.toString());
        }
    }
}