import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;

public class Shape {
    private double worldX, worldY;

    // Shapes travel in a circular path over time
    private double centerX, centerY;
    private double orbitRadius;
    private double orbitAngle; // Start at bottom of orbit
    private double orbitAngleSpeed;


    private int size;
    private BufferedImage image;

    public Shape(BufferedImage image) {
        this.image = image;
        size = image.getWidth();

        // Randomize orbit parameters within a typical map range
        centerX = Math.random() * 1000; // place somewhere in the world (map ~1000)
        centerY = Math.random() * 1000;
        orbitRadius = 20; // radius between 50 and 350
        orbitAngle = Math.random() * Math.PI * 2;
        orbitAngleSpeed = 0.003;
    }

    public void updateShape() {
        // Update orbit angle
        orbitAngle += orbitAngleSpeed;
        // Calculate new world position based on circular orbit
        worldX = centerX + Math.cos(orbitAngle) * orbitRadius;
        worldY = centerY + Math.sin(orbitAngle) * orbitRadius;
    }

    public void drawShape(Graphics2D g2, int camX, int camY) {
        // Draw the image at its world position adjusted for camera
        g2.drawImage(image, (int)(worldX - camX - size / 2), (int)(worldY - camY - size / 2), size, size, null);
    }
    
}
