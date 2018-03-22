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

public class deleteActivity extends AppCompatActivity {

    private EditText code;
    private Spinner year, semester, branch;
    private ProgressDialog progressDialog;
    String classes = "com.mysql.jdbc.Driver";
    String tablename;
     Button delete;
    private TextView result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete);
        code = (EditText) findViewById(R.id.deleteSubjectEditText);
        year = (Spinner) findViewById(R.id.spdelyear);
        semester = (Spinner) findViewById(R.id.spdelsem);
        branch = (Spinner) findViewById(R.id.spdelbranch);
        delete = (Button) findViewById(R.id.delbutton);
        List<String> listYear = new ArrayList<>();
        listYear.add("Select year");
        listYear.add("3");
        listYear.add("4");

        List<String> listsem = new ArrayList<>();
        listsem.add("Select Semester");
        listsem.add("1");
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
        year.setAdapter(adapter);
        year.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selyear = parent.getItemAtPosition(position).toString();
                Toast.makeText(deleteActivity.this, selyear + " Selected", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(deleteActivity.this, "Please Fill all the Feilds", Toast.LENGTH_SHORT);
            }
        });
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, listsem);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        semester.setAdapter(adapter1);
        semester.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selsem = parent.getItemAtPosition(position).toString();
                Toast.makeText(deleteActivity.this, selsem + " Selected", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, listBranch);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        branch.setAdapter(adapter2);
        branch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selbranch = parent.getItemAtPosition(position).toString();
                Toast.makeText(deleteActivity.this, selbranch + " Selected", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }
    public void deletesubject(View view) {
        Connection connection=null;
        Statement statement=null;
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            Class.forName(classes);
            connection = DriverManager.getConnection(MyConstants.url, MyConstants.user, MyConstants.password);
            Log.i("Connection :", connection.toString());
            String result = "Database connection success\n";
            statement = connection.createStatement();
            Log.i("Statement", statement.toString());
            String str = branch.getSelectedItem().toString();
            if (str.equals("CIVIL")) {
                tablename = "electives_civil";
            } else if (str.equals("EEE")) {
                tablename = "electives_eee";
            } else if (str.equals("MECH")) {
                tablename = "electives_mech";
            } else if (str.equals("ECE")) {
                tablename = "electives_ece";
            } else if (str.equals("CSE") || str.equals("IT")) {
                tablename = "electives_cseit";
            } else if (str.equals("EIE")) {
                tablename = "electives_eie";
            }
            String courseid =code.getText().toString();
            int cid = Integer.parseInt(courseid);
            Log.i("Query " , "DELETE FROM " +tablename+ " WHERE CourseID="+cid);
            statement.executeUpdate("DELETE FROM " +tablename+ " WHERE CourseID="+cid);
            startActivity(new Intent(deleteActivity.this,adminActivity.class));
            Toast.makeText(this,"Deleted subject with id "+cid,Toast.LENGTH_SHORT).show();
        } catch (ClassNotFoundException e) {
            Log.e("DeleteActivity",e.getLocalizedMessage());
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            try {
                statement.close();
            } catch (Exception e) {}
            try {
                connection.close();
            } catch (Exception e) {}
        }
    }
}
