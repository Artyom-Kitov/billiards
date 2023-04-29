package ru.nsu.fit.akitov.billiards.utils;

import java.io.*;
import java.util.Properties;

public record GameProperties(int fieldSize, int upperPanelSize, float relativeBorderSize,
                             int ballsCount, int relativeBallSize, int relativePocketSize,
                             int relativeCueStrength,
                             // CR: ok to hardcode all view fields
                             // CR: or you can do the following:
                             // interface ModelProperties {
                             //   int fieldSize();
                             // }
                             //
                             // interface ViewProperties {
                             //   String gameName();
                             //   // etc...
                             // }
                             //
                             // record GameProperties implements ModelProperties, ViewProperties {}
                             String gameName, String menuOption,
                             String newGameOption, String aboutOption, String exitOption) {

  // CR: take a look at https://projectlombok.org/features/Builder since you've liked this pattern :)
  // CR: not sure if it would be suitable here
  public static class Builder {

    private int fieldSize = 500;
    private int upperPanelSize = 50;
    private float relativeBorderSize = 0.066f;
    private int ballsCount = 15;
    private int relativeBallSize = 14;
    private int relativePocketSize = 8;
    private int relativeCueStrength = 4;
    private String gameName = "Billiards";
    private String menuOption = "Menu";
    private String newGameOption = "New Game";
    private String aboutOption = "About";
    private String exitOption = "Exit";

    public Builder setFieldSize(int fieldSize) {
      this.fieldSize = fieldSize;
      return this;
    }

    public Builder setUpperPanelSize(int upperPanelSize) {
      this.upperPanelSize = upperPanelSize;
      return this;
    }

    public Builder setRelativeBorderSize(float relativeBorderSize) {
      this.relativeBorderSize = relativeBorderSize;
      return this;
    }

    public Builder setBallsCount(int ballsCount) {
      this.ballsCount = ballsCount;
      return this;
    }

    public Builder setRelativeBallSize(int relativeBallSize) {
      this.relativeBallSize = relativeBallSize;
      return this;
    }

    public Builder setRelativePocketSize(int relativePocketSize) {
      this.relativePocketSize = relativePocketSize;
      return this;
    }

    public Builder setRelativeCueStrength(int relativeCueStrength) {
      this.relativeCueStrength = relativeCueStrength;
      return this;
    }

    public Builder setGameName(String gameName) {
      if (gameName == null) {
        throw new NullPointerException("game name cannot be null");
      }
      this.gameName = gameName;
      return this;
    }

    public Builder setMenuOption(String menuOption) {
      if (menuOption == null) {
        throw new NullPointerException("menu option name cannot be null");
      }
      this.menuOption = menuOption;
      return this;
    }

    public Builder setNewGameOption(String newGameOption) {
      if (newGameOption == null) {
        throw new NullPointerException("new game option name cannot be null");
      }
      this.newGameOption = newGameOption;
      return this;
    }

    public Builder setAboutOption(String aboutOption) {
      if (aboutOption == null) {
        throw new NullPointerException("about option name cannot be null");
      }
      this.aboutOption = aboutOption;
      return this;
    }

    public Builder setExitOption(String exitOption) {
      if (exitOption == null) {
        throw new NullPointerException("exit option name cannot be null");
      }
      this.exitOption = exitOption;
      return this;
    }

    public GameProperties build() {
      return new GameProperties(fieldSize, upperPanelSize, relativeBorderSize, ballsCount,
              relativeBallSize, relativePocketSize, relativeCueStrength, gameName, menuOption,
              newGameOption, aboutOption, exitOption);
    }
  }

  public static GameProperties readFromConfig() {
    try {
      InputStream stream = GameProperties.class.getResourceAsStream("/config.properties");
      Properties properties = new Properties();
      properties.load(stream);

      Builder builder = new Builder();
      builder.setFieldSize(Integer.parseUnsignedInt(properties.getProperty("FieldSize")))
              .setUpperPanelSize(Integer.parseUnsignedInt(properties.getProperty("UpperPanelSize")))
              .setRelativeBorderSize(Float.parseFloat(properties.getProperty("RelativeBorderSize")))
              .setBallsCount(Integer.parseUnsignedInt(properties.getProperty("BallsCount")))
              .setRelativeBallSize(Integer.parseUnsignedInt(properties.getProperty("RelativeBallSize")))
              .setRelativePocketSize(Integer.parseUnsignedInt(properties.getProperty("RelativePocketSize")))
              .setRelativeCueStrength(Integer.parseUnsignedInt(properties.getProperty("RelativeCueStrength")))
              .setGameName(properties.getProperty("GameName"))
              .setMenuOption(properties.getProperty("MenuOption"))
              .setNewGameOption(properties.getProperty("NewGameOption"))
              .setAboutOption(properties.getProperty("AboutOption"))
              .setExitOption(properties.getProperty("ExitOption"));
      return builder.build();
    } catch (IOException | NumberFormatException | NullPointerException e) {
      return new Builder().build();
    }
  }
}
