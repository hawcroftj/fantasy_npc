package hawcroftj.github.com.fantasynpc;

import android.content.Context;

import java.io.IOException;
import java.io.InputStream;

/**
 * Static class which provides Utility functions for FantasyNPC.
 */

public final class Utility {
    /**
     * Loads data from a json file located in the assets folder.
     * @param context The application context.
     * @param fileName The name of a json file that will be read.
     * @return String containing data read from json asset file.
     */
    public static String loadJSONFromAsset(Context context, String fileName) {
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

    /**
     * Loads race data from json asset files, making modifications to the selectedRace String
     * to match json file naming conventions. (See comments)
     * @param selectedRace The user-specified race to be used in character generation.
     * @return String containing race-specific information.
     */
    public static String loadRaceDataFromJSONSource(Context context, String selectedRace) {
        // due to the simple adapter used for racesAdapter, and the json file naming convention,
        // space characters (' ') must become a hyphen ('-') in addition to forcing lowercase.
        // (i.e. "Race Name" becomes "race-name")
        if(selectedRace.trim().contains(" ")) {
            selectedRace = selectedRace.trim().replace(" ", "-");
        }
        // race data is located in the assets/races/ directory, so it is added as a prefix to the race filename
        return Utility.loadJSONFromAsset(context, "races/" + selectedRace.toLowerCase());
    }

    /**
     * Removes 'dirty' json characters from Character information Strings.
     * @param string The String to be cleaned.
     * @return The clean String.
     */
    public static String cleanString(String string) {
        string = string.trim().replace("[", "")
                .replace("]", "").replace("{", "")
                .replace("}", "").replace("\"", "").replace(",", "")
                .replace("description:", "").replace("name:", "").replace("races:", "");
        return string;
    }

    /**
     * Removes 'dirty' json characters from Character information String arrays.
     * @param dirtyStringArr The String array to be cleaned.
     * @return The clean String array.
     */
    public static String[] cleanString(String[] dirtyStringArr) {
        String[] cleanedStringArr = new String[dirtyStringArr.length];
        // iterate through and clean each String in the dirty array
        for(int i = 0; i < dirtyStringArr.length; i++) {
            cleanedStringArr[i] = cleanString(dirtyStringArr[i]);
        }
        return cleanedStringArr;
    }
}
