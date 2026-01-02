import java.awt.image.BufferedImage;

public class Tank extends Sprite {
    // Tank movement speed
    private double speed = 5;
    // Tank dimensions default are set in constructor
    private int borderOverlap = 100;

    public Tank(double worldX, double worldY, BufferedImage image) {
        super(worldX, worldY, image.getWidth(), image.getHeight(), image, 100, 100);
        this.width = 200;
        this.height = 200;
    }

    public void getTankAngle(double mouseWorldX, double mouseWorldY) {
        // Center of the tank
        double centerX = worldX + width / 2.0;
        double centerY = worldY + height / 2.0;

        // Calculate the angle of rotation
        this.angle = Math.atan2(mouseWorldY - centerY, mouseWorldX - centerX);
    }

    public void updateMovement(boolean w, boolean a, boolean s, boolean d, int mapWidth, int mapHeight) {
        double moveX = 0;
        double moveY = 0;

        // If multiple keys are pressed, combine their effects
        // For example, if both w and s are pressed, they cancel each other out
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

        // Make sure the tank can't leave the map bounds (accounting for tank size and border overlap like in the real game)
        worldX = Math.max(-width / 2 - borderOverlap, Math.min(worldX, mapWidth - width / 2 + borderOverlap));
        worldY = Math.max(-height / 2 - borderOverlap, Math.min(worldY, mapHeight - height / 2 + borderOverlap));
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public double getSpeed() {
        return speed;
    }
}
