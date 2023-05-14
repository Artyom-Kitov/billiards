package ru.nsu.fit.akitov.billiards.view;

import ru.nsu.fit.akitov.billiards.utils.BallModel;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class FieldView extends JPanel {

  private final Image background;
  private final List<BallView> balls;
  private int borderSize;
  private BallView cueBall;
  private final List<PocketView> pockets;
  private final CueView cueView;

  public FieldView(int width, int height, int cueBallRadius, String path) {
    this.setLayout(null);
    this.setPreferredSize(new Dimension(width, height));
    background = Toolkit.getDefaultToolkit()
            .getImage(FieldView.class.getResource(path))
            .getScaledInstance(width, height, Image.SCALE_DEFAULT);
    balls = new ArrayList<>();
    pockets = new ArrayList<>();
    cueBall = new BallView(0, 0, 0, Color.black);
    cueView = new CueView(width / 2, height / 16, cueBallRadius);
    this.setVisible(true);
  }

  public void setBorderSize(int borderSize) {
    this.borderSize = borderSize;
  }

  public void addCueBall(BallModel cueBall) {
    this.cueBall = new BallView(cueBall.radius(), (int) cueBall.position().x(), (int) cueBall.position().y(), Color.darkGray);
  }

  public void addBall(BallView ball) {
    ball.setLocation(ball.getX() + borderSize, ball.getY() + borderSize);
    balls.add(ball);
  }

  public void setCueBallPosition(int x, int y) {
    cueBall.setLocation(x + borderSize, y + borderSize);
    cueView.setPosition(x + borderSize, y + borderSize);
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

  public void addPocket(PocketView pocket) {
    pocket.setLocation(pocket.getX() + borderSize, pocket.getY() + borderSize);
    pockets.add(pocket);
  }

  public void updateBalls(List<BallModel> balls) {
    for (int i = 0; i < balls.size(); i++) {
      int x = (int) balls.get(i).position().x();
      int y = (int) balls.get(i).position().y();
      this.balls.get(i).setLocation(x + borderSize, y + borderSize);
    }
  }

  public void clear() {
    balls.clear();
  }

  public void removeBall(int index) {
    this.remove(balls.get(index));
    balls.remove(index);
  }

  public void moveCue() {
    cueView.setPosition(cueBall.getX(), cueBall.getY());
  }

  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    g.drawImage(background, 0, 0, this);
    for (PocketView pocket : pockets) {
      pocket.paintComponent(g);
    }
    for (BallView ball : balls) {
      ball.paintComponent(g);
    }
    cueBall.paintComponent(g);
    cueView.paintComponent(g);
  }
}
