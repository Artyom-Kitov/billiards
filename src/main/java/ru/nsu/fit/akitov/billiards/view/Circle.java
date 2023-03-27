package ru.nsu.fit.akitov.billiards.view;

import javax.swing.*;
import java.awt.*;

public class Circle extends JComponent {
  private final int radius;
  private final Color color;

  public Circle(int radius, int x, int y, Color color) {
    this.radius = radius;
    this.color = color;
    setLocation(x - radius / 2, y - radius / 2);
  }

  @Override
  public void paint(Graphics g) {
    g.drawOval(getX(), getY(), radius, radius);
    g.setColor(color);
    g.fillOval(getX(), getY(), radius, radius);
  }
}
