import java.util.concurrent.ThreadLocalRandom;

public class Fighter extends Recruit {

    public Fighter( String name, int level ) {
        super(name,level);
        this.setStrength( this.getStrength()+5 );
        this.setMaxHitPoints(this.getMaxHitPoints() + 10); /* ThreadLocalRandom.current().nextInt(5, 10+1); */
        this.setDefence( this.getDefence()+5 );
        this.setHitPoints(this.getMaxHitPoints());
        this.setJob("Fighter");
    }

    public void special(){
        int diceRoll = ThreadLocalRandom.current().nextInt(1, 20+1);
        combat.special(4, diceRoll + this.getStrength());
    }

    public void levelUp(){
        this.setLevel( this.getLevel() + 1 );
        int i = ThreadLocalRandom.current().nextInt(10, 20+1);
        this.setMaxHitPoints(this.getMaxHitPoints() + i); /* ThreadLocalRandom.current().nextInt(5, 10+1); */
        this.setHitPoints(getHitPoints() + i);
        this.setStrength( this.getStrength() + ThreadLocalRandom.current().nextInt(5, 10+1) );
        this.setDefence( this.getDefence() + ThreadLocalRandom.current().nextInt(2, 10+1) );
    }

}
