import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import javax.imageio.ImageIO;

public class GamePanel extends JPanel {
    // private final int GRID_SIZE = 30;

    // Tank variables
    private int tankX = -1, tankY = -1; // Position
    private double tankAngle; // angle
    private int TANK_WIDTH = 250, TANK_HEIGHT = 250; // Tank size
    private double SPEED = 5; // Tank speed

    // Mouse variables
    private int mouseX, mouseY; // Coordinates of the cursor

    // WASD booleans
    private boolean wPressed = false;
    private boolean sPressed = false;
    private boolean aPressed = false;
    private boolean dPressed = false;

    // File path of the tank
    private String tank = "src/assets//tanks/basic.png";

    // Images
    private Image backgroundImage;
    private Image tankImage;

    public GamePanel() {
        setFocusable(true);

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
            mouseX = e.getX();
            mouseY = e.getY();
            }

            @Override
            public void mouseDragged(MouseEvent e) {
            mouseX = e.getX();
            mouseY = e.getY();
            }
        });

        try {
            tankImage = ImageIO.read(new File(tank));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        try {
            backgroundImage = ImageIO.read(new File("src/assets/grid.png"));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // Set up movement (WASD)
        movement();
        rotation();
    }

    private void movement() {
        // W - up
        getInputMap().put(KeyStroke.getKeyStroke("pressed W"), "wPressed");
        getActionMap().put("wPressed", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                wPressed = true;
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

            // Keep tank inside panel by comparing new coordinates to the max width and height of the panel
            tankX = Math.max(0, Math.min(tankX, getWidth() - TANK_WIDTH));
            tankY = Math.max(0, Math.min(tankY, getHeight() - TANK_HEIGHT));

            // Repaint the panel
            repaint();
        });
        timer.start();

        
    }

    private void rotation() {
        Timer timer = new Timer(15, e -> {
            // Center of the tank
            int centerX = tankX + TANK_WIDTH / 2;
            int centerY = tankY + TANK_HEIGHT / 2;

            // Difference between mouse and tank
            int dx = mouseX - centerX;
            int dy = mouseY - centerY;

            // Calculate angle (in radians)
            tankAngle = Math.atan2(dy, dx);
            repaint();
        });
        timer.start();
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();

        // Draw grid
        g2.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), null);

         // Set initial position if not set yet
        if (tankX == -1 && tankY == -1) {
            tankX = (getWidth() - TANK_WIDTH) / 2;
            tankY = (getHeight() - TANK_HEIGHT) / 2;
        }

        // Move origin to tank center
        g2.translate(tankX + TANK_WIDTH / 2, tankY + TANK_HEIGHT / 2);
        
        // Rotate tank
        g2.rotate(tankAngle);

        // Draw tank
        g2.drawImage(tankImage, -TANK_WIDTH / 2, -TANK_HEIGHT / 2, TANK_WIDTH, TANK_HEIGHT,null);

        g2.dispose();
    }
}