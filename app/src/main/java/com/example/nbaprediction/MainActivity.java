package com.example.nbaprediction;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.HashMap;


public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Spinner homeTeamSpinner = null;
    private Spinner awayTeamSpinner = null;
    private TextView result = null;
    private String homeTeam = null;
    private String awayTeam = null;
    private HashMap<String, String> map = null;

    private HashMap<String, HashMap<String, Integer>> prediction = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        homeTeamSpinner = (Spinner) findViewById(R.id.spinner);
        awayTeamSpinner = (Spinner) findViewById(R.id.spinner2);
        result = (TextView) findViewById(R.id.textView3);

        homeTeamSpinner.setOnItemSelectedListener(this);
        awayTeamSpinner.setOnItemSelectedListener(this);

        map = new HashMap<>();
        prediction = new HashMap<>();
        map.put("Atlanta Hawks", "ATL");
        map.put("Boston Celtics", "BOS");
        map.put("Brooklyn Nets", "BKN");
        map.put("Charlotte Hornets", "CHA");
        map.put("Chicago Bulls", "CHI");
        map.put("Cleveland Cavaliers", "CLE");
        map.put("Dallas Mavericks", "DAL");
        map.put("Denver Nuggets", "DEN");
        map.put("Detroit Pistons", "DET");
        map.put("Golden State Warriors", "GSW");
        map.put("Houston Rockets", "HOU");
        map.put("Indiana Pacers", "IND");
        map.put("LA Clippers", "LAC");
        map.put("Los Angeles Lakers", "LAL");
        map.put("Memphis Grizzlies", "MEM");
        map.put("Miami Heat", "MIA");
        map.put("Milwaukee Bucks", "MIL");
        map.put("Minnesota Timberwolves", "MIN");
        map.put("New Orleans Pelicans", "NOP");
        map.put("New York Knicks", "NYK");
        map.put("Oklahoma City Thunder", "OKC");
        map.put("Orlando Magic", "ORL");
        map.put("Philadelphia 76ers", "PHI");
        map.put("Phoenix Suns", "PHX");
        map.put("Portland Trail Blazers", "POR");
        map.put("Sacramento Kings", "SAC");
        map.put("San Antonio Spurs", "SAS");
        map.put("Toronto Raptors", "TOR");
        map.put("Utah Jazz", "UTA");
        map.put("Washington Wizards", "WAS");
        String[] teams = new String[]{"ATL", "BKN", "BOS", "CHA", "CHI", "CLE", "DAL", "DEN", "DET", "GSW", "HOU", "IND", "LAC", "LAL", "MEM", "MIA",
                "MIL", "MIN", "NOP", "NYK", "OKC", "ORL", "PHI", "PHX", "POR", "SAC", "SAS", "TOR", "UTA", "WAS"};
        for (int i = 0; i < 30; i++) {
            prediction.put(teams[i], new HashMap<String, Integer>());
        }
        for (int i = 0; i < 30; i++) {
            for (int j = 0; j < 30; j++) {
                String HT = teams[i];
                String AT = teams[j];
                if (HT.equals(AT))
                    prediction.get(HT).put(HT, -1);
                else
                    prediction.get(HT).put(AT, (int) (Math.random() + 0.5)); //todo get the result from backend, not random value
            }
        }
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String content = parent.getItemAtPosition(position).toString();

        switch ((parent.getId())) {
            case R.id.spinner:
                //Toast.makeText(MainActivity.this,"Home team is:"+content,Toast.LENGTH_SHORT).show();
                homeTeam = content;
                homeTeam = map.get(homeTeam);
                break;
            case R.id.spinner2:
                // Toast.makeText(MainActivity.this,"Away team is:"+content,Toast.LENGTH_SHORT).show();
                awayTeam = content;
                awayTeam = map.get(awayTeam);
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public void getPrediction(View view) {
        int winOrLost = prediction.get(homeTeam).get(awayTeam);
        if (winOrLost == -1)
            result.setText(homeTeam + " can not play with " + awayTeam);
        else if (winOrLost == 0)
            result.setText(homeTeam + " will lose to " + awayTeam);
        else
            result.setText(homeTeam + " will beat " + awayTeam);
    }
}
