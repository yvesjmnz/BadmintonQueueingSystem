package badmintonqueueingsystem;

import javax.swing.*;
import java.awt.*;

public class MainView {
    private GameController controller = new GameController();

    public MainView() {
        // Main Frame
        JFrame frame = new JFrame("Badminton Queueing System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(700, 800);

        // Layout and Components
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // Player Input
        JTextField nameField = new JTextField();
        nameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        nameField.setBorder(BorderFactory.createTitledBorder("Enter player name"));

        JComboBox<String> levelComboBox = new JComboBox<>(new String[]{"A", "B", "C", "D", "Beginner"});
        levelComboBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        levelComboBox.setBorder(BorderFactory.createTitledBorder("Select level"));

        JButton addPlayerButton = new JButton("Add Player");
        DefaultListModel<String> playerListModel = new DefaultListModel<>();
        JList<String> playerListView = new JList<>(playerListModel);

        // Match Management
        JButton createMatchButton = new JButton("Random Match");
        JButton recordResultButton = new JButton("Record Match Result");
        DefaultListModel<String> matchHistoryModel = new DefaultListModel<>();
        JList<String> matchHistoryView = new JList<>(matchHistoryModel);

        // Stats Display
        JButton viewStatsButton = new JButton("View Player Stats");
        JTextArea statsArea = new JTextArea(10, 30);
        statsArea.setEditable(false);
        statsArea.setBorder(BorderFactory.createTitledBorder("Player Stats"));

        // Add Player Button Action
        addPlayerButton.addActionListener(e -> {
            String name = nameField.getText();
            String level = (String) levelComboBox.getSelectedItem();

            if (name != null && !name.isEmpty() && level != null) {
                Player player = new Player(name, level);
                
                // Add listener to update the list view
                player.addListener(() -> {
                    int index = controller.getPlayerList().indexOf(player);
                    if (index != -1) {
                        playerListModel.set(index, player.toString());
                    }
                });
                
                controller.addPlayerToList(player);
                playerListModel.addElement(player.toString());
                nameField.setText("");
                levelComboBox.setSelectedIndex(-1);
            } else {
                JOptionPane.showMessageDialog(frame, "Please fill in all fields!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Update Player List After Match
        recordResultButton.addActionListener(e -> {
            int selectedMatchIndex = matchHistoryView.getSelectedIndex();
            if (selectedMatchIndex == -1) {
                JOptionPane.showMessageDialog(frame, "Please select a match to record the result.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int response = JOptionPane.showConfirmDialog(frame, "Did Team 1 win?", "Record Match Result", JOptionPane.YES_NO_OPTION);
            if (response == JOptionPane.YES_OPTION || response == JOptionPane.NO_OPTION) {
                boolean team1Wins = (response == JOptionPane.YES_OPTION);
                String result = controller.recordMatchResult(selectedMatchIndex, team1Wins);
                matchHistoryModel.set(selectedMatchIndex, result);
                
                // Refresh the entire player list view
                playerListModel.clear();
                for (Player player : controller.getPlayerList()) {
                    playerListModel.addElement(player.toString());
                }
            }
        });

        // Create Match Button Action
        createMatchButton.addActionListener(e -> {
            if (controller.getPlayerList().size() < 4) {
                JOptionPane.showMessageDialog(frame, "Not enough players to create a match.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        
            // Shuffle the player list and select the first 4 players
            String result = controller.createMatch();  // This will create a randomized match
            matchHistoryModel.addElement(result);

            // Update the player list display
            playerListModel.clear();
            for (Player player : controller.getPlayerList()) {
                playerListModel.addElement(player.toString());
            }
        });

        // Create buttons and logic for manual match
        JButton manualMatchButton = new JButton("Create Match");
        manualMatchButton.addActionListener(e -> {
            // Ask user to manually pick 4 players for the match
            Player p1 = (Player) JOptionPane.showInputDialog(frame, "Select Team 1 Player 1:", 
                "Manual Match", JOptionPane.QUESTION_MESSAGE, null, controller.getPlayerList().toArray(), null);
            Player p2 = (Player) JOptionPane.showInputDialog(frame, "Select Team 1 Player 2:", 
                "Manual Match", JOptionPane.QUESTION_MESSAGE, null, controller.getPlayerList().toArray(), null);
            Player p3 = (Player) JOptionPane.showInputDialog(frame, "Select Team 2 Player 1:", 
                "Manual Match", JOptionPane.QUESTION_MESSAGE, null, controller.getPlayerList().toArray(), null);
            Player p4 = (Player) JOptionPane.showInputDialog(frame, "Select Team 2 Player 2:", 
                "Manual Match", JOptionPane.QUESTION_MESSAGE, null, controller.getPlayerList().toArray(), null);

            if (p1 != null && p2 != null && p3 != null && p4 != null) {
                String result = controller.manualMatch(p1, p2, p3, p4);
                matchHistoryModel.addElement(result);
            }
        });

        // View Stats Button Action
        viewStatsButton.addActionListener(e -> {
            String playerName = JOptionPane.showInputDialog(frame, "Enter player name to view stats:");
        
            if (playerName != null && !playerName.isEmpty()) {
                for (Player player : controller.getPlayerList()) {
                    if (player.getName().equalsIgnoreCase(playerName)) {
                        statsArea.setText(controller.getPlayerStats(player));
                        return;
                    }
                }
                JOptionPane.showMessageDialog(frame, "Player not found in the list.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });


        // Delete match button and logic
        JButton deleteMatchButton = new JButton("Delete Match");
        deleteMatchButton.addActionListener(e -> {
            int selectedMatchIndex = matchHistoryView.getSelectedIndex();
            if (selectedMatchIndex == -1) {
                JOptionPane.showMessageDialog(frame, "Please select a match to delete.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String result = controller.deleteMatch(selectedMatchIndex);
            matchHistoryModel.remove(selectedMatchIndex);
            JOptionPane.showMessageDialog(frame, result);
        });

        // Delete player button and logic
        JButton deletePlayerButton = new JButton("Delete Player");
        deletePlayerButton.addActionListener(e -> {
            String playerName = JOptionPane.showInputDialog(frame, "Enter player name to delete:");
            if (playerName != null && !playerName.isEmpty()) {
                Player playerToDelete = null;
                for (Player player : controller.getPlayerList()) {
                    if (player.getName().equalsIgnoreCase(playerName)) {
                        playerToDelete = player;
                        break;
                    }
                }

                if (playerToDelete != null) {
                    String result = controller.deletePlayer(playerToDelete);
                    playerListModel.clear();
                    for (Player player : controller.getPlayerList()) {
                        playerListModel.addElement(player.toString());
                    }
                    JOptionPane.showMessageDialog(frame, result);
                } else {
                    JOptionPane.showMessageDialog(frame, "Player not found in the list.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });


        // Add components to the panel
        panel.add(nameField);
        panel.add(levelComboBox);
        panel.add(addPlayerButton);
        panel.add(deletePlayerButton);
        panel.add(new JScrollPane(playerListView));
        panel.add(createMatchButton);
        panel.add(manualMatchButton);
        panel.add(recordResultButton);
        panel.add(new JScrollPane(matchHistoryView));
        panel.add(deleteMatchButton);
        panel.add(viewStatsButton);
        panel.add(new JScrollPane(statsArea));

        // Add panel to frame
        frame.getContentPane().add(panel);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainView::new);
    }
}
