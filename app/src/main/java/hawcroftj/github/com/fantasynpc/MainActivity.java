package hawcroftj.github.com.fantasynpc;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //DatabaseHelper db;
    TextView tvRace, tvSpeed, tvAge, tvLanguages, tvTraits;
    Button btnRandom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // create new database
        //db = new DatabaseHelper(this);

        // initialize
        tvRace = findViewById(R.id.tvRace);
        tvSpeed = findViewById(R.id.tvSpeed);
        tvAge = findViewById(R.id.tvAge);
        tvLanguages = findViewById(R.id.tvLanguages);
        tvTraits = findViewById(R.id.tvTraits);

        btnRandom = findViewById(R.id.btnRandom);
        btnRandom.setOnClickListener(this);

        // load json data from asset files
        //String json = loadJSONFromAsset(getApplicationContext(), /* FILE_NAME /*);
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.btnRandom:
                // generate a random character and display it's information in activity
                Character newCharacter = generateNewCharacter();
                displayCharacterInfo(newCharacter);
                break;
        }
    }

    /**
     * Displays
     * @param newCharacter
     */
    private void displayCharacterInfo(Character newCharacter) {
        tvRace.setText(String.format(
                "Race: %s", newCharacter.getRace()));
        tvSpeed.setText(String.format(
                "Speed: %s", String.valueOf(newCharacter.getSpeed())));
        tvAge.setText(String.format(
                "Age: %s", String.valueOf(newCharacter.getAge())));
        tvLanguages.setText(String.format(
                "Languages: %s", Arrays.toString(newCharacter.getLanguages())));
        tvTraits.setText(String.format(
                "Traits: %s", Arrays.toString(newCharacter.getTraits())));
    }

    private Character generateNewCharacter() {
        String race;
        String[] languages, traits;
        int speed, age;

        // TODO generate character from user selected race, class, etc.
        String raceData = loadJSONFromAsset(getApplicationContext(), "human.json");
        Character character = null;
        JSONObject object = null;
        try {
            // generate a JSON object from selected race data
            // extract information required to generate new character
            object = new JSONObject(raceData);
            race = object.get("name").toString().trim();
            languages = object.get("languages").toString().trim().split(",");
            traits = object.get("traits").toString().trim().split(",");
            speed = Integer.parseInt(object.get("speed").toString().trim());
            age = Integer.parseInt(object.get("age").toString().trim());
            // create the character
            character = new Character(race, languages, traits, age, speed);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return character;
    }

    private String loadJSONFromAsset(Context context, String fileName) {
        String json = null;
        try {
            // open specified json asset file and fill buffer with data
            InputStream stream = context.getAssets().open("races/" + fileName);
            byte[] buffer = new byte[stream.available()];
            stream.read(buffer);
            stream.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return json;
    }
}
