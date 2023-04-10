package ru.nsu.fit.akitov.billiards.model;

public interface FieldListener {
  void ballInPocket(int ballIndex);
  void cueBallInPocket();
  void ballsMoved();
  void isMotionless();
  void strikePerformed();
}
