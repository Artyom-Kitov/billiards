package ru.nsu.fit.akitov.billiards.view;

import javax.swing.*;
import java.awt.*;

public class PocketView extends JComponent {

  private final int radius;

  public PocketView(int radius, int x, int y) {
    this.radius = radius;
    this.setLocation(x, y);
  }

  @Override
  public void paintComponent(Graphics g) {
    g.setColor(Color.black);
    g.fillOval(getX() - radius, getY() - radius, 2 * radius, 2 * radius);
  }
}
