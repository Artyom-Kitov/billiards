package ru.nsu.fit.akitov.billiards.utils;

import lombok.AccessLevel;
import lombok.Builder;
import ru.nsu.fit.akitov.billiards.model.ModelProperties;
import ru.nsu.fit.akitov.billiards.view.ViewProperties;

import java.io.*;
import java.util.*;

@Builder(builderClassName = "PropertiesBuilder", setterPrefix = "set", access = AccessLevel.PUBLIC)
public record GameProperties(int fieldSize, float relativeBallSize,
                             int relativePocketSize, int relativeCueStrength, List<Point2D> ballsCoordinates,

                             int upperPanelSize, float relativeBorderSize,
                             String gameName, String menuOption, String newGameOption, String highscoresOption,
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
    private String highscoresOption = "Highscores";
    private String aboutOption = "About";
    private String exitOption = "Exit";
  }

  private static List<Point2D> readBallCoordinates() {
    try (InputStream stream = GameProperties.class.getResourceAsStream("/coordinates")) {
      if (stream == null) {
        return List.of(new Point2D(14, 14));
      }
      Scanner scanner = new Scanner(stream);
      List<Point2D> result = new ArrayList<>();
      result.add(new Point2D(scanner.nextFloat(), scanner.nextFloat()));

      int ballsCount = scanner.nextInt();
      for (int i = 0; i < ballsCount; i++) {
        result.add(new Point2D(scanner.nextFloat(), scanner.nextFloat()));
      }
      return result;
    } catch (NoSuchElementException | IOException e) {
      BilliardsLogger.error("Error while reading balls positions");
      return List.of(new Point2D(14, 14));
    }
  }

  public static GameProperties readFromConfig() {
    PropertiesBuilder builder = new PropertiesBuilder();
    try {
      InputStream stream = GameProperties.class.getResourceAsStream("/config.properties");
      if (stream == null) {
        return builder.build();
      }
      Properties properties = new Properties();
      properties.load(stream);

      builder.setFieldSize(Integer.parseUnsignedInt(properties.getProperty("FieldSize")))
              .setRelativeBallSize(Float.parseFloat(properties.getProperty("RelativeBallSize")))
              .setRelativePocketSize(Integer.parseUnsignedInt(properties.getProperty("RelativePocketSize")))
              .setRelativeCueStrength(Integer.parseUnsignedInt(properties.getProperty("RelativeCueStrength")))
              .setBallsCoordinates(readBallCoordinates())
              .setUpperPanelSize(Integer.parseUnsignedInt(properties.getProperty("UpperPanelSize")))
              .setRelativeBorderSize(Float.parseFloat(properties.getProperty("RelativeBorderSize")))
              .setGameName(properties.getProperty("GameName"))
              .setMenuOption(properties.getProperty("MenuOption"))
              .setNewGameOption(properties.getProperty("NewGameOption"))
              .setHighscoresOption(properties.getProperty("HighscoresOption"))
              .setAboutOption(properties.getProperty("AboutOption"))
              .setExitOption(properties.getProperty("ExitOption"));
      return builder.build();
    } catch (IOException | NumberFormatException e) {
      BilliardsLogger.error("Error while reading game configuration, using default instead");
      return builder.build();
    }
  }
}
