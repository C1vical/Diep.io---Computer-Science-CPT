import javax.swing.JFrame;

public class MenuFrame extends JFrame {
    public MenuFrame() {
        setTitle("Tank Game");
        setExtendedState(JFrame.MAXIMIZED_BOTH);  // full screen window
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        MenuPanel panel = new MenuPanel();
        add(panel);

        setVisible(true);
    }

    public static void main(String[] args) {
        new MenuFrame();
    }
}
