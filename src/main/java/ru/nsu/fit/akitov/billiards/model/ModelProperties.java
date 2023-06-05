package ru.nsu.fit.akitov.billiards.model;

import ru.nsu.fit.akitov.billiards.utils.Point2D;

import java.util.List;

public interface ModelProperties {
  float relativeBallSize();
  int relativePocketSize();
  int relativeCueStrength();
  List<Point2D> ballsCoordinates();
}
