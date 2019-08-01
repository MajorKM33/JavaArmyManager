import java.util.ArrayList;

public class MonsterManual {

    private ArrayList<Monster> monstersList = new ArrayList<Monster>();

    public MonsterManual() {
        monstersList.add(  new Dragon("Szczerbatek", 100, 10, 10) );
        monstersList.add(  new Dragon("Brimscythe", 250, 13, 15) );
        monstersList.add(  new Demon("Glabrezu", 500, 26, 16) );
        monstersList.add(  new Lich("Arthas", 750, 30, 20) );
        monstersList.add(  new Dragon("Vorugal", 1000, 40, 34) );
        monstersList.add(  new Lich("Delilah Blackstar", 2000, 35, 20) );
        monstersList.add(  new Lich("Elias Blackstar", 3000, 35, 26) );
        monstersList.add(  new Dragon("Thordak", 4000, 46, 40) );
        monstersList.add(  new Demon("Demogorgon", 6000, 39, 20) );
        monstersList.add(  new Lich("Vecna", 10000, 55, 40) );
        //monstersList.add(  new Lich("Chris Perkins", 666666, 666, 666) );
    }

    public ArrayList<Monster> getMonstersList() {
        return monstersList;
    }
}
