package ru.nsu.fit.akitov.billiards.model;

public interface FieldListener {
  void fieldChanged();
  void isMotionless();
  void strikePerformed();
  void askForCueBall();
  void cueBallPlaceSuccessful();
  void gameOver();
}
