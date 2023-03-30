package ru.nsu.fit.akitov.billiards.model;

public interface FieldListener {
  void updateAll(float dt);
  void ballInPocket(int index);
}
