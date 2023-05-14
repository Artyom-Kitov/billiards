package ru.nsu.fit.akitov.billiards.model;

import ru.nsu.fit.akitov.billiards.utils.Point2D;

public class Ball {

  private static final float COMPARE_PRECISION = 1f;

  private float x;
  private float y;
  private final float radius;

  private float vx;
  private float vy;

  public Ball(float x, float y, float radius) {
    this.x = x;
    this.y = y;
    this.radius = radius;
    this.vx = 0.0f;
    this.vy = 0.0f;
  }

  public void move(float dt, float friction, float g) {
    if (isMotionless()) {
      return;
    }
    x += vx * dt;
    y += vy * dt;
    float normV = (float) Math.sqrt(vx * vx + vy * vy);
    vx -= vx / normV * friction * g * dt;
    vy -= vy / normV * friction * g * dt;
  }

  public boolean isMotionless() {
    return Math.abs(vx) < COMPARE_PRECISION && Math.abs(vy) < COMPARE_PRECISION;
  }

  public float getX() {
    return x;
  }

  public float getY() {
    return y;
  }

  public Point2D getPosition() {
    return new Point2D(x, y);
  }

  public float getVelocityX() {
    return vx;
  }

  public float getVelocityY() {
    return vy;
  }

  public void setVelocity(float vx, float vy) {
    this.vx = vx;
    this.vy = vy;
  }

  public boolean collides(Ball other) {
    return (x - other.x) * (x - other.x) + (y - other.y) * (y - other.y) <= 4 * radius * radius;
  }

  public void hit(Ball other) {
    float centralX = other.x - x;
    float centralY = other.y - y;

    float norm = (float) Math.sqrt(centralX * centralX + centralY * centralY);
    centralX /= norm;
    centralY /= norm;

    float coefficient1 = (vx * centralX + vy * centralY);
    float thisCentralProjectionX = coefficient1 * centralX;
    float thisCentralProjectionY = coefficient1 * centralY;

    float coefficient2 = (other.vx * centralX + other.vy * centralY);
    float otherCentralProjectionX = coefficient2 * centralX;
    float otherCentralProjectionY = coefficient2 * centralY;

    vx = (vx - thisCentralProjectionX + otherCentralProjectionX);
    vy = (vy - thisCentralProjectionY + otherCentralProjectionY);

    other.vx = (other.vx - otherCentralProjectionX + thisCentralProjectionX);
    other.vy = (other.vy - otherCentralProjectionY + thisCentralProjectionY);
  }

  public void unhookFrom(Ball other) {
    float dx = other.x - x;
    float dy = other.y - y;

    float distance = (float) Math.sqrt(dx * dx + dy * dy);
    float cos = dx / distance;
    float sin = dy / distance;

    other.x = x + (radius + other.radius) * cos;
    other.y = y + (radius + other.radius) * sin;
  }

  public void unhookFromWalls(float left, float right, float lower, float upper) {
    if (x - radius < left) {
      x = left;
    }
    if (x + radius > right) {
      x = right;
    }
    if (y - radius < lower) {
      y = lower;
    }
    if (y + radius > upper) {
      y = upper;
    }
  }

  public boolean isInPocket(Pocket pocket) {
    return (x - pocket.x()) * (x - pocket.x()) + (y - pocket.y()) * (y - pocket.y()) <= pocket.radius() * pocket.radius();
  }
}
