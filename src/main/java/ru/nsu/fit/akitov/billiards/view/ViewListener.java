package ru.nsu.fit.akitov.billiards.view;

public interface ViewListener {
  void newGame();
  void cueStrike(float vx, float vy);
  void moveBalls(float dt);
}
