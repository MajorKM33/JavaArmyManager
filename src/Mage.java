import java.util.concurrent.ThreadLocalRandom;

public class Mage extends Recruit {

    public Mage( String name, int level ) {
        super(name,level);
        this.setStrength( this.getStrength()+15 );
        this.setDefence( this.getDefence()+5 );
        this.setJob("Mage");
    }

    public void special(){
        int diceRoll = ThreadLocalRandom.current().nextInt(5, 20+1);
        combat.special(6, diceRoll + this.getStrength());
    }

    public void levelUp(){
        this.setLevel( this.getLevel() + 1 );
        int i = ThreadLocalRandom.current().nextInt(5, 10+1);
        this.setMaxHitPoints(this.getMaxHitPoints() + i); /* ThreadLocalRandom.current().nextInt(5, 10+1); */
        this.setHitPoints(getHitPoints() + i);
        this.setStrength( this.getStrength() + ThreadLocalRandom.current().nextInt(10, 20+1) );
        this.setDefence( this.getDefence() + ThreadLocalRandom.current().nextInt(2, 5+1) );
    }

}
