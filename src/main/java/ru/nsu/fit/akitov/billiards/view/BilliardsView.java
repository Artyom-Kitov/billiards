package ru.nsu.fit.akitov.billiards.view;

import ru.nsu.fit.akitov.billiards.dto.BallModel;
import ru.nsu.fit.akitov.billiards.dto.CueModel;
import ru.nsu.fit.akitov.billiards.dto.PocketModel;
import ru.nsu.fit.akitov.billiards.utils.*;

import java.util.List;

public interface BilliardsView {
  void start();
  void clear();
  void setListener(ViewListener listener);
  void addPocket(PocketModel pocket);
  void addBall(BallModel ball);
  void setCueBall(BallModel cueBall);
  void setCueAvailable(boolean b);
  void updateCueBall(BallModel cueBall);
  void updateBalls(List<BallModel> balls);
  void updateCue(CueModel cue);
  void updateTime(ClockTime time);
  void startPlacingCueBall();
  void stopPlacingCueBall();
  void startEnteringName();
}
