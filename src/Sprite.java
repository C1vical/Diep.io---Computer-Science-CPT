import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class Sprite {
    // Coordinates in the world
    protected double worldX;
    protected double worldY;
    // Angle the sprite is facing
    protected double angle;
    // Sprite dimensions
    protected int width, height;
    protected BufferedImage image;
    // Sprite health
    protected double health;
    protected double maxHealth;

    public Sprite(double worldX, double worldY, int width, int height, BufferedImage image, double health, double maxHealth) {
        this.worldX = worldX;
        this.worldY = worldY;
        this.width = width;
        this.height = height;
        this.image = image;
        this.health = health;
        this.maxHealth = maxHealth;
    }

    public void drawSprite(Graphics2D g2, int camX, int camY) {
        // Save current graphic state
        AffineTransform old = g2.getTransform();
        // Move the origin to the sprite's screen position
        g2.translate(worldX - camX + width / 2.0, worldY - camY + height / 2.0);
        // Rotate the graphics context to the sprite's angle
        g2.rotate(angle);
        // Draw the sprite centered at the origin
        g2.drawImage(image, -width / 2, -height / 2, width, height, null);
        // Hitbox (for debugging)
        if (GamePanel.hitbox) {
            g2.setColor(Color.BLUE);
            g2.setStroke(new BasicStroke(2)); // thickness of border
            g2.drawRect(-width / 2, -height / 2, width, height);
        }
        // Restore original graphic state
        g2.setTransform(old);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public double getWorldX() {
        return worldX;
    }

    public double getWorldY() {
        return worldY;
    }
    
    public double getAngle() {
        return angle;
    }
}
