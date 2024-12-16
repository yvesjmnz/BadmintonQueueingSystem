package badmintonqueueingsystem;

import javax.swing.*;
import java.awt.*;

public class MainView {
    private GameController controller = new GameController();

    public MainView() {
        // Set a modern look and feel
        try {
            UIManager.setLookAndFeel(new com.formdev.flatlaf.FlatDarkLaf());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Main Frame
        JFrame frame = new JFrame("Badminton Queueing System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);

        // Tabbed Pane for organization
        JTabbedPane tabbedPane = new JTabbedPane();

        // Player Management Panel
        JPanel playerPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Player Input
        JTextField nameField = new JTextField();
        nameField.setBorder(BorderFactory.createTitledBorder("Player Name"));
        nameField.setToolTipText("Enter the player's name");
        JComboBox<String> levelComboBox = new JComboBox<>(new String[]{"A", "B", "C", "D", "Beginner"});
        levelComboBox.setBorder(BorderFactory.createTitledBorder("Skill Level"));
        levelComboBox.setToolTipText("Select the player's skill level");

        JButton addPlayerButton = new JButton("Add Player");
        DefaultListModel<String> playerListModel = new DefaultListModel<>();
        JList<String> playerListView = new JList<>(playerListModel);
        playerListView.setBorder(BorderFactory.createTitledBorder("Players"));

        JButton deletePlayerButton = new JButton("Delete Player");

        // Add components to Player Panel
        gbc.gridx = 0;
        gbc.gridy = 0;
        playerPanel.add(nameField, gbc);

        gbc.gridy = 1;
        playerPanel.add(levelComboBox, gbc);

        gbc.gridy = 2;
        playerPanel.add(addPlayerButton, gbc);

        gbc.gridy = 3;
        playerPanel.add(deletePlayerButton, gbc);

        gbc.gridy = 4;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1;
        gbc.weighty = 1;
        playerPanel.add(new JScrollPane(playerListView), gbc);

        tabbedPane.addTab("Players", playerPanel);

        // Match Management Panel
        JPanel matchPanel = new JPanel(new GridBagLayout());

        JButton createMatchButton = new JButton("Random Match");
        JButton manualMatchButton = new JButton("Create Match");
        JButton recordResultButton = new JButton("Record Result");
        JButton deleteMatchButton = new JButton("Delete Match");

        DefaultListModel<String> matchHistoryModel = new DefaultListModel<>();
        JList<String> matchHistoryView = new JList<>(matchHistoryModel);
        matchHistoryView.setBorder(BorderFactory.createTitledBorder("Match History"));

        // Add components to Match Panel
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weighty = 0;
        matchPanel.add(createMatchButton, gbc);

        gbc.gridy = 1;
        matchPanel.add(manualMatchButton, gbc);

        gbc.gridy = 2;
        matchPanel.add(recordResultButton, gbc);

        gbc.gridy = 3;
        matchPanel.add(deleteMatchButton, gbc);

        gbc.gridy = 4;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1;
        gbc.weighty = 1;
        matchPanel.add(new JScrollPane(matchHistoryView), gbc);

        tabbedPane.addTab("Matches", matchPanel);

        // Sidebar for player stats
        JPanel sidebar = new JPanel(new BorderLayout());
        sidebar.setBorder(BorderFactory.createTitledBorder("Player Stats"));
        sidebar.setPreferredSize(new Dimension(200, frame.getHeight()));

        JTextArea playerStatsArea = new JTextArea();
        playerStatsArea.setEditable(false);
        sidebar.add(new JScrollPane(playerStatsArea), BorderLayout.CENTER);
        // Create a split panel with the tabbed pane on the left and the sidebar on the right
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, tabbedPane, sidebar);
        splitPane.setDividerLocation(600); // Adjust the divider location as needed
        splitPane.setResizeWeight(0.8);    // Allocate more space to the tabbedPane
        frame.add(splitPane);


        // Add Player Button Action
        addPlayerButton.addActionListener(e -> {
            String name = nameField.getText();
            String level = (String) levelComboBox.getSelectedItem();
        
            if (name != null && !name.isEmpty() && level != null) {
                try {
                    Player player = new Player(name, level);
                    controller.addPlayerToList(player);
                    playerListModel.addElement(player.toString());
                    nameField.setText("");
                } catch (IllegalArgumentException ex) {
                    JOptionPane.showMessageDialog(frame, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
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

        manualMatchButton.addActionListener(e -> {
            // Check if there are enough players to create a match
            if (controller.getPlayerList().size() < 4) {
                JOptionPane.showMessageDialog(frame, "Not enough players to create a manual match. Please add more players.", 
                                              "Error", JOptionPane.ERROR_MESSAGE);
                return; // Exit the method gracefully
            }
        
            // Ask user to manually pick 4 players for the match
            Player p1 = (Player) JOptionPane.showInputDialog(frame, "Select Team 1 Player 1:", 
                "Manual Match", JOptionPane.QUESTION_MESSAGE, null, controller.getPlayerList().toArray(), null);
            Player p2 = (Player) JOptionPane.showInputDialog(frame, "Select Team 1 Player 2:", 
                "Manual Match", JOptionPane.QUESTION_MESSAGE, null, controller.getPlayerList().toArray(), null);
            Player p3 = (Player) JOptionPane.showInputDialog(frame, "Select Team 2 Player 1:", 
                "Manual Match", JOptionPane.QUESTION_MESSAGE, null, controller.getPlayerList().toArray(), null);
            Player p4 = (Player) JOptionPane.showInputDialog(frame, "Select Team 2 Player 2:", 
                "Manual Match", JOptionPane.QUESTION_MESSAGE, null, controller.getPlayerList().toArray(), null);
        
            // Check that all players were selected
            if (p1 != null && p2 != null && p3 != null && p4 != null) {
                String result = controller.manualMatch(p1, p2, p3, p4);
                matchHistoryModel.addElement(result);
            } else {
                JOptionPane.showMessageDialog(frame, "Match creation was canceled or incomplete. Please try again.", 
                                              "Warning", JOptionPane.WARNING_MESSAGE);
            }
        });
        
        playerListView.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && playerListView.getSelectedValue() != null) {
                String selectedPlayer = playerListView.getSelectedValue();
                for (Player player : controller.getPlayerList()) {
                    if (selectedPlayer.equals(player.toString())) {
                        playerStatsArea.setText(controller.getPlayerStats(player));
                        return;
                    }
                }
            }
        });
        
        


        deleteMatchButton.addActionListener(e -> {
            int selectedMatchIndex = matchHistoryView.getSelectedIndex();
            if (selectedMatchIndex == -1) {
                JOptionPane.showMessageDialog(frame, "Please select a match to delete.",
                                              "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        
            // Prepare a StringBuilder to receive the result message
            StringBuilder resultMessage = new StringBuilder();
            boolean isDeleted = controller.deleteMatch(selectedMatchIndex, resultMessage);
        
            if (isDeleted) {
                // Remove the match from the view only if deletion was successful
                matchHistoryModel.remove(selectedMatchIndex);
                JOptionPane.showMessageDialog(frame, resultMessage.toString(),
                                              "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                // Show the appropriate error message
                JOptionPane.showMessageDialog(frame, resultMessage.toString(),
                                              "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        

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
        
        // Add tabbedPane to frame
        frame.add(splitPane);
        frame.setVisible(true);

    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainView::new);
    }
}
