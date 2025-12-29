import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.*;

public class GamePanel extends JPanel {
    private Tank tank;
    private BufferedImage mapImage, borderTop, borderBottom, borderLeft, borderRight;
    private int borderThickness = 1000;
    // private int 
    private BufferedImage tankImage;

    // Camera coordinates - world coordinates of the top-left of the screen
    private int camX , camY;

    // World size
    private final int mapWidth = 1000;
    private final int mapHeight = 1000;

    // Tank dimensions
    private int width = 200;
    private int height = 200;

    // Input
    private boolean w, a, s, d;
    private int mouseX, mouseY; // Coordinates of the cursor in the screen (not world)
    private boolean autoFire;
    private boolean mouseDown = false;

    // Bullets
    private BufferedImage bulletImage;
    private ArrayList<Bullet> bullets = new ArrayList<>();
    private double reloadTime = 0;
    private double reload = 40;

    // Shapes
    private BufferedImage square;
    private BufferedImage triangle;
    private BufferedImage pentagon;
    private ArrayList<Shape> shapes = new ArrayList<>();

    // Labels
    private JLabel infoLabel;
    private JLabel autoFireLabel;

    public GamePanel() {
        try {
            // Load images
            tankImage = ImageIO.read(new File("src/assets/tanks/basic.png"));
            bulletImage = ImageIO.read(new File("src/assets/game/bullet.png"));
            square = ImageIO.read(new File("src/assets/shapes/square.png"));
            triangle = ImageIO.read(new File("src/assets/shapes/triangle.png"));
            pentagon = ImageIO.read(new File("src/assets/shapes/pentagon.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Draw map and border images
        mapImage = drawMap(3000, 20);
        borderTop = drawBorder(mapWidth, borderThickness, 20);
        borderBottom = drawBorder(mapWidth, borderThickness, 20);
        borderLeft = drawBorder(borderThickness, mapHeight, 20);
        borderRight = drawBorder(borderThickness, mapHeight, 20);

        // Place tank near the center of the square world
        tank = new Tank(mapWidth / 2 - width / 2, mapHeight / 2 - height / 2, tankImage);

        setFocusable(true);

        setupInput();
        startGameLoop();

        // Coordinates info label
        infoLabel = new JLabel("<html>tank: 0, 0<br>mouse: 0, 0</html>");
        infoLabel.setOpaque(true);
        infoLabel.setBackground(Color.RED);
        infoLabel.setForeground(Color.WHITE);
        infoLabel.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
        this.add(infoLabel);

        // Autofire label
        autoFireLabel = new JLabel("Autofire: OFF");
        autoFireLabel.setForeground(Color.WHITE);
        autoFireLabel.setOpaque(true);
        autoFireLabel.setBackground(new Color(0, 0, 0, 150));
        autoFireLabel.setBorder(BorderFactory.createEmptyBorder(4, 6, 4, 6));
        add(autoFireLabel);
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
        // Left mouse button click/drag
        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    mouseDown = true;
                }
            }
            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    mouseDown = false;
                }
            }
        });
        // Autofire (e pressed)
        addKeyListener(new KeyAdapter() {
            // Key pressed
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_E)
                    if (autoFire) {
                        autoFire = false;
                        autoFireLabel.setText("Autofire: OFF");

                    } else {
                        autoFire = true;
                        autoFireLabel.setText("Autofire: ON");
                    }
            }
        });
        // Generate shape (for testing purposes)
        addKeyListener(new KeyAdapter() {
            // Key pressed
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_Q) {
                    shapes.add(new Square(square));
                }

                if (e.getKeyCode() == KeyEvent.VK_T) {
                    shapes.add(new Triangle(triangle));
                }

                if (e.getKeyCode() == KeyEvent.VK_P) {
                    shapes.add(new Pentagon(pentagon));
                }
            }
        });
    }

    private void startGameLoop() {
        // Game loop every 15 ms
        Timer timer = new Timer(15, e -> {
            // Update movement
            tank.updateMovement(w, a, s, d, mapWidth, mapHeight);

            // Convert cursor screen coordinates to world coordinates
            double mouseWorldX = mouseX + camX;
            double mouseWorldY = mouseY + camY;

            // Rotate tank using the mouse world coordinates
            tank.rotateTank(mouseWorldX, mouseWorldY);

            // Update camera position
            updateCamera();

            // Handle shooting/reloading
            handleShooting();

            // Update info label
            infoLabel.setText("<html>camera: " + camX + ", " + camY + "<br>tank: " + ((int) tank.getWorldX() + tank.getWidth() / 2) + ", " + ((int) tank.getWorldY() + tank.getHeight() / 2) + "<br>mouse: " + (int) mouseWorldX + ", " + (int) mouseWorldY + "</html>");

            // Repaint
            repaint();
        });
        timer.start(); // Start the timer
    }

    private void updateCamera() {
        // Dimensions of the screen
        int screenWidth = getWidth();
        int screenHeight = getHeight();

        // Move the camera so that the tank is at the center of the screen (use tank center)
        camX = (int) Math.round(tank.getWorldX() + tank.getWidth() / 2.0 - screenWidth / 2.0);
        camY = (int) Math.round(tank.getWorldY() + tank.getHeight() / 2.0 - screenHeight / 2.0);

        // Maximum camera coordinates (allow border visibility)
        int maxCamX = Math.max(0, mapWidth - screenWidth + borderThickness);
        int maxCamY = Math.max(0, mapHeight - screenHeight + borderThickness);

        // Clamp camera position (to avoid showing beyond the map borders)
        camX = Math.max(-borderThickness, Math.min(camX, maxCamX));
        camY = Math.max(-borderThickness, Math.min(camY, maxCamY));
    } 

    private void handleShooting() {
        if (reloadTime > 0) {
            reloadTime--;
        }

        if ((mouseDown && reloadTime == 0) || (autoFire && reloadTime == 0)) {
            // Add a new bullet at the tank's position
            bullets.add(new Bullet(tank.getWorldX() + tank.getWidth() / 2, tank.getWorldY() + tank.getHeight() / 2, tank.getAngle(), bulletImage));
            reloadTime = reload; // Set reload time back to original value
        }
    }

    // This draws the game
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        int screenWidth = getWidth();
        int screenHeight = getHeight();

        int tileWidth = mapImage.getWidth();
        int tileHeight = mapImage.getHeight();

        // Draw map
        for (int x = -camX % tileWidth; x < screenWidth; x += tileWidth) {
            for (int y = -camY % tileHeight; y < screenHeight; y += tileHeight) {
                g2.drawImage(mapImage, x, y, null);
            }
        }

        // Draw top border
        for (int x = (-camX - borderThickness) % borderTop.getWidth(); x < screenWidth; x += borderTop.getWidth()) {
            g2.drawImage(borderTop, x, -camY - borderThickness, null);
        }

        // Draw bottom border
        for (int x = -camX % borderBottom.getWidth(); x < screenWidth; x += borderBottom.getWidth()) {
            g2.drawImage(borderBottom, x, -camY + mapHeight, null);
        }

        // Draw left border
        for (int y = -camY % borderLeft.getHeight(); y < screenHeight; y += borderLeft.getHeight()) {
            g2.drawImage(borderLeft, -camX - borderThickness, y, null);
        }

        // Draw right border
        for (int y = -camY % borderRight.getHeight(); y < screenHeight; y += borderRight.getHeight()) {
            g2.drawImage(borderRight, -camX + mapWidth, y, null);
        }

        // Draw tank
        tank.drawTank(g2, camX, camY);

        // Draw bullets
        for (Bullet bullet : bullets) {
            if (bullet.getAlive() == true) {
                bullet.updateBullet();
                bullet.drawBullet(g2, camX, camY);
            } else {
                bullets.remove(bullet);
                break;
            }
        }

        // Draw shapes
        for (Shape shape : shapes) {
            shape.updateShape();
            shape.drawShape(g2, camX, camY);
        }
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