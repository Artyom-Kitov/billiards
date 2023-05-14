package ru.nsu.fit.akitov.billiards.presenter;

import ru.nsu.fit.akitov.billiards.model.Field;
import ru.nsu.fit.akitov.billiards.model.FieldListener;
import ru.nsu.fit.akitov.billiards.utils.BallModel;
import ru.nsu.fit.akitov.billiards.utils.PocketModel;
import ru.nsu.fit.akitov.billiards.view.BilliardsView;
import ru.nsu.fit.akitov.billiards.view.ViewListener;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

// CR: add game end
// CR: add records
public class BilliardsPresenter implements Runnable, FieldListener, ViewListener {

  private final BilliardsView view;
  private final Field field;

  private final Timer gameRunner;
  private final Timer gameClock;

  public BilliardsPresenter(Field field, BilliardsView view) {
    this.field = field;
    this.view = view;

    gameRunner = new Timer(5, new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent event) {
        field.update(gameRunner.getDelay());
      }
    });

    gameClock = new Timer(1000, event -> {
      field.tickClock();
      view.updateTime(field.getElapsedTime());
    });

    newGame();
  }

  @Override
  public void newGame() {
    field.reset();
    view.clear();
    gameClock.restart();

    view.addCueBall(field.getCueBall());
    List<BallModel> balls = field.getBalls();
    for (BallModel ball : balls) {
      view.addBall(ball);
    }
//    view.setCue();
    view.setCueAvailable(true);
  }

  @Override
  public void run() {
    view.attachListener(this);
    field.setListener(this);
    field.reset();
    for (PocketModel pocket : field.getPockets()) {
      view.addPocket(pocket);
    }
    view.start();
  }

  @Override
  public void onSpacePressed() {
    if (field.getCue().velocity() == 0) {
      return;
    }
    field.performCueStrike();
    view.setCueAvailable(false);
  }

  @Override
  public void onLeftPressed() {
    field.rotateCueLeft();
    view.updateCue(field.getCue());
  }

  @Override
  public void onRightPressed() {
    field.rotateCueRight();
    view.updateCue(field.getCue());
  }

  @Override
  public void onUpPressed() {
    field.reduceCueVelocity();
    view.updateCue(field.getCue());
  }

  @Override
  public void onDownPressed() {
    field.increaseCueVelocity();
    view.updateCue(field.getCue());
  }

  @Override
  public void ballsMoved() {
    view.updateCueBall(field.getCueBall());
    view.updateBalls(field.getBalls());
  }

  @Override
  public void ballInPocket(int ballIndex) {
    field.removeBall(ballIndex);
    view.removeBall(ballIndex);
  }

  @Override
  public void cueBallInPocket() {
    // CR: do we need this?
  }

  @Override
  public void isMotionless() {
    gameRunner.stop();
    view.setCueAvailable(true);
    view.updateCue(field.getCue());
  }

  @Override
  public void strikePerformed() {
    view.setCueAvailable(false);
    gameRunner.restart();
  }
}
