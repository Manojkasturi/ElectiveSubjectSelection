package com.example.ahmed.electivesubjectselection;

import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class update extends AppCompatActivity {

    private EditText updatedpassword;
    private Button updatePassword;
    private FirebaseUser firebaseUser;
    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        updatedpassword =(EditText)findViewById(R.id.updatedPassword);
        updatePassword =(Button)findViewById(R.id.updatePasswordbutton);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        updatePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newpass = updatedpassword.getText().toString();
                firebaseUser.updatePassword(newpass).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(update.this,"Password Changed Successful",Toast.LENGTH_SHORT).show();
                            finish();
                        }
                        else{
                            Toast.makeText(update.this,"Task Failed ..Retry",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case  android.R.id.home:
                onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
