package ru.nsu.fit.akitov.billiards.view;

import javax.swing.*;
import java.awt.*;

public class CuePanel extends JComponent {
  private static final float DISPLAY_COEFFICIENT = 0.05f;
  private final Image body;
  private final int cueBallRadius;

  private final int width;
  private final int height;

  private int x;
  private int y;

  private float velocity;
  private float angle;

  public CuePanel(String path, int cueBallRadius) {
    body = Toolkit.getDefaultToolkit().getImage(path);
    this.cueBallRadius = cueBallRadius;
    width = 756;
    height = 45;
  }

  public void setPosition(int x, int y) {
    this.x = x - width;
    this.y = y - height / 2;
  }

  public void setAngle(float angle) {
    this.angle = angle;
  }

  public void setVelocity(float velocity) {
    this.velocity = velocity;
  }

  @Override
  public void paint(Graphics g) {
    super.paint(g);
    if (!isVisible()) {
      return;
    }
    Graphics2D g2d = (Graphics2D) g;
    int drawnX = (int) (x - cueBallRadius - DISPLAY_COEFFICIENT * velocity);
    int drawnY = y;
    g2d.rotate(angle, x + width, y + height / 2);
    g2d.drawImage(body, drawnX, drawnY, this);
  }
}
