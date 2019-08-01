import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Monster implements CombatConnector {

    private int maxHitPoints;
    private int hitPoints;
    private int strength;
    private int defence;
    private String name;
    private String type;
    public Combat combat;

    public Monster( String name, int health, int str, int def ) {       //  Podstawowe wartości dla potworów
        Random rand = new Random();
        this.maxHitPoints = health;
        this.hitPoints = maxHitPoints;
        this.strength = str;
        this.defence = def;
        this.name = name;
    }

    public void connectCombat( Combat com ){
        this.combat = com;
    }

    public int attack(){
        int diceRoll = ThreadLocalRandom.current().nextInt(1, 20+1);
        if( diceRoll == 20 )
            this.special();
        return diceRoll + this.getStrength();
    }

    public void special(){}

    public int getMaxHitPoints() {
        return maxHitPoints;
    }

    public void setMaxHitPoints(int maxHitPoints) {
        this.maxHitPoints = maxHitPoints;
    }

    public int getHitPoints() {
        return hitPoints;
    }

    public void setHitPoints(int hitPoints) {
        this.hitPoints = hitPoints;
    }

    public int getStrength() {
        return strength;
    }

    public void setStrength(int strength) {
        this.strength = strength;
    }

    public int getDefence() {
        return defence;
    }

    public void setDefence(int defence) {
        this.defence = defence;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
