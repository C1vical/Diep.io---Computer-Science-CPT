import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel {

    private final int GRID_SIZE = 30;

    public GamePanel() {
        setBackground(new Color(245, 245, 245));
        setFocusable(true);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        drawGrid(g2);
        drawBorder(g2);
    }

    private void drawGrid(Graphics2D g2) {
        g2.setColor(new Color(210, 210, 210));

        int w = getWidth();
        int h = getHeight();

        for (int x = 0; x < w; x += GRID_SIZE) {
            g2.drawLine(x, 0, x, h);
        }
        for (int y = 0; y < h; y += GRID_SIZE) {
            g2.drawLine(0, y, w, y);
        }
    }

    private void drawBorder(Graphics2D g2) {
        g2.setColor(Color.GRAY);
        g2.setStroke(new BasicStroke(5));
        g2.drawRect(0, 0, getWidth(), getHeight());
    }
}