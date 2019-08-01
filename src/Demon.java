import java.util.concurrent.ThreadLocalRandom;

public class Demon extends Monster {

    public Demon(String name, int health, int str, int def) {
        super(name, health, str, def);
        this.setType("Demon");
    }

    public void special(){
        int diceRoll = ThreadLocalRandom.current().nextInt(1, 10+1);
        combat.special(2,diceRoll + (this.getStrength()/2));
    }
}
