package ru.nsu.fit.akitov.billiards.view;

import javax.swing.*;
import java.awt.*;

public class CueView extends JComponent {
  private static final float VELOCITY_DISPLAY_COEFFICIENT = 0.02f;
  private final Image body;
  private final int cueBallRadius;

  private final int width;
  private final int height;

  private int x;
  private int y;

  private float velocity;
  private float angle;

  public CueView(int width, int height, int cueBallRadius) {
    Image image = Toolkit.getDefaultToolkit().getImage(CueView.class.getResource("/cue.png"));
    body = image.getScaledInstance(width, height, Image.SCALE_DEFAULT);
    this.cueBallRadius = cueBallRadius;
    this.width = width;
    this.height = height;
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
    int drawnX = (int) (x - cueBallRadius - VELOCITY_DISPLAY_COEFFICIENT * velocity);
    int drawnY = y;
    g2d.rotate(angle, x + width, y + height / 2f);
    g2d.drawImage(body, drawnX, drawnY, this);
  }
}
