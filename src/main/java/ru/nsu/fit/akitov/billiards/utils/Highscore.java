package ru.nsu.fit.akitov.billiards.utils;

import java.util.*;

public record Highscore(String name, int ballsCount, int time) implements Comparable<Highscore> {

  public Highscore {
    if (name.length() == 0) {
      throw new IllegalArgumentException("name cannot be empty");
    }
    if (ballsCount < 0) {
      throw new IllegalArgumentException("amount of balls cannot be negative");
    }
    if (time < 0) {
      throw new IllegalArgumentException("elapsed time cannot be negative");
    }
  }

  @Override
  public int compareTo(Highscore o) {
    return Comparator.comparingInt(Highscore::ballsCount)
            .reversed()
            .thenComparingInt(Highscore::time)
            .thenComparing(Highscore::toString)
            .compare(this, o);
  }

  @Override
  public String toString() {
    return name + " " + ballsCount + " " + time;
  }
}
