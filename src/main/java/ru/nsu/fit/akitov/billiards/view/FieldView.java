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

  public void setCueVisible(boolean b) {
    cueView.setVisible(b);
    moveCue();
  }

  public void addPocket(PocketView pocket) {
    pocket.setLocation(pocket.getX() + borderSize, pocket.getY() + borderSize);
    pockets.add(pocket);
  }

  // CR: pass player ball separately
  public void updateBalls(List<BallModel> balls) {
    for (int i = 0; i < balls.size(); i++) {
      int x = (int) (balls.get(i).position().x() + borderSize);
      int y = (int) (balls.get(i).position().y() + borderSize);
      this.balls.get(i).update(x, y, balls.get(i).isAvailable());
    }
  }

  public void clear() {
    balls.clear();
  }

  public void moveCue() {
    if (balls.size() < 1) {
      return;
    }
    cueView.setPosition(balls.get(0).getX(), balls.get(0).getY());
  }

  public void resetBalls(List<BallModel> balls) {
    this.balls.clear();
    for (int i = 0; i < balls.size(); i++) {
      Color color = (i == 0) ? Color.darkGray : Color.white;
      this.balls.add(new BallView(balls.get(i).radius(), color));
    }
    updateBalls(balls);
    moveCue();
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
    cueView.paintComponent(g);
  }
}
