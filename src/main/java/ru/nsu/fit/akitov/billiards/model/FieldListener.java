package ru.nsu.fit.akitov.billiards.model;

public interface FieldListener {
  void ballInPocket(int ballIndex);
  void ballsMoved();
  void isMotionless();
  void strikePerformed();
}
