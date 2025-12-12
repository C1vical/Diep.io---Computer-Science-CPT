import javax.swing.*;
import java.awt.*;

public class MenuPanel extends JPanel {
    JPanel menuPanel = new JPanel();
    
    public MenuPanel() {
        menuPanel.setLayout(new BorderLayout());
        menuPanel.setBackground(new Color(128, 128, 255));
        menuPanel.add(formPanel, BorderLayout.NORTH);
        menuPanel.add(lbWelcome, BorderLayout.CENTER);
        menuPanel.add(buttonsPanel, BorderLayout.SOUTH);
    }
}
