import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuFrame extends JFrame {
    public MenuFrame() {
        setTitle("Tank Game"); // Sets the title of the menu frame
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        setMinimumSize(new Dimension(800, 600)); // Sets the minimum window size to 800 x 600
        setLocationRelativeTo(null); // Centers the 1280x720 window

        // Main panel (background image)
        ImagePanel menuPanel = new ImagePanel("src/assets/menu/mainmenu.png");
        menuPanel.setLayout(null);

        // Play button
        ImageIcon playIcon = new ImageIcon("src/assets/menu/playButton.png");
        int playWidth = 720;
        int playHeight = 275;
        Image resizedPlay = playIcon.getImage().getScaledInstance(playWidth, playHeight, Image.SCALE_SMOOTH);
        ImageIcon resizedPlayIcon = new ImageIcon(resizedPlay);
        JButton playButton = new JButton(resizedPlayIcon);
        // JButton playButton = new JButton(playIcon);
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
        int creditsWidth = 280;
        int creditsHeight = 100;
        Image resizedCredits = creditsIcon.getImage().getScaledInstance(creditsWidth, creditsHeight, Image.SCALE_SMOOTH);
        ImageIcon resizedCreditsIcon = new ImageIcon(resizedCredits);
        JButton creditButton = new JButton(resizedCreditsIcon);
        // JButton creditButton = new JButton(creditsIcon);
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

        // Add buttons to panel
        playButton.setBounds(600, 565, playWidth, playHeight);
        menuPanel.add(playButton);
        creditButton.setBounds(-10, 940, creditsWidth, creditsHeight);
        menuPanel.add(creditButton);

        // Remove focus from the buttons
        playButton.setFocusable(false);
        creditButton.setFocusable(false);

        // Add menu panel to menu frame
        add(menuPanel);
        setVisible(true);
        setResizable(false);
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
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        }
    }

    public static void main(String[] args) {
        new MenuFrame();
    }
}
