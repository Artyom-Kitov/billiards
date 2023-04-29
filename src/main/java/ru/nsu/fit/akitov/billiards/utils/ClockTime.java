package ru.nsu.fit.akitov.billiards.utils;

import java.util.Comparator;

public record ClockTime(int minutes, int seconds) implements Comparable<ClockTime> {

  public ClockTime {
    if (minutes < 0) {
      throw new IllegalArgumentException("minutes cannot be negative");
    }
    if (seconds < 0 || seconds >= 60) {
      throw new IllegalArgumentException("wrong seconds value: expected an integer from 0 to 59");
    }
  }

  @Override
  public int compareTo(ClockTime o) {
    return Comparator.comparingInt(ClockTime::minutes).thenComparingInt(ClockTime::seconds).compare(this, o);
  }

  // CR: redundant? default impl seems to be suitable
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
