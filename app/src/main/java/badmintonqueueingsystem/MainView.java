package badmintonqueueingsystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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

        JComboBox<Character> levelComboBox = new JComboBox<>(new Character[]{'A', 'B', 'C'});
        levelComboBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        levelComboBox.setBorder(BorderFactory.createTitledBorder("Select level"));

        JButton addPlayerButton = new JButton("Add Player");
        DefaultListModel<String> playerListModel = new DefaultListModel<>();
        JList<String> playerListView = new JList<>(playerListModel);

        // Match Management
        JButton createMatchButton = new JButton("Create Match");
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
            Character level = (Character) levelComboBox.getSelectedItem();

            if (name != null && !name.isEmpty() && level != null) {
                Player player = new Player(name, level);
                controller.addPlayerToList(player);
                playerListModel.addElement(player.toString());
                nameField.setText("");
                levelComboBox.setSelectedIndex(-1);
            } else {
                JOptionPane.showMessageDialog(frame, "Please fill in all fields!", "Error", JOptionPane.ERROR_MESSAGE);
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

        // Record Match Result Button Action
        recordResultButton.addActionListener(e -> {
            int selectedMatchIndex = matchHistoryView.getSelectedIndex();
            if (selectedMatchIndex == -1) {
                JOptionPane.showMessageDialog(frame, "Please select a match to record the result.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            JTextField team1ScoreField = new JTextField();
            JTextField team2ScoreField = new JTextField();

            Object[] message = {
                "Enter Team 1 Score:", team1ScoreField,
                "Enter Team 2 Score:", team2ScoreField
            };

            int option = JOptionPane.showConfirmDialog(frame, message, "Record Match Result", JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION) {
                try {
                    int team1Score = Integer.parseInt(team1ScoreField.getText());
                    int team2Score = Integer.parseInt(team2ScoreField.getText());
                    String result = controller.recordMatchResult(selectedMatchIndex, team1Score, team2Score);

                    matchHistoryModel.set(selectedMatchIndex, result);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "Invalid score input!", "Error", JOptionPane.ERROR_MESSAGE);
                }
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

        // Add components to the panel
        panel.add(nameField);
        panel.add(levelComboBox);
        panel.add(addPlayerButton);
        panel.add(new JScrollPane(playerListView));
        panel.add(createMatchButton);
        panel.add(recordResultButton);
        panel.add(new JScrollPane(matchHistoryView));
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
