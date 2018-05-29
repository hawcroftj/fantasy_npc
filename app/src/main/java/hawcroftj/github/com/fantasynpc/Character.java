package hawcroftj.github.com.fantasynpc;

import android.os.Parcel;
import android.os.Parcelable;

/**
 *
 */

public class Character implements Parcelable {
    private String race;
    private String[] abilities;
    private String[] languages;
    private String[] traits;
    private int age;
    private int speed;

    public Character(String race, String[] abilities, String[] languages, String[] traits, int age, int speed) {
        this.race = race;
        this.abilities = abilities;
        this.languages = languages;
        this.traits = traits;
        this.age = age;
        this.speed = speed;
    }

    public Character() {

    }

    //region Parcelable Implementation
    protected Character(Parcel in) {
        race = in.readString();
        abilities = in.createStringArray();
        languages = in.createStringArray();
        traits = in.createStringArray();
        age = in.readInt();
        speed = in.readInt();
    }

    // parcelable write
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(race);
        dest.writeStringArray(abilities);
        dest.writeStringArray(languages);
        dest.writeStringArray(traits);
        dest.writeInt(age);
        dest.writeInt(speed);
    }

    // parcelable describe
    @Override
    public int describeContents() {
        return 0;
    }

    // parcelable creator
    public static final Creator<Character> CREATOR = new Creator<Character>() {
        @Override
        public Character createFromParcel(Parcel in) {
            return new Character(in);
        }

        @Override
        public Character[] newArray(int size) {
            return new Character[size];
        }
    };
    //endregion Parcelable Implementation

    public String getRace() {
        return race;
    }

    public void setRace(String race) {
        this.race = race;
    }

    public String[] getAbilities() { return abilities; }

    public void setAbilities(String[] abilities) { this.abilities = abilities; }

    public String[] getLanguages() {
        return languages;
    }

    public void setLanguages(String[] languages) {
        this.languages = languages;
    }

    public String[] getTraits() {
        return traits;
    }

    public void setTraits(String[] traits) {
        this.traits = traits;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }
}
