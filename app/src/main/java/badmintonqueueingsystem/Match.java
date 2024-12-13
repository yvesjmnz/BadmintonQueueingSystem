package badmintonqueueingsystem;

import java.util.ArrayList;
import java.util.List;

public class Match {
    private Player team1Player1;
    private Player team1Player2;
    private Player team2Player1;
    private Player team2Player2;
    private int team1Score;
    private int team2Score;
    private boolean concluded;

    public Match(Player team1Player1, Player team1Player2, Player team2Player1, Player team2Player2) {
        this.team1Player1 = team1Player1;
        this.team1Player2 = team1Player2;
        this.team2Player1 = team2Player1;
        this.team2Player2 = team2Player2;
        this.team1Score = 0;
        this.team2Score = 0;
        this.concluded = false;
    }

    public void concludeMatch(boolean team1Wins) {
        team1Player1.addMatch();
        team1Player2.addMatch();
        team2Player1.addMatch();
        team2Player2.addMatch();
        if (team1Wins) {
            team1Player1.addWin();
            team1Player2.addWin();
            team2Player1.addLoss();
            team2Player2.addLoss();
        } else {
            team1Player1.addLoss();
            team1Player2.addLoss();
            team2Player1.addWin();
            team2Player2.addWin();
        }
        concluded = true;
    }

    // Getters
    public Player getTeam1Player1() {
        return team1Player1;
    }

    public Player getTeam1Player2() {
        return team1Player2;
    }

    public Player getTeam2Player1() {
        return team2Player1;
    }

    public Player getTeam2Player2() {
        return team2Player2;
    }

    public int getTeam1Score() {
        return team1Score;
    }

    public int getTeam2Score() {
        return team2Score;
    }

    public boolean isConcluded() {
        return concluded;
    }

    // Setters
    public void setScores(int team1Score, int team2Score) {
        this.team1Score = team1Score;
        this.team2Score = team2Score;
    }

    public void setConcluded(boolean concluded) {
        this.concluded = concluded;
    }

    /**
     * Returns a list of all players involved in this match.
     * @return List of players
     */
    public List<Player> getPlayers() {
        List<Player> players = new ArrayList<>();
        players.add(team1Player1);
        players.add(team1Player2);
        players.add(team2Player1);
        players.add(team2Player2);
        return players;
    }

    @Override
    public String toString() {
        return "Team (" + team1Player1.getName() + " & " + team1Player2.getName() + ") vs " +
               "Team (" + team2Player1.getName() + " & " + team2Player2.getName() + ")";
    }
}
