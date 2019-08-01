import java.util.concurrent.ThreadLocalRandom;

public class Ranger extends Recruit {

    public Ranger( String name, int level ) {
        super(name,level);
        this.setDefence( this.getDefence()+10 );
        this.setMaxHitPoints(this.getMaxHitPoints() + 5); /* ThreadLocalRandom.current().nextInt(5, 10+1); */
        this.setHitPoints(this.getMaxHitPoints());
        this.setJob("Ranger");
    }

    public void special(){
        int diceRoll = ThreadLocalRandom.current().nextInt(1, 5+1);
        combat.special(5, diceRoll );
    }

    public void levelUp(){
        this.setLevel( this.getLevel() + 1 );
        int i = ThreadLocalRandom.current().nextInt(5, 10+1);
        this.setMaxHitPoints(this.getMaxHitPoints() + i); /* ThreadLocalRandom.current().nextInt(5, 10+1); */
        this.setHitPoints(getHitPoints() + i);
        this.setStrength( this.getStrength() + ThreadLocalRandom.current().nextInt(10, 15+1) );
        this.setDefence( this.getDefence() + ThreadLocalRandom.current().nextInt(2, 8+1) );
    }

}
