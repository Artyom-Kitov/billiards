package ru.nsu.fit.akitov.billiards.model;

import ru.nsu.fit.akitov.billiards.utils.ClockTime;
import ru.nsu.fit.akitov.billiards.utils.GameProperties;
import ru.nsu.fit.akitov.billiards.utils.Point2D;

import java.util.ArrayList;
import java.util.List;

public class Field {

  private final GameProperties properties;

  private static final float SURFACE_FRICTION = 40;
  private static final float GRAVITY = 10;

  private final float sizeX;
  private final float sizeY;

  private final float ballRadius;
  private final List<Ball> balls;

  private final float pocketRadius;
  private final List<Pocket> pockets;

  private final Cue cue;
  private Ball cueBall;

  private final Clock clock;

  private FieldListener listener;

  public Field(GameProperties properties) {
    this.sizeX = properties.fieldSize() * 2;
    this.sizeY = properties.fieldSize();
    this.ballRadius = (float) properties.fieldSize() / properties.relativeBallSize() / 2;
    this.pocketRadius = (float) properties.fieldSize() / properties.relativePocketSize() / 2;
    this.properties = properties;

    balls = new ArrayList<>();
    pockets = new ArrayList<>();
    for (int i = 0; i < 3; i++) {
      pockets.add(new Pocket(i * sizeX / 2.0f, 0, pocketRadius));
      pockets.add(new Pocket(i * sizeX / 2.0f, sizeY, pocketRadius));
    }

    cue = new Cue(properties.fieldSize() * properties.relativeCueStrength());
    clock = new Clock();
    reset();
  }
  public Ball getCueBall() {
    return cueBall;
  }

  public float getBallRadius() {
    return ballRadius;
  }

  public float getPocketRadius() {
    return pocketRadius;
  }

  public void setListener(FieldListener listener) {
    this.listener = listener;
  }

  public void reset() {
    balls.clear();
    clock.reset();
    cueBall = new Ball(sizeX / 4, sizeY / 2, ballRadius);

    int drawn = 0;
    float x0 = sizeX / 4f * 3f;
    float y0 = sizeY / 2;
    float dr = 2 * ballRadius;
    // CR: we can pass start positions of everything into our model constructor
    // CR: this way it would be easier to write tests for model (pass initial positions and then invoke
    for (int i = 1; drawn != properties.ballsCount(); i++) {
      for (int j = 0; j < i && drawn != properties.ballsCount(); j++) {
        balls.add(new Ball(x0 + dr * (i - 1), y0 - dr * (i / 2)
                + ((i + 1) % 2) * ballRadius + dr * j, ballRadius));
        drawn++;
      }
    }
  }

  public void removeBall(int index) {
    balls.remove(index);
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

  public List<Point2D> getBallsCoordinates() {
    return balls.stream()
            .map(ball -> new Point2D((int) ball.getX(), (int) ball.getY()))
            .toList();
  }

  public List<Point2D> getPocketsCoordinates() {
    return pockets.stream()
            .map(pocket -> new Point2D((int) pocket.x(), (int) pocket.y()))
            .toList();
  }

  private void move(float dt) {
    cueBall.move(dt, SURFACE_FRICTION, GRAVITY);
    if (cueBall.getX() - ballRadius < 0 || cueBall.getX() + ballRadius > sizeX) {
      cueBall.setVelocity(-cueBall.getVelocityX(), cueBall.getVelocityY());
    }
    if (cueBall.getY() - ballRadius < 0 || cueBall.getY() + ballRadius > sizeY) {
      cueBall.setVelocity(cueBall.getVelocityX(), -cueBall.getVelocityY());
    }
    for (Ball ball : balls) {
      ball.move(dt, SURFACE_FRICTION, GRAVITY);
      if (ball.getX() - ballRadius < 0 || ball.getX() + ballRadius > sizeX) {
        ball.setVelocity(-ball.getVelocityX(), ball.getVelocityY());
        ball.move(dt, SURFACE_FRICTION, GRAVITY);
      }
      if (ball.getY() - ballRadius < 0 || ball.getY() + ballRadius > sizeY) {
        ball.setVelocity(ball.getVelocityX(), -ball.getVelocityY());
      }
    }
  }

  private void updateVelocities() {
    for (Ball ball : balls) {
      if (cueBall.collides(ball)) {
        cueBall.hit(ball);
        cueBall.unhookFrom(ball);
      }
    }
    for (int i = 0; i < balls.size(); i++) {
      for (int j = i + 1; j < balls.size(); j++) {
        if (!balls.get(i).collides(balls.get(j))) {
          continue;
        }
        balls.get(i).hit(balls.get(j));
        balls.get(i).unhookFrom(balls.get(j));
      }
    }
  }

  private void checkPockets() {
    for (Pocket pocket : pockets) {
      if (cueBall.isInPocket(pocket)) {
         listener.cueBallInPocket();
      }
    }
    List<Integer> ballsInPockets = new ArrayList<>();
    for (int i = 0; i < balls.size(); i++) {
      for (Pocket pocket : pockets) {
        if (balls.get(i).isInPocket(pocket)) {
          ballsInPockets.add(i);
        }
      }
    }
    for (int i : ballsInPockets) {
      listener.ballInPocket(i);
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

  public void tickClock() {
    clock.tick();
  }

  public ClockTime getElapsedTime() {
    return clock.getTime();
  }
}
