package hawcroftj.github.com.fantasynpc;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
import java.util.Arrays;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    // class-level constants
    private static String TAG = "FantasyNPC_log";   // Logcat tag
    private static String DEFAULT_RACE = "random";  // default race

    //DatabaseHelper db;
    private Spinner spRaces;
    private Button btnRandom;

    private String[] availableRaces;
    private Random randomRaceSelector;
    private String selectedRace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // create new database
        //db = new DatabaseHelper(this);

        spRaces = findViewById(R.id.spRaces);
        btnRandom = findViewById(R.id.btnRandom);

        // load json data from asset files
        //String json = loadJSONFromAsset(getApplicationContext (), /* FILE_NAME /*);
    }

    @Override
    protected void onStart() {
        super.onStart();

        //region Retrieve Races and Set Defaults
        availableRaces = this.getResources().getStringArray(R.array.races_array);
        randomRaceSelector = new Random();
        selectedRace = DEFAULT_RACE;
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
    protected void onPostResume() {
        super.onPostResume();
        // return to random race when activity resumes
        selectedRace = DEFAULT_RACE;
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
        switch (v.getId()) {
            case R.id.btnRandom:
                if (selectedRace.toLowerCase().equals("random")) {
                    // select a random race from the list of available races
                    selectedRace = availableRaces[randomRaceSelector.nextInt(availableRaces.length - 1) + 1];
                }
                // generate a random character and display it's information in activity
                Character newCharacter = generateNewCharacter(selectedRace);
                Intent displayCharacter = new Intent(this, DisplayCharacter.class);
                displayCharacter.putExtra("character", newCharacter);
                startActivity(displayCharacter);
        }
    }

    /**
     * Generates a new character using data from asset files.
     * @return Character object containing relevant character information (race, class, etc.)
     */
    private Character generateNewCharacter(String selectedRace) {
        String race;
        String[] abilities, languages, traits;
        int speed, age;

        // TODO generate character from user selected race, class, etc.
        String raceData = loadRaceDataFromJSONSource(selectedRace);
        Character character = null;
        JSONObject object = null;
        try {
            // generate a JSON object from selected race data
            // extract information required to generate new character
            object = new JSONObject(raceData);
            race = object.get("name").toString().trim();
            abilities = object.get("ability_bonuses").toString().trim().split(",");
            languages = object.get("languages").toString().trim().split(",");
            traits = object.get("traits").toString().trim().split(",");
            speed = Integer.parseInt(object.get("speed").toString().trim());
            age = Integer.parseInt(object.get("age").toString().trim());
            // create the character
            character = new Character(race, abilities, languages, traits, age, speed);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return character;
    }

    /**
     * Loads race data from json asset files, making modifications to the selectedRace String
     * to match json file naming conventions. (See comments)
     * @param selectedRace The user-specified race to be used in character generation.
     * @return String containing race-specific information.
     */
    private String loadRaceDataFromJSONSource(String selectedRace) {
        // due to the simple adapter used for racesAdapter, and the json file naming convention,
        // space characters (' ') must become a hyphen ('-') in addition to forcing lowercase.
        // (i.e. "Hill Dwarf" becomes "hill-dwarf")
        if(selectedRace.trim().contains(" ")) {
            selectedRace = selectedRace.trim().replace(" ", "-");
        }
        // race data is located in the assets/races/ directory, so it is added as a prefix to the race filename
        return loadJSONFromAsset(getApplicationContext(), "races/" + selectedRace.toLowerCase());
    }

    /**
     * Loads data from a json file located in the assets folder.
     * @param context The application context.
     * @param fileName The name of a json file that will be read.
     * @return String containing data read from json asset file.
     */
    private String loadJSONFromAsset(Context context, String fileName) {
        String json = null;
        String fullFileNameWithType = fileName + ".json";
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
