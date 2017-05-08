package com.nahom.alphageek.csit_voting_app;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kosalgeek.asynctask.AsyncResponse;
import com.kosalgeek.asynctask.PostResponseAsyncTask;

import java.util.HashMap;

public class VoteActivity extends AppCompatActivity {

    GridLayout gridLayout;
    Button[] buttons;
    TextView[] views;
    TextView[] nviews;
    String[] devId;
    String[] firstNames;
    String[] lastNames;
    String[] titles;
    String[] stu_votes;
    String[] staff_votes;
    static int total=0;
    static int votes=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parse();
        Log.d("MYAPP","Total: "+total);
        if(titles!=null)
        Log.d("MYAPP","Array: "+titles.length);

        gridLayout=new GridLayout(this);

        gridLayout.setRowCount(total+2);
        gridLayout.setColumnCount(3);
        TextView col1=new TextView(this);
        col1.setText("Title");
        TextView col2=new TextView(this);
        col2.setText("Developer");
        TextView col3=new TextView(this);
        col3.setText("Vote/Unvote");
        gridLayout.addView(col1);
        gridLayout.addView(col2);
        gridLayout.addView(col3);

        setContentView(gridLayout);
    }
    public void voteClicked(View v){
        final Button button=(Button)v;
        final String id=button.getHint().toString();
        final String mode=button.getText().toString();
        Log.d("MYAPP","button id: "+id);
        Log.d("MYAPP","button mode: "+mode);
        if(mode.equals("vote") || mode.equals("Vote")){
            int i=getIndex(id);
            AlertDialog.Builder builder=new AlertDialog.Builder(this);
            builder.setTitle("Confirm vote");
            builder.setMessage("Are you sure to vote for project: "+titles[i]+" ?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    vote(id,Global.voter,button);


                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(VoteActivity.this,"Vote Canceled by the user",Toast.LENGTH_LONG).show();
                }
            });
            builder.show();

        }
        else{
            unvote(id,Global.voter,button);
        }
    }
    static boolean state=false;
    static int index=0;
    public boolean vote(String id,String voter,final Button button){
         index=getIndex(id);
        String mac=Global.getMac(this);
        HashMap map=new HashMap();
        map.put("request","vote");
        map.put("mac",mac);
        if(voter.equals("student"))
        map.put("type","stu_vote");
        else map.put("type","staff_vote");
        map.put("id",id);
        AsyncResponse response=new AsyncResponse() {
            @Override
            public void processFinish(String s) {
                if(s==null || s.isEmpty()){
                    showDialog("Error","Voting failed, check your wifi connection and try again later!");
                    state=false;
                }
                else if(s.contains("error")){
                    showDialog("Error",s);
                    state=false;
                }
                else if(s.contains("true")){
                    AlertDialog.Builder builder=new AlertDialog.Builder(VoteActivity.this);
                    builder.setTitle("Success");
                    builder.setMessage("You successfully vote for "+firstNames[index]+ " "+lastNames[index]);
                    builder.show();
                    state=true;
                    button.setText("Un vote");
                    button.setBackgroundColor(Color.RED);
                }
                else showDialog("Error",s);
            }
        };
        PostResponseAsyncTask task=new PostResponseAsyncTask(this,map,response);
        task.execute(Global.server+"developer.php");
        return state;
    }
    public int getIndex(String id){
        int index=-1;
        for(int i=0;i<devId.length;i++){
            if(devId[i].equals(id))
                return i;
        }
        return index;
    }
    static boolean uvote=false;
    public boolean unvote(String id,String voter,final Button button){
        final int index=getIndex(id);
        String mac=Global.getMac(this);
        HashMap map=new HashMap();
        map.put("request","unvote");
        map.put("mac",mac);
        if(Global.voter.equals("student"))
            map.put("type","student");
        else
            map.put("type","staff");
        map.put("id",id);
        AsyncResponse response=new AsyncResponse() {
            @Override
            public void processFinish(String s) {
              if(s==null || s.isEmpty()){
                  showDialog("Error","Unable to clear vote, check your connection");
                  uvote=false;
              }
              else if(s.contains("error")){
                  showDialog("Error",s);
                  uvote=false;
              }
              else if(s.contains("false")){
                  showDialog("Error","Failed to clear vote, check your connection");
                  uvote=false;
              }
              else if(s.contains("true")){
                  showDialog("Success","You un-voted for project: "+titles[index]+" successfully");
                  button.setText("Vote");
                  button.setBackgroundColor(Color.GREEN);
                  uvote=true;
              }
            }
        };
        PostResponseAsyncTask task=new PostResponseAsyncTask(this,map,response);
        task.execute(Global.server+"developer.php");
        return uvote;
    }
    public void parse(){
        String mac=Global.getMac(this);
        HashMap data=new HashMap();
        data.put("request","list");
        data.put("mac",mac);
        AsyncResponse response=new AsyncResponse() {
            @Override
            public void processFinish(String s) {
                Log.d("MYAPP","Response: "+s);
                if(s==null || s.isEmpty()){
                    Toast.makeText(VoteActivity.this,"Connection Problem Please make sure that you are connected to Wifi",Toast.LENGTH_LONG).show();
                }
                else if(s.contains("error") || s.contains("Error")){
                    showDialog("Error","An Error occured please consult Nahom for help"+s);
                }
                else{

                    String[] raw=s.split(":");
                    total=Integer.parseInt(raw[0]);
                    Log.d("MYAPP","Total-real: "+total);
                    String[] row=raw[1].split(";");
                    devId=new String[row.length];
                    firstNames=new String[row.length];
                    lastNames=new String[row.length];
                    titles=new String[row.length];
                    stu_votes=new String[row.length];
                    staff_votes=new String[row.length];
                    for(int i=0;i<row.length;i++) {
                        String[] data = row[i].split(",");
                        devId[i] = data[0];
                        firstNames[i] = data[1];
                        lastNames[i] = data[2];
                        titles[i] = data[3];
                        stu_votes[i] = data[4];
                        staff_votes[i] = data[5];
                        Log.d("MYAPP","ID: "+devId[i]+" Title: "+titles[i]+" FirstName: "+firstNames[i]+" LastName: "+lastNames[i]);

                    }
                    //Add to Layout
                    views=new TextView[total];
                    buttons=new Button[total];
                    nviews=new TextView[total];
                    for(int i=0;i<total;i++){
                        views[i]=new TextView(VoteActivity.this);//holds project title(1st col)
                        views[i].setText(titles[i]);
                        nviews[i]=new TextView(VoteActivity.this);//holds firstname & lastname (2nd col)
                        nviews[i].setText(firstNames[i]+" "+lastNames[i]);
                        buttons[i]=new Button(VoteActivity.this);
                        buttons[i].setText("Vote");
                        buttons[i].setHint(devId[i]);//additional value to identify the buttons
                        buttons[i].setOnClickListener(
                                new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        voteClicked(v);
                                    }
                                }
                        );
                        views[i].setPadding(5,5,5,5);
                        views[i].setTextSize(20);
                        views[i].setBackgroundColor(Color.GREEN);
                        nviews[i].setPadding(5,5,5,5);
                        nviews[i].setTextSize(20);
                        nviews[i].setBackgroundColor(Color.LTGRAY);
                        buttons[i].setPadding(5,5,5,5);
                        buttons[i].setTextSize(20);
                        gridLayout.addView(views[i]);
                        gridLayout.addView(nviews[i]);
                        gridLayout.addView(buttons[i]);
                    }
                    //Finished Adding to layout
                    Toast.makeText(VoteActivity.this,"Data fetched succesfully",Toast.LENGTH_LONG);
                }
            }
        };
        PostResponseAsyncTask task=new PostResponseAsyncTask(this,data,response);
        task.execute(Global.server+"developer.php");

    }
    public void showDialog(String title,String message){
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }
    @Override
    public void onBackPressed(){
        Intent intent=new Intent(this,ModeSelect.class);
        startActivity(intent);
        finish();
    }
}
