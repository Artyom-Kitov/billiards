package ru.nsu.fit.akitov.billiards.view;

import ru.nsu.fit.akitov.billiards.utils.*;

import java.util.List;

public interface BilliardsView {
  void start();
  void clear();
  void attachListener(ViewListener listener);
  void addPocket(PocketModel pocket);
  void setCueAvailable(boolean b);
  void updateBalls(List<BallModel> balls);
  void resetBalls(List<BallModel> balls);
  void updateCue(CueModel cue);
  void updateTime(ClockTime time);
  void startPlacingCueBall();
  void stopPlacingCueBall();
}
