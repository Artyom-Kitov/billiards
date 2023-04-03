package ru.nsu.fit.akitov.billiards.view;

public interface ViewListener {
  void newGame();
  void onSpacePressed();
  void onLeftPressed();
  void onRightPressed();
  void onUpPressed();
  void onDownPressed();
}
