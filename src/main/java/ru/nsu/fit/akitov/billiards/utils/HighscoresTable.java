package ru.nsu.fit.akitov.billiards.utils;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.spi.FileSystemProvider;
import java.util.*;
import java.util.stream.Stream;

public class HighscoresTable {

  private static final Set<Highscore> HIGHSCORES = new TreeSet<>();

  private HighscoresTable() {}

  private static void readFromConfig() {
    // CR: write to local var
    HIGHSCORES.clear();
    try (InputStream stream = HighscoresTable.class.getResourceAsStream("/highscores")) {
      List<String> lines = new BufferedReader(new InputStreamReader(Objects.requireNonNull(stream)))
              .lines()
              .toList();
      for (String line : lines) {
        String[] args = line.split(" ");
        Highscore highscore = new Highscore(args[0], Integer.parseUnsignedInt(args[1]), Integer.parseUnsignedInt(args[2]));
        HIGHSCORES.add(highscore);
      }
    } catch (IndexOutOfBoundsException | IOException | NumberFormatException e) {
      HIGHSCORES.clear();
    }
  }

  public static void writeToConfig() {
    try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(HighscoresTable.class.getResource("/highscores").toURI()))) {
      for (Highscore highscore : HIGHSCORES) {
        writer.write(highscore.toString() + "\n");
      }
    } catch (IOException | URISyntaxException e) {
      // CR: log
    }
  }

  public static String[][] getTable() {
    // CR: do not read each time
    readFromConfig();
    return HIGHSCORES.stream()
            .map(hs -> new String[] {hs.name(), String.valueOf(hs.ballsCount()), String.valueOf(hs.time())})
            .toArray(String[][]::new);
  }

  public static void addHighscore(Highscore highscore) {
    HIGHSCORES.add(highscore);
    writeToConfig();
  }
}
