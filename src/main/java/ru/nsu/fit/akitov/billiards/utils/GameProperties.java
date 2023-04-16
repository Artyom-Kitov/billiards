package ru.nsu.fit.akitov.billiards.utils;

import java.io.*;
import java.util.Properties;

public record GameProperties(int fieldSize, int upperPanelSize, float relativeBorderSize,
                             int ballsCount, int relativeBallSize, int relativePocketSize,
                             int relativeCueStrength, String gameName, String menuOption,
                             String newGameOption, String aboutOption, String exitOption) {

  public static class Builder {

    private int fieldSize;
    private int upperPanelSize;
    private float relativeBorderSize;
    private int ballsCount;
    private int relativeBallSize;
    private int relativePocketSize;
    private int relativeCueStrength;
    private String gameName;
    private String menuOption;
    private String newGameOption;
    private String aboutOption;
    private String exitOption;

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
      this.gameName = gameName;
      return this;
    }

    public Builder setMenuOption(String menuOption) {
      this.menuOption = menuOption;
      return this;
    }

    public Builder setNewGameOption(String newGameOption) {
      this.newGameOption = newGameOption;
      return this;
    }

    public Builder setAboutOption(String aboutOption) {
      this.aboutOption = aboutOption;
      return this;
    }

    public Builder setExitOption(String exitOption) {
      this.exitOption = exitOption;
      return this;
    }

    public GameProperties build() {
      return new GameProperties(fieldSize, upperPanelSize, relativeBorderSize, ballsCount,
              relativeBallSize, relativePocketSize, relativeCueStrength, gameName, menuOption,
              newGameOption, aboutOption, exitOption);
    }
  }

  public static GameProperties readFromFile() throws IOException {
    InputStream stream = Thread.currentThread().getContextClassLoader().getResourceAsStream("config.properties");
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
  }
}
