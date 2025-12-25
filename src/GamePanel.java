import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;

public class GamePanel extends JPanel {
    private Tank tank;
    private BufferedImage mapImage, borderTop, borderBottom, borderLeft, borderRight;
    private int borderThickness = 120;
    private BufferedImage tankImage;

    // Camera coordinates - world coordinates of the top-left of the screen
    private int camX = -1, camY = -1;
    private JLabel worldLabel;


    // World size
    private final int mapWidth = 3000;
    private final int mapHeight = 3000;

    // Input
    private boolean w, a, s, d;
    private int mouseX, mouseY; // Coordinates of the cursor in the screen (not world)

    public GamePanel() {
        try {
            // Load images
            tankImage = ImageIO.read(new File("src/assets/tanks/basic.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Create map and tile image
        mapImage = drawMap(3000, 20);
        borderTop = drawBorder(mapWidth, borderThickness, 20);
        borderBottom = drawBorder(mapWidth, borderThickness, 20);
        borderLeft = drawBorder(borderThickness, mapHeight, 20);
        borderRight = drawBorder(borderThickness, mapHeight, 20);

        // Place tank near the center of the square world
        tank = new Tank(mapWidth / 2 - 125, mapHeight / 2 - 125, tankImage);

        setFocusable(true);
<<<<<<< HEAD

        setupInput();
        startGameLoop();

        // Cordinates info label
=======
        setupInput();
        startGameLoop();
        //fixed
        // Small on-screen camera info label (updated each tick)
        cameraLabel = new JLabel("<html>camX: 0<br>camY: 0</html>");
        cameraLabel.setOpaque(true);
        cameraLabel.setBackground(Color.RED);
        cameraLabel.setForeground(Color.WHITE);
        cameraLabel.setBorder(BorderFactory.createEmptyBorder(4,4,4,4));
        this.add(cameraLabel);

        // Small on-screen world info label (updated each tick)
>>>>>>> a0845d19cbe1969dacff945bab0effad9af0bcb9
        worldLabel = new JLabel("<html>tank: 0, 0<br>mouse: 0, 0</html>");
        worldLabel.setOpaque(true);
        worldLabel.setBackground(Color.DARK_GRAY);
        worldLabel.setForeground(Color.WHITE);
        worldLabel.setBorder(BorderFactory.createEmptyBorder(4,4,4,4));
        this.add(worldLabel);
    }

    private void setupInput() {
        // WASD inputs
        addKeyListener(new KeyAdapter() {
            // Key pressed
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_W)
                    w = true;
                if (e.getKeyCode() == KeyEvent.VK_A)
                    a = true;
                if (e.getKeyCode() == KeyEvent.VK_S)
                    s = true;
                if (e.getKeyCode() == KeyEvent.VK_D)
                    d = true;
            }
            // Key released
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_W)
                    w = false;
                if (e.getKeyCode() == KeyEvent.VK_A)
                    a = false;
                if (e.getKeyCode() == KeyEvent.VK_S)
                    s = false;
                if (e.getKeyCode() == KeyEvent.VK_D)
                    d = false;
            }
        });
        // Mouse cursor coordinates
        addMouseMotionListener(new MouseMotionAdapter() {
            // Cursor moved
            public void mouseMoved(MouseEvent e) {
                mouseX = e.getX();
                mouseY = e.getY();
            }
            // Cursor dragged
            public void mouseDragged(MouseEvent e) {
                mouseX = e.getX();
                mouseY = e.getY();
            }
        });
    }

    private void startGameLoop() {
        // In timer (repeats every 15 ms)
        Timer timer = new Timer(15, e -> {
            // Update movement
            tank.updateMovement(w, a, s, d, mapWidth, mapHeight);

            // Convert cursor screen coordinates to world coordinates
            double mouseWorldX = mouseX + camX;
            double mouseWorldY = mouseY + camY;

            // Rotate tank using the mouse world coordinates
            tank.rotateTank(mouseWorldX, mouseWorldY);

            // Dimensions of the screen
            int screenWidth = getWidth();
            int screenHeight = getHeight();

            // Move the camera so that the tank is at the center of the screen (use tank center)
            camX = (int) Math.round(tank.worldX + tank.width / 2.0 - screenWidth / 2.0);
            camY = (int) Math.round(tank.worldY + tank.height / 2.0 - screenHeight / 2.0);
            
            // "Clamp" the camera to ensure we don't see beyond the world (just a bit of the border is ok)
            // Maximum camera coordinates
            int maxCamX = Math.max(0, mapWidth - screenWidth + borderThickness);
            int maxCamY = Math.max(0, mapHeight - screenHeight + borderThickness);
            if (camX < -borderThickness) camX = -borderThickness;
            if (camY < -borderThickness) camY = -borderThickness;
            if (camX > maxCamX) camX = maxCamX;
            if (camY > maxCamY) camY = maxCamY;
<<<<<<< HEAD
            
            // Update info label
            worldLabel.setText("<html>camera: " + camX + ", " + camY + "<br>tank: " + ((int) tank.worldX + tank.getWidth() / 2) + ", " + ((int) tank.worldY + tank.getHeight() / 2) + "<br>mouse: " + (int)mouseWorldX + ", " + (int)mouseWorldY + "</html>");
=======

            // Update the persistent camera label instead of creating a new field each tick
            cameraLabel.setText("<html>camX: " + camX + "<br>camY: " + camY + "</html>");
            // Update world label with tank center world coords and mouse world coords
            worldLabel.setText("<html>tank: " + (int)(tank.worldX + tank.width/2.0) + ", " + (int)(tank.worldY + tank.height/2.0) + "<br>mouse: " + (int)mouseWorldX + ", " + (int)mouseWorldY + "</html>");
>>>>>>> a0845d19cbe1969dacff945bab0effad9af0bcb9

            // Repaint
            repaint();
        });
        timer.start(); // Start the timer
    }

    // This draws the game
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        // Draw map
        for (int x = -camX % mapImage.getWidth(); x < getWidth(); x += mapImage.getWidth()) {
            for (int y = -camY % mapImage.getHeight(); y < getHeight(); y += mapImage.getHeight()) {
                g2.drawImage(mapImage, x, y, null);
            }
        }

        // Draw top border
        for (int x = -camX - borderThickness % borderTop.getWidth(); x < getWidth(); x += borderTop.getWidth()) {
            g2.drawImage(borderTop, x, -camY - borderThickness, null);
        }

        // Draw bottom border
        for (int x = -camX % borderBottom.getWidth(); x < getWidth(); x += borderBottom.getWidth()) {
            g2.drawImage(borderBottom, x, -camY + mapHeight, null);
        }

        // Draw left border
        for (int y = -camY % borderLeft.getHeight(); y < getHeight(); y += borderLeft.getHeight()) {
            g2.drawImage(borderLeft, -camX - borderThickness, y, null);
        }

        // Draw right border
        for (int y = -camY % borderRight.getHeight(); y < getHeight (); y += borderRight.getHeight()) {
            g2.drawImage(borderRight, -camX + mapWidth, y, null);
        }

        // Draw tank
        tank.draw(g2, camX, camY);
    }

    private BufferedImage drawMap(int tileSize, int gridSize) {
        BufferedImage img = new BufferedImage(tileSize, tileSize, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = img.createGraphics();

        // Background
        g2.setColor(new Color(65, 65, 65));
        g2.fillRect(0, 0, tileSize, tileSize);

        // Grid lines
        g2.setColor(new Color(78, 78, 78));
        g2.setStroke(new BasicStroke(1));

        for (int i = 0; i <= tileSize; i += gridSize) {
            g2.drawLine(i, 0, i, tileSize); // Vertical
            g2.drawLine(0, i, tileSize, i); // Horizontal
        }

        g2.dispose();
        return img;
    }

    private BufferedImage drawBorder(int tileWidth, int tileHeight, int gridSize) {
        BufferedImage img = new BufferedImage(tileWidth, tileHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = img.createGraphics();

        // Background
        g2.setColor(new Color(34, 34, 34));
        g2.fillRect(0, 0, tileWidth, tileHeight);

        // Grid lines
        g2.setColor(new Color(45, 45, 45));
        g2.setStroke(new BasicStroke(1));

        for (int i = 0; i <= tileWidth; i += gridSize) {
            g2.drawLine(i, 0, i, tileHeight); // Vertical
        }
        for (int i = 0; i <= tileHeight; i += gridSize) {
            g2.drawLine(0, i, tileWidth, i); // Horizontal
        }
        
        g2.dispose();
        return img;
    }

}