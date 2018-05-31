package hawcroftj.github.com.fantasynpc;

import android.content.Intent;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,
        AdapterView.OnItemSelectedListener,
        RadioGroup.OnCheckedChangeListener {

    //region Constants and Class Variables
    // class-level constants
    private static final String TAG = "FantasyNPC_log";             // Logcat tag
    private static final String DEFAULT_RANDOM = "Random";          // default (random)
    private static final String SEX_FEMALE = "Female";
    private static final String SEX_MALE = "Male";
    private static final String[] SEXES = {SEX_FEMALE, SEX_MALE};   // array of sexes

    //DatabaseHelper db;
    private Spinner spRaces;
    private Button btnRandom;
    private RadioGroup rgSexGroup;
    private RadioButton rbSexRandom;
    private RadioButton rbSexFemale;
    private RadioButton rbSexMale;

    private String[] availableRaces;
    private Random randomRaceSelector;
    private Random randomNameSelector;
    private Random randomAgeSelector;
    private Random randomSexSelector;
    private String selectedRace;
    private String selectedSex;
    //endregion Constants and Class Variables

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // create new database
        //db = new DatabaseHelper(this);

        spRaces = findViewById(R.id.spRaces);
        btnRandom = findViewById(R.id.btnRandom);
        rgSexGroup = findViewById(R.id.rgSexGroup);
        rbSexRandom = findViewById(R.id.rbSexRandom);
        rbSexFemale = findViewById(R.id.rbSexFemale);
        rbSexMale = findViewById(R.id.rbSexMale);

        // load json data from asset files
        //String json = loadJSONFromAsset(getApplicationContext (), /* FILE_NAME /*);
    }

    @Override
    protected void onStart() {
        super.onStart();

        //region Retrieve Races and Set Defaults
        availableRaces = this.getResources().getStringArray(R.array.races_array);
        randomRaceSelector = new Random();
        randomNameSelector = new Random();
        randomAgeSelector = new Random();
        randomSexSelector = new Random();
        selectedRace = DEFAULT_RANDOM;              // default race
        selectedSex = DEFAULT_RANDOM;               // default sex
        rgSexGroup.check(rbSexRandom.getId());      // default sex checked
        //endregion Set Defaults

        // further preparation for UI elements
        btnRandom.setOnClickListener(this);
        spRaces.setOnItemSelectedListener(this);
        rgSexGroup.setOnCheckedChangeListener(this);

        // create adapter for race selection spinner
        ArrayAdapter<CharSequence> racesAdapter = ArrayAdapter.createFromResource(
                this,
                R.array.races_array,
                android.R.layout.simple_spinner_item);
        racesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spRaces.setAdapter(racesAdapter);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        // return to defaults when activity resumes
        selectedRace = DEFAULT_RANDOM;
        selectedRace = DEFAULT_RANDOM;
        rgSexGroup.check(rbSexRandom.getId());
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
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        // assign user-selected sex to selectedSex
        switch(checkedId) {
            case R.id.rbSexRandom:
                selectedSex = DEFAULT_RANDOM;
                break;
            case R.id.rbSexFemale:
                selectedSex = SEX_FEMALE;
                break;
            case R.id.rbSexMale:
                selectedSex = SEX_MALE;
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.btnRandom:
                if(selectedRace.equals(DEFAULT_RANDOM)) {   // choose a random race
                    selectedRace = availableRaces[randomRaceSelector.nextInt(availableRaces.length - 1) + 1];
                }
                if(selectedSex.equals(DEFAULT_RANDOM)) {    // choose a random sex
                    selectedSex = SEXES[randomSexSelector.nextInt(2)];
                }
                // generate a random character using selected parameters
                Character newCharacter = generateNewCharacter(selectedRace, selectedSex);
                Intent displayCharacter = new Intent(this, DisplayCharacter.class);
                displayCharacter.putExtra("character", newCharacter);
                startActivity(displayCharacter);
        }
    }

    /**
     * Generates a new character using data from asset files.
     * @param selectedRace The new Character race.
     * @param selectedSex The new Character sex.
     * @return Character object containing relevant character information (race, class, etc.)
     */
    private Character generateNewCharacter(String selectedRace, String selectedSex) {
        String race;
        String[] firstNames, lastNames, abilities, languages, traits;
        int speed, maxAge, age;

        // TODO generate character from user selected race, class, etc.
        String raceData = Utility.loadRaceDataFromJSONSource(getApplicationContext(), selectedRace);
        Character character = null;
        JSONObject object = null;
        try {
            // generate a JSON object from selected race data
            // extract information required to generate new character
            object = new JSONObject(raceData);

            race = Utility.cleanString(object.get("name").toString());
            speed = Integer.parseInt(object.get("speed").toString().trim());

            maxAge = Integer.parseInt(object.get("age").toString());
            age = randomAgeSelector.nextInt((maxAge - 25) + 15);    // generate age between 15 and maxAge - 10

            abilities = Utility.cleanString(object.get("ability_bonuses").toString().split(","));
            languages = Utility.cleanString(object.get("languages").toString().split(","));
            traits = Utility.cleanString(object.get("traits").toString().split(","));

            // select a random sex-specific name from first and last name String arrays
            firstNames = Utility.cleanString(object.get("first_names_" + selectedSex.toLowerCase()).toString().split(","));
            lastNames = Utility.cleanString(object.get("last_names").toString().split(","));
            String[] characterName = createCharacterName(firstNames, lastNames);

            // create the character
            character = new Character(race, selectedSex, characterName[0], characterName[1], abilities, languages, traits, age, speed);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return character;
    }

    /**
     * Selects a first and last name for a new Character from a list of race-specific names.
     * @param firstNames The list of available race-specific first names.
     * @param lastNames The list of available race-specific first names.
     * @return String array containing a first and last name.
     */
    private String[] createCharacterName(String[] firstNames, String[] lastNames) {
        String firstName, lastName;
        // select a random first and last name from available names
        firstName = firstNames[randomNameSelector.nextInt(firstNames.length)];
        lastName = lastNames[randomNameSelector.nextInt(lastNames.length)];

        return new String[]{firstName, lastName};
    }
}
