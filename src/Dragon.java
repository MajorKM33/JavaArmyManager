import java.util.concurrent.ThreadLocalRandom;

public class Dragon extends Monster {

    public Dragon(String name, int health, int str, int def) {
        super(name, health, str, def);
        this.setType("Dragon");
    }

    public void special(){
        int diceRoll = ThreadLocalRandom.current().nextInt(1, 20+1);
        combat.special(1,diceRoll);
    }

}
