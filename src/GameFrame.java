import javax.swing.*;

public class GameFrame extends JFrame {
    public GameFrame() {
        setTitle("Tank Game");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        GamePanel panel = new GamePanel();
        add(panel);

        setVisible(true);
    }
    public static void runGame() {
        new GameFrame();
    }
}