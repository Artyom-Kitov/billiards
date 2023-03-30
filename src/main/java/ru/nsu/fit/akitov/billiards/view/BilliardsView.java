package ru.nsu.fit.akitov.billiards.view;

import ru.nsu.fit.akitov.billiards.presenter.BilliardsPresenter;

import java.awt.*;
import java.util.List;

public interface BilliardsView {
  void attachPresenter(BilliardsPresenter presenter);
  void addCueBall(Point center, int radius);
  void addBall(Point center, int radius);
  void addPocket(Point center, int radius);
  void updateBalls(List<Point> balls);
}
