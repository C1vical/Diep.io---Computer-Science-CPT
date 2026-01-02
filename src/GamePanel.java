import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class GamePanel extends JPanel {
    private Tank tank;
    private BufferedImage mapImage, borderTop, borderBottom, borderLeft, borderRight;
    private int borderThickness = 1000;
    // private int 
    private BufferedImage tankImage;

    // Camera coordinates - world coordinates of the top-left of the screen
    private int camX , camY;

    // World size
    private static int mapWidth = 2000;
    private static int mapHeight = 2000;

    // Tank dimensions
    private int width = 200;
    private int height = 200;

    // Input
    private boolean w, a, s, d;
    private int mouseX, mouseY; // Coordinates of the cursor in the screen (not world)
    private boolean autoFire;
    private boolean mouseDown = false;
    static boolean hitbox = false;

    // Bullets
    private BufferedImage bulletImage;
    private ArrayList<Bullet> bullets = new ArrayList<>();
    private double reloadTime = 0;
    private double reload = 40;
    private int bulletSize = 30;
    private int bulletSpeed = 5;

    // Shapes
    private BufferedImage square;
    private BufferedImage triangle;
    private BufferedImage pentagon;
    private ArrayList<Shape> shapes = new ArrayList<>();
    private int shapeCount = 0;

    // Labels
    private JLabel infoLabel;
    private JLabel autoFireLabel;
    private JLabel hitboxLabel;
    
    // Framerate
    private int frames = 0;
    private int fps = 0;
    private long lastTime = System.nanoTime();

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

        setFocusable(true);

        // Add key listeners
        setupInput();

        setLayout(new BorderLayout());

        // Labels
        JPanel labels = createLabels();
        labels.setOpaque(false);
        add(labels, BorderLayout.NORTH);

        // Sliders
        JPanel sliders = createSliders();
        sliders.setOpaque(false);
        add(sliders, BorderLayout.SOUTH);
    
        // Start the game loop
        startGameLoop();
    }

    private void setupInput() {
        // WASD inputs
        addKeyListener(new KeyAdapter() {
            // Key pressed
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_W) w = true;
                if (e.getKeyCode() == KeyEvent.VK_A) a = true;
                if (e.getKeyCode() == KeyEvent.VK_S) s = true;
                if (e.getKeyCode() == KeyEvent.VK_D) d = true;
            }

            // Key released
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_W) w = false;
                if (e.getKeyCode() == KeyEvent.VK_A) a = false;
                if (e.getKeyCode() == KeyEvent.VK_S) s = false;
                if (e.getKeyCode() == KeyEvent.VK_D) d = false;
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
                if (e.getButton() == MouseEvent.BUTTON1) mouseDown = true;
            }
            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) mouseDown = false;
            }
        });
        // Autofire (e pressed)
        addKeyListener(new KeyAdapter() {
            // Key pressed
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_E) {
                    if (autoFire) {
                        autoFire = false;
                        autoFireLabel.setText("Autofire: OFF");

                    } else {
                        autoFire = true;
                        autoFireLabel.setText("Autofire: ON");
                    }
                }  
            }
        });
        // Generate shape (for testing purposes)
        addKeyListener(new KeyAdapter() {
            // Key pressed
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_Q) shapes.add(new Shape(square));
                if (e.getKeyCode() == KeyEvent.VK_T) shapes.add(new Shape(triangle));
                if (e.getKeyCode() == KeyEvent.VK_P) shapes.add(new Shape(pentagon));
            }
        });
        // Hitboxes (b pressed)
        addKeyListener(new KeyAdapter() {
            // Key pressed
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_B) {
                    if (hitbox) {
                        hitbox = false;
                        hitboxLabel.setText("Hitbox: OFF");

                    } else {
                        hitbox = true;
                        hitboxLabel.setText("Hitbox: ON");
                    }
                }  
            }
        });
    }

    private void startGameLoop() {
        // Place tank near the center of the square world
        tank = new Tank(mapWidth / 2 - width / 2, mapHeight / 2 - height / 2, tankImage);
        // Game loop every 15 ms
        Timer timer = new Timer(15, e -> {
            requestFocusInWindow();
            // Update tank
            // Convert cursor screen coordinates to world coordinates
            double mouseWorldX = mouseX + camX;
            double mouseWorldY = mouseY + camY;
            updateTank(mouseWorldX, mouseWorldY);

            // Update camera position
            updateCamera();
            
            // Handle shooting/reloading
            handleShooting();

            // Shape generation
            if (shapeCount < 10) { // Generate 10 shapes
                shapeCount++;
                int shapeType = (int) (Math.random() * 3); // Random number between 0-2
                if (shapeType == 0) {
                    shapes.add(new Shape(square));
                } else if (shapeType == 1) {
                    shapes.add(new Shape(triangle));
                } else {
                    shapes.add(new Shape(pentagon));
                }
            }

            // Update info label
            infoLabel.setText("<html>Camera: " + camX + ", " + camY + "<br>Tank: " + ((int) tank.getWorldX() + tank.getWidth() / 2) + ", " + ((int) tank.getWorldY() + tank.getHeight() / 2) + "<br>Mouse: " + (int) mouseWorldX + ", " + (int) mouseWorldY + "</html>");

            // Repaint
            repaint();
        });
        timer.start(); // Start the timer
    }

    private void updateTank(double mouseWorldX, double mouseWorldY) {
        // Get the angle of the tank
        tank.getTankAngle(mouseWorldX, mouseWorldY);

        // Update movement
        tank.updateMovement(w, a, s, d, mapWidth, mapHeight);
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
            bullets.add(new Bullet(tank.getWorldX() + tank.getWidth() / 2, tank.getWorldY() + tank.getHeight() / 2, tank.getAngle(), bulletImage, bulletSize, bulletSpeed));
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

        // Draw shapes
        for (Shape shape : shapes) {
            shape.updateShape();
            // Shape's screen coordinates
            double shapeScreenX = shape.getWorldX() - camX;
            double shapeScreenY = shape.getWorldY() - camY;
            // Only draw if the shape is in the screen
            if (shapeScreenX >= 0 && shapeScreenX <= getWidth() && shapeScreenY >= 0 && shapeScreenY <= getHeight()) {
                shape.drawSprite(g2, camX, camY);
            }
        }

        // Draw fps
        updateFPS();
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.BOLD, 20));
        g2.drawString("FPS: " + fps, 10, 20);

        // Draw tank
        tank.drawSprite(g2, camX, camY);

        // Draw bullets
        for (Bullet bullet : bullets) {
            if (bullet.getAlive() == true) {
                bullet.updateBullet();
                // Bullet's screen coordinates
                double bulletScreenX = bullet.getWorldX() - camX;
                double bulletScreenY = bullet.getWorldY() - camY;
                // Only draw if the bullet is in the screen
                if (bulletScreenX >= 0 && bulletScreenX <= getWidth() && bulletScreenY >= 0 && bulletScreenY <= getHeight()) {
                    bullet.drawBullet(g2, camX, camY, tank.width);
                }
            } else {
                bullets.remove(bullet);
                break;
            }
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

    private JPanel createLabels() {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());
        // Coordinates
        infoLabel = new JLabel("<html>Camera: <br>Tank: <br>Mouse: ");
        infoLabel.setOpaque(true);
        infoLabel.setBackground(Color.RED);
        infoLabel.setForeground(Color.WHITE);
        infoLabel.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
        panel.add(infoLabel);

        // Autofire
        autoFireLabel = new JLabel("Autofire: OFF");
        autoFireLabel.setForeground(Color.WHITE);
        autoFireLabel.setOpaque(true);
        autoFireLabel.setBackground(new Color(0, 0, 0, 150));
        autoFireLabel.setBorder(BorderFactory.createEmptyBorder(4, 6, 4, 6));
        panel.add(autoFireLabel);

         // Hitbox
        hitboxLabel = new JLabel("Hitbox: OFF");
        hitboxLabel.setForeground(Color.WHITE);
        hitboxLabel.setOpaque(true);
        hitboxLabel.setBackground(new Color(0, 0, 0, 150));
        hitboxLabel.setBorder(BorderFactory.createEmptyBorder(4, 6, 4, 6));
        panel.add(hitboxLabel);

        return(panel);
    }

    private JPanel createSliders() {
        JPanel slidersPanel = new JPanel();
        slidersPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 5));

        // Movement speed
        JSlider movementSpeedSlider = new JSlider(5, 100, 5);
        JLabel movementSpeedLabel = new JLabel("Movement Speed: " + 5);
        // Detects change in the slider
        movementSpeedSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                JSlider source = (JSlider) e.getSource();
                movementSpeedLabel.setText("Movement Speed: " + source.getValue());
                tank.setSpeed(source.getValue());
            }
        });
        // Add the slider + the text label to the panel
        slidersPanel.add(makeSliderPanel(movementSpeedLabel, movementSpeedSlider));

        // Repeat for the rest of the sliders

        // Reload speed
        JSlider reloadSpeedSlider = new JSlider(5, 100, (int) reload);
        JLabel reloadSpeedLabel = new JLabel("Reload Speed: " + (int) reload);

        reloadSpeedSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                JSlider source = (JSlider) e.getSource();
                reloadSpeedLabel.setText("Reload Speed: " + source.getValue());
                reload = source.getValue();
            }
        });

        slidersPanel.add(makeSliderPanel(reloadSpeedLabel, reloadSpeedSlider));

        // Tank size
        JSlider tankSizeSlider = new JSlider(100, 1000, 200);
        JLabel tankSizeLabel = new JLabel("Tank Size: " + 200);
        
        tankSizeSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                JSlider source = (JSlider) e.getSource();
                tankSizeLabel.setText("Tank Size: " + source.getValue());
                tank.width = source.getValue();
                tank.height = source.getValue();
            }
        });

        slidersPanel.add(makeSliderPanel(tankSizeLabel, tankSizeSlider));

        // Bullet size
        JSlider bulletSizeSlider = new JSlider(10, 300, bulletSize);
        JLabel bulletSizeLabel = new JLabel("Bullet Size: " + bulletSize);

        bulletSizeSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                JSlider source = (JSlider) e.getSource();
                bulletSizeLabel.setText("Bullet Size: " + source.getValue());
                bulletSize = source.getValue();
            }
        });
        slidersPanel.add(makeSliderPanel(bulletSizeLabel, bulletSizeSlider));

        // Bulllet speed
        JSlider bulletSpeedSlider = new JSlider(3, 15, bulletSpeed);
        JLabel bulletSpeedLabel = new JLabel("Bullet Speed: " + bulletSpeed);

        bulletSpeedSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                JSlider source = (JSlider) e.getSource();
                bulletSpeedLabel.setText("Bullet Speed: " + source.getValue());
                bulletSpeed = source.getValue();
            }
        });
        slidersPanel.add(makeSliderPanel(bulletSpeedLabel, bulletSpeedSlider));

        
        return(slidersPanel);
    }

    private JPanel makeSliderPanel(JLabel label, JSlider slider) {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBackground(new Color(60, 75, 85, 150));
        label.setForeground(Color.WHITE);
        slider.setBackground(new Color(60, 75, 85, 150));

        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        slider.setAlignmentX(Component.CENTER_ALIGNMENT);

        p.add(label);
        p.add(slider);
        
        return p;
    }

    public static int getMapWidth() {
        return mapWidth;
    }

    public static int getMapHeight() {
        return mapHeight;
    }

    private void updateFPS() {
        frames++;
        long currentTime = System.nanoTime();

        if (currentTime - lastTime >= 1_000_000_000L) {
            fps = frames;
            frames = 0;
            lastTime = currentTime;
        }
    }

}