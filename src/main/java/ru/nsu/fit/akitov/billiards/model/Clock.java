package ru.nsu.fit.akitov.billiards.model;

import ru.nsu.fit.akitov.billiards.utils.ClockTime;

public class Clock {

  private int minutes;
  private int seconds;

  public void reset() {
    minutes = 0;
    seconds = 0;
  }

  public void tick() {
    seconds++;
    minutes += seconds / 60;
    seconds %= 60;
  }

  public ClockTime getTime() {
    return new ClockTime(minutes, seconds);
  }
}
