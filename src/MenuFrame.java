import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuFrame extends JFrame {
    double PLAY_Y_RATIO = 0.65; // 65% down the screen
    double CREDITS_X_RATIO = 0.001; // 0.1% from left
    double CREDITS_Y_RATIO = 0.965; // 96.5% down the screen

    public MenuFrame() {
        setTitle("Tank Game"); // Sets the title of the menu frame
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Start maximized for menu

        setMinimumSize(new Dimension(800, 600)); // Sets the minimum window size to 800 x 600
        setLocationRelativeTo(null); // Centers the 1280x720 window

        // Main panel (background image)
        ImagePanel menuPanel = new ImagePanel("src/assets/menu/mainmenu.png");
        menuPanel.setLayout(null);

        // Play button
        ImageIcon playIcon = new ImageIcon("src/assets/menu/playButton.png");
        int playIconWidth = 720;
        int playIconHeight = 275;
        Image resizedPlay = playIcon.getImage().getScaledInstance(playIconWidth, playIconHeight, Image.SCALE_SMOOTH);
        ImageIcon resizedPlayIcon = new ImageIcon(resizedPlay);
        JButton playButton = new JButton(resizedPlayIcon); // Play starts the game
        playButton.setBorderPainted(false);
        playButton.setContentAreaFilled(false);
        playButton.setFocusPainted(false);
        playButton.setOpaque(false);
        playButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Clear the frame
                dispose();
                // Run game frame
                GameFrame.runGame();
            }
        });

        // Credits button
        ImageIcon creditsIcon = new ImageIcon("src/assets/menu/creditsButton.png");
        int creditsIconWidth = 280;
        int creditsIconHeight = 100;
        Image resizedCredits = creditsIcon.getImage().getScaledInstance(creditsIconWidth, creditsIconHeight, Image.SCALE_SMOOTH);
        ImageIcon resizedCreditsIcon = new ImageIcon(resizedCredits);
        JButton creditButton = new JButton(resizedCreditsIcon); // Show credits dialog
        creditButton.setBorderPainted(false);
        creditButton.setContentAreaFilled(false);
        creditButton.setFocusPainted(false);
        creditButton.setOpaque(false);
        creditButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(MenuFrame.this, "Created by:\nJonathan Yu\nCheney Chen", "Credits",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        });

        // Add buttons to panel (position relative to screen size)
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenW = screenSize.width;
        int screenH = screenSize.height;

        // Center the Play button on the screen
        int playX = (screenW - playIconWidth) / 2;
        int playY = (int)(screenH * PLAY_Y_RATIO - playIconHeight / 2);
        playButton.setBounds(playX, playY, playIconWidth, playIconHeight);
        menuPanel.add(playButton);

        // Place Credits button at bottom-left with a small margin
        int creditsX = (int)(screenW * CREDITS_X_RATIO);
        int creditsY = (int)(screenH * CREDITS_Y_RATIO - creditsIconHeight);
        creditButton.setBounds(creditsX, creditsY, creditsIconWidth, creditsIconHeight);
        
        menuPanel.add(creditButton);

        // Add menu panel to menu frame
        add(menuPanel);
        setVisible(true);
    }

    private class ImagePanel extends JPanel {
        private Image backgroundImage;

        public ImagePanel(String imagePath) {
            ImageIcon icon = new ImageIcon(imagePath);
            backgroundImage = icon.getImage();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (backgroundImage != null) {
                // Stretch the background to fill the panel
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        }
    }

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> new MenuFrame());
    }
}
