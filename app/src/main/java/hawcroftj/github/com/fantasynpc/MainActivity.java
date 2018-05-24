package hawcroftj.github.com.fantasynpc;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    //DatabaseHelper db;
    TextView tvRace, tvSpeed, tvAge, tvLanguages, tvTraits;
    Spinner spRaces;
    Button btnRandom;

    String selectedRace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // create new database
        //db = new DatabaseHelper(this);

        //region Initialize UI Elements
        tvRace = findViewById(R.id.tvRace);
        tvSpeed = findViewById(R.id.tvSpeed);
        tvAge = findViewById(R.id.tvAge);
        tvLanguages = findViewById(R.id.tvLanguages);
        tvTraits = findViewById(R.id.tvTraits);

        spRaces = findViewById(R.id.spRaces);
        btnRandom = findViewById(R.id.btnRandom);
        //endregion Initialize UI Elements

        // load json data from asset files
        //String json = loadJSONFromAsset(getApplicationContext(), /* FILE_NAME /*);
    }

    @Override
    protected void onStart() {
        super.onStart();

        //region Set Defaults
        selectedRace = "random";
        //endregion Set Defaults

        // further preparation for UI elements
        btnRandom.setOnClickListener(this);
        spRaces.setOnItemSelectedListener(this);
        // create adapter for race selection spinner
        ArrayAdapter<CharSequence> racesAdapter = ArrayAdapter.createFromResource(
                this,
                R.array.races_array,
                android.R.layout.simple_spinner_item);
        racesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spRaces.setAdapter(racesAdapter);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // alter selected race based on user input
        selectedRace = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        Toast.makeText(this, "Please select a race.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.btnRandom:
                // generate a random character and display it's information in activity
                Character newCharacter = generateNewCharacter(selectedRace.toLowerCase());
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

    /**
     *
     * @return
     */
    private Character generateNewCharacter(String selectedRace) {
        String race;
        String[] languages, traits;
        int speed, age;

        // TODO generate character from user selected race, class, etc.
        String raceData = loadJSONFromAsset(getApplicationContext(), selectedRace);
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

    /**
     *
     * @param context
     * @param fileName
     * @return
     */
    private String loadJSONFromAsset(Context context, String fileName) {
        String json = null;
        String fullFileNameWithType = "races/" + fileName + ".json";
        try {
            // open specified json asset file and fill buffer with data
            InputStream stream = context.getAssets().open(fullFileNameWithType);
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
