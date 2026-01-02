import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;

public class Bullet extends Sprite {
    // Bullet speed
    private double speed = 5;
    // Bullet lifetime
    private double lifeTime = 100;
    // Bullet alive status
    private boolean alive = true;
    
    public Bullet(double worldX, double worldY, double angle, BufferedImage image, int bulletSize, double speed) {
        super(worldX, worldY, image.getWidth(), image.getHeight(), image, 30, 30);
        this.angle = angle;
        this.width = bulletSize;
        this.height = bulletSize;
        this.speed = speed;
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

    public void drawBullet(Graphics2D g2, int camX, int camY, int tankSize) {
        AffineTransform old = g2.getTransform();
        g2.translate(worldX - camX, worldY - camY);
        g2.rotate(angle);
        g2.drawImage(image, (int) (-width / 2 + (3 * tankSize / 8) + (width / 2)), -height / 2, width, height, null);
        if (GamePanel.hitbox) {
            g2.setColor(Color.YELLOW);
            g2.setStroke(new BasicStroke(2)); // thickness of border
            g2.drawRect((int) (-width / 2 + ((3 * tankSize / 8) + (width / 2))), -height / 2, width, height);
        }
        g2.setTransform(old);
    }

    public boolean getAlive() {
        return alive;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }
}
