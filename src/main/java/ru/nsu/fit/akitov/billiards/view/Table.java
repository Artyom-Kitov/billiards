package ru.nsu.fit.akitov.billiards.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class Table extends JComponent {

  private static final Point IMAGE_OFFSET = new Point(0, -10);
  private static final Point TABLE_OFFSET = new Point(60 + IMAGE_OFFSET.x, 46 + IMAGE_OFFSET.y);

  private final Image background;
  private final List<Circle> balls;
  private Circle cueBall;
  private final List<Circle> pockets;
  private final Cue cue;

  public Table(String path) {
    setLayout(null);
    background = Toolkit.getDefaultToolkit().getImage(path);
    balls = new ArrayList<>();
    pockets = new ArrayList<>();
    cue = new Cue("src/main/images/cue.png", 30);
    cue.setVisible(false);
    this.setVisible(true);

    this.addKeyListener(new KeyAdapter() {
      @Override
      public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_A) {
          cue.rotate((float) Math.PI / 180.0f);
        } else if (e.getKeyCode() == KeyEvent.VK_D) {
          cue.rotate((float) Math.PI / -180.0f);
        }
      }
    });
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

  public Cue getCue() {
    return cue;
  }

  public void moveCue() {
    cue.setPosition(cueBall.getX(), cueBall.getY());
  }

  @Override
  public void paint(Graphics g) {
    super.paint(g);
    g.drawImage(background, IMAGE_OFFSET.x, IMAGE_OFFSET.y, this);
    for (Circle pocket : pockets) {
      pocket.paint(g);
    }
    if (cueBall != null) {
      cueBall.paint(g);
    }
    for (Circle ball : balls) {
      ball.paint(g);
    }
    cue.paint(g);
  }
}
