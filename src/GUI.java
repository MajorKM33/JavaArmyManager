import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Scanner;

public class GUI implements FightObserver{
    private JRadioButton radioButton1;
    private JRadioButton radioButton2;
    private JRadioButton radioButton3;
    private JButton recruitButton;
    private JPanel panel1;
    private JTextField nameField;
    private JEditorPane combatLog;
    private JButton callToArmsButton;
    private JScrollPane armyList;
    private JPanel armyListViewer;
    private JSpinner spinner1;
    private JPanel recPanel;
    private JLabel goldCounter;
    private JButton healButton;
    private JSlider slider1;
    private JButton saveGameButton;
    private JButton loadGameButton;
    private JLabel monstersCounter;
    private JButton pauseButton;
    private GUI guiX;
    private Combat combatX;

    private ArrayList<Recruit> army = new ArrayList<Recruit>();                             //  Lista żołnierzy

    GlobalSettings gs = new GlobalSettings();                           // GlobalSettings - klasa na często używane zmienne
    MonsterManual mm = new MonsterManual();
    private ArrayList<Monster> monstersList = mm.getMonstersList();
    private int gold = gs.getGold();
    private int monstersDefeated = 0;
    private int currentState = 0;           // 0 - przed walką, 1 - w trakcie walki, 2 - oczekiwanie na kontynuację

    private void addSoldier( String name, int job ) throws GoldException{                            //  Zwerbowanie żołnierza
        int chosenLvl = Integer.parseInt( spinner1.getValue().toString() );
        switch (job){
            case 1:
                if(chosenLvl*gs.getFighterCost() > gold){
                    throw new GoldException();
                }else {
                    army.add(new Fighter(name, chosenLvl));
                    gold = gold - chosenLvl * gs.getFighterCost();
                    break;
                }
            case 2:
                if(chosenLvl*gs.getRangerCost() > gold){
                    throw new GoldException();
                }else {
                    army.add(new Ranger(name, chosenLvl));
                    gold = gold - chosenLvl*gs.getRangerCost();
                    break;
                }
            case 3:
                if(chosenLvl*gs.getMageCost() > gold){
                    throw new GoldException();
                }else {
                    army.add(new Mage(name, chosenLvl));
                    gold = gold - chosenLvl*gs.getMageCost();
                    break;
                }
        }
        refreshArmyList();
        refreshCounters(chosenLvl);
    }

    private void refreshArmyList(){                     //  Odświeżenie listy żołnierzy
        armyListViewer.removeAll();

        armyListViewer.setLayout(new BoxLayout(armyListViewer, BoxLayout.Y_AXIS));

        for( int i = 0; i < army.size(); i++ ){
            JEditorPane soldier = new JEditorPane();
            soldier.setText(army.get(i).getName() + " \n" + army.get(i).getJob() + " lvl." + army.get(i).getLevel()
                    + "        HP: " + army.get(i).getHitPoints() + "/" + army.get(i).getMaxHitPoints() +
                    "      A:" + army.get(i).getStrength() + "   D:" + army.get(i).getDefence());
            soldier.setText(soldier.getText() + "\n______________________________________");
            soldier.setSize(200,50);
            soldier.setBounds(0,0,200, 5);
            soldier.setAlignmentX(Component.LEFT_ALIGNMENT);
            armyListViewer.add(soldier);
        }
        armyListViewer.revalidate();
    }

    public void callClear(){
        armyListViewer.removeAll();
        armyListViewer.revalidate();
    }

    private void refreshCounters(int chosenLvl){            //  Odświeżenie liczb zwiazanych z ekonomią
        goldCounter.setText("Gold: " + gold);
        radioButton1.setText("Fighter (" + (chosenLvl*gs.getFighterCost()) + ")");
        radioButton2.setText("Ranger (" + (chosenLvl*gs.getRangerCost()) + ")");
        radioButton3.setText("Mage (" + (chosenLvl*gs.getMageCost()) + ")");
        healButton.setText(" Heal (" + healCost() + ")");
        monstersCounter.setText("Monsters slayed: " + monstersDefeated);
        recPanel.revalidate();
    }

