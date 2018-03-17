package com.example.ahmed.electivesubjectselection;

import android.os.AsyncTask;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by Ahmed on 3/17/2018.
 */

public class InsertAsyncTask extends AsyncTask<UserOptionModel,Integer,Boolean>{

    private SubjectPrefCallback subjectPrefCallback;

    public void setCallback(SubjectPrefCallback subjectPrefCallback){
        this.subjectPrefCallback = subjectPrefCallback;
    }

    @Override
    protected Boolean doInBackground(UserOptionModel... model) {
        try {
            Connection con = DriverManager.getConnection(model[0].url, model[0].user, model[0].password);
            String query = "INSERT INTO "+"useroptions_"+model[0].branch+" VALUES ('"+model[0].rollNumber+"'" +
                    ",'"+model[0].pref1+"','"+model[0].pref2+"'" +
                    ",'"+model[0].pref3+"','"+model[0].date+"','"+model[0].time+"' )" ;
            PreparedStatement preparedStatement = con.prepareStatement(query);
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        subjectPrefCallback.isPrefSubInserted(true);

    }
}
