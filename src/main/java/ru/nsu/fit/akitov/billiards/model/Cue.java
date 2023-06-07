package ru.nsu.fit.akitov.billiards.model;

public class Cue {

  private float velocity;
  private float angle;
  private final float maxVelocity;

  public Cue(float maxVelocity) {
    this.maxVelocity = maxVelocity;
  }

  public void addVelocity(float dv) {
    if (velocity + dv < 0.0f || velocity + dv > maxVelocity) {
      return;
    }
    velocity += dv;
  }

  public void rotate(float theta) {
    angle += theta;
    if (angle >= Math.PI * 2) {
      angle -= Math.PI * 2;
    } else if (angle <= 0) {
      angle += Math.PI * 2;
    }
  }

  public float getVelocity() {
    return velocity;
  }

  public float getAngle() {
    return angle;
  }

  public void strike(Ball ball) {
    float vx = (float) (velocity * Math.cos(angle));
    float vy = (float) (velocity * Math.sin(angle));
    ball.setVelocity(vx, vy);
  }
}
