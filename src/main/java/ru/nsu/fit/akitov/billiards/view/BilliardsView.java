package ru.nsu.fit.akitov.billiards.view;

import ru.nsu.fit.akitov.billiards.utils.*;

import java.util.List;

public interface BilliardsView {
  void start();
  void clear();
  void attachListener(ViewListener listener);
  void addBall(BallModel ball);
  void addCueBall(BallModel cueBall);
  void addPocket(PocketModel pocket);
  void setCueAvailable(boolean b);
  void updateCueBall(BallModel cueBall);
  void updateBalls(List<BallModel> balls);
  void updateCue(CueModel cue);
  void updateTime(ClockTime time);
  void removeBall(int index);
}
