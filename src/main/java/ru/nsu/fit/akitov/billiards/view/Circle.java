package ru.nsu.fit.akitov.billiards.view;

import javax.swing.*;
import java.awt.*;

// CR: BallView?
public class Circle extends JComponent {

  private final int radius;
  private final Color color;

  public Circle(int radius, int x, int y, Color color) {
    this.radius = radius;
    this.color = color;
    setLocation(x, y);
  }

  public int getRadius() {
    return radius;
  }

  @Override
  public void paint(Graphics g) {
    super.paint(g);
    g.setColor(color);
    g.drawOval(getX() - radius, getY() - radius, 2 * radius, 2 * radius);
    g.fillOval(getX() - radius, getY() - radius, 2 * radius, 2 * radius);
  }
}
