import java.awt.image.BufferedImage;

public class Shape extends Sprite {
    // Shapes travel in a circular path over time
    // Shape center
    private double centerX, centerY;
    // Shape orbit parameters
    private double orbitRadius;
    private double orbitAngle; // Start at bottom of orbit
    private double orbitAngleSpeed;
    private double rotationSpeed;

    public Shape(BufferedImage image) {
        super(0, 0, image.getWidth(), image.getHeight(), image, 50, 50);
        orbitRadius = 100;
        centerX = orbitRadius - width / 2 + Math.random() * (GamePanel.getMapWidth() - orbitRadius * 2 - width / 2);
        centerY = orbitRadius - height / 2 + Math.random() * (GamePanel.getMapHeight() - orbitRadius * 2 - height / 2);
        orbitAngle = Math.random() * Math.PI * 2;
        orbitAngleSpeed = 0.001;
        this.angle = Math.random() * Math.PI * 2;
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
}
