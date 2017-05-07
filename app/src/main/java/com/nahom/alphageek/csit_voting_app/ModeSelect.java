package com.nahom.alphageek.csit_voting_app;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ModeSelect extends AppCompatActivity {

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
        if(Global.language.equals("english")){
            TextView view =(TextView)findViewById(R.id.tvTitle);
            view.setText("Please Choose an Option");
            btnStudent.setText("I AM STUDENT");
            btnStaff.setText("I AM STAFF Member");
        }
        btnStudent.setOnClickListener(
                new Button.OnClickListener(){
                    public void onClick(View v){
                        Intent i=new Intent(ModeSelect.this,VoteActivity.class);
                        startActivity(i);
                        finish();
                    }
                }
        );
    }

}
