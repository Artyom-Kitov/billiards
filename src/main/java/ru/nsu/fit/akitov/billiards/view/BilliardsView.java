package ru.nsu.fit.akitov.billiards.view;

import java.awt.*;
import java.util.List;

public interface BilliardsView {
  void start();
  void clear();
  void attachListener(ViewListener listener);
  void addCueBall(int x, int y, int radius);
  void addBall(int x, int y, int radius);
  void addPocket(int x, int y, int radius);
  void setCueVisible(boolean b);
  void updateBalls(Point cueBall, List<Point> balls);
  void updateCue(float velocity, float angle);
}
