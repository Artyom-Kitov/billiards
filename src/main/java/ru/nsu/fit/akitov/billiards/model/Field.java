package ru.nsu.fit.akitov.billiards.model;

import java.util.ArrayList;
import java.util.List;

public class Field {

  private static final float SURFACE_FRICTION = 40;

  private final float sizeX;
  private final float sizeY;

  private FieldListener listener;

  private final List<Pocket> pockets;
  private final List<Ball> balls;
  private Ball cueBall;
  private final Cue cue;

  public Field(int sizeX, int sizeY) {
    this.sizeX = sizeX;
    this.sizeY = sizeY;

    balls = new ArrayList<>();
    pockets = new ArrayList<>();
    for (int i = 0; i < 3; i++) {
      pockets.add(new Pocket(i * sizeX / 2.0f, 0));
      pockets.add(new Pocket(i * sizeX / 2.0f, sizeY));
    }

    cue = new Cue();
  }

  public Ball getCueBall() {
    return cueBall;
  }

  public void setListener(FieldListener listener) {
    this.listener = listener;
  }

  public void reset() {
    balls.clear();
    cueBall = new Ball(sizeX / 4 * 3, sizeY / 2);
    for (int i = 0; i < 16; i++) {
      // CR: pass from main
      balls.add(new Ball(400 + 64 * (i % 4), sizeY / 2 - 64 * 2 + 64 * (i / 4)));
    }
  }

  public boolean isMotionless() {
    if (!cueBall.isMotionless()) {
      return false;
    }
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
    cueBall.move(dt, SURFACE_FRICTION, 10);
    if (cueBall.getX() - Ball.RADIUS < 0 || cueBall.getX() + Ball.RADIUS > sizeX) {
      cueBall.setVelocity(-cueBall.getVelocityX(), cueBall.getVelocityY());
    }
    if (cueBall.getY() - Ball.RADIUS < 0 || cueBall.getY() + Ball.RADIUS > sizeY) {
      cueBall.setVelocity(cueBall.getVelocityX(), -cueBall.getVelocityY());
    }
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
    for (Ball ball : balls) {
      if (cueBall.collides(ball)) {
        cueBall.hit(ball);
      }
    }
    for (int i = 0; i < balls.size(); i++) {
      for (int j = i + 1; j < balls.size(); j++) {
        if (!balls.get(i).collides(balls.get(j))) {
          continue;
        }
        balls.get(i).hit(balls.get(j));
      }
    }
  }

  private void checkPockets() {
    for (Pocket pocket : pockets) {
      if (cueBall.isInPocket(pocket)) {
        // listener.cueBallInPocket
      }
    }
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
      listener.isMotionless();
      return;
    }
    move(dt);
    updateVelocities();
    checkPockets();
    listener.ballsMoved();
  }

  public void addCueVelocity(float dv) {
    cue.addVelocity(dv);
  }

  public void rotateCue(float theta) {
    cue.rotate(theta);
  }

  public float getCueVelocity() {
    return cue.getVelocity();
  }

  public float getCueAngle() {
    return cue.getAngle();
  }

  public void performCueStrike() {
    cue.strike(cueBall);
    listener.strikePerformed();
  }
}
