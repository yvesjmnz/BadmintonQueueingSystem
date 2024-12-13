package badmintonqueueingsystem;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private String name;
    private String level;
    private int matchesPlayed;
    private int feesDue;
    private int wins;
    private int losses;
    private List<Runnable> listeners = new ArrayList<>();

    public Player(String name, String level) {
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

    public String getLevel() {
        return level;
    }

    public int getMatchesPlayed() {
        return matchesPlayed;
    }

    public int getFeesDue() {
        return feesDue;
    }

    public int getWins() {
        return wins;
    }

    public int getLosses() {
        return losses;
    }

    public void addMatch() {
        matchesPlayed++;
        feesDue += 25; // Fee per game
        notifyListeners();
    }

    public void decrementMatchesPlayed() {
        matchesPlayed--;
        feesDue -= 25;
        notifyListeners();
    }

    public void addWin() {
        wins++;
        notifyListeners();
    }

    public void addLoss() {
        losses++;
        notifyListeners();
    }

    public void addListener(Runnable listener) {
        listeners.add(listener);
    }

    private void notifyListeners() {
        for (Runnable listener : listeners) {
            listener.run();
        }
    }

    @Override
    public String toString() {
        return name + " (" + level + ") - Matches Played: " + matchesPlayed + ", Fees Due: " + feesDue + " PHP";
    }
}
