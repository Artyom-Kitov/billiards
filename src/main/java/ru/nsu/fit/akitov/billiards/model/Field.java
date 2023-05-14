package ru.nsu.fit.akitov.billiards.model;

import ru.nsu.fit.akitov.billiards.utils.*;

import java.util.ArrayList;
import java.util.List;

public class Field {

  private static final float SURFACE_FRICTION = 40;
  private static final float GRAVITY = 10;

  private static final float DELTA_VELOCITY = 50f;
  private static final float DELTA_ANGLE = (float) Math.PI / 180f;

  private final float sizeX;
  private final float sizeY;

  private final List<Point2D> startCoordinates;

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
    this.startCoordinates = properties.ballsCoordinates();
    this.ballRadius = properties.relativeBallSize() * properties.fieldSize();
    this.pocketRadius = (float) properties.fieldSize() / properties.relativePocketSize() / 2;

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
  public BallModel getCueBall() {
    return new BallModel(cueBall.getPosition(), (int) ballRadius);
  }

  public void setListener(FieldListener listener) {
    this.listener = listener;
  }

  public void reset() {
    balls.clear();
    clock.reset();

    Point2D startCueBall = startCoordinates.get(0);
    cueBall = new Ball(startCueBall.x() * ballRadius, startCueBall.y() * ballRadius, ballRadius);

    for (int i = 1; i < startCoordinates.size(); i++) {
      Point2D position = startCoordinates.get(i);
      balls.add(new Ball(position.x() * ballRadius, position.y() * ballRadius, ballRadius));
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

  public List<BallModel> getBalls() {
    return balls.stream()
            .map(ball -> new BallModel(ball.getPosition(), (int) ballRadius))
            .toList();
  }

  public List<PocketModel> getPockets() {
    return pockets.stream()
            .map(pocket -> new PocketModel(new Point2D(pocket.x(), pocket.y()), (int) pocketRadius))
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
        cueBall.unhookFromWalls(0, sizeX, 0, sizeY);
      }
    }
    for (int i = 0; i < balls.size(); i++) {
      for (int j = i + 1; j < balls.size(); j++) {
        if (!balls.get(i).collides(balls.get(j))) {
          continue;
        }
        balls.get(i).hit(balls.get(j));
        balls.get(i).unhookFrom(balls.get(j));
        balls.get(i).unhookFromWalls(0, sizeX, 0, sizeY);
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

  public void update(float milliseconds) {
    if (isMotionless()) {
      listener.isMotionless();
      return;
    }
    move(milliseconds * 0.001f);
    updateVelocities();
    checkPockets();
    listener.ballsMoved();
  }

  public void increaseCueVelocity() {
    cue.addVelocity(DELTA_VELOCITY);
  }

  public void reduceCueVelocity() {
    cue.addVelocity(-DELTA_VELOCITY);
  }

  public void rotateCueLeft() {
    cue.rotate(DELTA_ANGLE);
  }

  public void rotateCueRight() {
    cue.rotate(-DELTA_ANGLE);
  }

  public CueModel getCue() {
    return new CueModel(cue.getVelocity(), cue.getAngle());
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