    private int healCost(){
        int hc = 0;
        for( int i = 0; i < army.size(); i++ ){
            hc += army.get(i).getMaxHitPoints() - army.get(i).getHitPoints();
        }
        return hc;
    }

    public void simulationSpeed(int i) {                        //  Wstrzymywanie walki
        int speed = 0;
        if(i == 1) {
            speed = 1000 - (slider1.getValue() * 250);
        }else if(i == 2){
            speed = 100;
        }
        try {
            Thread.sleep(speed);
        } catch (InterruptedException e) {
            System.out.println(e);
        }
    }

    public void winScenario(){
        monstersDefeated++;
        gold += (500*monstersDefeated);
        refreshCounters(Integer.parseInt( spinner1.getValue().toString()));
        callToArmsButton.setText("Call to arms!");
        if( monstersDefeated < monstersList.size() ){
            callToArmsButton.setEnabled(true);
            saveGameButton.setEnabled(true);
            loadGameButton.setEnabled(true);
            currentState = 0;
        }
        else{
            JFrame frame = new JFrame("Zwycięstwo!");
            JOptionPane.showMessageDialog(frame, "Ukończyłeś grę!");
        }
    }

    public void loseScenario(){
        healButton.setText(" Heal (0)");
        callToArmsButton.setText("Call to arms!");
        callToArmsButton.setEnabled(true);
        saveGameButton.setEnabled(true);
        loadGameButton.setEnabled(true);
        currentState = 0;
    }

    public void wantConfirm(){
        currentState = 2;
        callToArmsButton.setText("Continue");
        pauseButton.setText("Resume");
        callToArmsButton.setEnabled(true);
    }

    public void connectCombat( Combat combat ){
        combatX = combat;
    }

    public void callRefresh(){
        refreshArmyList();
        refreshCounters( Integer.parseInt( spinner1.getValue().toString()) );
    }

