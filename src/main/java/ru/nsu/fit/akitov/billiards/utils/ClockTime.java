package ru.nsu.fit.akitov.billiards.utils;

import java.util.Comparator;

public record ClockTime(int minutes, int seconds) implements Comparable<ClockTime> {

  @Override
  public int compareTo(ClockTime o) {
    return Comparator.comparingInt(ClockTime::minutes).thenComparingInt(ClockTime::seconds).compare(this, o);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ClockTime clockTime = (ClockTime) o;
    return minutes == clockTime.minutes && seconds == clockTime.seconds;
  }

  @Override
  public String toString() {
    return minutes + ":" + seconds;
  }
}
