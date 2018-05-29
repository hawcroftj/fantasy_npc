package hawcroftj.github.com.fantasynpc;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Arrays;

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
        tvName.setText(String.format(
                "Name: %s %s", character.getFirstName(), character.getLastName()));
        tvRace.setText(String.format(
                "Race: %s", character.getRace()));
        tvSex.setText(String.format(
                "Sex: %s", character.getSex()));
        tvAbilities.setText(String.format(
                "Abilities: %s", Arrays.toString(character.getAbilities())));
        tvSpeed.setText(String.format(
                "Speed: %s", String.valueOf(character.getSpeed())));
        tvAge.setText(String.format(
                "Age: %s", String.valueOf(character.getAge())));
        tvLanguages.setText(String.format(
                "Languages: %s", Arrays.toString(character.getLanguages())));
        tvTraits.setText(String.format(
                "Traits: %s", Arrays.toString(character.getTraits())));
    }
}
