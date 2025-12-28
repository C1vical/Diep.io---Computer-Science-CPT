import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;

public class Bullet {
    private double worldX, worldY;
    private double speed = 6;
    private int size = 30;
    private double lifeTime = 100;
    private double angle;
    private boolean alive = true;

    private BufferedImage image;

    public Bullet(double worldX, double worldY, double angle, BufferedImage image) {
        this.worldX = worldX;
        this.worldY = worldY;
        this.angle = angle;
        this.image = image;
    }

    public void updateBullet() {
        worldX += Math.cos(angle) * speed;
        worldY += Math.sin(angle) * speed;
        lifeTime--;
        try {
            if (lifeTime < 60 & lifeTime > 30) {
                image = ImageIO.read(new File("src/assets/game/bullet-1.png"));
            } else if (lifeTime <= 30) {
                image = ImageIO.read(new File("src/assets/game/bullet-2.png"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (lifeTime <= 0) {
            alive = false;
        }
    }


    public void drawBullet(Graphics2D g2, int camX, int camY) {
        // Save current graphic state
        AffineTransform old = g2.getTransform();
        // Move the origin to the bullet's screen position
        g2.translate(worldX - camX, worldY - camY);
        // Rotate the graphics context to the bullet's angle
        g2.rotate(angle);
        // Draw the image centered at the origin and then translated to the front of the turret
        g2.drawImage(image, -size / 2 + 75, -size / 2, size, size, null);
        // Hitbox (for debugging)
        // g2.setColor(Color.GREEN);
        // g2.setStroke(new BasicStroke(2)); // thickness of border
        // g2.drawRect(-size / 2 + 75, -size / 2, size, size);
        // Restore original graphic state
        g2.setTransform(old);
    }

    public boolean getAlive() {
        return alive;
    }
}
