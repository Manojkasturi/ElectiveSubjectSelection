package com.example.ahmed.electivesubjectselection;

import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;


public class loginSuccessActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private FirebaseAuth firebaseAuth;
    private EditText rollNumber;
    private TextView name,rollno,branch;
    private Button findme;
    double n;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_success);
        rollNumber = (EditText)findViewById(R.id.etRollNo);
        name = (TextView)findViewById(R.id.tv1);
        rollno = (TextView)findViewById(R.id.tv2);
        branch= (TextView)findViewById(R.id.tv3);
        firebaseAuth= FirebaseAuth.getInstance();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        final NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        //String str = rollNumber.getText().toString().trim();


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.login_success, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

      if (id == R.id.nav_logout) {
          Logout();
          Toast.makeText(loginSuccessActivity.this,"Logged Out",Toast.LENGTH_SHORT);
        } else if (id == R.id.nav_manage) {
            startActivity(new Intent(android.provider.Settings.ACTION_SETTINGS));
        } else if (id == R.id.nav_share) {
          Intent i1= new Intent(Intent.ACTION_SEND);
          i1.setType("text/plain");
          String shareBody="Share this app";
          String sharesub ="Share using";
          i1.putExtra(Intent.EXTRA_SUBJECT,shareBody);
          i1.putExtra(Intent.EXTRA_TEXT,sharesub);
          startActivity(Intent.createChooser(i1,"share using"));
        } else if (id == R.id.nav_send) {
          Intent i = new Intent(Intent.ACTION_SEND);
          i.setData(Uri.parse("mailto:"));
          String[] to = {"kashifahmed38@gmail.com"};
          i.putExtra(Intent.EXTRA_EMAIL,to);
          i.putExtra(Intent.EXTRA_SUBJECT,"Suggestion");
          i.setType("message/rfc822");
          Intent.createChooser(i,"Send Email");
          startActivity(i);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    private void Logout(){
        firebaseAuth.signOut();
        finish();
        startActivity(new Intent(loginSuccessActivity.this,loginActivity.class));

    }
/*    private boolean check(){
        boolean res=false;
        String rn = rollNumber.getText().toString();
        if(rn.isEmpty()){
            rollNumber.setError("Please Enter the Valid Email");
            res = false;
        }
        else res= true;

        return res;
    }*/
}
