package ru.nsu.fit.akitov.billiards.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BallTest {

  @Test
  public void initialParameters() {
    Ball ball = new Ball(42, 54, 100);
    assertEquals(42, ball.getX());
    assertEquals(54, ball.getY());
    assertTrue(ball.isMotionless());
    assertTrue(ball.isAvailable());
  }

  @Test
  public void movingWithoutFriction() {
    Ball ball = new Ball(0, 0, 100);
    ball.setVelocity(10, -10);
    ball.move(1, 0, 0);
    assertEquals(10, ball.getX());
    assertEquals(-10, ball.getY());
  }

  @Test
  public void FrictionReducesSpeed() {
    Ball ball = new Ball(0, 0, 100);
    ball.setVelocity(10, 10);
    ball.move(1, 10, 10);
    assertTrue(ball.getVelocityX() < 10);
    assertTrue(ball.getVelocityY() < 10);
  }

  @Test
  public void collisions() {
    Ball ball1 = new Ball(0, 0, 10);
    Ball ball2 = new Ball(0, 50, 10);
    assertFalse(ball1.collides(ball2));
    assertFalse(ball2.collides(ball1));
    ball1.setPosition(0, 45);
    assertTrue(ball1.collides(ball2));
    assertTrue(ball2.collides(ball1));
  }

  @Test
  public void momentumConservation() {
    Ball ball1 = new Ball(0, 0, 10);
    Ball ball2 = new Ball(20, 0, 10);
    int vx1 = 25, vy1 = -10;
    int vx2 = -100, vy2 = 100;
    ball1.setVelocity(vx1, vy1);
    ball2.setVelocity(vx2, vy2);
    ball1.hit(ball2);
    assertEquals(vx1 + vx2, ball1.getVelocityX() + ball2.getVelocityX());
    assertEquals(vy1 + vy2, ball1.getVelocityY() + ball2.getVelocityY());
    assertTrue(vx1 != ball1.getVelocityX() || vy1 != ball1.getVelocityY());
    assertTrue(vx2 != ball2.getVelocityX() || vy2 != ball2.getVelocityY());
  }

  @Test
  public void pocketCollisions() {
    Pocket pocket = new Pocket(0, 0, 100);
    Ball ball = new Ball(111, 0, 10);
    assertFalse(ball.isInPocket(pocket));
    ball.setPosition(101, 0);
    assertFalse(ball.isInPocket(pocket));
    ball.setPosition(99, 0);
    assertTrue(ball.isInPocket(pocket));
  }
}