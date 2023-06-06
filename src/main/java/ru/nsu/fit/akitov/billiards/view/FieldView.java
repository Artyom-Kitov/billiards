package ru.nsu.fit.akitov.billiards.view;

import ru.nsu.fit.akitov.billiards.dto.BallModel;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class FieldView extends JPanel {

  private final Image background;
  private final BallView cueBall;
  private final List<BallView> balls;
  private int borderSize;
  private final List<PocketView> pockets;
  private final CueView cueView;

  public FieldView(int width, int height, int cueBallRadius, String path) {
    this.setLayout(null);
    this.setPreferredSize(new Dimension(width, height));
    background = Toolkit.getDefaultToolkit()
            .getImage(FieldView.class.getResource(path))
            .getScaledInstance(width, height, Image.SCALE_DEFAULT);

    cueBall = new BallView(cueBallRadius, Color.darkGray);
    balls = new ArrayList<>();
    pockets = new ArrayList<>();

    cueView = new CueView(width / 2, height / 16, cueBallRadius);
    this.setVisible(true);
  }

  public void setBorderSize(int borderSize) {
    this.borderSize = borderSize;
  }

  public void setCueVelocity(float velocity) {
    cueView.setVelocity(velocity);
  }

  public void setCueAngle(float angle) {
    cueView.setAngle(angle);
  }

  public void setCueBall(BallView cueBall) {
    cueBall.setLocation(cueBall.getX() + borderSize, cueBall.getY() + borderSize);
  }

  public void setCueVisible(boolean b) {
    cueView.setVisible(b);
    moveCue();
  }

  public void addPocket(PocketView pocket) {
    pocket.setLocation(pocket.getX() + borderSize, pocket.getY() + borderSize);
    pockets.add(pocket);
  }

  public void addBall(BallView ball) {
    ball.setLocation(ball.getX() + borderSize, ball.getY() + borderSize);
    this.balls.add(ball);
  }

  public void updateCueBall(BallModel cueBall) {
    int x = (int) cueBall.position().x() + borderSize;
    int y = (int) cueBall.position().y() + borderSize;
    this.cueBall.update(x, y, cueBall.isAvailable());
  }

  public void updateBalls(List<BallModel> balls) {
    for (int i = 0; i < balls.size(); i++) {
      int x = (int) (balls.get(i).position().x() + borderSize);
      int y = (int) (balls.get(i).position().y() + borderSize);
      this.balls.get(i).update(x, y, balls.get(i).isAvailable());
    }
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
