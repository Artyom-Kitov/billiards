package ru.nsu.fit.akitov.billiards.view;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Table extends JComponent {
  private final Image background;
  private final List<Circle> circles;

  public Table(String path) {
    background = Toolkit.getDefaultToolkit().getImage(path);
    circles = new ArrayList<>();
    setVisible(true);
  }

  public void addCircle(Circle circle) {
    circles.add(circle);
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    g.drawImage(background, 0, 0, this);
    for (Circle circle : circles) {
      circle.paint(g);
    }
  }
}
