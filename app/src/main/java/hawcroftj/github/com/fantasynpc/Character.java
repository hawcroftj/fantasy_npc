package hawcroftj.github.com.fantasynpc;

/**
 *
 */

public class Character {
    private String race;
    private String[] languages;
    private String[] traits;
    private int age;
    private int speed;

    public Character(String race, String[] languages, String[] traits, int age, int speed) {
        this.race = race;
        this.languages = languages;
        this.traits = traits;
        this.age = age;
        this.speed = speed;
    }

    public Character() {

    }

    public String getRace() {
        return race;
    }

    public void setRace(String race) {
        this.race = race;
    }

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
