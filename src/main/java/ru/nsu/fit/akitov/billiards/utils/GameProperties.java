package ru.nsu.fit.akitov.billiards.utils;

import java.awt.*;
import java.io.*;
import java.util.Properties;

public record GameProperties(int screenWidth, int screenHeight, int fieldSize) {
  public static class Builder {
    private int screenWidth = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
    private int screenHeight = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
    private int fieldSize = 0;

    public Builder setScreenWidth(int screenWidth) {
      this.screenWidth = screenWidth;
      return this;
    }

    public Builder setScreenHeight(int screenHeight) {
      this.screenHeight = screenHeight;
      return this;
    }

    public Builder setFieldSize(int fieldSize) {
      this.fieldSize = fieldSize;
      return this;
    }

    public GameProperties build() {
      return new GameProperties(screenWidth, screenHeight, fieldSize);
    }
  }

  public static GameProperties readFromFile() throws IOException {
    InputStream stream = Thread.currentThread().getContextClassLoader().getResourceAsStream("config.properties");
    Properties properties = new Properties();
    properties.load(stream);

    Builder builder = new Builder();
    builder.setScreenWidth(Integer.parseUnsignedInt(properties.getProperty("ScreenWidth")));
    builder.setScreenHeight(Integer.parseUnsignedInt(properties.getProperty("ScreenHeight")));
    builder.setFieldSize(Integer.parseUnsignedInt(properties.getProperty("FieldSize")));

    return builder.build();
  }
}
