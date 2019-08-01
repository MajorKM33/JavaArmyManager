public interface FightObserver {
    void connectCombat( Combat combat );
    void winScenario();
    void loseScenario();
    void wantConfirm();
    void callRefresh();
    void callClear();
    void simulationSpeed(int i);
}
