package ru.nsu.fit.akitov.billiards.model;

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

  public int getMinutes() {
    return minutes;
  }

  public int getSeconds() {
    return seconds;
  }
}
