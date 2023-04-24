package ru.nsu.fit.akitov.billiards.view;

import ru.nsu.fit.akitov.billiards.utils.Point2D;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class FieldView extends JComponent {

  private final Image background;
  private final List<BallView> balls;
  private int borderSize;
  private final BallView cueBall;
  private final List<BallView> pockets;
  private final CueView cueView;

  public FieldView(int width, int height, String path, BallView cueBall) {
    setLayout(null);
    this.setPreferredSize(new Dimension(width, height));
    background = Toolkit.getDefaultToolkit()
            .getImage(FieldView.class.getResource(path))
            .getScaledInstance(width, height, Image.SCALE_DEFAULT);
    balls = new ArrayList<>();
    pockets = new ArrayList<>();
    this.cueBall = cueBall;
    cueView = new CueView(width / 2, height / 16, cueBall.getRadius());
    cueView.setVisible(false);
    this.setVisible(true);
  }

  public void setBorderSize(int borderSize) {
    this.borderSize = borderSize;
  }

  public void addBall(BallView ball) {
    ball.setLocation(ball.getX() + borderSize, ball.getY() + borderSize);
    balls.add(ball);
  }

  public void setCueBallPosition(int x, int y) {
    cueBall.setLocation(x + borderSize, y + borderSize);
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

  public void addPocket(BallView pocket) {
    pocket.setLocation(pocket.getX() + borderSize, pocket.getY() + borderSize);
    pockets.add(pocket);
  }

  public void updateBalls(List<Point2D> coordinates) {
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
    g.drawImage(background, 0, 0, this);
    for (BallView pocket : pockets) {
      pocket.paint(g);
    }
    cueBall.paint(g);
    for (BallView ball : balls) {
      ball.paint(g);
    }
    cueView.paint(g);
    super.paint(g);
  }
}
