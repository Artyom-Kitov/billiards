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
  private static final float DELTA_VELOCITY = 50.0f;
  private static final float DELTA_ANGLE = (float) Math.PI / 180.0f;

  private final BilliardsView view;
  private final Field field;

  private final Timer timer;

  public BilliardsPresenter(Field field, BilliardsView view) {
    this.field = field;
    this.view = view;

    timer = new Timer(2, new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        field.update(timer.getDelay() * 0.001f);
      }
    });
  }

  @Override
  public void newGame() {
    field.reset();
    view.clear();

    Ball cueBall = field.getCueBall();
    view.addCueBall((int) cueBall.getX(), (int) cueBall.getY(), (int) field.getBallRadius());
    List<Point2D> balls = field.getBallsCoordinates();
    for (Point2D ball : balls) {
      view.addBall(ball.x(), ball.y(), (int) field.getBallRadius());
    }
    view.setCueAvailable(true);
  }

  @Override
  public void run() {
    view.attachListener(this);
    field.setListener(this);
    field.reset();
    for (Point2D pocket : field.getPocketsCoordinates()) {
      view.addPocket(pocket.x(), pocket.y(), (int) field.getPocketRadius());
    }
    Ball cueBall = field.getCueBall();
    view.addCueBall((int) cueBall.getX(), (int) cueBall.getY(), (int) field.getBallRadius());
    view.start();
  }

  @Override
  public void onSpacePressed() {
    field.performCueStrike();
    view.setCueAvailable(false);
    timer.restart();
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
    view.updateBalls(getCueBallCoordinates(), field.getBallsCoordinates());
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
    timer.stop();
    view.setCueAvailable(true);
    view.updateCue(field.getCueVelocity(), field.getCueAngle());
  }

  @Override
  public void strikePerformed() {
    view.setCueAvailable(false);
    timer.start();
  }
}
