package com.nahom.alphageek.csit_voting_app;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.kosalgeek.asynctask.AsyncResponse;
import com.kosalgeek.asynctask.PostResponseAsyncTask;

import java.util.HashMap;

public class ModeSelect extends AppCompatActivity {

    String message;
    String stuOpt;
    String staffOpt;
    String title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mode_select);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        Button btnStudent=(Button)findViewById(R.id.btnStudent);
        Button btnStaff=(Button)findViewById(R.id.btnStaff);
        Button btnRank=(Button)findViewById(R.id.btnRank);
        message="የፕሮጀክቶችን ደረጃ በምን መስፈርት ማየት ይፈልጋሉ?";
        stuOpt="በተማሪዎች ምርጫ";
        staffOpt="በስታፍ አባላት ምርጫ";
        title="የደረጃ አወጣጥ";
        if(Global.language.equals("english")){
            TextView view =(TextView)findViewById(R.id.tvTitle);
            view.setText("Please Choose an Option");
            btnStudent.setText("I AM STUDENT");
            btnStaff.setText("I AM STAFF Member");
            btnRank.setText("View Rank/Score only");
            message="In which way do u want to rank the projects?";
            stuOpt="Students Vote";
            staffOpt="Staff Vote";
            title="Ranking method";
        }
        btnStudent.setOnClickListener(
                new Button.OnClickListener(){
                    public void onClick(View v){
                        requestAccess("student");

                    }
                }
        );
        btnStaff.setOnClickListener(
                new Button.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        requestAccess("staff");
                    }
                }
        );
        btnRank.setOnClickListener(
                new Button.OnClickListener(){
                    public void onClick(View v){
                        AlertDialog.Builder builder=new AlertDialog.Builder(ModeSelect.this);
                        builder.setTitle(title);
                        builder.setMessage(message);
                        builder.setPositiveButton(stuOpt,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Global.rankType="student";
                                       Intent i=new Intent(ModeSelect.this,RankActivity.class);
                                        startActivity(i);
                                        finish();
                                    }
                                }
                        );
                        builder.setNegativeButton(staffOpt,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Global.rankType="staff";
                                        Intent i=new Intent(ModeSelect.this,RankActivity.class);
                                        startActivity(i);
                                        finish();
                                    }
                                }
                        );
                        builder.show();
                    }
                }
        );
    }
    public void requestAccess(final String mode){
        String mac=Global.getMac(this);
        HashMap map=new HashMap();
        map.put("request","access");
        map.put("mac",mac);
        AsyncResponse response=new AsyncResponse() {
            @Override
            public void processFinish(String s) {
                if(s==null || s.isEmpty()){
                    Global.showDialog(ModeSelect.this,"Error","Connection failed, connect to wifi network and try again\nIf you are using proxy, clear proxy and try again");
                }
                else if(s.contains("error") || s.contains("Error") || s.contains("false")){
                    Global.showDialog(ModeSelect.this,"Error","An Error Occured, try again later");
                    Log.d("MYAPP","result: "+s);
                }
                else if(s.contains("denied") || s.contains("Denied")){
                    Global.showDialog(ModeSelect.this,"Access Denied",s);
                }
                else{
                    if(s.contains("Info")) {
                        AlertDialog.Builder builder=new AlertDialog.Builder(ModeSelect.this);
                        builder.setTitle("Info");
                        builder.setMessage(s);
                        builder.setPositiveButton("Okay",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent i = null;
                                        if (mode.equals("student"))
                                            i = new Intent(ModeSelect.this, VoteActivity.class);
                                        else
                                            i = new Intent(ModeSelect.this, StaffLogin.class);
                                        startActivity(i);
                                        finish();
                                    }
                                }
                        );
                        builder.show();
                    }
                    else {
                        Intent i = null;
                        if (mode.equals("student"))
                            i = new Intent(ModeSelect.this, VoteActivity.class);
                        else
                            i = new Intent(ModeSelect.this, VoteActivity.class);
                        startActivity(i);
                        finish();
                    }
                }
            }
        };
        PostResponseAsyncTask task=new PostResponseAsyncTask(this,map,response);
        task.execute(Global.server+"access.php");
    }
  @Override
    public void onBackPressed(){
      Intent intent=new Intent(this,LauncherActivity.class);
      startActivity(intent);
      finish();
  }
}
