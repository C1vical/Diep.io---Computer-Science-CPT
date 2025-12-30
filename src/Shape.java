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
    private double angle; // Rotation angle
    private double rotationSpeed;


    private int size;
    private BufferedImage image;

    public Shape(BufferedImage image) {
        this.image = image;
        size = image.getWidth();
        orbitRadius = 100;
        centerX = orbitRadius - size / 2 + Math.random() * (GamePanel.getMapWidth() - orbitRadius * 2 - size / 2);
        centerY = orbitRadius - size / 2 + Math.random() * (GamePanel.getMapHeight() - orbitRadius * 2 - size / 2);
        orbitAngle = Math.random() * Math.PI * 2;
        orbitAngleSpeed = 0.002;
        angle = Math.random() * Math.PI * 2;
        rotationSpeed = 0.002;
    }

    public void updateShape() {
        // Update orbit angle
        orbitAngle += orbitAngleSpeed;
        // Update rotation angle
        angle += rotationSpeed;
        // Calculate new world position based on circular orbit
        worldX = centerX + Math.cos(orbitAngle) * orbitRadius;
        worldY = centerY + Math.sin(orbitAngle) * orbitRadius;
    }

    public void drawShape(Graphics2D g2, int camX, int camY) {
        // Save current graphic state
        AffineTransform old = g2.getTransform();
        // Move the origin to the shape's screen position
        g2.translate(worldX - camX + size / 2.0, worldY - camY + size / 2.0);
        // Rotate the tank and the rest of the graphic state
        g2.rotate(angle);
        // Draw the image
        g2.drawImage(image, -size / 2, -size / 2, size, size, null);
        // Hitbox (for debugging)
        // g2.setColor(Color.BLUE);
        // g2.setStroke(new BasicStroke(2)); // thickness of border
        // g2.drawRect(-size / 2, -size / 2, size, size);
        // Restore original graphic state
        g2.setTransform(old);
        
    }
    
}
