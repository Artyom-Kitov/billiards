package ru.nsu.fit.akitov.billiards.presenter;

import ru.nsu.fit.akitov.billiards.model.Field;
import ru.nsu.fit.akitov.billiards.model.FieldListener;
import ru.nsu.fit.akitov.billiards.utils.PocketModel;
import ru.nsu.fit.akitov.billiards.view.BilliardsView;
import ru.nsu.fit.akitov.billiards.view.ViewListener;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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

    view.resetBalls(field.getBalls());
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
  public void cueStrike() {
    if (field.getCue().velocity() == 0) {
      return;
    }
    field.performCueStrike();
    view.setCueAvailable(false);
  }

  @Override
  public void rotateCueLeft() {
    field.rotateCueLeft();
    view.updateCue(field.getCue());
  }

  @Override
  public void rotateCueRight() {
    field.rotateCueRight();
    view.updateCue(field.getCue());
  }

  @Override
  public void reduceCueVelocity() {
    field.reduceCueVelocity();
    view.updateCue(field.getCue());
  }

  @Override
  public void increaseCueVelocity() {
    field.increaseCueVelocity();
    view.updateCue(field.getCue());
  }

  @Override
  public void placeCueBall() {
    field.placeCueBall();
  }

  @Override
  public void moveCueBallLeft() {
    field.moveCueBallLeft();
  }

  @Override
  public void moveCueBallRight() {
    field.moveCueBallRight();
  }

  @Override
  public void moveCueBallUp() {
    field.moveCueBallUp();
  }

  @Override
  public void moveCueBallDown() {
    field.moveCueBallDown();
  }

  @Override
  public void fieldChanged() {
    view.updateBalls(field.getBalls());
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

  @Override
  public void askForCueBall() {
    gameRunner.stop();
    view.startPlacingCueBall();
  }

  @Override
  public void cueBallPlaceSuccessful() {
    view.stopPlacingCueBall();
  }
}
