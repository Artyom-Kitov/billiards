package ru.nsu.fit.akitov.billiards.view;

public interface ViewListener {
  void newGame();
  void cueStrike();
  void rotateCueLeft();
  void rotateCueRight();
  void reduceCueVelocity();
  void increaseCueVelocity();
  void placeCueBall();
  void moveCueBallLeft();
  void moveCueBallRight();
  void moveCueBallUp();
  void moveCueBallDown();
}
