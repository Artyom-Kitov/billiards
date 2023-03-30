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
  public void run() {
    view.attachPresenter(this);
    field.setListener(this);
    for (Pocket pocket : field.getPockets()) {
      view.addPocket(new Point((int) pocket.x(), (int) pocket.y()), (int) Pocket.RADIUS);
    }
    Ball cueBall = field.getCueBall();
    view.addCueBall(new Point((int) cueBall.getX(), (int) cueBall.getY()), (int) Ball.RADIUS);
    List<Ball> balls = field.getBalls();
    for (int i = 1; i < balls.size(); i++) {
      view.addBall(new Point((int) balls.get(i).getX(), (int) balls.get(i).getY()), (int) Ball.RADIUS);
    }
  }

  @Override
  public void updateAll(float dt) {
    field.update(dt);
    view.updateBalls(getBallsCoordinates());
  }

  @Override
  public void ballInPocket(int index) {

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
//    while (!field.isMotionless()) {
//      try {
//        Thread.sleep(10);
//      } catch (InterruptedException e) {
//        throw new RuntimeException(e);
//      }
//      field.update();
//    }
  }
}
