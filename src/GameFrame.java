import javax.swing.*;

public class GameFrame extends JFrame {
    public GameFrame() {
        setTitle("Tank Game");
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Start the window maximized
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        GamePanel gamePanel = new GamePanel();
        add(gamePanel);

        // setResizable(false);
        setVisible(true);
    }

    public static void runGame() {
        javax.swing.SwingUtilities.invokeLater(() -> new GameFrame());
    }
}