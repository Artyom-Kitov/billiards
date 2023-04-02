package ru.nsu.fit.akitov.billiards.view;

import javax.swing.*;
import java.awt.*;

public class Cue extends JComponent {
  private static final float MAX_FORCE = 4000.0f;
  private static final float DISPLAY_COEFFICIENT = 0.05f;
  private final Image body;
  private final int cueBallRadius;

  private final int width;
  private final int height;

  private int x;
  private int y;

  private float force;
  private float angle;

  public Cue(String path, int cueBallRadius) {
    body = Toolkit.getDefaultToolkit().getImage(path);
    this.cueBallRadius = cueBallRadius;
    width = 756;
    height = 45;
  }

  public void setPosition(int x, int y) {
    this.x = x - width;
    this.y = y - height / 2;
  }

  public void rotate(float theta) {
    angle += theta;
    if (angle >= Math.PI * 2) {
      angle -= Math.PI * 2;
    } else if (angle <= 0) {
      angle += Math.PI * 2;
    }
  }

  public void addForce(float df) {
    force += df;
    if (force < 0.0f) {
      force = 0.0f;
    } else if (force > MAX_FORCE) {
      force = MAX_FORCE;
    }
  }

  public float getAngle() {
    return angle;
  }

  public float getForce() {
    return force;
  }

  @Override
  public void paint(Graphics g) {
    super.paint(g);
    if (!isVisible()) {
      return;
    }
    Graphics2D g2d = (Graphics2D) g;
    int drawnX = (int) (x - cueBallRadius - DISPLAY_COEFFICIENT * force);
    int drawnY = y;
    g2d.rotate(angle, x + width, y + height / 2);
    g2d.drawImage(body, drawnX, drawnY, this);
  }
}
