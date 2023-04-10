package ru.nsu.fit.akitov.billiards.view;

import ru.nsu.fit.akitov.billiards.utils.Point2D;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class FieldPanel extends JComponent {

  private final Image background;
  private final List<Circle> balls;
  private int borderSize;
  private Circle cueBall;
  private final List<Circle> pockets;
  private final CueView cueView;

  public FieldPanel(int width, int height, String path, int cueBallRadius) {
    setLayout(null);
    background = Toolkit.getDefaultToolkit()
            .getImage(Thread.currentThread().getContextClassLoader().getResource(path))
            .getScaledInstance(width, height, Image.SCALE_DEFAULT);
    balls = new ArrayList<>();
    pockets = new ArrayList<>();
    cueView = new CueView("cue.png", cueBallRadius);
    cueView.setVisible(false);
    this.setVisible(true);
  }

  public void setBorderSize(int borderSize) {
    this.borderSize = borderSize;
  }

  public void addBall(Circle ball) {
    ball.setLocation(ball.getX() + borderSize, ball.getY() + borderSize);
    balls.add(ball);
  }

  public void setCueBall(Circle cueBall) {
    cueBall.setLocation(cueBall.getX() + borderSize, cueBall.getY() + borderSize);
    this.cueBall = cueBall;
  }

  public void setCueVelocity(float velocity) {
    cueView.setVelocity(velocity);
  }

  public void setCueAngle(float angle) {
    cueView.setAngle(angle);
  }

  public void setCueVisible(boolean b) {
    cueView.setVisible(b);
  }

  public void addPocket(Circle pocket) {
    pocket.setLocation(pocket.getX() + borderSize, pocket.getY() + borderSize);
    pockets.add(pocket);
  }

  public void updateBalls(Point2D cueBall, List<Point2D> coordinates) {
    this.cueBall.setLocation(cueBall.x() + borderSize, cueBall.y() + borderSize);
    for (int i = 0; i < coordinates.size(); i++) {
      this.balls.get(i).setLocation(coordinates.get(i).x() + borderSize, coordinates.get(i).y() + borderSize);
    }
  }

  public void clear() {
    balls.clear();
  }

  public void removeBall(int index) {
    balls.remove(index);
  }

  public void moveCue() {
    cueView.setPosition(cueBall.getX(), cueBall.getY());
  }

  @Override
  public void paint(Graphics g) {
    super.paint(g);
    g.drawImage(background, 0, 0, this);
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
    cueView.paint(g);
  }
}
