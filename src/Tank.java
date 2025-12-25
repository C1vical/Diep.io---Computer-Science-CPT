import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class Tank {
    // Tank coordinates in the "world"
    public double worldX = -1, worldY = -1;
     // Angle the tank is facing
    private double angle;
     // Tank movement speed
    private double speed = 5;
    // Tank dimensions
    private int width = 250;
    private int height = 250;

    private BufferedImage image;

    public Tank(double worldX, double worldY, BufferedImage image) {
        this.worldX = worldX;
        this.worldY = worldY;
        this.image = image;
    }

    public void updateMovement(boolean w, boolean a, boolean s, boolean d, int mapWidth, int mapHeight) {
        double moveX = 0;
        double moveY = 0;

        // If multiple keys are pressed, combine their effects
        // For example, if both w and s are pressed, they cancel each other out (1-1=0)
        if (w) moveY -= 1;
        if (s) moveY += 1;
        if (a) moveX -= 1;
        if (d) moveX += 1;

        // Normalize diagonal movement (so moving diagonally isn't faster)
        if (moveX != 0 && moveY != 0) {
            moveX /= Math.sqrt(2);
            moveY /= Math.sqrt(2);
        }

        // Apply speed
        worldX += moveX * speed;
        worldY += moveY * speed;

        // Make sure the tank can't leave the map bounds (acounting for tank size)
        if (worldX + width / 2 < 0) worldX = -width / 2;
        if (worldY + height / 2 < 0) worldY = -height / 2;
        if (worldX + width / 2 > mapWidth) worldX = mapWidth - width / 2;
        if (worldY + height / 2 > mapHeight) worldY = mapHeight - height / 2;
    }

    public void rotateTank(double mouseWorldX, double mouseWorldY) {
        // Center of the tank
        double centerX = worldX + width / 2.0;
        double centerY = worldY + height / 2.0;

        // Calculate the angle of rotation
        angle = Math.atan2(mouseWorldY - centerY, mouseWorldX - centerX);
    }

    public void draw(Graphics2D g2, int camX, int camY) {
        // Save current graphic state
        AffineTransform old = g2.getTransform();
        // Move the origin to the tank's screen position
        g2.translate(worldX - camX + width / 2.0, worldY - camY + height / 2.0);
        // Rotate the tank and the rest of the graphic state
        g2.rotate(angle);
        // Draw the image centered at the origin
        g2.drawImage(image, -width / 2, -height / 2, width, height, null);
        // Restore original graphic state
        g2.setTransform(old);
    }
    
    public int getWidth() {
        return width;
    }
    public int getHeight() {
        return height;
    }
}
