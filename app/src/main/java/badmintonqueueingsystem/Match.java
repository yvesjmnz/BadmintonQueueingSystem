package badmintonqueueingsystem;

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

    @Override
    public String toString() {
        return "Team (" + team1Player1.getName() + " & " + team1Player2.getName() + ") " +
               team1Score + " - " + team2Score + " Team (" + team2Player1.getName() + " & " + team2Player2.getName() + ")";
    }
}
