package ru.nsu.fit.akitov.billiards.utils;

import lombok.Builder;

import java.io.*;
import java.util.*;

@Builder(builderClassName = "PropertiesBuilder", setterPrefix = "set")
public record GameProperties(int fieldSize, float relativeBallSize, int relativePocketSize, int relativeCueStrength,
                             List<Point2D> ballsCoordinates, int upperPanelSize, float relativeBorderSize,
                             String gameName, String menuOption, String newGameOption,
                             String aboutOption, String exitOption) implements ModelProperties, ViewProperties {

  public static class PropertiesBuilder {
    private int fieldSize = 500;
    private float relativeBallSize = 0.036f;
    private int relativePocketSize = 8;
    private int relativeCueStrength = 4;
    private List<Point2D> ballsCoordinates = List.of(new Point2D(14, 14));
    private int upperPanelSize = 50;
    private float relativeBorderSize = 0.066f;
    private String gameName = "Billiards";
    private String menuOption = "Menu";
    private String newGameOption = "New Game";
    private String aboutOption = "About";
    private String exitOption = "Exit";
  }

  public static List<Point2D> getBallsCoordinates() {
    try (InputStream stream = GameProperties.class.getResourceAsStream("/coordinates")) {
      Scanner scanner = new Scanner(Objects.requireNonNull(stream));
      List<Point2D> result = new ArrayList<>();
      result.add(new Point2D(scanner.nextFloat(), scanner.nextFloat()));

      int ballsCount = scanner.nextInt();
      for (int i = 0; i < ballsCount; i++) {
        result.add(new Point2D(scanner.nextFloat(), scanner.nextFloat()));
      }
      return result;
    } catch (NoSuchElementException | NullPointerException | IOException e) {
      return List.of(new Point2D(14, 14));
    }
  }

  public static GameProperties readFromConfig() {
    try {
      InputStream stream = GameProperties.class.getResourceAsStream("/config.properties");
      Properties properties = new Properties();
      properties.load(stream);

      PropertiesBuilder builder = new PropertiesBuilder();
      builder.setFieldSize(Integer.parseUnsignedInt(properties.getProperty("FieldSize")))
              .setRelativeBallSize(Float.parseFloat(properties.getProperty("RelativeBallSize")))
              .setRelativePocketSize(Integer.parseUnsignedInt(properties.getProperty("RelativePocketSize")))
              .setRelativeCueStrength(Integer.parseUnsignedInt(properties.getProperty("RelativeCueStrength")))
              .setBallsCoordinates(getBallsCoordinates())
              .setUpperPanelSize(Integer.parseUnsignedInt(properties.getProperty("UpperPanelSize")))
              .setRelativeBorderSize(Float.parseFloat(properties.getProperty("RelativeBorderSize")))
              .setGameName(properties.getProperty("GameName"))
              .setMenuOption(properties.getProperty("MenuOption"))
              .setNewGameOption(properties.getProperty("NewGameOption"))
              .setAboutOption(properties.getProperty("AboutOption"))
              .setExitOption(properties.getProperty("ExitOption"));
      return builder.build();
    } catch (IOException | NumberFormatException | NullPointerException e) {
      return new PropertiesBuilder().build();
    }
  }
}
