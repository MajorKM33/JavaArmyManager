import java.util.ArrayList;

public class SavedDataFile {

    private ArrayList<Recruit> army = new ArrayList<Recruit>();
    private int gold;
    private int monstersDefeated;

    public SavedDataFile(ArrayList<Recruit> army, int gold, int monstersDefeated) {
        this.army = army;
        this.gold = gold;
        this.monstersDefeated = monstersDefeated;
    }

    public ArrayList<Recruit> getArmy() {
        return army;
    }

    public int getGold() {
        return gold;
    }

    public int getMonstersDefeated() {
        return monstersDefeated;
    }
}
