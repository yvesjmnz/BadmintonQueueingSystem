package badmintonqueueingsystem;


public class Player {
    private String name;
    private char level;
    private int matchesPlayed;
    private int feesDue;
    private int wins;
    private int losses;

    public Player(String name, char level) {
        this.name = name;
        this.level = level;
        this.matchesPlayed = 0;
        this.feesDue = 100; // Entrance fee
        this.wins = 0;
        this.losses = 0;
    }

    public String getName() {
        return name;
    }

    public char getLevel() {
        return level;
    }

    public int getMatchesPlayed() {
        return matchesPlayed;
    }

    public int getFeesDue() {
        return feesDue;
    }

    public void addMatch() {
        matchesPlayed++;
        feesDue += 25; // Fee per game
    }

    public void addWin() {
        wins++;
    }

    public void addLoss() {
        losses++;
    }

    public int getWins() {
        return wins;
    }

    public int getLosses() {
        return losses;
    }


    @Override
    public String toString() {
        return name + " (" + level + ")";
    }
}
