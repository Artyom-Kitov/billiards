package ru.nsu.fit.akitov.billiards.model;

import ru.nsu.fit.akitov.billiards.utils.*;

import java.util.ArrayList;
import java.util.List;

public class Field {

  private static final float SURFACE_FRICTION = 40;
  private static final float GRAVITY = 10;

  private static final float DELTA_VELOCITY = 50f;
  private static final float DELTA_ANGLE = (float) Math.PI / 180f;

  private static final float CUE_BALL_DELTA = 4;

  private final float sizeX;
  private final float sizeY;

  private final List<Point2D> startCoordinates;

  private final float ballRadius;
  private final Ball cueBall;
  private final List<Ball> balls;

  private final float pocketRadius;
  private final List<Pocket> pockets;

  private final Cue cue;

  private final Clock clock;

  private final List<FieldListener> listeners;

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

    cueBall = new Ball(0, 0, ballRadius);
    balls.add(cueBall);
    for (int i = 1; i < startCoordinates.size(); i++) {
      balls.add(new Ball(0, 0, ballRadius));
    }
    cue = new Cue(properties.fieldSize() * properties.relativeCueStrength());
    clock = new Clock();
    listeners = new ArrayList<>();
    reset();
  }

  public void addListener(FieldListener listener) {
    this.listeners.add(listener);
  }

  public void reset() {
    clock.reset();
    for (int i = 0; i < startCoordinates.size(); i++) {
      Point2D position = startCoordinates.get(i);
      balls.get(i).setPosition(position.x() * ballRadius, position.y() * ballRadius);
      balls.get(i).setAvailable(true);
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

  public List<BallModel> getBalls() {
    return balls.stream()
            .map(ball -> new BallModel(ball.getPosition(), (int) ballRadius, ball.isAvailable()))
            .toList();
  }

  public List<PocketModel> getPockets() {
    return pockets.stream()
            .map(pocket -> new PocketModel(new Point2D(pocket.x(), pocket.y()), (int) pocketRadius))
            .toList();
  }

  private void move(float dt) {
    for (Ball ball : balls) {
      ball.move(dt, SURFACE_FRICTION, GRAVITY);
      if (ball.getX() - ballRadius < 0 || ball.getX() + ballRadius > sizeX) {
        ball.setVelocity(-ball.getVelocityX(), ball.getVelocityY());
      }
      if (ball.getY() - ballRadius < 0 || ball.getY() + ballRadius > sizeY) {
        ball.setVelocity(ball.getVelocityX(), -ball.getVelocityY());
      }
      ball.unhookFromWalls(0, sizeX, 0, sizeY);
    }
  }

  private void updateVelocities() {
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
    for (Ball ball : balls) {
      for (Pocket pocket : pockets) {
        if (ball.isInPocket(pocket)) {
          ball.setAvailable(false);
        }
      }
    }
  }

  private boolean isEnd() {
    for (int i = 1; i < balls.size(); i++) {
      if (balls.get(i).isAvailable()) {
        return false;
      }
    }
    return true;
  }

  public void update(float milliseconds) {
    if (isMotionless()) {
      for (FieldListener listener : listeners) {
        listener.isMotionless();
      }
      if (isEnd()) {
        for (FieldListener listener : listeners) {
          listener.gameOver();
        }
      }
      if (!cueBall.isAvailable()) {
        cueBall.setAvailable(true);
        cueBall.setPosition(sizeX / 4, sizeY / 2);
        for (FieldListener listener : listeners) {
          listener.fieldChanged();
          listener.askForCueBall();
        }
      }
      return;
    }
    move(milliseconds * 0.001f);
    updateVelocities();
    checkPockets();
    for (FieldListener listener : listeners) {
      listener.fieldChanged();
    }
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

  public boolean performCueStrike() {
    if (cue.getVelocity() == 0) {
      return false;
    }
    cue.strike(cueBall);
    for (FieldListener listener : listeners) {
      listener.strikePerformed();
    }
    return true;
  }

  public void tickClock() {
    clock.tick();
  }

  public ClockTime getElapsedTime() {
    return clock.getTime();
  }

  private boolean isFree(float x, float y) {
    if (x - ballRadius < 0 || x + ballRadius > sizeX / 4) {
      return false;
    }
    if (y - ballRadius < 0 || y + ballRadius > sizeY) {
      return false;
    }
    for (Pocket pocket : pockets) {
      if (cueBall.isInPocket(pocket)) {
        return false;
      }
    }
    for (int i = 1; i < balls.size(); i++) {
      if (cueBall.collides(balls.get(i))) {
        return false;
      }
    }
    return true;
  }
  public void placeCueBall() {
    if (isFree(cueBall.getX(), cueBall.getY())) {
      for (FieldListener listener : listeners) {
        listener.cueBallPlaceSuccessful();
      }
    }
  }

  public void moveCueBallLeft() {
    if (cueBall.getX() - CUE_BALL_DELTA - ballRadius < 0) {
      return;
    }
    cueBall.setPosition(cueBall.getX() - CUE_BALL_DELTA, cueBall.getY());
    for (FieldListener listener : listeners) {
      listener.fieldChanged();
    }
  }

  public void moveCueBallRight() {
    if (cueBall.getX() + CUE_BALL_DELTA + ballRadius > sizeX / 4) {
      return;
    }
    cueBall.setPosition(cueBall.getX() + CUE_BALL_DELTA, cueBall.getY());
    for (FieldListener listener : listeners) {
      listener.fieldChanged();
    }
  }

  public void moveCueBallUp() {
    if (cueBall.getY() - CUE_BALL_DELTA - ballRadius < 0) {
      return;
    }
    cueBall.setPosition(cueBall.getX(), cueBall.getY() - CUE_BALL_DELTA);
    for (FieldListener listener : listeners) {
      listener.fieldChanged();
    }
  }

  public void moveCueBallDown() {
    if (cueBall.getY() + CUE_BALL_DELTA + ballRadius > sizeY) {
      return;
    }
    cueBall.setPosition(cueBall.getX(), cueBall.getY() + CUE_BALL_DELTA);
    for (FieldListener listener : listeners) {
      listener.fieldChanged();
    }
  }
}
