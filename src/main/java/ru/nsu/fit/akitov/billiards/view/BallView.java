package ru.nsu.fit.akitov.billiards.view;

import javax.swing.*;
import java.awt.*;

public class BallView extends JComponent {

  private final int radius;
  private final Color color;

  public BallView(int radius, int x, int y, Color color) {
    this.radius = radius;
    this.setLocation(x, y);
    this.color = color;
  }

  @Override
  public void paintComponent(Graphics g) {
    g.setColor(color);
    g.drawOval(getX() - radius, getY() - radius, 2 * radius, 2 * radius);
    g.fillOval(getX() - radius, getY() - radius, 2 * radius, 2 * radius);
  }
}
