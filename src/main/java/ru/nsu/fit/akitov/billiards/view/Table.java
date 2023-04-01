package ru.nsu.fit.akitov.billiards.view;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Table extends JComponent {

  private static final Point IMAGE_OFFSET = new Point(0, -10);
  private static final Point TABLE_OFFSET = new Point(60 + IMAGE_OFFSET.x, 46 + IMAGE_OFFSET.y);

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
    ball.setLocation(ball.getX() + TABLE_OFFSET.x, ball.getY() + TABLE_OFFSET.y);
    balls.add(ball);
  }

  public void addPocket(Circle pocket) {
    pocket.setLocation(pocket.getX() + TABLE_OFFSET.x, pocket.getY() + TABLE_OFFSET.y);
    pockets.add(pocket);
  }

  public void updateBalls(List<Point> coordinates) {
    for (int i = 0; i < coordinates.size(); i++) {
      this.balls.get(i).setLocation(coordinates.get(i).x + TABLE_OFFSET.x, coordinates.get(i).y + TABLE_OFFSET.y);
    }
  }

  public void clear() {
    balls.clear();
  }

  @Override
  public void paint(Graphics g) {
    super.paint(g);
    g.drawImage(background, IMAGE_OFFSET.x, IMAGE_OFFSET.y, this);
    for (Circle pocket : pockets) {
      pocket.paint(g);
    }
    for (Circle ball : balls) {
      ball.paint(g);
    }
  }
}
