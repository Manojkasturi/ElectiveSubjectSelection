package com.example.ahmed.electivesubjectselection;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class YeraBackStudentsActivity extends AppCompatActivity {

    private EditText rollno,branch,year,sem;
    private Button proceed;
    boolean result = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yera_back_students);
        rollno= (EditText)findViewById(R.id.editText);
        branch=(EditText)findViewById(R.id.editText2);
        year=(EditText)findViewById(R.id.editText3);
        sem=(EditText)findViewById(R.id.editText4);
        proceed =(Button)findViewById(R.id.button2);
        final String rollNumber = getIntent().getStringExtra("rollNumber");
        final String intentbranch =getIntent().getStringExtra("branch");
        final String semester = String.valueOf(getIntent().getIntExtra("sem",1));
        final String currYear= getIntent().getStringExtra("year");
        rollno.setText(rollNumber);
        rollno.setEnabled(false);
        branch.setText(intentbranch);
        branch.setEnabled(false);
        sem.setText(semester);
        sem.setEnabled(false);
        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {
                    String currYear1 = year.getText().toString();
                    Intent intent = new Intent(YeraBackStudentsActivity.this, userOptionActivity.class);
                    intent.putExtra("branch", intentbranch);
                    intent.putExtra("year", currYear1);
                    intent.putExtra("sem", semester);
                    intent.putExtra("rollNumber", rollNumber);
                    startActivity(intent);
                }
            }
        });
    }

    private boolean validate() {

        String useryear=year.getText().toString();
        if(useryear.isEmpty()){
            year.setError("Please enter your current year");
            Toast.makeText(this, "Year feild empty", Toast.LENGTH_SHORT).show();
            result = false;
        }
        else result = true;
        return result;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(YeraBackStudentsActivity.this,loginSuccessActivity.class));
    }
}
