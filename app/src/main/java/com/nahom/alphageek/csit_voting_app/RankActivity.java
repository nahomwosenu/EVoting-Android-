package com.nahom.alphageek.csit_voting_app;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.widget.GridLayout;
import android.widget.TextView;

import com.kosalgeek.asynctask.AsyncResponse;
import com.kosalgeek.asynctask.PostResponseAsyncTask;

import java.util.HashMap;

public class RankActivity extends AppCompatActivity {

    GridLayout gridLayout;
    TextView[] titles;
    TextView[] scores;
    TextView[] ranks;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gridLayout=new GridLayout(this);
        setContentView(gridLayout);
        TextView scoreView=new TextView(this);
        scoreView.setText("Vote");
        TextView titleView=new TextView(this);
        titleView.setText("Developer Name");
        TextView rankView=new TextView(this);
        rankView.setText("Rank");
        //scoreView.setTextAppearance(R.style.TextAppearance_AppCompat_Large);
        //rankView.setTextAppearance(R.style.TextAppearance_AppCompat_Large);
        //titleView.setTextAppearance(R.style.TextAppearance_AppCompat_Large);
        scoreView.setBackgroundColor(Color.GREEN);
        titleView.setBackgroundColor(Color.GREEN);
        rankView.setBackgroundColor(Color.GREEN);
        gridLayout.setColumnCount(3);
        gridLayout.addView(rankView);
        gridLayout.addView(titleView);
        gridLayout.addView(scoreView);
        gridLayout.setPadding(5,5,5,5);
        postExecute();
    }
    public void postExecute(){
      HashMap map=new HashMap();
        if(Global.rankType.equals("student"))
        map.put("type","student");
        else map.put("type","staff");
        AsyncResponse response=new AsyncResponse() {
            @Override
            public void processFinish(String s) {
                    if(s==null || s.isEmpty()){
                        Global.showDialog(RankActivity.this,"Error","Connection failed at the moment, check your connection and try again");

                    }
                    else if(s.contains("error") || s.contains("Error")){
                        Global.showDialog(RankActivity.this,"Error","Unkown error occured, please try again later");
                        Log.d("MYAPP","RESULT: "+s);
                    }
                    else{
                        Log.d("MYAPP","RESULT: "+s);
                        parse(s);
                    }

            }
        };
        PostResponseAsyncTask task=new PostResponseAsyncTask(this,map,response);
        task.execute(Global.server+"rank.php");
    }
    public void parse(String s){
        String[] raw=s.split(":");
        int total=Integer.parseInt(raw[0]);
        gridLayout.setRowCount(total+2);
        gridLayout.setRowOrderPreserved(true);
        titles=new TextView[total];
        scores=new TextView[total];
        ranks=new TextView[total];
        String[] rows=raw[1].split(";");
        for(int i=0;i<rows.length;i++){
            String[] row=rows[i].split(",");
            String id=row[0];
            String fname=row[1];
            String lname=row[2];
            String title=row[3];
            String stuVote=row[4];
            String staffVote=row[5];
            titles[i]=new TextView(this);
            titles[i].setText(fname+" "+lname);
            titles[i].setBackgroundColor(Color.LTGRAY);
            ranks[i]=new TextView(this);
            ranks[i].setText(""+(i+1));
            ranks[i].setBackgroundColor(Color.MAGENTA);
            scores[i]=new TextView(this);
            if(Global.rankType.equals("student"))
            scores[i].setText(stuVote);
            else scores[i].setText(staffVote);
            scores[i].setBackgroundColor(Color.CYAN);
            gridLayout.addView(ranks[i]);
            gridLayout.addView(titles[i]);
            gridLayout.addView(scores[i]);
        }

    }
    @Override
    public void onBackPressed(){
        Intent intent=new Intent(this,ModeSelect.class);
        startActivity(intent);
        finish();
    }
}

