package ru.nsu.fit.akitov.billiards.model;

public class Ball {
  public static final float MASS = 0.256f;
  public static final float RADIUS = 0.068f;
  public static final float COMPARE_PRECISION = 0.0001f;

  private float x;
  private float y;

  private float vx;
  private float vy;

  public Ball(float x, float y) {
    this.x = x;
    this.y = y;
    this.vx = 0.0f;
    this.vy = 0.0f;
  }

  public void move(float dt, float mu, float g) {
    if (Math.abs(vx) < COMPARE_PRECISION && Math.abs(vy) < COMPARE_PRECISION) {
      return;
    }
    x += vx * dt;
    y += vy * dt;
    float normV = (float) Math.sqrt(vx * vx + vy * vy);
    vx -= vx / normV * mu * g * dt;
    vy -= vy / normV * mu * g * dt;
  }

  public void setVelocity(float vx, float vy) {
    this.vx = vx;
    this.vy = vy;
  }

  public boolean collides(Ball other) {
    return (x - other.x) * (x - other.x) + (y - other.y) * (y - other.y) <= 4 * RADIUS * RADIUS;
  }

  public void hit(Ball other) {
    float centralX = other.x - x;
    float centralY = other.y - y;

    double sqrt = Math.sqrt(centralX * centralX + centralY * centralY);

    float coefficient1 = (vx * centralX + vy * centralY) / (float) sqrt;
    float thisCentralProjectionX = coefficient1 * centralX;
    float thisCentralProjectionY = coefficient1 * centralY;

    float coefficient2 = (other.vx * centralX + vy * centralY) / (float) sqrt;
    float otherCentralProjectionX = coefficient2 * centralX;
    float otherCentralProjectionY = coefficient2 * centralY;

    vx = vx - thisCentralProjectionX + otherCentralProjectionX;
    vy = vy - thisCentralProjectionY + otherCentralProjectionY;

    other.vx = other.vx - otherCentralProjectionX + thisCentralProjectionX;
    other.vy = other.vy - otherCentralProjectionY + thisCentralProjectionY;
  }

  public boolean isInPocket(Pocket pocket) {
    return (x - pocket.x()) * (x - pocket.x()) + (y - pocket.y()) * (y - pocket.y()) <= Pocket.RADIUS * Pocket.RADIUS;
  }
}
