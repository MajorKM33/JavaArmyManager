import java.util.concurrent.ThreadLocalRandom;

public class Lich extends Monster {

    public Lich(String name, int health, int str, int def) {
        super(name, health, str, def);
        this.setType("Lich");
    }

    public void special(){
        int diceRoll = ThreadLocalRandom.current().nextInt(1, 20+1);
        this.setHitPoints( this.getHitPoints() + diceRoll + (this.getStrength()/2) );
        combat.special(3, diceRoll + (this.getStrength()/2));
    }
}
