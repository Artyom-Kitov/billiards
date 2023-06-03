package ru.nsu.fit.akitov.billiards.model;

import org.junit.jupiter.api.Test;
import ru.nsu.fit.akitov.billiards.utils.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FieldTest {

  @Test
  public void initialConfiguration() {
    List<Point2D> startPositions = List.of(new Point2D(14, 14), new Point2D(16, 14));
    GameProperties gameProperties = GameProperties.builder().setBallsCoordinates(startPositions).build();

    Field field = new Field(gameProperties);
    assertTrue(field.isMotionless());

    List<BallModel> balls = field.getBalls();
    assertEquals(2, balls.size());
    for (int i = 0; i < field.getBalls().size(); i++) {
      float radius = gameProperties.relativeBallSize() * gameProperties.fieldSize();
      assertTrue(balls.get(i).isAvailable());
      Point2D position = new Point2D(startPositions.get(i).x() * radius, startPositions.get(i).y() * radius);
      assertEquals(position, balls.get(i).position());
    }

    List<PocketModel> pockets = field.getPockets();
    assertEquals(6, pockets.size());
    for (int i = 0; i < 3; i++) {
      float fieldSize = gameProperties.fieldSize();
      PocketModel p1 = new PocketModel(new Point2D(fieldSize * i, 0), pockets.get(0).radius());
      PocketModel p2 = new PocketModel(new Point2D(fieldSize * i, fieldSize), pockets.get(0).radius());
      assertTrue(pockets.contains(p1));
      assertTrue(pockets.contains(p2));
    }
  }

  @Test
  public void ballInPocket() {
    List<Point2D> startPositions = List.of(new Point2D(2, 1));
    GameProperties gameProperties = GameProperties.builder().setBallsCoordinates(startPositions).build();

    // CR: set fake listener, remove list of listeners
    Field field = new Field(gameProperties);
    for (int i = 0; i < 100; i++) {
      field.increaseCueVelocity();
    }
    field.performCueStrike();
    while (!field.isMotionless()) {
      field.update(5);
    }
    assertFalse(field.getBalls().get(0).isAvailable());
  }
}