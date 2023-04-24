package ru.nsu.fit.akitov.billiards.presenter;

import ru.nsu.fit.akitov.billiards.model.Ball;
import ru.nsu.fit.akitov.billiards.model.Field;
import ru.nsu.fit.akitov.billiards.model.FieldListener;
import ru.nsu.fit.akitov.billiards.utils.Point2D;
import ru.nsu.fit.akitov.billiards.view.BilliardsView;
import ru.nsu.fit.akitov.billiards.view.ViewListener;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class BilliardsPresenter implements Runnable, FieldListener, ViewListener {

  private static final float DELTA_VELOCITY = 50f;
  private static final float DELTA_ANGLE = (float) Math.PI / 180f;

  private final BilliardsView view;
  private final Field field;

  private final Timer gameRunner;
  private final Timer gameClock;

  public BilliardsPresenter(Field field, BilliardsView view) {
    this.field = field;
    this.view = view;

    gameRunner = new Timer(5, new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        field.update(gameRunner.getDelay() * 0.001f);
      }
    });

    gameClock = new Timer(1000, event -> {
      field.tickClock();
      view.updateTime(field.getElapsedTime());
    });
  }

  @Override
  public void newGame() {
    field.reset();
    view.clear();
    gameClock.restart();

    Ball cueBall = field.getCueBall();
    view.updateCueBall((int) cueBall.getX(), (int) cueBall.getY());
    List<Point2D> balls = field.getBallsCoordinates();
    for (Point2D ball : balls) {
      view.addBall(ball.x(), ball.y(), (int) field.getBallRadius());
    }
    view.setCueAvailable(true);
    view.updateCue(field.getCueVelocity(), field.getCueAngle());
  }

  @Override
  public void run() {
    view.attachListener(this);
    field.setListener(this);
    field.reset();
    for (Point2D pocket : field.getPocketsCoordinates()) {
      view.addPocket(pocket.x(), pocket.y(), (int) field.getPocketRadius());
    }
    view.start();
  }

  @Override
  public void onSpacePressed() {
    if (field.getCueVelocity() == 0) {
      return;
    }
    field.performCueStrike();
    view.setCueAvailable(false);
  }

  @Override
  public void onLeftPressed() {
    field.rotateCue(DELTA_ANGLE);
    view.updateCue(field.getCueVelocity(), field.getCueAngle());
  }

  @Override
  public void onRightPressed() {
    field.rotateCue(-DELTA_ANGLE);
    view.updateCue(field.getCueVelocity(), field.getCueAngle());
  }

  @Override
  public void onUpPressed() {
    field.addCueVelocity(-DELTA_VELOCITY);
    view.updateCue(field.getCueVelocity(), field.getCueAngle());
  }

  @Override
  public void onDownPressed() {
    field.addCueVelocity(DELTA_VELOCITY);
    view.updateCue(field.getCueVelocity(), field.getCueAngle());
  }

  @Override
  public void ballsMoved() {
    Point2D coordinates = getCueBallCoordinates();
    view.updateCueBall(coordinates.x(), coordinates.y());
    view.updateBalls(field.getBallsCoordinates());
  }

  @Override
  public void ballInPocket(int ballIndex) {
    field.removeBall(ballIndex);
    view.removeBall(ballIndex);
  }

  @Override
  public void cueBallInPocket() {

  }

  private Point2D getCueBallCoordinates() {
    return new Point2D((int) field.getCueBall().getX(), (int) field.getCueBall().getY());
  }

  @Override
  public void isMotionless() {
    gameRunner.stop();
    view.setCueAvailable(true);
    view.updateCue(field.getCueVelocity(), field.getCueAngle());
  }

  @Override
  public void strikePerformed() {
    view.setCueAvailable(false);
    gameRunner.restart();
  }
}
