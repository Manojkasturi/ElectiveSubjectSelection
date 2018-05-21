package com.example.ahmed.electivesubjectselection;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Handler;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.Calendar;
import java.util.Date;

public class loginSuccessActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private ProgressDialog progressDialog;
    private TextView tv,branch,year,sem,status,name,usertextview;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    private Button clickhere;
    private DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference().child("");
    String uid,br;
    int y,s;
    String user_roll,options,rollNumber,user_name;
    Boolean Status;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_success);
        final SwipeRefreshLayout swipeRefreshLayout= (SwipeRefreshLayout)findViewById(R.id.refresh);
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_dark,android.R.color.holo_blue_light,android.R.color.holo_green_dark ,android.R.color.holo_orange_dark);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                (new Handler()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                        finish();
                        startActivity(getIntent());
                    }
                },1500);
            }
        });
        clickhere = (Button)findViewById(R.id.button3);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Fetching Data Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        status =(TextView)findViewById(R.id.status);
        name=(TextView)findViewById(R.id.name_textview);
        tv=(TextView)findViewById(R.id.roll_no_textview);
        branch =(TextView)findViewById(R.id.branch_textview);
        year=(TextView) findViewById(R.id.year_textview);
        sem = (TextView) findViewById(R.id.sem_textview);
        usertextview=(TextView)findViewById(R.id.user_textview);
        user=FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                try {
                    user_name = (String) dataSnapshot.child(uid).child("userName").getValue();
                    user_roll = (String) dataSnapshot.child(uid).child("userRoll").getValue();
                    options = (String) dataSnapshot.child(uid).child("options").getValue();
                    Log.i("TAG",user_roll+ " , "+ options);
                    evaluateDataFromRollNo();
                    checkStatus();
                }
                catch (Exception e){
                    e.printStackTrace();
                    Log.i("TAG",e.toString());
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        clickhere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(br == null || y == 0 || s == 0){
                    Toast toast=Toast.makeText(getApplicationContext(),"Data Not Retrieved, Try Again..",Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.TOP| Gravity.LEFT, 320, 40);
                    toast.show();
                }else {
                    Intent i = new Intent(loginSuccessActivity.this, userOptionActivity.class);
                    i.putExtra("branch", br);
                    i.putExtra("year", y);
                    i.putExtra("sem", s);
                    i.putExtra("rollNumber",user_roll);
                    startActivityForResult(i, 1);
                    Log.w("passedintentdetails", rollNumber+" "+branch + " " + year + " " + sem);

                }
            }
        });
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        final NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }
    private void checkStatus() {
        if(options.equals("True")) {
            status.setText(status.getText()+" "+"Preferences recorded ");
            Status =true;
            clickhere.setVisibility(View.INVISIBLE);
            final AlertDialog.Builder builder = new AlertDialog.Builder(loginSuccessActivity.this);
            builder.setMessage("Preferences Recorded");
            builder.setCancelable(false);
            builder.setNegativeButton("OK!", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    startActivity(new Intent(loginSuccessActivity.this,OptionsCompletedActivity.class));
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();

        }
        else {
            status.setText(status.getText()+" "+"Preferences not recorded ");
        }
    }

    private void evaluateDataFromRollNo() {
        int m,a,b,currYear;
        String str ;
        Date now;
        Calendar c = Calendar.getInstance();
        tv.setText(String.format(tv.getText() + " " + user_roll));

        char arr[]=user_roll.toCharArray();

        m=c.get(Calendar.MONTH)+1;
        if(m>=6 && m<=10){
            sem.setText(sem.getText() + " " + "1st Semester");
            s=1;
        } else{
            sem.setText(sem.getText() + " " + "2nd Semester");
            s=2;
        }
        name.setText(name.getText()+" "+user_name);
       // usertextview.setText(user_name);
        str = user_roll.substring(6,8);
        if(str.equals("01")){
            branch.setText(branch.getText() + " " + "CIVIL");
            br="CIVIL";
        } else if(str.equals("02")) {
            branch.setText(branch.getText() + " " + "EEE");
            br="EEE";
        } else if(str.equals("03")){
            branch.setText(branch.getText() +" "+"MECHANICAL");
            br="MECHANICAL";
        } else if(str.equals("04")){
            branch.setText(branch.getText() +" "+"ECE");
            br="ECE";
        } else if(str.equals("05")){
            branch.setText(branch.getText() +" "+"CSE");
            br="CSEIT";
        } else if(str.equals("11")){
            branch.setText(branch.getText() +" "+ "EIE");
            br="EIE";
        } else if(str.equals("12")){
            branch.setText(branch.getText() +" "+"IT");
            br="IT";
        }
        a = Character.getNumericValue(arr[0]);
        b = Character.getNumericValue(arr[1]);
        now = new Date();
        currYear = 1900+now.getYear();
        currYear = currYear- (2000+(a*10+b));
        if(currYear == 3){
            year.setText(year.getText()+  " 3rd Year(Junior)");
        }
        else if(currYear == 4){
            year.setText(year.getText()+ " 4th Year(Senior)");
        }

        else {
            year.setText(year.getText()+ " ?");
            progressDialog.dismiss();
            if(options.equals("False")) {
                new AlertDialog.Builder(this)
                        .setMessage("You look like a year back Student")
                        .setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(loginSuccessActivity.this, YeraBackStudentsActivity.class);
                                intent.putExtra("branch", br);
                                intent.putExtra("year", y);
                                intent.putExtra("sem", s);
                                intent.putExtra("rollNumber", user_roll);
                                startActivity(intent);
                            }
                        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        clickhere.setVisibility(View.INVISIBLE);
                        Toast toast =Toast.makeText(loginSuccessActivity.this,"Pull down to refresh",Toast.LENGTH_LONG);
                        toast.show();
                    }
                }).setCancelable(false)
                        .show();
            }
        }
        y=currYear;
        progressDialog.dismiss();
    }

    @Override
    public void onActivityResult(int req, int res, Intent data) {
        switch (req) {
            case 1:
                progressDialog.dismiss();
                break;
        }
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
       /* if (id == R.id.action_settings) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
            int id = item.getItemId();

      if (id == R.id.nav_logout) {
          FirebaseAuth.getInstance().signOut();
          startActivity(new Intent(this,loginActivity.class));
          Toast toast=Toast.makeText(loginSuccessActivity.this,"Logged Out",Toast.LENGTH_SHORT);
          toast.setGravity(Gravity.TOP| Gravity.LEFT, 320, 40);
          toast.show();

        }else if(id==R.id.nav_profile){
            startActivity(new Intent(loginSuccessActivity.this,userDetailsActivity.class));
        }
        else if (id == R.id.nav_manage) {
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
        else if(id==R.id.nav_subjectdetails){
          startActivity(new Intent(loginSuccessActivity.this,subjectDetailsActivity.class));
        }
        else if(id==R.id.credits){
            startActivity(new Intent(loginSuccessActivity.this,creditsActivity.class));
      }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
