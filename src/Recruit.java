import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Recruit implements CombatConnector {

    private int maxHitPoints;
    private int hitPoints;
    private int strength;
    private int defence;
    private int level;
    private int exp;
    private String name;
    private String job;
    public transient Combat combat;

    public Recruit( String name, int level ) {                          //  Podstawowa klasa
        Random rand = new Random();
        this.maxHitPoints = rand.nextInt(25)+10;
        this.hitPoints = maxHitPoints;
        this.strength = rand.nextInt(12)+5;
        this.defence = rand.nextInt(8)+3;
        this.level = 1;
        this.exp = 0;
        this.name = name;
        while( level > 1 ){         //  jeśli rekrutujemy żołnierza na wyższym levelu, system automatycznie go podnosi
            levelUp();
            level--;
        }
    }

    public int attack(){                         //  ATAK
        int diceRoll = ThreadLocalRandom.current().nextInt(1, 20+1);
        if( diceRoll == 20 )
            this.special();
        return diceRoll + this.getStrength();
    }

    public void special(){}                                              //  SPECJALNY SKILL

    public void connectCombat( Combat com ){
        this.combat = com;
    }

    public boolean checkLevelUp(){                                        //   1000 punktów = nowy poziom
        while( this.exp >= this.level*100 ){
            this.exp -= this.level*100;
            levelUp();
            return true;
        }
        return false;
    }

    public void levelUp(){                                                              //  FUNKCJA LEVELUJĄCA
        this.setLevel( this.getLevel() + 1 );
        this.setStrength( this.getStrength() + ThreadLocalRandom.current().nextInt(5, 10+1));
        System.out.println( this.getLevel() );
    }

    public int getMaxHitPoints() {
        return maxHitPoints;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
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

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getExp() {
        return exp;
    }

    public void setExp(int exp) {
        this.exp = exp;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
