package ru.nsu.fit.akitov.billiards.model;

import org.junit.jupiter.api.Test;
import ru.nsu.fit.akitov.billiards.dto.BallModel;
import ru.nsu.fit.akitov.billiards.dto.PocketModel;
import ru.nsu.fit.akitov.billiards.utils.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FieldTest {

  static class TestFieldListener implements FieldListener {

    private int numFieldChanged;
    private int numIsMotionless;
    private int numStrikePerformed;
    private int numAskForCueBall;
    private int numCueBallPlaceSuccessful;
    private int numGameOver;

    @Override
    public void fieldChanged() {
      numFieldChanged++;
    }

    @Override
    public void isMotionless() {
      numIsMotionless++;
    }

    @Override
    public void strikePerformed() {
      numStrikePerformed++;
    }

    @Override
    public void askForCueBall() {
      numAskForCueBall++;
    }

    @Override
    public void cueBallPlaceSuccessful() {
      numCueBallPlaceSuccessful++;
    }

    @Override
    public void gameOver() {
      numGameOver++;
    }
  }

  @Test
  public void initialConfiguration() {
    List<Point2D> startPositions = List.of(new Point2D(14, 14), new Point2D(16, 14));
    GameProperties gameProperties = GameProperties.builder().setBallsCoordinates(startPositions).build();
    Field field = new Field(gameProperties);
    assertTrue(field.isMotionless());

    BallModel cueBall = field.getCueBall();
    float x = startPositions.get(0).x() * cueBall.radius();
    float y = startPositions.get(0).y() * cueBall.radius();
    assertEquals(new Point2D(x, y), cueBall.position());

    List<BallModel> balls = field.getBalls();
    assertEquals(1, balls.size());
    for (int i = 1; i < field.getBalls().size(); i++) {
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
  public void strikeDirection() {
    List<Point2D> startPositions = List.of(new Point2D(5, 5));
    GameProperties gameProperties = GameProperties.builder().setBallsCoordinates(startPositions).build();
    Field field = new Field(gameProperties);
    TestFieldListener listener = new TestFieldListener();
    field.setListener(listener);

    BallModel initial = field.getCueBall();

    field.increaseCueVelocity();
    field.performCueStrike();
    field.update(5);
    assertNotEquals(initial.position().x(), field.getCueBall().position().x());
    assertEquals(initial.position().y(), field.getCueBall().position().y());

    field.reset();
    field.rotateCueLeft();
    field.performCueStrike();
    field.update(5);
    assertNotEquals(initial.position(), field.getCueBall().position());

    field.reset();
    field.rotateCueRight();
    field.performCueStrike();
    field.update(5);
    assertNotEquals(initial.position(), field.getCueBall().position());

    assertEquals(3, listener.numStrikePerformed);
  }

  @Test
  public void ballInPocket() {
    List<Point2D> startPositions = List.of(new Point2D(2, 1.1f));
    GameProperties gameProperties = GameProperties.builder().setBallsCoordinates(startPositions).build();
    Field field = new Field(gameProperties);
    TestFieldListener listener = new TestFieldListener();
    field.setListener(listener);

    for (int i = 0; i < 100; i++) {
      field.increaseCueVelocity();
    }
    field.performCueStrike();
    while (!field.isMotionless()) {
      field.update(5);
    }
    assertFalse(field.getCueBall().isAvailable());
    assertEquals(1, listener.numStrikePerformed);
    assertNotEquals(0, listener.numFieldChanged);
  }

  @Test
  public void ballBouncedIntoPocket() {
    List<Point2D> startCoordinates = List.of(new Point2D(2, 1), new Point2D(5, 1), new Point2D(8, 1));
    GameProperties gameProperties = GameProperties.builder().setBallsCoordinates(startCoordinates).build();
    Field field = new Field(gameProperties);
    TestFieldListener listener = new TestFieldListener();
    field.setListener(listener);

    for (int i = 0; i < 100; i++) {
      field.increaseCueVelocity();
    }
    field.performCueStrike();
    while (listener.numIsMotionless == 0) {
      field.update(5);
    }

    assertTrue(field.getCueBall().isAvailable());
    assertTrue(field.getBalls().get(0).isAvailable());
    assertFalse(field.getBalls().get(1).isAvailable());
    assertEquals(0, listener.numGameOver);
    assertNotEquals(0, listener.numFieldChanged);
  }

  @Test
  public void cueBallPlacing() {
    List<Point2D> startCoordinates = List.of(new Point2D(4, 1), new Point2D(2, 1), new Point2D(2, 4));
    GameProperties gameProperties = GameProperties.builder().setBallsCoordinates(startCoordinates).build();
    Field field = new Field(gameProperties);
    TestFieldListener listener = new TestFieldListener();
    field.setListener(listener);

    for (int i = 0; i < 100; i++) {
      field.increaseCueVelocity();
    }
    field.performCueStrike();
    while (listener.numIsMotionless == 0) {
      field.update(5);
    }
    assertEquals(1, listener.numIsMotionless);
    assertEquals(1, listener.numAskForCueBall);
    for (int i = 0; i < 100; i++) {
      field.moveCueBallLeft();
    }
    BallModel prevPos = field.getCueBall();
    field.moveCueBallLeft();
    assertEquals(prevPos, field.getCueBall());
    field.placeCueBall();
    assertEquals(1, listener.numCueBallPlaceSuccessful);
  }
}
