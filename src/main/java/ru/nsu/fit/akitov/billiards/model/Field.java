package ru.nsu.fit.akitov.billiards.model;

import java.util.ArrayList;
import java.util.List;

public class Field {

  private static final float SURFACE_FRICTION = 30;

  private final float sizeX;
  private final float sizeY;

  private FieldListener listener;

  private final List<Pocket> pockets;
  private List<Ball> balls;
  private Ball cueBall;

  public Field(int sizeX, int sizeY) {
    this.sizeX = sizeX;
    this.sizeY = sizeY;

    pockets = new ArrayList<>();
    for (int i = 0; i < 3; i++) {
      pockets.add(new Pocket(i * sizeX / 2.0f, 0));
      pockets.add(new Pocket(i * sizeX / 2.0f, sizeY));
    }
  }

  public Ball getCueBall() {
    return cueBall;
  }

  public void setListener(FieldListener listener) {
    this.listener = listener;
  }

  public void reset() {
    balls = new ArrayList<>();
    cueBall = new Ball(200, 200);
    balls.add(cueBall);
    for (int i = 0; i < 15; i++) {
      balls.add(new Ball(400 + 62 * (i % 4), 400 + 62 * (i / 4)));
    }


  }

  public boolean isMotionless() {
    for (Ball ball : balls) {
      if (!ball.isMotionless()) {
        return false;
      }
    }
    return true;
  }

  public List<Ball> getBalls() {
    return balls;
  }

  public List<Pocket> getPockets() {
    return pockets;
  }

  private void move(float dt) {
    for (Ball ball : balls) {
      ball.move(dt, SURFACE_FRICTION, 10);
      if (ball.getX() - Ball.RADIUS < 0 || ball.getX() + Ball.RADIUS > sizeX) {
        ball.setVelocity(-ball.getVelocityX(), ball.getVelocityY());
        ball.move(dt, SURFACE_FRICTION, 10);
      }
      if (ball.getY() - Ball.RADIUS < 0 || ball.getY() + Ball.RADIUS > sizeY) {
        ball.setVelocity(ball.getVelocityX(), -ball.getVelocityY());
      }
    }
  }

  private void updateVelocities() {
    for (int i = 0; i < balls.size(); i++) {
      for (int j = i + 1; j < balls.size(); j++) {
        if (!balls.get(i).collides(balls.get(j))) {
          continue;
        }
        balls.get(i).hit(balls.get(j));
//      balls.get(i).move(dt, SURFACE_FRICTION, 10);
//      balls.get(j).move(dt, SURFACE_FRICTION, 10);
      }
    }
  }

  private void checkPockets() {
    for (int i = 0; i < balls.size(); i++) {
      for (Pocket pocket : pockets) {
        if (balls.get(i).isInPocket(pocket)) {
          listener.ballInPocket(i);
        }
      }
    }
  }

  public void update(float dt) {
    if (isMotionless()) {
      return;
    }
    move(dt);
    updateVelocities();
    checkPockets();
    listener.ballsMoved();
  }
}
