package ru.nsu.fit.akitov.billiards.view;

import javax.swing.*;
import java.awt.*;

public class BallView extends JComponent {

  private final int radius;
  private final Color color;

  public BallView(int radius, Color color) {
    this.radius = radius;
    this.color = color;
  }

  public void update(int x, int y, boolean visible) {
    this.setLocation(x, y);
    this.setVisible(visible);
  }

  @Override
  public void paintComponent(Graphics g) {
    if (!isVisible()) {
      return;
    }
    g.setColor(color);
    g.fillOval(getX() - radius, getY() - radius, 2 * radius, 2 * radius);
  }
}
