package com.example.ahmed.electivesubjectselection;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mysql.jdbc.log.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

//import static android.support.v4.content.ContextCompat.startActivity;

/**
 * Created by Ahmed on 3/17/2018.
 */

public class InsertAsyncTask extends AsyncTask<UserOptionModel,Integer,Boolean>{

    private SubjectPrefCallback subjectPrefCallback;
    FirebaseDatabase firebaseDatabase;
    FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
    FirebaseUser firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("");
    String uid= firebaseUser.getUid();
    public String status = "True";

    public void setCallback(SubjectPrefCallback subjectPrefCallback){
        this.subjectPrefCallback = subjectPrefCallback;
    }

    @Override
    protected Boolean doInBackground(UserOptionModel... model) {
        Connection  connection = null;
        PreparedStatement preparedStatement=null;
        PreparedStatement preparedStatement1=null;
        PreparedStatement preparedStatement2= null;
        PreparedStatement preparedStatement3= null;
        Statement statement= null;
        ResultSet resultSet = null;
        try {
            connection = DriverManager.getConnection(model[0].url, model[0].user, model[0].password);
            String query = "INSERT INTO "+"useroptions_"+model[0].branch+" VALUES ('"+model[0].rollNumber+"'" +
                    ",'"+model[0].pref1+"','"+model[0].pref2+"'" +
                    ",'"+model[0].pref3+"','"+model[0].date+"','"+model[0].time+"' )" ;
            String query2 ="UPDATE results_"+model[0].branch+" SET count_pref2 = count_pref1 + 1 WHERE Subjects ='"+model[0].pref2+"'";
            String query3 ="UPDATE results_"+model[0].branch+" SET count_pref3 = count_pref1 + 1 WHERE Subjects ="+model[0].pref3;
            String query4 ="select count_pref1";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement1 = connection.prepareStatement("UPDATE results_"+model[0].branch+" SET count_pref1 = count_pref1 + 1 WHERE Subjects =?");
            preparedStatement1.setString(1,model[0].pref1);
            preparedStatement2 = connection.prepareStatement(query2);
            preparedStatement3 = connection.prepareStatement("UPDATE results_"+model[0].branch+" SET count_pref3 = count_pref1 + 1 WHERE Subjects =?");
            preparedStatement3.setString(1,model[0].pref3);
            preparedStatement.executeUpdate();
            preparedStatement1.executeUpdate();
            preparedStatement2.executeUpdate();
            preparedStatement3.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            try {
                connection.close();
            } catch (Exception e) {}
            try {
                preparedStatement.close();
            } catch (Exception e) {}
            try {
                preparedStatement1.close();
            } catch (Exception e) {}
            try {
                preparedStatement2.close();
            } catch (Exception e) {}
            try {
                preparedStatement3.close();
            } catch (Exception e) {}
        }

        return false;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        subjectPrefCallback.isPrefSubInserted(true);
        changeUserData();
    }
    
    private void changeUserData(){
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference myRef =firebaseDatabase.getReference(firebaseAuth.getUid());
        myRef.child("options").setValue("True");
    }
}
