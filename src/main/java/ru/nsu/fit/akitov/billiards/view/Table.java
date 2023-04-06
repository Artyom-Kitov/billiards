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
  private Circle cueBall;
  private final List<Circle> pockets;
  private final CuePanel cuePanel;

  public Table(String path) {
    setLayout(null);
    background = Toolkit.getDefaultToolkit().getImage(path);
    balls = new ArrayList<>();
    pockets = new ArrayList<>();
    // CR: hardcode
    cuePanel = new CuePanel("src/main/resources/cue.png", 30);
    cuePanel.setVisible(false);
    this.setVisible(true);
  }

  public void addBall(Circle ball) {
    ball.setLocation(ball.getX() + TABLE_OFFSET.x, ball.getY() + TABLE_OFFSET.y);
    balls.add(ball);
  }

  public void setCueBall(Circle cueBall) {
    cueBall.setLocation(cueBall.getX() + TABLE_OFFSET.x, cueBall.getY() + TABLE_OFFSET.y);
    this.cueBall = cueBall;
  }

  public void addPocket(Circle pocket) {
    pocket.setLocation(pocket.getX() + TABLE_OFFSET.x, pocket.getY() + TABLE_OFFSET.y);
    pockets.add(pocket);
  }

  public void updateBalls(Point cueBall, List<Point> coordinates) {
    this.cueBall.setLocation(cueBall.x + TABLE_OFFSET.x, cueBall.y + TABLE_OFFSET.y);
    for (int i = 0; i < coordinates.size(); i++) {
      this.balls.get(i).setLocation(coordinates.get(i).x + TABLE_OFFSET.x, coordinates.get(i).y + TABLE_OFFSET.y);
    }
  }

  public void clear() {
    balls.clear();
  }

  public CuePanel getCuePanel() {
    return cuePanel;
  }

  public void moveCue() {
    cuePanel.setPosition(cueBall.getX(), cueBall.getY());
  }

  @Override
  public void paint(Graphics g) {
    super.paint(g);
    g.drawImage(background, IMAGE_OFFSET.x, IMAGE_OFFSET.y, this);
    for (Circle pocket : pockets) {
      pocket.paint(g);
    }
    // CR: maybe draw even before new game
    if (cueBall != null) {
      cueBall.paint(g);
    }
    for (Circle ball : balls) {
      ball.paint(g);
    }
    cuePanel.paint(g);
  }
}
