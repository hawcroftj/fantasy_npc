package hawcroftj.github.com.fantasynpc;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Arrays;
import java.util.Random;

public class DisplayCharacter extends AppCompatActivity {

    private TextView tvRace, tvSex, tvName, tvAbilities, tvSpeed, tvAge, tvLanguages, tvTraits;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_character);

        // initialize UI elements
        tvRace = findViewById(R.id.tvRace);
        tvSex = findViewById(R.id.tvSex);
        tvName = findViewById(R.id.tvName);
        tvAbilities = findViewById(R.id.tvAbilities);
        tvSpeed = findViewById(R.id.tvSpeed);
        tvAge = findViewById(R.id.tvAge);
        tvLanguages = findViewById(R.id.tvLanguages);
        tvTraits = findViewById(R.id.tvTraits);

        // receive and display Character information
        Character character = getIntent().getParcelableExtra("character");
        displayCharacterInfo(character);
    }

    /**
     * Populates TextView elements with character data.
     */
    private void displayCharacterInfo(Character character) {
         //set TextView text to the appropriate Character info
        tvName.setText(String.format("%s %s", character.getFirstName(), character.getLastName()));
        tvAge.setText(String.format("%s year old ", String.valueOf(character.getAge())));
        tvSex.setText(String.format("%s ", character.getSex().toLowerCase()));
        tvRace.setText(String.format("%s", character.getRace()));
        tvSpeed.setText(String.format("%s", String.valueOf(character.getSpeed())));

        // format Character abilities be displayed
        String[] abilities = character.getAbilities();
        String[] abilityNames = {"STR:", "DEX:", "CON:", "INT:", "WIS:", "CHA:"};
        StringBuilder abilitiesString = new StringBuilder();
        for(int i = 0; i < abilities.length; i++) {
            abilitiesString.append(String.format("%s %s ", abilityNames[i], abilities[i]));
        }
        tvAbilities.setText(abilitiesString.toString());
        tvLanguages.setText(formatArrayForText(character.getLanguages()));
        tvTraits.setText(formatArrayForText(character.getTraits()));
    }

    private String formatArrayForText(String[] stringArr) {
        StringBuilder builder = new StringBuilder();
        for(int i = 0; i < stringArr.length; i++) {
            builder.append(stringArr[i] + ",\n");
        }
        return builder.toString();
    }
}
