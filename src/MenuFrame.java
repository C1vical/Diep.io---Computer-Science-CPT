import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuFrame extends JFrame {
    public MenuFrame() {
        setTitle("Tank Game"); // Sets the title of the menu frame
        setSize(1280, 720); // Sets window size as 1280 x 720
        setMinimumSize(new Dimension(800, 600)); // Sets the minimum window size to 800 x 600
        setLocationRelativeTo(null); // Centers the 1280x720 window

        // Main panel
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new BorderLayout());
        menuPanel.setBackground(new Color(162, 162, 162));

        // Title label
        JLabel titleLabel = new JLabel("Tank Game");
        titleLabel.setFont(new Font("Segoe print", Font.BOLD, 48));
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        titleLabel.setForeground(new Color(30, 60, 120));
        titleLabel.setOpaque(false);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(30, 0, 30, 0));
        add(titleLabel, BorderLayout.NORTH);

        // Buttons
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setBackground(new Color(162, 162, 162));
        buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.Y_AXIS));
        
        // Play button
        JButton playButton = new JButton("Play");
        playButton.setFont(new Font("Segoe Print", Font.BOLD, 32));
        playButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        playButton.setMaximumSize(new Dimension(200, 60));
        playButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Clear the frame
                MenuFrame.this.getContentPane().removeAll();
                // Add the GamePanel
                GamePanel gamePanel = new GamePanel();
                MenuFrame.this.add(gamePanel);
                // Refresh the JFrame
                MenuFrame.this.revalidate();
                MenuFrame.this.repaint();
            }
        });

        // Credits button
        JButton creditButton = new JButton("Credits");
        creditButton.setFont(new Font("Segoe Print", Font.BOLD, 32));
        creditButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        creditButton.setMaximumSize(new Dimension(200, 60));
        creditButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(MenuFrame.this, "Created by:\nJonathan Yu\nCheney Chen", "Credits", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        // Exit button
        JButton exitButton = new JButton("Exit");
        exitButton.setFont(new Font("Segoe Print", Font.BOLD, 32));
        exitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        exitButton.setMaximumSize(new Dimension(200, 60));
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        // Add spacing and buttons to panel
        buttonsPanel.add(Box.createVerticalStrut(20)); // space
        buttonsPanel.add(playButton);
        buttonsPanel.add(Box.createVerticalStrut(20)); // space
        buttonsPanel.add(creditButton);
        buttonsPanel.add(Box.createVerticalStrut(20)); // space
        buttonsPanel.add(exitButton);

        // Add buttons panel to menu panel
        menuPanel.add(buttonsPanel, BorderLayout.CENTER);
        
        // Add menu panel to menu frame
        add(menuPanel);
        setVisible(true);
    }
    
    public static void main(String[] args) {
        new MenuFrame();
    }
}
