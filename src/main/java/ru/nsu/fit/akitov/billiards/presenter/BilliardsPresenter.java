package ru.nsu.fit.akitov.billiards.presenter;

import ru.nsu.fit.akitov.billiards.dto.BallModel;
import ru.nsu.fit.akitov.billiards.model.Field;
import ru.nsu.fit.akitov.billiards.model.FieldListener;
import ru.nsu.fit.akitov.billiards.utils.Highscore;
import ru.nsu.fit.akitov.billiards.utils.HighscoresTable;
import ru.nsu.fit.akitov.billiards.dto.PocketModel;
import ru.nsu.fit.akitov.billiards.view.BilliardsView;
import ru.nsu.fit.akitov.billiards.view.ViewListener;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
  }

  @Override
  public void newGame() {
    field.reset();
    gameClock.restart();

    view.updateCueBall(field.getCueBall());
    view.updateBalls(field.getBalls());
    view.setCueAvailable(true);
    view.clear();
  }

  @Override
  public void run() {
    view.setListener(this);
    field.setListener(this);
    field.reset();
    for (PocketModel pocket : field.getPockets()) {
      view.addPocket(pocket);
    }
    view.setCueBall(field.getCueBall());
    for (BallModel ball : field.getBalls()) {
      view.addBall(ball);
    }
    view.start();
    newGame();
  }

  @Override
  public void cueStrike() {
    if (field.performCueStrike()) {
      view.setCueAvailable(false);
    }
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
  public void playerNameEnter(String name) {
    int elapsed = field.getElapsedTime().minutes() * 60 + field.getElapsedTime().seconds();
    Highscore highscore = new Highscore(name, field.getBalls().size(), elapsed);
    HighscoresTable.INSTANCE.addHighscore(highscore);
    newGame();
  }

  @Override
  public void fieldChanged() {
    view.updateCueBall(field.getCueBall());
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

  @Override
  public void gameOver() {
    gameRunner.stop();
    gameClock.stop();
    view.startEnteringName();
  }
}
