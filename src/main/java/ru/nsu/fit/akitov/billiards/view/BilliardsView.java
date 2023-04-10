package ru.nsu.fit.akitov.billiards.view;

import ru.nsu.fit.akitov.billiards.utils.Point2D;

import java.util.List;

public interface BilliardsView {
  void start();
  void clear();
  void attachListener(ViewListener listener);
  void addCueBall(int x, int y, int radius);
  void addBall(int x, int y, int radius);
  void addPocket(int x, int y, int radius);
  void setCueAvailable(boolean b);
  void updateBalls(Point2D cueBall, List<Point2D> balls);
  void updateCue(float velocity, float angle);
  void removeBall(int index);
}
