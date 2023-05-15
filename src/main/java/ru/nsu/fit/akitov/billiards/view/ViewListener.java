package ru.nsu.fit.akitov.billiards.view;

public interface ViewListener {
  void newGame();
  void cueStrike();
  void rotateCueLeft();
  void rotateCueRight();
  void reduceCueVelocity();
  void increaseCueVelocity();
}
