package ru.nsu.fit.akitov.billiards.utils;

import java.util.List;

public interface ModelProperties {
  int relativePocketSize();
  int relativeCueStrength();
  List<Point2D> ballsCoordinates();
}
