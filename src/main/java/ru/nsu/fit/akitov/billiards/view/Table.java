package ru.nsu.fit.akitov.billiards.view;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Table extends JComponent {

  private static final Point OFFSET = new Point(64, 46);

  private final Image background;
  private final List<Circle> balls;
  private final List<Circle> pockets;

  public Table(String path) {
    setLayout(null);
    background = Toolkit.getDefaultToolkit().getImage(path);
    balls = new ArrayList<>();
    pockets = new ArrayList<>();
    setVisible(true);
  }

  public void addBall(Circle ball) {
    ball.setLocation(ball.getX() + OFFSET.x, ball.getY() + OFFSET.y);
    balls.add(ball);
  }

  public void addPocket(Circle pocket) {
    pocket.setLocation(pocket.getX() + OFFSET.x, pocket.getY() + OFFSET.y);
    pockets.add(pocket);
  }

  public void updateBalls(List<Point> coordinates) {
    for (int i = 0; i < coordinates.size(); i++) {
      this.balls.get(i).setLocation(coordinates.get(i).x + OFFSET.x, coordinates.get(i).y + OFFSET.y);
    }
  }

  @Override
  public void paint(Graphics g) {
    super.paint(g);
    g.drawImage(background, 0, 0, this);
    for (Circle pocket : pockets) {
      pocket.paint(g);
    }
    for (Circle ball : balls) {
      ball.paint(g);
    }
  }
}
