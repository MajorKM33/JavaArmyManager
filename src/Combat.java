import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultCaret;
import javax.swing.text.Document;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Combat implements Runnable, FightObserved, CombatantsObserver{

    @Override
    public void run(){
        while( !ending ){                                                   //  WALKA
            for( int i = 0; i < army.size(); i++ ){
                refreshConnection(i);
                fight( army.get(i), monster );
                if( army.get(i).checkLevelUp() ){
                    logger( army.get(i).getName(), 7);
                    waitForConfirm = true;
                    gui.wantConfirm();
                    while ( waitForConfirm ){
                        gui.simulationSpeed(2);
                    }
                    gui.callRefresh();
                }
                gui.simulationSpeed(1);
                if( specialEffect != 0 ){
                    checkSpecialEffect( i );
                }
                if( monster.getHitPoints() <= 0 ){
                    ending = true;
                    logger( monster.getName(), 6);
                    gui.winScenario();
                    break;
                }
                while ( waitForConfirm ){
                    gui.simulationSpeed(2);
                }
                fight( monster, army.get(i) );
                gui.simulationSpeed(1);
                if( specialEffect != 0 ){
                    checkSpecialEffect( i );
                }
                if( army.get(i).getHitPoints() <= 0 ){
                    gui.callRefresh();
                    logger( army.get(i).getName(), 4);
                    army.remove(i);
                    i--;
                    waitForConfirm = true;
                    gui.wantConfirm();
                    while ( waitForConfirm ){
                        gui.simulationSpeed(2);
                    }
                }
                gui.callRefresh();
                if( army.size() == 0 ){
                    ending = true;
                    logger(5);
                    gui.callClear();
                    gui.loseScenario();
                    break;
                }
                while ( waitForConfirm ){
                    gui.simulationSpeed(2);
                }
            }
        }
    }

    public Combat( ArrayList<Recruit> army, Monster monster, JEditorPane combatLog, GUI gui ) {
        Random rand = new Random();
        //ThreadLocalRandom.current().nextInt(-5, 5 + 1); alternatywna metoda losowania
        this.gui = gui;
        gui.connectCombat( this );
        this.army = army;
        for( int i = 0; i < army.size(); i++ ){
            army.get(i).connectCombat(this);
        }
        this.monster = monster;
        this.monster.connectCombat(this);
        this.combatLog = combatLog;
        doc = combatLog.getDocument();
        this.combatX = this;

        DefaultCaret caret = (DefaultCaret)combatLog.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        combatLog.setText("<html>Następnym przeciwnikiem jest <b style='color:red;'>" + this.monster.getName() + "!</b></html>");
        try {
            doc.insertString(doc.getLength(), "\n", null);
        } catch (BadLocationException e){}
    }

    private ArrayList<Recruit> army = null;
    private Monster monster;
    private JEditorPane combatLog;
    private boolean ending = false;
    private int attPower;
    private int defPower;
    private int dmgDealt;
    private String tmpStr;
    private Document doc;
    private FightObserver gui;
    private boolean waitForConfirm = false;
    private int specialEffect = 0;
    private int specialValue = 0;
    private Combat combatX;

    public void continuePressed(){
        waitForConfirm = false;
    }               //  Funkcje z interfejsów

    public void pausePressed(){
        waitForConfirm = true;
        gui.wantConfirm();
    }

    public void healPressed(){ logger(8); }

    private void refreshConnection( int i ){
            army.get(i).connectCombat(combatX);
    }

    private void fight( Recruit ally, Monster enemy ){                           //  ATAK    Zołnierz -> Potwór
        attPower = ally.attack();
        defPower = monster.getDefence();
        if( attPower > defPower ){
            dmgDealt = attPower - defPower;
            monster.setHitPoints( monster.getHitPoints() - dmgDealt );
            ally.setExp( ally.getExp() + (attPower) );
            logger( ally.getName(), monster.getName(), 1, dmgDealt );
        }
        else{
            ally.setExp( ally.getExp() + attPower/2 );
            logger( ally.getName(), 3 );
        }
    }

    private void fight( Monster enemy, Recruit ally ){                           //  ATAK    Potwór -> Zołnierz
        attPower = monster.attack();
        defPower = ally.getDefence();
        if( attPower > defPower ){
            dmgDealt = attPower - defPower;
            ally.setHitPoints( ally.getHitPoints() - dmgDealt );
            logger( monster.getName(), ally.getName(), 1, dmgDealt );
        }
        else{
            logger( monster.getName(), 3 );
        }
    }

    public void special( int effect, int value ){                                 // SKILLE KRYTYCZNE
                specialEffect = effect;
                specialValue = value;
    }

    private void checkSpecialEffect( int i ){
        switch (specialEffect){
            case 1:
                for( int y = 0; y < army.size(); y++ ){
                    army.get(y).setHitPoints( army.get(y).getHitPoints() - specialValue );
                }
                logger( monster.getName(), "", 9, specialValue);
                break;
            case 2:
                army.get(i).setDefence( army.get(i).getDefence() - specialValue );
                logger( monster.getName(), army.get(i).getName(), 10, specialValue );
                break;
            case 3:
                army.get(i).setHitPoints( army.get(i).getHitPoints() - specialValue );
                logger( monster.getName(), army.get(i).getName(), 11, specialValue );
                break;
            case 4:
                monster.setHitPoints( monster.getHitPoints() - specialValue);
                logger( army.get(i).getName(), monster.getName(), 12, specialValue );
                break;
            case 5:
                monster.setDefence( monster.getDefence() - specialValue);
                logger( army.get(i).getName(), monster.getName(), 13, specialValue );
                break;
            case 6:
                for( int y = 0; y < army.size(); y++ ){
                    army.get(y).setHitPoints( army.get(y).getHitPoints() + specialValue );
                }
                logger( army.get(i).getName(), monster.getName(), 12, specialValue );
                break;
        }
        specialEffect = 0;
        specialValue = 0;
        gui.callRefresh();
        gui.simulationSpeed(1);
    }

    private void logger( String c1, int stat ){                          //  WYPISYWANIE ZDARZEN
        switch (stat){
            case 3:             // MISS
                tmpStr = c1 + " chybia.\n";
                break;
            case 4:             // Death
                tmpStr = c1 + " ginie w walce!\n";
                break;
            case 6:             // Death of Monster
                tmpStr = c1 + " ginie! Zwycięstwo!\n";
                break;
            case 7:             // Level Up
                tmpStr = c1 + " podnosi swoje umiejętności!\n";
                break;
        }
        printMsg( tmpStr );
    }

    private void logger( String c1, String c2, int stat, int dmg ){
        switch(stat){
            case 1:             //  DAMAGE
                tmpStr = c1 + " trafia " + c2 + " za " + dmg + " obrażeń.\n";
                break;
            case 9:             //  Dragonfire
                tmpStr = c1 + " zionie ogniem! Wszyscy otrzymują " + dmg + " obrażeń!\n";
                break;
            case 10:             //  Demon's Fright
                tmpStr = c1 + " jest przerażający! " + c2 + " traci ochronę!\n";
                break;
            case 11:             //  Lich's Blight
                tmpStr = c1 + " wysysa z " + c2 + " " + dmg + " punktów zdrowia!\n";
                break;
            case 12:             //  Fighter's Strike
                tmpStr = c1 + " atakuje krytycznie i zadaje " + dmg + " obrażeń!\n";
                break;
            case 13:             //  Ranger's Shot
                tmpStr = c1 + " celnym strzałem kruszy pancerz!\n";
                break;
            case 14:             //  Mage's Bless
                tmpStr = c1 + " leczy sojuszników za " + dmg + " punktów zdrowia!\n";
                break;
        }
        printMsg( tmpStr );
    }

    private void logger( int stat ){
        switch (stat){
        case 5:             //  TOTAL PARTY KILL
            tmpStr = "Wszyscy nie żyją...\n";
            break;
        case 8:             //  HEAL
            tmpStr = "Armia wyleczona!\n";
            break;
        }
        printMsg( tmpStr );
    }

    private void printMsg( String tmp ){
        try {
            doc.insertString(doc.getLength(), tmp, null);
        } catch (BadLocationException e){
            System.out.println(e);
        }
    }

}
