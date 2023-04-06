package ru.nsu.fit.akitov.billiards.presenter;

import ru.nsu.fit.akitov.billiards.model.Ball;
import ru.nsu.fit.akitov.billiards.model.Field;
import ru.nsu.fit.akitov.billiards.model.FieldListener;
import ru.nsu.fit.akitov.billiards.model.Pocket;
import ru.nsu.fit.akitov.billiards.view.BilliardsView;
import ru.nsu.fit.akitov.billiards.view.ViewListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class BilliardsPresenter implements Runnable, FieldListener, ViewListener {
  private static final float DELTA_VELOCITY = 100.0f;
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
        view.updateBalls(getCueBallCoordinates(), getBallsCoordinates());
      }
    });
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
    view.setCueAvailable(true);
  }

  @Override
  public void run() {
    view.attachListener(this);
    field.setListener(this);
    for (Pocket pocket : field.getPockets()) {
      view.addPocket((int) pocket.x(), (int) pocket.y(), (int) Pocket.RADIUS);
    }
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
    // CR: use custom point
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
