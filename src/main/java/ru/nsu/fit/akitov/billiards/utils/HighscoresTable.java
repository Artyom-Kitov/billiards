package ru.nsu.fit.akitov.billiards.utils;

import java.io.*;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Set;
import java.util.List;
import java.util.Objects;
import java.util.TreeSet;

public class HighscoresTable {

  private static final Set<Highscore> highscores = new TreeSet<>();

  private HighscoresTable() {}

  private static void readFromConfig() {
    highscores.clear();
    try (InputStream stream = HighscoresTable.class.getResourceAsStream("/highscores")) {
      List<String> lines = new BufferedReader(new InputStreamReader(Objects.requireNonNull(stream)))
              .lines()
              .toList();
      for (String line : lines) {
        String[] args = line.split(" ");
        Highscore highscore = new Highscore(args[0], Integer.parseUnsignedInt(args[1]), Integer.parseUnsignedInt(args[2]));
        highscores.add(highscore);
      }
    } catch (IndexOutOfBoundsException | IOException | NumberFormatException e) {
      highscores.clear();
    }
  }

  public static void writeToConfig() {
    try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(HighscoresTable.class.getResource("/highscores").toURI()))) {
      for (Highscore highscore : highscores) {
        writer.write(highscore.toString() + "\n");
      }
    } catch (IOException | URISyntaxException e) {
    }
  }

  public static String[][] getTable() {
    readFromConfig();
    return highscores.stream()
            .map(hs -> new String[] {hs.name(), String.valueOf(hs.ballsCount()), String.valueOf(hs.time())})
            .toArray(String[][]::new);
  }

  public static void addHighscore(Highscore highscore) {
    highscores.add(highscore);
    writeToConfig();
  }
}
