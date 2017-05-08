package com.nahom.alphageek.csit_voting_app;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

public class LauncherActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Simple E-Voting System for AMU CS-IT Exhibition 2017 \nDeveloped by: Nahom ", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        fab.setImageResource(R.mipmap.ic_launcher_round);
        findViewById(R.id.btnEnglish).setOnClickListener(
                new Button.OnClickListener(){
                    public void onClick(View v){
                        Global.language="english";
                        Intent intent=new Intent(LauncherActivity.this,ModeSelect.class);
                        startActivity(intent);
                        finish();
                    }
                }
        );
        findViewById(R.id.btnAmharic).setOnClickListener(
                new Button.OnClickListener(){
                    public void onClick(View v){
                        Global.language="amharic";
                        Intent intent=new Intent(LauncherActivity.this,ModeSelect.class);
                        startActivity(intent);
                        finish();
                    }
                }
        );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_launcher, menu);
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
    @Override
    public void onBackPressed(){
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("Are you sure to exit?");
        builder.setMessage("Thanks for using this app, Please check out my other apps on git. \n-> Amharic Translator (Android)\n->Flash Protector (Windows & Linux) & others ...\n-> For Computer Maintenance & ICT Support,\nCall me: 0965 29 01 33\nEmail: alphageekict@gmail.com");
        builder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        System.exit(0);
                    }
                }
        );
        builder.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }
        );
        builder.show();
    }
}
