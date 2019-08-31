package com.example.nbaprediction;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Spinner homeTeamSpinner = null;
    private Spinner awayTeamSpinner = null;
    private TextView result = null;
    private String homeTeam = null;
    private String awayTeam =null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        homeTeamSpinner = (Spinner) findViewById(R.id.spinner);
        awayTeamSpinner = (Spinner) findViewById(R.id.spinner2);
        result = (TextView) findViewById(R.id.textView3);

        homeTeamSpinner.setOnItemSelectedListener(this);
        awayTeamSpinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position,long id){
        String content = parent.getItemAtPosition(position).toString();

        switch ((parent.getId())){
            case R.id.spinner:
                //Toast.makeText(MainActivity.this,"Home team is:"+content,Toast.LENGTH_SHORT).show();
                homeTeam = content;
                break;
            case R.id.spinner2:
               // Toast.makeText(MainActivity.this,"Away team is:"+content,Toast.LENGTH_SHORT).show();
                awayTeam =content;
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView){

    }

    public void getPrediction(View view) {
        result.setText(homeTeam+" beat "+awayTeam );
    }
}
