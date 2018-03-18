package com.example.ahmed.electivesubjectselection;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

interface ResultCallback{
    void setResultSet(List<String> list);
}
public class userOptionActivity extends AppCompatActivity implements ResultCallback, SubjectPrefCallback {

    private Spinner s1, s2, s3;
    private String branch, sem, year;
    private ProgressDialog progressDialog;
    final String query2="";
    MyModelClass myModelClass;
    Button submit;
    boolean isPref1Selected = false,isPref2Selected = false,isPref3Selected = false;
    private String pref1Sub, pref2Sub, pref3Sub, rollNumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_option);
        submit = findViewById(R.id.submit__button);
        submit.setVisibility(View.INVISIBLE);
        branch = getIntent().getStringExtra("branch");
        year = String.valueOf(getIntent().getIntExtra("year",3));
        sem = String.valueOf(getIntent().getIntExtra("sem",1));
        //TODO get roll number from intent
        rollNumber = "14B81A12B8";

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Fetching Data...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        s1 = (Spinner) findViewById(R.id.sppreference1);
        s2 = (Spinner) findViewById(R.id.sppreference2);
        s3 = (Spinner) findViewById(R.id.sppreference3);

        Log.i("ChakraChutiya","Inside Async Task");
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        try {
            Class.forName(MyConstants.classes);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

      /*  progressDialog= new ProgressDialog(this);
        progressDialog.setMessage("Loading Please Wait....");
        progressDialog.show();*/

        myModelClass = new MyModelClass(MyConstants.url, MyConstants.user, MyConstants.password,branch,year,sem);
        MyAsyncTask task = new MyAsyncTask();
        task.setCallback(this);
        task.execute(myModelClass);
    }

    @Override
    public void setResultSet(List<String> list) {
        list.add(0,MyConstants.SELECT_PREF);
        final ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, list);
        /*final ArrayAdapter adapter2 = new ArrayAdapter(this, android.R.layout.simple_list_item_1, list);
        final ArrayAdapter adapter3 = new ArrayAdapter(this, android.R.layout.simple_list_item_1, list);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        */adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        s1.setAdapter(adapter);
        s2.setAdapter(adapter);
        s3.setAdapter(adapter);
        progressDialog.dismiss();

        //Same Item selection logic
        setItemSelectionLogic(adapter);
    }

    private void setItemSelectionLogic(final ArrayAdapter adapter1) {
        s1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {


            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position != 0) {
                    if (s1.getSelectedItem() == s2.getSelectedItem() || s1.getSelectedItem() == s3.getSelectedItem()) {
                        Toast.makeText(getApplicationContext(), "Same Subject Selected", Toast.LENGTH_SHORT).show();
                        submit.setVisibility(View.INVISIBLE);
                        isPref1Selected =false;

                    }

                    else {
                        isPref1Selected = true;
                        if(allPrefSelected()) submit.setVisibility(View.VISIBLE);
                        pref1Sub = (String) s1.getSelectedItem();
                    }

                }
                else {
                    isPref1Selected = false;
                    submit.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        s2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position != 0) {
                    if (s2.getSelectedItem() == s1.getSelectedItem() || s2.getSelectedItem() == s3.getSelectedItem()) {
                        Toast.makeText(getApplicationContext(), "Same Subject Selected", Toast.LENGTH_SHORT).show();
                        submit.setVisibility(View.INVISIBLE);
                        isPref2Selected = false;
                    }

                    else {
                        isPref2Selected = true;
                        if (allPrefSelected()) submit.setVisibility(View.VISIBLE);
                        pref2Sub = (String) s2.getSelectedItem();
                    }
                }
                else {
                    isPref2Selected = false;
                    submit.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        s3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position != 0) {
                    if (s3.getSelectedItem() == s1.getSelectedItem() || s3.getSelectedItem() == s2.getSelectedItem()) {
                        Toast.makeText(getApplicationContext(), "Same Subject Selected", Toast.LENGTH_SHORT).show();
                        submit.setVisibility(View.INVISIBLE);
                        isPref3Selected = false;
                    }

                    else{
                        isPref3Selected = true;
                        if(allPrefSelected()) submit.setVisibility(View.VISIBLE);
                        pref3Sub = (String) s3.getSelectedItem();
                    }

                }
                else {
                    isPref3Selected = false;
                    submit.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        final userOptionActivity activity = this;
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final UserOptionModel userOptionModel = new UserOptionModel(pref1Sub,pref2Sub,pref3Sub,branch);
                final InsertAsyncTask insertAsyncTask = new InsertAsyncTask();
                insertAsyncTask.setCallback(activity);
                insertAsyncTask.execute(userOptionModel);
            }
        });
    }

    private boolean allPrefSelected() {
        return isPref1Selected && isPref2Selected && isPref3Selected;
    }

    @Override
    public void isPrefSubInserted(Boolean status) {
        if (status)
        Toast.makeText(getApplicationContext(),"Saved Successfully",Toast.LENGTH_LONG).show();
        else
            Toast.makeText(getApplicationContext(),"Failed",Toast.LENGTH_LONG).show();
    }
}

class MyAsyncTask extends AsyncTask<MyModelClass,Integer,ResultSet>{
    private ResultCallback resultCallback;
    void setCallback(ResultCallback resultCallback){
        this.resultCallback = resultCallback;
    }

    @Override
    protected ResultSet doInBackground(MyModelClass... objects){
        ResultSet resultSet = null;
            try {
                Connection con = DriverManager.getConnection(objects[0].url, objects[0].user, objects[0].password);
                Statement st = con.createStatement();
                String query ="select * from electives_"+objects[0].branch +" " + "WHERE"
                        +" " + "year = " + objects[0].year +" "+ "AND" + " " + "semester = "+objects[0].sem;
                Log.i("FormedQuery",query);
                resultSet = st.executeQuery(query);
                return resultSet;
            }
            catch (Exception e){
             Log.e("TAG","MyException",e);
            }
            Log.i("TAG","NULL VALUE RETURNED");
        return resultSet;
    }

    @Override
    protected void onPostExecute(final ResultSet o) {
        super.onPostExecute(o);
        getDatabaseData(o);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }

    private void getDatabaseData(ResultSet resultSet){

        ArrayList<String> list = new ArrayList<>();
        try {
            while (resultSet.next()){
                String name = resultSet.getString("courseName");
                list.add(name);
            }
            resultCallback.setResultSet(list);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
class MyModelClass {
    String url = "";
    String user = "";
    String branch = "";
    String year = "";
    String sem = "";
    String password = "";

    MyModelClass(String url, String user, String password, String branch, String year, String sem) {
        this.url = url;
        this.password = password;
        this.user = user;
        this.branch = branch;
        this.year = year;
        this.sem = sem;
    }
}