    private GUI() {

        Monster testMonster = new Dragon("TestDragon", 100, 10, 10);
        spinner1.setValue(1);
        spinner1.updateUI();
        refreshCounters(1);
        guiX = this;

        recruitButton.addActionListener(new ActionListener() {              //  Przycisk zwerbowania nowego żołnierza
            @Override
            public void actionPerformed(ActionEvent e) {
                if( !nameField.getText().equals("") ){
                    int selectedJob = 0;
                    if( radioButton1.isSelected() ){
                        selectedJob = 1;
                    }
                    if( radioButton2.isSelected() ){
                        selectedJob = 2;
                    }
                    if( radioButton3.isSelected() ){
                        selectedJob = 3;
                    }
                    if( selectedJob != 0 ) {
                        try {
                            addSoldier(nameField.getText(), selectedJob);
                        } catch (GoldException e1) {
                            JFrame frame = new JFrame("The treasury lacks profit!");
                            JOptionPane.showMessageDialog(frame, "Not enough gold to recruit this man.");
                        }
                    }
                }
            }
        });

        callToArmsButton.addActionListener(new ActionListener() {       //  Przycisk wysłania żołnierzy do walki
            @Override
            public void actionPerformed(ActionEvent e) {
                if( army.size() > 0 && currentState == 0 ){
                    saveGameButton.setEnabled(false);
                    loadGameButton.setEnabled(false);
                    pauseButton.setText("Pause");
                    currentState = 1;
                    callToArmsButton.setEnabled(false);
                    Combat combat = new Combat(army, monstersList.get(monstersDefeated), combatLog, guiX);
                    Thread fight = new Thread(combat);
                    fight.start();
                }else if(army.size() == 0 && currentState == 0) {
                    try {
                        throw new CombatException();
                    } catch (CombatException e1) {
                        JFrame frame = new JFrame("You and what army?");
                        JOptionPane.showMessageDialog(frame, "You need troops to engage monsters.");
                    }
                }
                else if(currentState == 2){
                    combatX.continuePressed();
                    pauseButton.setText("Pause");
                    currentState = 1;
                    callToArmsButton.setEnabled(false);
                }
            }
        });

        spinner1.addChangeListener(new ChangeListener() {               // Spinner do wyboru poziomu rekrutowanego
            @Override
            public void stateChanged(ChangeEvent e) {
                int chosenLvl = Integer.parseInt( spinner1.getValue().toString() );
                if( chosenLvl < 1 ){
                    spinner1.setValue(1);
                    chosenLvl = 1;
                }
                refreshCounters(chosenLvl);
            }
        });

        nameField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {       //  Sprawdzenie czy wpisano jakieś imię
                super.keyReleased(e);
                if( nameField.getText().equals("") ){
                    recruitButton.setEnabled(false);
                }
                if( !nameField.getText().equals("") ){
                    recruitButton.setEnabled(true);
                }
            }
        });

        healButton.addActionListener(new ActionListener() {             //przycisk leczenia
            @Override
            public void actionPerformed(ActionEvent e) {
                if(healCost() > gold){
                    try {
                        throw new GoldException();
                    } catch (GoldException e1) {
                        JFrame frame = new JFrame("Error");
                        JOptionPane.showMessageDialog(frame, "Healer wants gold to work.");
                    }
                }else {
                    gold = gold - healCost();
                    for( int i = 0; i < army.size(); i++ ){
                        army.get(i).setHitPoints(army.get(i).getMaxHitPoints());
                    }
                    combatX.healPressed();
                    refreshArmyList();
                    refreshCounters(Integer.parseInt( spinner1.getValue().toString() ));
                }
            }
        });

        saveGameButton.addActionListener(new ActionListener() {                 //  ZAPIS I ODCZYT
            @Override
            public void actionPerformed(ActionEvent e) {
                SavedDataFile dataToSave = new SavedDataFile(army, gold, monstersDefeated);
                Gson gson = new Gson();
                String json = gson.toJson(dataToSave);
                try {
                    FileWriter writer = new FileWriter("savedData.json");
                    writer.write(json);
                    writer.close();
                    System.out.println("Data saved to file");
                    combatLog.setText("Stan gry został zapisany!\n");
                } catch (IOException io) {
                    System.out.print(io);
                }
            }
        });
        loadGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                SavedDataFile dataToLoad = null;
                Type type = new TypeToken<SavedDataFile>(){}.getType();
                Gson gson = new Gson();

                    try(BufferedReader reader = new BufferedReader(new FileReader("savedData.json"))) {
                        StringBuilder jsonData = new StringBuilder();
                        String line;
                        while((line = reader.readLine()) != null) {
                            jsonData.append(line);
                        }
                        dataToLoad = gson.fromJson(jsonData.toString(), type);

                        System.out.println("Data read");
                    army = dataToLoad.getArmy();
                    gold = dataToLoad.getGold();
                    monstersDefeated = dataToLoad.getMonstersDefeated();
                    refreshArmyList();
                    refreshCounters(Integer.parseInt( spinner1.getValue().toString() ));
                    combatLog.setText("Stan gry został wczytany!\n");
                } catch (IOException io) {
                    System.out.print(io);
                    JFrame frame = new JFrame("Error");
                    JOptionPane.showMessageDialog(frame, "Błąd odczytu danych z pliku.");
                }

                    mm = new MonsterManual();
                    monstersList = mm.getMonstersList();
                    if(monstersDefeated == monstersList.size()){
                        callToArmsButton.setEnabled(false);
                    }
            }
        });
        pauseButton.addActionListener(new ActionListener() {                    //  PAUSE
            @Override
            public void actionPerformed(ActionEvent e) {
                if( pauseButton.getText().equals("Pause") && currentState == 1 ){
                    combatX.pausePressed();
                    pauseButton.setText("Resume");
                    currentState = 2;
                }
                else if( pauseButton.getText().equals("Resume") && currentState == 2){
                    combatX.continuePressed();
                    callToArmsButton.setEnabled(false);
                    currentState = 1;
                    pauseButton.setText("Pause");
                }
            }
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("GUI");
        frame.setContentPane(new GUI().panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

}
