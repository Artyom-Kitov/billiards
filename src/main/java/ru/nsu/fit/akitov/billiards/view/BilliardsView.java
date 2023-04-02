package ru.nsu.fit.akitov.billiards.view;

import java.awt.*;
import java.util.List;

public interface BilliardsView {
  void start();
  void stop();
  void clear();
  void attachListener(ViewListener listener);
  void addCueBall(int x, int y, int radius);
  void addBall(int x, int y, int radius);
  void addPocket(int x, int y, int radius);
  void updateBalls(Point cueBall, List<Point> balls);
  void performCueStrike();
}
