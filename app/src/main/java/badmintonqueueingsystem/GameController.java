package badmintonqueueingsystem;

import java.util.ArrayList;
import java.util.Collections;

public class GameController {
    private ArrayList<Player> playerList = new ArrayList<>();
    private ArrayList<Match> matchHistory = new ArrayList<>();

    // Add a player to the list
    public void addPlayerToList(Player player) {
        playerList.add(player);
    }

    // Manually create a match by selecting random players
    public String createMatch() {
        if (playerList.size() >= 4) {
            Collections.shuffle(playerList);  // Randomly shuffle players

            Player p1 = playerList.get(0);
            Player p2 = playerList.get(1);
            Player p3 = playerList.get(2);
            Player p4 = playerList.get(3);

            Match match = new Match(p1, p2, p3, p4);
            matchHistory.add(match);

            return "Match created: " + match.toString();
        } else {
            return "Not enough players to create a match.";
        }
    }

    public String recordMatchResult(int matchIndex, boolean team1Wins) {
        if (matchIndex < 0 || matchIndex >= matchHistory.size()) {
            return "Invalid match index.";
        }
    
        Match match = matchHistory.get(matchIndex);
        if (match.isConcluded()) {
            return "This match has already been concluded: " + match.toString();
        }
        // Determine the winner and update wins/losses
        match.concludeMatch(team1Wins);
    
        return match.toString();
    }
    
    
    public String manualMatch(Player p1, Player p2, Player p3, Player p4) {
            if (playerList.size() >= 4) {
                Match match = new Match(p1, p2, p3, p4);
                matchHistory.add(match);
                return match.toString();
            }
            else {
                return "Not enough players to create a match.";
            }
    }

    // Delete a match and adjust player statistics
    public String deleteMatch(int matchIndex) {
        if (matchIndex < 0 || matchIndex >= matchHistory.size()) {
            return "Invalid match index.";
        }

        Match match = matchHistory.remove(matchIndex);

        // Adjust match count for players in the deleted match
        match.getTeam1Player1().decrementMatchesPlayed();
        match.getTeam1Player2().decrementMatchesPlayed();
        match.getTeam2Player1().decrementMatchesPlayed();
        match.getTeam2Player2().decrementMatchesPlayed();

        return "Match deleted successfully.";
    }

    // Delete a player from the player list and matches
    public String deletePlayer(Player player) {
        if (!playerList.contains(player)) {
            return "Player not found in the list.";
        }

        playerList.remove(player);
        return "Player deleted successfully.";
    }


    // Display match history
    public ArrayList<String> getMatchHistory() {
        ArrayList<String> history = new ArrayList<>();
        for (int i = 0; i < matchHistory.size(); i++) {
            Match match = matchHistory.get(i);
            history.add((i + 1) + ". " + match.toString());
        }
        return history;
    }

    // Display player stats
    public String getPlayerStats(Player player) {
        return "Player: " + player.getName() + "\n" +
                "Level: " + player.getLevel() + "\n" +
                "Matches Played: " + player.getMatchesPlayed() + "\n" +
                "Wins: " + player.getWins() + "\n" +
                "Losses: " + player.getLosses();
    }

    // Get player list
    public ArrayList<Player> getPlayerList() {
        return new ArrayList<>(playerList);
    }
}
