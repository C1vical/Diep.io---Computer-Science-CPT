import javax.swing.*;

public class GameFrame extends JFrame {

    public GameFrame() {
        setTitle("Tank Game");
        setExtendedState(JFrame.MAXIMIZED_BOTH);  // full screen window
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        GamePanel panel = new GamePanel();
        add(panel);

        setVisible(true);
    }

    public static void main(String[] args) {
        new GameFrame();
    }
}