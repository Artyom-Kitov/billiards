package ru.nsu.fit.akitov.billiards.presenter;

import ru.nsu.fit.akitov.billiards.model.Ball;
import ru.nsu.fit.akitov.billiards.model.Field;
import ru.nsu.fit.akitov.billiards.model.FieldListener;
import ru.nsu.fit.akitov.billiards.model.Pocket;
import ru.nsu.fit.akitov.billiards.view.BilliardsView;
import ru.nsu.fit.akitov.billiards.view.ViewListener;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class BilliardsPresenter implements Runnable, FieldListener, ViewListener {

  private final BilliardsView view;
  private final Field field;

  public BilliardsPresenter(Field field, BilliardsView view) {
    this.field = field;
    this.view = view;
  }

  @Override
  public void newGame() {
    field.reset();
    view.clear();

    Ball cueBall = field.getCueBall();
    view.addCueBall((int) cueBall.getX(), (int) cueBall.getY(), (int) Ball.RADIUS);
    List<Ball> balls = field.getBalls();
    for (Ball ball : balls) {
      view.addBall((int) ball.getX(), (int) ball.getY(), (int) Ball.RADIUS);
    }
    view.start();
  }

  @Override
  public void run() {
    view.attachListener(this);
    field.setListener(this);
    for (Pocket pocket : field.getPockets()) {
      view.addPocket((int) pocket.x(), (int) pocket.y(), (int) Pocket.RADIUS);
    }
  }

  @Override
  public void moveBalls(float dt) {
    field.update(dt);
  }

  @Override
  public void ballsMoved() {
    view.updateBalls(getCueBallCoordinates(), getBallsCoordinates());
  }

  @Override
  public void ballInPocket(int ballIndex) {

  }

  private Point getCueBallCoordinates() {
    return new Point((int) field.getCueBall().getX(), (int) field.getCueBall().getY());
  }
  private List<Point> getBallsCoordinates() {
    List<Point> coordinates = new ArrayList<>();
    for (Ball ball : field.getBalls()) {
      coordinates.add(new Point((int) ball.getX(), (int) ball.getY()));
    }
    return coordinates;
  }
  @Override
  public void cueStrike(float vx, float vy) {
    field.getCueBall().setVelocity(vx, vy);
  }

  @Override
  public void isMotionless() {
    view.stop();
    view.performCueStrike();
  }
}
