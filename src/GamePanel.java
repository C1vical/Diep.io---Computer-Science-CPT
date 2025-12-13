import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;
import javax.imageio.ImageIO;

public class GamePanel extends JPanel {

    private final int GRID_SIZE = 30;
    private Image tankImage;
    private int tankX = -1, tankY = -1; // Tank position
    private final int TANK_WIDTH = 250;
    private final int TANK_HEIGHT = 250;
    private final int SPEED = 10; // pixels per move
    private boolean wPressed = false;
    private boolean sPressed = false;
    private boolean aPressed = false;
    private boolean dPressed = false;

    public GamePanel() {
        setBackground(new Color(245, 245, 245));
        setFocusable(true);

        // Load the tank image
        try {
            tankImage = ImageIO.read(new File("src/assets/baseTank.png"));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Tank image not found!");
        }

        // Set up key bindings for WASD
        setupKeyBindings();
    }

    private void setupKeyBindings() {
        // W - up
        getInputMap().put(KeyStroke.getKeyStroke("pressed W"), "wPressed");
        getActionMap().put("wPressed", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                wPressed = true;
                // tankY = Math.max(tankY - SPEED, 0);
                // repaint();
            }
        });
        getInputMap().put(KeyStroke.getKeyStroke("released W"), "wReleased");
        getActionMap().put("wReleased", new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
                wPressed = false;
            }
        });

        // S - down
        getInputMap().put(KeyStroke.getKeyStroke("pressed S"), "sPressed");
        getActionMap().put("sPressed", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sPressed = true;
            }
        });
        getInputMap().put(KeyStroke.getKeyStroke("released S"), "sReleased");
        getActionMap().put("sReleased", new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
                sPressed = false;
            }
        });

        // A - left
        getInputMap().put(KeyStroke.getKeyStroke("pressed A"), "aPressed");
        getActionMap().put("aPressed", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                aPressed = true;
            }
        });
        getInputMap().put(KeyStroke.getKeyStroke("released A"), "aReleased");
        getActionMap().put("aReleased", new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
                aPressed = false;
            }
        });

        // D - right
        getInputMap().put(KeyStroke.getKeyStroke("pressed D"), "dPressed");
        getActionMap().put("dPressed", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dPressed = true;
            }
        });
        getInputMap().put(KeyStroke.getKeyStroke("released D"), "dReleased");
        getActionMap().put("dReleased", new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
                dPressed = false;
            }
        });

        int SPEED = 5;

        Timer timer = new Timer(15, e -> {
            double moveX = 0;
            double moveY = 0;

            if (wPressed) moveY -= 1;
            if (sPressed) moveY += 1;
            if (aPressed) moveX -= 1;
            if (dPressed) moveX += 1;

            // Normalize diagonal movement
            if (moveX != 0 && moveY != 0) {
                moveX /= Math.sqrt(2);
                moveY /= Math.sqrt(2);
            }

            // Apply speed
            tankX += (int)(moveX * SPEED);
            tankY += (int)(moveY * SPEED);

            // Keep tank inside panel
            tankX = Math.max(0, Math.min(tankX, getWidth() - TANK_WIDTH));
            tankY = Math.max(0, Math.min(tankY, getHeight() - TANK_HEIGHT));
            repaint();
        });
        timer.start();
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        drawGrid(g2);
        drawBorder(g2);

         // Set initial position if not set yet
        if (tankX == -1 && tankY == -1) {
            tankX = (getWidth() - TANK_WIDTH) / 2;
            tankY = (getHeight() - TANK_HEIGHT) / 2;
        }
         // Draw the tank image
        if (tankImage != null) {
            g2.drawImage(tankImage, tankX, tankY, TANK_WIDTH, TANK_HEIGHT, this);
        }
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