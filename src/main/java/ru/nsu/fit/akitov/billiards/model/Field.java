package ru.nsu.fit.akitov.billiards.model;

public class Field {
  private static final float SURFACE_FRICTION = 0.002f;

  private final float sizeX;
  private final float sizeY;

  private Pocket[] pockets;
  private Ball[] balls;
  private Ball blackBall;

  public Field(int sizeX, int sizeY) {
    this.sizeX = sizeX;
    this.sizeY = sizeY;

    pockets = new Pocket[6];

    balls = new Ball[16];
    blackBall = balls[0];
  }

  public boolean isMotionless() {
    boolean result = true;
    for (Ball b : balls) {
    }

    return result;
  }

  public void update() {
    if (isMotionless()) {
      return;
    }

    for (Ball ball : balls) {
    }
  }
}
