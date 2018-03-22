package com.example.ahmed.electivesubjectselection;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class adminActivity extends AppCompatActivity {
    private Button add,delete,displayresult;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        add=(Button)findViewById(R.id.insertsubjectbutton);
        delete=(Button)findViewById(R.id.deletesubject);
        displayresult=(Button)findViewById(R.id.result_button);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(adminActivity.this,addSubjectActivity.class));
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(adminActivity.this,deleteActivity.class));
            }
        });
        displayresult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(adminActivity.this,resultsActivity.class));
            }
        });
    }
}
